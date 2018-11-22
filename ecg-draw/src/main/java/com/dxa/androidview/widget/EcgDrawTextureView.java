package com.dxa.androidview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.dxa.android.logger.DLogger;

public class EcgDrawTextureView extends View {
    private static final DLogger logger = new DLogger(EcgDrawTextureView.class);

    private final Painter painter;

    public EcgDrawTextureView(Context context) {
        this(context, null);
    }

    public EcgDrawTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EcgDrawTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.painter = new Painter(context.getResources().getDisplayMetrics());
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        painter.init(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制背景网格
        painter.drawBackground(canvas);
    }


    public static class Painter {
        private static final int COLOR_THIN = Color.parseColor("#DD0000");
        private static final int COLOR_THICK = Color.parseColor("#FF0000");

//        private static final int COLOR_THIN = Color.parseColor("#092100");
//        private static final int COLOR_THICK = Color.parseColor("#1b4200");

        private static final float WIDTH_THIN = 1.5f;
        private static final float WIDTH_THICK = 3.0f;

        private final float textSize;

        private int width;
        private int height;


        // 网格
        private Paint thinPaint;
        private Paint thickPaint;

        /**
         * 小网格的数量
         */
        private int smallGridCount = 5;
        /**
         * 网格的数量: 宽度
         */
        private int gridCount = 20;

        private int thinColor = COLOR_THIN;
        private int thickColor = COLOR_THICK;

        private float thinWidth = WIDTH_THIN;
        private float thickWidth = WIDTH_THICK;

        // 画横线
        private int horizontalCount; // horizontal
        // 画竖线
        private int verticalCount; // vertical

        // 小网格的宽度
        private int gridSize;
        // 背景色，默认白色
        private int backgroundColor = Color.WHITE;
        // 线的宽度和高度
        private int lineWidth, lineHeight;
        // 空余的宽度和高度
        private int paddingWidth, paddingHeight;

        private final DisplayMetrics metrics;

        public Painter(DisplayMetrics metrics) {
            logger.e("metrics ==>: ", metrics);
            this.metrics = metrics;
            this.textSize = 8 * metrics.scaledDensity;
            // 网格的线
            thinPaint = createPaint(thinColor, thinWidth);
            thickPaint = createPaint(thickColor, thickWidth);
        }

        /**
         * 初始化网格参数
         *
         * @param width  视图的宽度
         * @param height 视图的高度
         */
        private void init(int width, int height) {
            this.width = width;
            this.height = height;

            int maxCount = width;
//            int maxCount = width > height ? width : height;
            gridSize = (maxCount / gridCount) / smallGridCount;
            // 网格数
            horizontalCount = (height / (gridSize * smallGridCount)) * smallGridCount;
            verticalCount = (width / (gridSize * smallGridCount)) * smallGridCount;

            // 保持横向有20个大格子(代表是5秒心电)
            lineHeight = gridSize * horizontalCount;
            lineWidth = gridSize * verticalCount;

            paddingWidth = (width - lineWidth) / 2;
            paddingHeight = (height - lineHeight) / 2;

            // 计算缩放比
        }

        /**
         * 绘制小网格竖线
         */
        private void drawGrid(Canvas canvas, int verticalCount, int horizontalCount, int gridSize, int smallGridCount,
                             int paddingWidth, int paddingHeight, int lineWidth, int lineHeight) {
            // 绘制小网格竖线
            int startX;
            Paint vPaint;
            for (int i = 0; i <= verticalCount; i++) {
                startX = i * gridSize + paddingWidth;
                vPaint = i % smallGridCount == 0 ? getThickPaint() : getThinPaint();
                canvas.drawLine(startX, paddingHeight, startX, lineHeight + paddingHeight, vPaint);

                // 绘制文字
                if (vPaint == getThickPaint()) {
                    float size = vPaint.measureText(String.valueOf(i / 5));
                    canvas.drawText(String.valueOf(i / 5), ((startX - gridSize * 2.5f)) - size / 2, paddingHeight, vPaint);
                }
            }
            // 绘制小网格横线
            int startY;
            Paint hPaint;
            for (int i = 0; i <= horizontalCount; i++) {
                startY = i * gridSize + paddingHeight;
                hPaint = i % smallGridCount == 0 ? getThickPaint() : getThinPaint();
                canvas.drawLine(paddingWidth, startY, lineWidth + paddingWidth, startY, hPaint);
            }
        }

        /**
         * 绘制背景
         */
        public void drawBackground(Canvas canvas) {
            // 背景
            canvas.drawColor(backgroundColor);
            // 绘制网格竖线
            drawGrid(canvas, verticalCount, horizontalCount, gridSize, smallGridCount, paddingWidth, paddingHeight, lineWidth, lineHeight);
//            // x轴
//            canvas.drawLine(paddingWidth, (lineHeight + paddingHeight) / 2,
//                    lineWidth + paddingWidth, (lineHeight + paddingHeight) / 2, xAxisPaint);

            logger.e("绘制背景网格");
        }

        /**
         * 创建普通的画笔
         *
         * @param color       画笔颜色
         * @param strokeWidth 线条粗细
         * @return 返回创建的Paint
         */
        public Paint createPaint(int color, float strokeWidth) {
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

        public Paint getThickPaint() {
            return thickPaint;
        }

        public Paint getThinPaint() {
            return thinPaint;
        }
    }

}
