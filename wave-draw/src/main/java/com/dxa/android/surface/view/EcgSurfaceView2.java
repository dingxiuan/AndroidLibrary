package com.dxa.android.surface.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Frankie on 2016/5/26.
 */
public class EcgSurfaceView2 extends SurfaceView implements SurfaceHolder.Callback {

    private final AtomicBoolean runState = new AtomicBoolean(true);
    private final SurfaceHolder surfaceHolder;

    /**
     * 绘制的线程
     */
    private Thread drawThread;

    private int waveSpeed = 25;// 波速: 25mm/s
    private int sleepTime = 16; // 每次休眠的时间间距，单位:ms
    private final Painter painter;

    private final GridPainter gridPainter;

    public EcgSurfaceView2(Context context, AttributeSet attrs) {
        super(context, attrs);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.scaledDensity;
        this.gridPainter = new GridPainter(density);
        this.painter = new Painter(metrics);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        gridPainter.onSizeChanged(w, h);
        painter.onSizeChanged(w, h, gridPainter.getPaddingWidth(), gridPainter.getPaddingHeight());
    }

    public boolean isRunning() {
        return runState.get();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        runState.set(true);
        drawThread = new Thread(() -> {
            while (runState.get()) {
                long startTime = System.currentTimeMillis();
                final Canvas canvas = painter.lockCanvas(surfaceHolder);
                if (canvas == null){
                    continue;
                }
                try {
                    // 绘制背景网格
                    gridPainter.draw(canvas);
                    painter.drawWave(canvas, 4096 / 2);
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }

                long endTime = System.currentTimeMillis();
                if (endTime - startTime < sleepTime) {
                    try {
                        Thread.sleep(sleepTime - (endTime - startTime));
                    } catch (InterruptedException e) {
                        Log.e("EcgWave", "throw InterruptedException: " + e.getMessage(), e);
                    }
                }
            }
        });
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        runState.set(false);
        drawThread.interrupt();
    }

}
