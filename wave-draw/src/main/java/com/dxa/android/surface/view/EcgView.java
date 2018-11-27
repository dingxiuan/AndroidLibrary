package com.dxa.android.surface.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Frankie on 2016/5/26.
 */
public class EcgView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "EcgView";

    private SurfaceHolder surfaceHolder;
    private final AtomicBoolean runState = new AtomicBoolean(false);

    private float ecgMax = 4096;//心电的最大值
    private int bgColor = Color.BLACK; // Color.parseColor("#3FB57D");
    private int waveSpeed = 25;//波速: 25mm/s
    private int sleepTime = 8; //每次锁屏的时间间距，单位:ms
    private float lockWidth;//每次锁屏需要画的
    private int ecgPerCount = 8;//每次画心电数据的个数，心电每秒有500个数据包

    private final Queue<Float> ecg0Data = new LinkedList<>();
    private final Queue<Float> ecg1Data = new LinkedList<>();

    private final Paint paint;//画波形图的画笔
    private float ecgYRatio;
    private float startY0;
    private float startY1;
    private float yOffset1;//波2的Y坐标偏移值

    private final Rect rect = new Rect();

    private int startX;//每次画线的X坐标起点
    private float ecgXOffset;//每次X坐标偏移的像素
    private int blankLineWidth = 6;//右侧空白点的宽度
    private Thread drawThread;

    public EcgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);

        paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(4);

        this.lockWidth = (float) convertXOffset(getResources().getDisplayMetrics(), waveSpeed, sleepTime);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        initSize(w, h);
    }

    private void initSize(int w, int h) {
        ecgXOffset = lockWidth / ecgPerCount;
        startY0 = h * (1.0f / 4f);// 波1初始Y坐标是控件高度的1/4
        startY1 = h * (3.0f / 4);
        ecgYRatio = h / 2 / ecgMax;
        yOffset1 = h / 2;

        Log.e(TAG, "ecgXOffset: " + ecgXOffset + ", ecgYRatio: " + ecgYRatio);
        Log.e(TAG, "w: " + w + ", h: " + h);
    }

    /**
     * 根据波速计算每次X坐标增加的像素
     * <p>
     * 计算出每次锁屏应该画的px值
     */
    public double convertXOffset(DisplayMetrics metrics, int waveSpeed, int sleepTime) {
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        double inch = Math.sqrt(width * width + height * height) / metrics.densityDpi;//单位：英寸
        //获取屏幕对角线的长度，单位:px
        double diagonalMm = inch * 2.54 * 10;//转换单位为：毫米
        double diagonalPx = width * width + height * height;
        diagonalPx = Math.sqrt(diagonalPx);
        // 每毫米有多少px
        double px1mm = diagonalPx / diagonalMm;
        //每秒画多少px
        double px1s = waveSpeed * px1mm;
        //每次锁屏所需画的宽度
        return (float) (px1s * (sleepTime / 1000f));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initSize(getWidth(), getHeight());

        runState.set(true);
        drawThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (runState.get()) {
                    long startTime = System.currentTimeMillis();

                    rect.set(startX, 0, (int) (startX + lockWidth + blankLineWidth), getHeight());
                    final SurfaceHolder holder = surfaceHolder;
                    final Canvas canvas = holder.lockCanvas(rect);
                    if (canvas != null) {
                        try {
                            canvas.drawColor(bgColor);
                            startY0 = drawWave0(canvas);
                            startY1 = drawWave1(canvas);
                        } finally {
                            holder.unlockCanvasAndPost(canvas);
                        }
                        startX = (int) (startX + lockWidth);
                        if (startX > getWidth()) {
                            startX = 0;
                        }
                    }

                    long endTime = System.currentTimeMillis();
                    if (endTime - startTime < sleepTime) {
                        try {
                            Thread.sleep(sleepTime - (endTime - startTime));
                        } catch (InterruptedException e) {
                            Log.e(TAG, "throw InterruptedException: " + e.getMessage());
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

    /**
     * 画波1
     */
    private float drawWave0(Canvas canvas) {
        float finalStartY = startY0;
        try {
            float mStartX = startX;
            if (ecg0Data.size() > ecgPerCount) {
                float newX, newY;
                for (int i = 0; i < ecgPerCount; i++) {
                    newX = mStartX + ecgXOffset;
                    newY = ecgConvert(ecg0Data.poll());
                    canvas.drawLine(mStartX, finalStartY, newX, newY, paint);
                    mStartX = newX;
                    finalStartY = newY;
                }
            } else {
                // 如果没有数据，因为有数据一次画ecgPerCount个数，
                // 那么无数据时候就应该画ecgPercount倍数长度的中线
                float newX = (mStartX + ecgXOffset * ecgPerCount);
                float newY = ecgConvert((ecgMax / 2));
                canvas.drawLine(mStartX, finalStartY, newX, newY, paint);
                finalStartY = newY;
            }
        } catch (NoSuchElementException e) {
            Log.e(TAG, "throw NoSuchElementException: " + e.getMessage());
        }
        return finalStartY;
    }

    /**
     * 画波2
     */
    private float drawWave1(Canvas canvas) {
        float finalStartY = startY1;
        try {
            float mStartX = startX;
            if (ecg1Data.size() > ecgPerCount) {
                float newX, newY;
                for (int i = 0; i < ecgPerCount; i++) {
                    newX = mStartX + ecgXOffset;
                    newY = ecgConvert(ecg1Data.poll()) + yOffset1;
                    canvas.drawLine(mStartX, finalStartY, newX, newY, paint);
                    mStartX = newX;
                    finalStartY = newY;
                }
            } else {
                // 如果没有数据，因为有数据一次画ecgPerCount个数，
                // 那么无数据时候就应该画ecgPercount倍数长度的中线
                float newX = (mStartX + ecgXOffset * ecgPerCount);
                float newY = ecgConvert((ecgMax / 2)) + yOffset1;
                canvas.drawLine(mStartX, finalStartY, newX, newY, paint);
                finalStartY = newY;
            }
        } catch (NoSuchElementException e) {
            Log.e(TAG, "throw NoSuchElementException: " + e.getMessage());
        }
        return finalStartY;
    }

    /**
     * 将心电数据转换成用于显示的Y坐标
     */
    private float ecgConvert(Float data) {
        return ((ecgMax - data) * ecgYRatio);
    }

    public void addEcgData0(Float data) {
        ecg0Data.add(data);
    }

    public void addEcgData1(Float data) {
        ecg1Data.add(data);
    }

    public boolean isRunning() {
        return runState.get();
    }

}
