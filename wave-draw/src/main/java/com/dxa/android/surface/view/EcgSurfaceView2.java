package dev.frankie.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
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
    private int sleepTime = 8; // 每次锁屏的时间间距，单位:ms
    private final Painter painter;

    private final GridPainter gridPainter;

    public EcgSurfaceView2(Context context, AttributeSet attrs) {
        super(context, attrs);

        float xOffset = Painter.calculateOffset(getResources().getDisplayMetrics(), waveSpeed, sleepTime);

        this.gridPainter = new GridPainter(getResources().getDisplayMetrics());
        this.painter = new Painter(this, xOffset);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        gridPainter.onSizeChanged(w, h);
        painter.onSizeChanged(w, h, oldw, oldh);
    }

    public boolean isRunning() {
        return runState.get();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        runState.set(true);
        drawThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (runState.get()) {
                    long startTime = System.currentTimeMillis();
                    final Canvas canvas = painter.lockCanvas(surfaceHolder);
                    if (canvas == null){
                        continue;
                    }
                    try {
                        // 绘制背景网格
                        gridPainter.drawBackground(canvas);
                        painter.draw(canvas, null);
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
