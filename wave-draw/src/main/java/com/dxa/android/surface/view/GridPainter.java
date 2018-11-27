package com.dxa.android.surface.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class GridPainter extends BasePainter {

//    private static final int COLOR_THIN = Color.parseColor("#DD0000");
//    private static final int COLOR_THICK = Color.parseColor("#FF0000");

    private static final int COLOR_THIN = Color.parseColor("#092100");
    private static final int COLOR_THICK = Color.parseColor("#1b4200");

    private static final float WIDTH_THIN = 1.5f;
    private static final float WIDTH_THICK = 3.0f;
    /**
     * 小网格的数量
     */
    private static final int SG_COUNT = 5;
    private static final int GRID_COUNT = 20;

    private Paint paint;


    private int thinColor = COLOR_THIN;
    private int thickColor = COLOR_THICK;

    private float thinWidth = WIDTH_THIN;
    private float thickWidth = WIDTH_THICK;

    // 画横线
    private int horizontalCount; // horizontal
    // 画竖线
    private int verticalCount; // vertical
    /**
     * 网格的数量: 宽度
     */
    private int gridCount = GRID_COUNT;
    /**
     * 小网格的宽度
     */
    private int sgSize;
    /**
     * 小网格的数量
     */
    private final int sgCount = SG_COUNT;
    /**
     * 背景色，默认黑色
     */
    private int bgColor = Color.BLACK;
    /**
     * 线的宽度和高度
     */
    private int lineWidth, lineHeight;
    /**
     * 空余的宽度和高度
     */
    private int paddingWidth, paddingHeight;


    public GridPainter(float density) {
        // 网格的线
        this.paint = createPaint(thinColor, thinWidth, 8 * density);
    }

    /**
     * 绘制背景
     */
    public void draw(Canvas canvas) {
        // 背景
        canvas.drawColor(bgColor);

        // 绘制网格竖线
        int gridSize = getSgSize();
        int sgCount = getSgCount();
        int verticalCount = getVerticalCount();
        int horizontalCount = getHorizontalCount();

        // 绘制小网格
        paint.setColor(thinColor);
        paint.setStrokeWidth(thinWidth);
        for (int i = 0; i <= verticalCount; i++) {
            drawVerticalLine(canvas, paint, getStartX(i, gridSize));
        }
        for (int i = 0; i <= horizontalCount; i++) {
            drawHorizontalLine(canvas, paint, getStartY(i, gridSize));
        }

        paint.setColor(thickColor);
        paint.setStrokeWidth(thickWidth);
        for (int i = 0; i <= verticalCount; i += sgCount) {
            drawVerticalLine(canvas, paint, getStartX(i, gridSize));
        }
        for (int i = 0; i <= horizontalCount; i += sgCount) {
            drawHorizontalLine(canvas, paint, getStartY(i, gridSize));
        }
    }


    /**
     * 初始化网格参数
     *
     * @param width  视图的宽度
     * @param height 视图的高度
     */
    public void onSizeChanged(int width, int height) {
        int sgCount = getSgCount();
        sgSize = calSgSize(width, getGridCount(), sgCount);
        // 网格数
        horizontalCount = calHorizontalCount(height, sgSize, sgCount);
        verticalCount = calVerticalCount(width, sgSize, sgCount);

        // 保持横向有20个大格子(代表是5秒心电)
        lineHeight = sgSize * horizontalCount;
        lineWidth = sgSize * verticalCount;

        paddingWidth = (width - lineWidth) / 2;
        paddingHeight = (height - lineHeight) / 2;

        // 计算缩放比: 最大值 / height
    }

    /**
     * 计算小网格的大小
     *
     * @param width     宽度
     * @param gridCount 大网格的数量
     * @param sgCount   每个大网格对应的小网格的数量
     * @return
     */
    public int calSgSize(int width, int gridCount, int sgCount) {
        return (int) (width / (gridCount * sgCount * 1.0f));
    }

    /**
     * 计算竖线的数量
     *
     * @param width   宽度
     * @param sgSize  小网格的大小
     * @param sgCount 小网格的数量
     * @return 返回竖线的数量
     */
    public int calVerticalCount(int width, int sgSize, int sgCount) {
        return (width / (sgSize * sgCount)) * sgCount;
    }

    /**
     * 计算横线的数量
     *
     * @param height  高度
     * @param sgSize  小网格的大小
     * @param sgCount 小网格的数量，默认是5
     * @return 返回横线的数量
     */
    public int calHorizontalCount(int height, int sgSize, int sgCount) {
        return (height / (sgSize * sgCount)) * sgCount;
    }

    public int getSgCount() {
        return sgCount;
    }

    /**
     * 绘制竖线
     *
     * @param canvas Canvas
     * @param paint Paint
     * @param startX 开始的 X 点
     */
    public void drawVerticalLine(Canvas canvas, Paint paint, float startX) {
        canvas.drawLine(startX, getPaddingHeight(), startX, getStopY(), paint);
    }

    /**
     * 绘制横线
     *
     * @param canvas Canvas
     * @param paint Paint
     * @param startY 开始的 Y 点
     */
    public void drawHorizontalLine(Canvas canvas, Paint paint, float startY) {
        canvas.drawLine(getPaddingWidth(), startY, getStopX(), startY, paint);
    }

    /**
     * 获取开始的 X 点
     *
     * @param i 第几条线
     * @param gridSize 网格的大小
     * @return 返回开始的 X 点
     */
    public float getStartX(int i, float gridSize) {
        return i * gridSize + getPaddingWidth();
    }

    /**
     * 获取开始的 Y 点
     *
     * @param i 第几条线
     * @param gridSize 网格的大小
     * @return 返回开始的 Y 点
     */
    public float getStartY(int i, float gridSize) {
        return i * gridSize + getPaddingHeight();
    }

    public float getStopX() {
        return getLineWidth() + getPaddingWidth();
    }

    public float getStopY() {
        return getLineHeight() + getPaddingHeight();
    }


    public int getGridCount() {
        return gridCount;
    }

    public void setGridCount(int gridCount) {
        this.gridCount = gridCount;
    }

    public int getHorizontalCount() {
        return horizontalCount;
    }

    public int getVerticalCount() {
        return verticalCount;
    }

    public int getSgSize() {
        return sgSize;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public int getLineHeight() {
        return lineHeight;
    }

    public int getPaddingWidth() {
        return paddingWidth;
    }

    public int getPaddingHeight() {
        return paddingHeight;
    }
}
