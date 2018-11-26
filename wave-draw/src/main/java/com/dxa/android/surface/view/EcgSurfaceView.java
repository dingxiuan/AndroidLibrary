package dev.frankie.view;

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
public class EcgSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private final AtomicBoolean runState = new AtomicBoolean(true);
    private final SurfaceHolder surfaceHolder;

    private float ecgMax = 4096;// 心电的最大值
    private int bgColor = Color.BLACK;
    private int waveSpeed = 25;// 波速: 25mm/s
    private int sleepTime = 8; // 每次锁屏的时间间距，单位:ms
    private float lockWidth;// 每次锁屏需要画的
    private int ecgPerCount = 8;// 每次画心电数据的个数，心电每秒有500个数据包

    private Queue<Float> ecg0Data = new LinkedList<>();
    private Queue<Float> ecg1Data = new LinkedList<>();

    private Paint paint;//画波形图的画笔
    private float ecgYRatio;
    private float startY0;
    private float startY1;
    private float yOffset1;//波2的Y坐标偏移值
    private final Rect rect = new Rect();

    private int startX;//每次画线的X坐标起点
    private float ecgXOffset;//每次X坐标偏移的像素
    private int blankLineWidth = 6;//右侧空白点的宽度

    /**
     * 绘制的线程
     */
    private Thread drawThread;

    public EcgSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(6);

        this.lockWidth = convertXOffset(getResources().getDisplayMetrics(), waveSpeed, sleepTime);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    /**
     * 根据波速计算每次X坐标增加的像素
     * <p>
     * 计算出每次锁屏应该画的px值
     */
    public static float convertXOffset(DisplayMetrics metrics, int waveSpeed, int sleepTime) {
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        //获取屏幕对角线的长度，单位:px
        double inch = Math.sqrt(width * width + height * height) / metrics.densityDpi;//单位：英寸
        double diagonalMm = inch * 2.54 * 10;//转换单位为：毫米
        double diagonalPx = Math.sqrt(width * width + height * height);
        // 每毫米有多少px
        double px1mm = diagonalPx / diagonalMm;
        // 每秒画多少px
        double px1s = waveSpeed * px1mm;
        // 每次锁屏所需画的宽度
        return (float) (px1s * (sleepTime / 1000f));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        runState.set(true);

        ecgXOffset = lockWidth / ecgPerCount;
        startY0 = h * (1.0f / 4);// 波1初始Y坐标是控件高度的1/4
        startY1 = h * (3.0f / 4);
        ecgYRatio = h / 2 / ecgMax;
        yOffset1 = h / 2;
    }

    /**
     * 画波1
     */
    private void drawWave0(Canvas canvas) {
        try {
            float startX = this.startX;
            if (ecg0Data.size() > ecgPerCount) {
                float newX, newY;
                for (int i = 0; i < ecgPerCount; i++) {
                    Float poll = ecg0Data.poll();
                    if (poll != null) {
                        newX = startX + ecgXOffset;
                        newY = ecgConvert(poll);
                        canvas.drawLine(startX, startY0, newX, newY, paint);
                        startX = newX;
                        startY0 = newY;
                    }
                }
            } else {
                // 如果没有数据，因为有数据一次画ecgPerCount个数，
                // 那么无数据时候就应该画ecgPercount倍数长度的中线
                float newX = (int) (startX + ecgXOffset * ecgPerCount);
                float newY = ecgConvert((int) (ecgMax / 2));
                canvas.drawLine(startX, startY0, newX, newY, paint);
                startY0 = newY;
            }
        } catch (NoSuchElementException e) {
            Log.e("EcgView", "throw NoSuchElementException: " + e.getMessage(), e);
        }
    }

    /**
     * 画波2
     */
    private void drawWave1(Canvas canvas) {
        try {
            float mStartX = startX;
            if (ecg1Data.size() > ecgPerCount) {
                for (int i = 0; i < ecgPerCount; i++) {
                    Float poll = ecg1Data.poll();
                    if (poll != null) {
                        float newX = mStartX + ecgXOffset;
                        float newY = ecgConvert(poll) + yOffset1;
                        canvas.drawLine(mStartX, startY1, newX, newY, paint);
                        mStartX = newX;
                        startY1 = newY;
                    }
                }
            } else {
                // 如果没有数据，因为有数据一次画ecgPerCount个数，
                // 那么无数据时候就应该画ecgPercount倍数长度的中线
                float newX = (mStartX + ecgXOffset * ecgPerCount);
                float newY = ecgConvert((ecgMax / 2)) + yOffset1;
                canvas.drawLine(mStartX, startY1, newX, newY, paint);
                startY1 = newY;
            }
        } catch (NoSuchElementException e) {
            Log.e("EcgView", "throw NoSuchElementException: " + e.getMessage());
        }
    }

    /**
     * 将心电数据转换成用于显示的Y坐标
     */
    private float ecgConvert(float data) {
        return (ecgMax - data) * ecgYRatio;
    }

    public void addEcgData0(float data) {
        ecg0Data.add(data);
    }

    public void addEcgData1(float data) {
        ecg1Data.add(data);
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

                    rect.set(startX, 0, (int) (startX + lockWidth + blankLineWidth), getHeight());
                    final Canvas canvas = surfaceHolder.lockCanvas(rect);
                    if (canvas == null) return;
                    try {
                        canvas.drawColor(bgColor);
                        drawWave0(canvas);
                        drawWave1(canvas);
                    } finally {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }

                    startX = (int) (startX + lockWidth);
                    if (startX > getWidth()) {
                        startX = 0;
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
