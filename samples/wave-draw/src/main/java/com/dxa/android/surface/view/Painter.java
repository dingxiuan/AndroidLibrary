package com.dxa.android.surface.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;

public class Painter extends BasePainter {

    private final DisplayMetrics metrics;

    private float maxPoint = 4096;// 心电的最大值
//    /**
//     * 波速: 25mm/s
//     */
//    private int waveSpeed = 25;
//    /**
//     * 每次休眠的时间间距，单位:ms
//     */
//    private int sleepTime = 8;

    private float lockWidth;// 每次锁屏需要画的
    private int perCount = 8;// 每次画心电数据的个数，心电每秒有500个数据包

    private final Paint wavePaint;//画波形图的画笔
    private final Rect rect = new Rect();
    /**
     * Y轴的缩放比
     */
    private float scaleRatio;
    private float xOffset;// X坐标偏移的像素

    private float pointX; // X坐标起点
    private float pointY; // Y坐标起点

    private float blankLineWidth = 6;//右侧空白点的宽度

    private int width;
    private int height;
    private int paddingWidth;
    private int paddingHeight;

    public Painter(DisplayMetrics metrics) {
        this.metrics = metrics;
        this.lockWidth = (float) Painter.calOffset(metrics, 25, 16);
        this.blankLineWidth = metrics.density * 8;
        this.wavePaint = createPaint(Color.GREEN, 3, 8 * metrics.scaledDensity);
    }

    public void onSizeChanged(int width, int height, int paddingWidth, int paddingHeight) {
        this.width = width;
        this.height = height;
        this.paddingWidth = paddingWidth;
        this.paddingHeight = paddingHeight;

        // 每次的偏移量
        xOffset = lockWidth / perCount;
        // 初始Y坐标
        pointY = height * 0.5f;
        // 缩放比
        scaleRatio = height / maxPoint;
    }

    /**
     * 绘制波形
     */
    public boolean drawWave(Canvas canvas, float point) {
        float newX = pointX + xOffset * perCount;
        float newY = ecgConvert(point);
        canvas.drawLine(pointX, pointY, newX, newY, wavePaint);
        this.pointX = newX;
        this.pointY = newY;
        if (newX > (width - paddingWidth)) {
            this.pointX = 0;
            return true;
        }
        return false;
    }


    /**
     * 绘制波形
     */
    private void drawWave(Canvas canvas, float[] points) {
        float startX = pointX;
        if (points != null && points.length > 0) {
            float newX, newY;
            for (float point : points) {
                newX = startX + xOffset;
                newY = ecgConvert(point);
                canvas.drawLine(startX, pointY, newX, newY, wavePaint);
                startX = newX;
                pointY = newY;
            }
        } else {
            // 如果没有数据，因为有数据一次画ecgPerCount个数，
            // 那么无数据时候就应该画pointPercount倍数长度的中线
            float newX = startX + xOffset * perCount;
            float newY = ecgConvert(maxPoint / 2);
            canvas.drawLine(startX, pointY, newX, newY, wavePaint);
            pointY = newY;
        }

        pointX = pointX + lockWidth;
        if (pointX > width - paddingWidth) {
            pointX = 0;
        }
    }

    public Canvas lockCanvas(final SurfaceHolder holder) {
        rect.set((int) pointX, 0, (int) (pointX + lockWidth + blankLineWidth), height);
        return holder.lockCanvas(rect);
    }

    /**
     * 将心电数据转换成用于显示的Y坐标
     */
    private float ecgConvert(float data) {
        return (maxPoint - data) * scaleRatio;
    }

    /**
     * 根据波速计算每次X坐标增加的像素：计算出每次休眠时需要绘制的px值
     *
     * @param metrics   DisplayMetrics
     * @param waveSpeed 波形绘制的速度：25mm（每秒2.5厘米）
     * @param sleepTime 线程休眠时长：假如设备的采样率是 500，波形每秒绘制8个点 =>: 1000毫秒 / (500 / 8) = 休眠时长
     * @return
     */
    public static double calOffset(DisplayMetrics metrics, int waveSpeed, int sleepTime) {
//        int width = metrics.widthPixels;
//        int height = metrics.heightPixels;
//        double diagonalPx = Math.sqrt(width * width + height * height);
//        //获取屏幕对角线的长度，单位:px
//        double diagonalMm = (diagonalPx / metrics.densityDpi) * 2.54 * 10;//转换单位为：毫米
        // 每毫米有多少px
//        double px1mm = diagonalPx / diagonalMm;
        double px1mm = calPxCountOfMm(metrics);
        // 每秒画多少px
        double px1s = waveSpeed * px1mm;
        // 每次锁屏所需画的宽度
        return px1s * sleepTime / 1000;
    }


    /**
     * 计算每毫米有多少像素
     *
     * @param metrics DisplayMetrics
     * @return 每毫米的像素数量
     */
    public static double calPxCountOfMm(DisplayMetrics metrics) {
        // 屏幕对角线的长度，单位: px
        double diagonalPX = Math.sqrt(Math.pow(metrics.widthPixels, 2) + Math.pow(metrics.heightPixels, 2));
        // 屏幕对角线的长度，单位: mm
        double diagonalMM = (diagonalPX / metrics.densityDpi) * 2.54 * 10;
        return diagonalPX / diagonalMM;
    }

    /**
     * 计算每英寸有多少像素
     *
     * @param metrics DisplayMetrics
     * @return 每毫米的像素数量
     */
    public static double calPxCountOfInch(DisplayMetrics metrics) {
        // 屏幕对角线的长度，单位: px
        double diagonalPX = Math.sqrt(Math.pow(metrics.widthPixels, 2) + Math.pow(metrics.heightPixels, 2));
        // 屏幕对角线的长度，单位: inch
        return (diagonalPX / metrics.densityDpi);
    }
}
