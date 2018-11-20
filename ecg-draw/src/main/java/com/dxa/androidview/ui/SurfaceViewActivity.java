package com.dxa.androidview.ui;

import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.dxa.androidview.R;
import com.dxa.android.logger.DLogger;
import com.dxa.android.timer.Timer;
import com.dxa.android.timer.TimerTask;
import com.dxa.android.ui.ActivityPresenter;
import com.dxa.android.ui.SuperActivity;

import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.ButterKnife;

public class SurfaceViewActivity extends SuperActivity implements SurfaceHolder.Callback {

    private volatile int xAxis = 0;
    private volatile int yAxis = 0;

    private final Timer timer = new Timer(true);

    private final AtomicBoolean execState = new AtomicBoolean(true);

    private static final String TASK_NAME = "EcgDrawTask";

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    private int scaleX = 20;
    private int scaleY = 50;
    private double ox = 10;
    private double oy = 240;
    private double x = 0;
    private double y = 0;
    private double t = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setDebug(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_view);
        ButterKnife.bind(this);

        if (isLandscape()) {
            logger.i("横屏");
            surfaceView = findViewById(R.id.surface_view);
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(this);
        } else {
            logger.i("竖屏");
        }
    }

    @Override
    protected void onDestroy() {
        execState.set(false);
        timer.cancel();
        super.onDestroy();
    }

    @Nullable
    @Override
    protected ActivityPresenter buildPresenter() {
        return null;
    }

    public boolean isLandscape() {
        return Configuration.ORIENTATION_LANDSCAPE
                == getResources().getConfiguration().orientation;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        timer.cancel(TASK_NAME);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        timer.cancel(TASK_NAME);
        timer.schedule(new TimerTask(TASK_NAME) {

            private final Paint paint = new Paint();
            {
                paint.setAntiAlias(true);
                paint.setStrokeWidth(3);
            }

            @Override
            public void run() {
                if (execState.get()) {
                    final Canvas canvas = surfaceHolder.lockCanvas();
                    try {
                        if (canvas != null) {
                            //绘制坐标轴
                            paint.setColor(Color.WHITE);
                            canvas.drawText("O", 20, 220, paint);
                            paint.setColor(Color.BLUE);
                            canvas.drawLine(10, 10, 10, 480, paint);
                            canvas.drawText("Y", 20, 30, paint);
                            paint.setColor(Color.GREEN);
                            canvas.drawLine(0, 240, 320, 240, paint);
                            canvas.drawText("X", 300, 260, paint);
                            //绘制正弦曲线
                            t += 0.1;
                            x = t * scaleX;
                            y = Math.sin(t) * scaleY + 240;
                            logger.i("sin: ", Math.round(Math.sin(t)));
                            if (t > 0) {
                                paint.setColor(Color.RED);
                                canvas.drawLine(10 + (int) ox, (int) oy, 10 + (int) x, (int) y, paint);
                                logger.i(x + " & " + y);
                            }
                            ox = x;
                            oy = y;
                            if (t > 15) {
                                execState.set(false);
                            }
                        }
                    } finally {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }, 2000, 200);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub
    }
}
