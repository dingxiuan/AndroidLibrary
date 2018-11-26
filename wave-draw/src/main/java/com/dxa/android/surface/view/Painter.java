package dev.frankie.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.View;

public class Painter {

    private final View view;
    private DisplayMetrics metrics;

    private float ecgMax = 4096;// 心电的最大值
    private int bgColor = Color.BLACK;

    private float lockWidth;// 每次锁屏需要画的
    private int ecgPerCount = 8;// 每次画心电数据的个数，心电每秒有500个数据包

    private final Paint paint;//画波形图的画笔
    private float ecgYRatio;
    private float pointY;
    private final Rect rect = new Rect();

    private int pointX; // 每次画线的X坐标起点
    private float ecgXOffset;//每次X坐标偏移的像素
    private float blankLineWidth = 6;//右侧空白点的宽度

    private int width;
    private int height;

    public Painter(View view, float lockWidth) {
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
        // 波1初始Y坐标是控件高度的1/2
        pointY = height * (1.0f / 2);
        ecgYRatio = height / ecgMax;
    }


    /**
     * 画波1
     */
    private void drawWave(Canvas canvas, float[] points) {
        float startX = pointX;
        if (points != null) {
            float newX, newY;
            for (float point : points) {
                newX = startX + ecgXOffset;
                newY = ecgConvert(point);
                canvas.drawLine(startX, pointY, newX, newY, paint);
                startX = newX;
                pointY = newY;
            }
        } else {
            // 如果没有数据，因为有数据一次画ecgPerCount个数，
            // 那么无数据时候就应该画ecgPercount倍数长度的中线
            float newX = startX + ecgXOffset * ecgPerCount;
            float newY = ecgConvert(ecgMax / 2);
            canvas.drawLine(startX, pointY, newX, newY, paint);
            pointY = newY;
        }
    }

    public Canvas lockCanvas(final SurfaceHolder holder) {
        rect.set(pointX, 0, (int) (pointX + lockWidth + blankLineWidth), height);
        return holder.lockCanvas(rect);
    }

    public void draw(Canvas canvas, float[] points) {
        drawWave(canvas, points);
        pointX = (int) (pointX + lockWidth);
        if (pointX > width) {
            pointX = 0;
        }
    }

    /**
     * 将心电数据转换成用于显示的Y坐标
     */
    private float ecgConvert(float data) {
        return (ecgMax - data) * ecgYRatio;
    }

    /**
     * 根据波速计算每次X坐标增加的像素
     * <p>
     * 计算出每次锁屏应该画的px值
     */
    public static float calculateOffset(DisplayMetrics metrics, int waveSpeed, int sleepTime) {
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
