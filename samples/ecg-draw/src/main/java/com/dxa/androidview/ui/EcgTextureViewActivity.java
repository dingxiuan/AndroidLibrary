package com.dxa.androidview.ui;

import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.TextureView;

import com.dxa.android.timer.Timer;
import com.dxa.android.timer.TimerTask;
import com.dxa.android.ui.ActivityPresenter;
import com.dxa.android.ui.SuperActivity;
import com.dxa.androidview.R;
import com.dxa.androidview.widget.DrawSurfaceView;
import com.dxa.androidview.widget.EcgTextureView;
import com.dxa.common.CloseQuietly;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EcgTextureViewActivity extends SuperActivity implements TextureView.SurfaceTextureListener {

    @BindView(R.id.texture_view)
    EcgTextureView textureView;

    private final Timer drawTimer = new Timer(true);
    private final AtomicBoolean execState = new AtomicBoolean(true);
    private final AtomicInteger count = new AtomicInteger();
    private final Object lock = new Object();

    private List<String> pointLines = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecg_texture_view);
        ButterKnife.bind(this);

        if (isLandscape()) {
            logger.i("横屏");
            textureView.setSurfaceTextureListener(this);
            textureView.updateDensity();
            textureView.setValue(1024, 4000, -4000);
//            waveView.setValue(2048, 8000, -8000);;
//            waveView.setValue(2048, 8000, -8000);
            drawTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    InputStream stream = null;
                    try {
                        stream = getAssets().open("2018年06月05日17时13分44秒.txt");
                        pointLines = IOUtils.readLines(stream, "UTF-8");
                        logger.i("读取数据, size[", pointLines.size(), "], first line: ", pointLines.get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        CloseQuietly.close(stream);
                        cancel();
                    }
                }
            }, 1, 1);
        } else {
            logger.i("竖屏");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        execState.set(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        execState.set(false);
    }

    @Override
    protected void onDestroy() {
        drawTimer.cancel();
        super.onDestroy();
    }

    public boolean isLandscape() {
        return Configuration.ORIENTATION_LANDSCAPE
                == getResources().getConfiguration().orientation;
    }

    @Nullable
    @Override
    protected ActivityPresenter buildPresenter() {
        return null;
    }

    /**
     * Invoked when a {@link TextureView}'s SurfaceTexture is ready for use.
     *
     * @param surface The surface returned by
     *                {@link TextureView#getSurfaceTexture()}
     * @param width   The width of the surface
     * @param height  The height of the surface
     */
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        logger.i("onSurfaceTextureAvailable: width[", width, "]  height[", height, "]");

        drawTimer.schedule(new TimerTask("DrawTask") {
            @Override
            public void run() {
                if (execState.get()) {
                    synchronized (lock) {
                        try {
                            if (count.getAndIncrement() % 4 != 0) {
                                return;
                            }

                            if (count.get() >= pointLines.size()) {
                                cancel();
                                return;
                            }

                            String line = pointLines.get(count.get());
                            String[] split = line.split(", ");
                            if (split.length != 3) {
                                return;
                            }
                            final Canvas canvas = textureView.lockCanvas();
                            if (canvas != null) {
                                try {
                                    textureView.updatePoint(canvas, Integer.parseInt(split[2]) / 2);
                                } finally {
                                    textureView.unlockCanvasAndPost(canvas);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, 1500, 5);
    }

    /**
     * Invoked when the {@link SurfaceTexture}'s buffers size changed.
     *
     * @param surface The surface returned by
     *                {@link TextureView#getSurfaceTexture()}
     * @param width   The new width of the surface
     * @param height  The new height of the surface
     */
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        logger.i("onSurfaceTextureSizeChanged: width[", width, "]  height[", height, "]");
    }

    /**
     * Invoked when the specified {@link SurfaceTexture} is about to be destroyed.
     * If returns true, no rendering should happen inside the surface texture after this method
     * is invoked. If returns false, the client needs to call {@link SurfaceTexture#release()}.
     * Most applications should return true.
     *
     * @param surface The surface about to be destroyed
     */
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        logger.i("onSurfaceTextureDestroyed");
        synchronized (lock) {
            drawTimer.cancel("DrawTask");
        }
        return false;
    }

    /**
     * Invoked when the specified {@link SurfaceTexture} is updated through
     * {@link SurfaceTexture#updateTexImage()}.
     *
     * @param surface The surface just updated
     */
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        logger.i("onSurfaceTextureUpdated");
    }


}
