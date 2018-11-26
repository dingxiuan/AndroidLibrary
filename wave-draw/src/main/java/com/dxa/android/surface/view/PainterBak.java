package dev.frankie.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class PainterBak {

    private final View view;
    private DisplayMetrics metrics;

    private float ecgMax = 4096;// 心电的最大值
    private int bgColor = Color.BLACK;

    private float lockWidth;// 每次锁屏需要画的
    private int ecgPerCount = 8;// 每次画心电数据的个数，心电每秒有500个数据包

    private Queue<Float> ecg0Data = new LinkedList<>();
    private Queue<Float> ecg1Data = new LinkedList<>();

    private final Paint paint;//画波形图的画笔
    private float ecgYRatio;
    private float startY0;
    private float startY1;
    private float yOffset1;//波2的Y坐标偏移值
    private final Rect rect = new Rect();

    private int startX; // 每次画线的X坐标起点
    private float ecgXOffset;//每次X坐标偏移的像素
    private float blankLineWidth = 6;//右侧空白点的宽度

    private int width;
    private int height;

    public PainterBak(View view, float lockWidth) {
        this.view = view;
        this.metrics = view.getResources().getDisplayMetrics();
        this.lockWidth = lockWidth;
        this.blankLineWidth = metrics.density * 6;
        this.paint = createPaint(Color.GREEN, 3);
    }

    public void onSizeChanged(int width, int height, int oldw, int oldh) {
        this.width = width;
        this.height = height;

        ecgXOffset = lockWidth / ecgPerCount;
        startY0 = height * (1.0f / 4);// 波1初始Y坐标是控件高度的1/4
        startY1 = height * (3.0f / 4);
        ecgYRatio = height / 2 / ecgMax;
        yOffset1 = height / 2;
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
                float newX = startX + ecgXOffset * ecgPerCount;
                float newY = ecgConvert(ecgMax / 2);
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


    public Canvas lockCanvas(final SurfaceHolder holder) {
        rect.set(startX, 0, (int) (startX + lockWidth + blankLineWidth), height);
        return holder.lockCanvas(rect);
    }


    public void draw(Canvas canvas) {
        canvas.drawColor(bgColor);
        drawWave0(canvas);
        drawWave1(canvas);

        startX = (int) (startX + lockWidth);
        if (startX > width) {
            startX = 0;
        }
    }

    /**
     * 将心电数据转换成用于显示的Y坐标
     */
    private float ecgConvert(Float data) {
        return (ecgMax - data) * ecgYRatio;
    }

    public void addEcgData0(Float data) {
        ecg0Data.add(data);
    }

    public void addEcgData1(Float data) {
        ecg1Data.add(data);
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

    public static Paint createPaint(int color, int width) {
        Paint paint = new Paint();
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(width);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(color);
        return paint;
    }


}
