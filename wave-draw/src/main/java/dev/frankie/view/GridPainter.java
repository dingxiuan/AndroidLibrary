package dev.frankie.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;

import com.dxa.android.logger.DLogger;

public class GridPainter {
    private static final DLogger logger = DLogger.getLogger(GridPainter.class);

//    private static final int COLOR_THIN = Color.parseColor("#DD0000");
//    private static final int COLOR_THICK = Color.parseColor("#FF0000");

    private static final int COLOR_THIN = Color.parseColor("#092100");
    private static final int COLOR_THICK = Color.parseColor("#1b4200");

    private static final float WIDTH_THIN = 1.5f;
    private static final float WIDTH_THICK = 3.0f;

    private final float textSize;

    // 网格
    private Paint gridPaint;

    /**
     * 小网格的数量
     */
    private int sgCount = 5;
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
//    private int backgroundColor = Color.WHITE;
    private int backgroundColor = Color.BLACK;
    // 线的宽度和高度
    private int lineWidth, lineHeight;
    // 空余的宽度和高度
    private int paddingWidth, paddingHeight;

    private final DisplayMetrics metrics;

    public GridPainter(DisplayMetrics metrics) {
        logger.e("metrics ==>: ", metrics);
        this.metrics = metrics;
        this.textSize = 8 * metrics.scaledDensity;
        // 网格的线
        gridPaint = createPaint(thinColor, thinWidth, textSize);
    }

    /**
     * 初始化网格参数
     *
     * @param width  视图的宽度
     * @param height 视图的高度
     */
    public void onSizeChanged(int width, int height) {
        int maxCount = width;
//            int maxCount = width > height ? width : height;
        gridSize = (maxCount / gridCount) / sgCount;
        // 网格数
        horizontalCount = (height / (gridSize * sgCount)) * sgCount;
        verticalCount = (width / (gridSize * sgCount)) * sgCount;

        // 保持横向有20个大格子(代表是5秒心电)
        lineHeight = gridSize * horizontalCount;
        lineWidth = gridSize * verticalCount;

        paddingWidth = (width - lineWidth) / 2;
        paddingHeight = (height - lineHeight) / 2;

        // 计算缩放比
    }

    /**
     * 绘制网格
     */
    public void drawGrid(Canvas canvas, int verticalCount, int horizontalCount, int gridSize, int sgCount,
                         int paddingWidth, int paddingHeight, int lineWidth, int lineHeight) {
        // 绘制小网格
        int startX, startY;
        gridPaint.setColor(thinColor);
        gridPaint.setStrokeWidth(thinWidth);
        for (int i = 0; i <= verticalCount; i++) {
            startX = i * gridSize + paddingWidth;
            canvas.drawLine(startX, paddingHeight, startX, lineHeight + paddingHeight, gridPaint);
        }
        for (int i = 0; i <= horizontalCount; i++) {
            startY = i * gridSize + paddingHeight;
            canvas.drawLine(paddingWidth, startY, lineWidth + paddingWidth, startY, gridPaint);
        }

        gridPaint.setColor(thickColor);
        gridPaint.setStrokeWidth(thickWidth);
        for (int i = 0; i <= verticalCount; i += sgCount) {
            startX = i * gridSize + paddingWidth;
            canvas.drawLine(startX, paddingHeight, startX, lineHeight + paddingHeight, gridPaint);
        }
        for (int i = 0; i <= horizontalCount; i += sgCount) {
            startY = i * gridSize + paddingHeight;
            canvas.drawLine(paddingWidth, startY, lineWidth + paddingWidth, startY, gridPaint);
        }
    }

    private void setPaint(int i) {
        if (i % sgCount == 0) {
            gridPaint.setColor(thickColor);
            gridPaint.setStrokeWidth(thickWidth);
        } else if (i % sgCount == 1) {
            gridPaint.setColor(thinColor);
            gridPaint.setStrokeWidth(thinWidth);
        }
    }

    /**
     * 绘制背景
     */
    public void drawBackground(Canvas canvas) {
        // 背景
        canvas.drawColor(backgroundColor);
        // 绘制网格竖线
        drawGrid(canvas, verticalCount, horizontalCount, gridSize, sgCount, paddingWidth, paddingHeight, lineWidth, lineHeight);
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
