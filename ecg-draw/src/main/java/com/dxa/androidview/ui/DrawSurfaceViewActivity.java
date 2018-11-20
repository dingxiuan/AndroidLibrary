package com.dxa.androidview.ui;

import android.content.res.Configuration;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.SurfaceHolder;

import com.dxa.android.timer.Timer;
import com.dxa.android.timer.TimerTask;
import com.dxa.android.ui.ActivityPresenter;
import com.dxa.android.ui.SuperActivity;
import com.dxa.androidview.R;
import com.dxa.androidview.widget.DrawSurfaceView;
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

public class DrawSurfaceViewActivity extends SuperActivity
        implements SurfaceHolder.Callback {

    @BindView(R.id.surface_view)
    DrawSurfaceView surfaceView;

    private final Timer drawTimer = new Timer(true);
    private final AtomicBoolean execState = new AtomicBoolean(true);
    private final AtomicInteger count = new AtomicInteger();
    private List<String> pointLines = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_surface_view);
        ButterKnife.bind(this);

        if (isLandscape()) {
            logger.i("横屏");
            surfaceView.getHolder().addCallback(this);
            surfaceView.updateDensity();
            surfaceView.setValue(1024, 4000, -4000);
//            waveView.setValue(2048, 8000, -8000);;
//            waveView.setValue(2048, 8000, -8000);
            drawTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    InputStream stream = null;
                    try {
                        stream = getAssets().open("2018年05月23日16时54分03秒.txt");
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
    protected void onDestroy() {
        execState.set(false);
        drawTimer.cancel();
        super.onDestroy();
        surfaceView.getHolder().removeCallback(this);
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

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        logger.i("surfaceCreated");
        drawTimer.schedule(new TimerTask("DrawTask") {
            @Override
            public void run() {
                if (execState.get()) {
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
                    final SurfaceHolder holder = surfaceView.getHolder();
                    final Canvas canvas = holder.lockCanvas();
                    try {
                        surfaceView.updatePoint(canvas, Integer.parseInt(split[2]) / 3);
                    } finally {
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }, 1500, 5);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        logger.i("surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        logger.i("surfaceDestroyed");
        drawTimer.cancel("DrawTask");
    }

}
