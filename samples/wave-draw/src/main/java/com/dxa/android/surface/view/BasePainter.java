package com.dxa.android.surface.view;

import android.graphics.Paint;

public class BasePainter {


    /**
     * 创建普通的画笔
     *
     * @param color       画笔颜色
     * @param strokeWidth 线条粗细
     * @return 返回创建的Paint
     */
    public static Paint createPaint(int color, float strokeWidth, float textSize) {
        Paint paint = new Paint(Paint.DITHER_FLAG);
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setTextSize(textSize);
        return paint;
    }
}
