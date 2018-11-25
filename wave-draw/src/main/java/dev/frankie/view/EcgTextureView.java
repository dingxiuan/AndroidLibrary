package dev.frankie.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TextureView;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Frankie on 2016/5/26.
 */
public class EcgTextureView extends TextureView implements TextureView.SurfaceTextureListener {

    private static final String TAG = "EcgTextureView";

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

    public EcgTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(4);

        this.lockWidth = (float) convertXOffset(getResources().getDisplayMetrics(), waveSpeed, sleepTime);

        setSurfaceTextureListener(this);
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
        yOffset1 = h / 2;

        ecgYRatio = h / 2 / ecgMax;

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
                float newX = (int) (mStartX + ecgXOffset * ecgPerCount);
                float newY = ecgConvert((int) (ecgMax / 2));
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
                    newX = (float) (mStartX + ecgXOffset);
                    newY = ecgConvert(ecg1Data.poll()) + yOffset1;
                    canvas.drawLine(mStartX, finalStartY, newX, newY, paint);
                    mStartX = newX;
                    finalStartY = newY;
                }
            } else {
                // 如果没有数据，因为有数据一次画ecgPerCount个数，
                // 那么无数据时候就应该画ecgPercount倍数长度的中线
                float newX = (int) (mStartX + ecgXOffset * ecgPerCount);
                float newY = ecgConvert((int) (ecgMax / 2)) + yOffset1;
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
    private float ecgConvert(float data) {
        return ((ecgMax - data) * ecgYRatio);
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
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        initSize(width, height);
        runState.set(true);
        drawThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (runState.get()) {
                    long startTime = System.currentTimeMillis();

                    rect.set(startX, 0, (int) (startX + lockWidth + blankLineWidth), getHeight());
                    final Canvas canvas = lockCanvas(rect);
                    if (canvas != null) {
                        try {
                            canvas.drawColor(bgColor);
                            startY0 = drawWave0(canvas);
                            startY1 = drawWave1(canvas);
                        } finally {
                            unlockCanvasAndPost(canvas);
                        }
                        startX = (int) (startX + lockWidth);
                        if (startX > getWidth()) {
                            startX = 0;
                        }
                    }

                    long endTime = System.currentTimeMillis();
                    long sleep = sleepTime - (endTime - startTime);
                    if (sleep > 0) {
                        try {
                            Thread.sleep(sleep);
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
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        runState.set(false);
        drawThread.interrupt();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
