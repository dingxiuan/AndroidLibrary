package com.dxa.androidview.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 绘制网格
 */
public class GridBackgroundView extends View {
    /**
     * 大网格
     */
    private Paint thickPaint;
    /**
     * 小网格
     */
    private Paint thinPaint;
    /**
     * 粗线条
     */
    private Paint xAxisPaint;

    /**
     * 画横线
     */
    private int heightCount;
    /**
     * 画竖线
     */
    private int widthCount;

    /**
     * 小网格的宽度
     */
    private int gridSize;
    /**
     * 每个大网格内的小网格数
     */
    private int smallGridCount = 5;
    /**
     * 背景色，默认白色
     */
    private int backgroundColor = Color.BLACK;

    /**
     * 线的宽度和高度
     */
    private int lineWidth, lineHeight;
    /**
     * 空余的宽度和高度
     */
    private int paddingWidth, paddingHeight;

    /**
     * 粗线条的宽度
     */
    private float thickLineWidth = 3.0f;
    /**
     * 细线条的宽度
     */
    private float thinLineWidth = 1.5f;
    /**
     * x轴线的宽度（心电图线的宽度）
     */
    private float xAxis = 3.5f;

    private int halfHeight = 0;

    public GridBackgroundView(Context context) {
        this(context, null);
    }

    public GridBackgroundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 设置背景
        initialize();
    }


    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        /*
         *  粗线条p1，细线条p2，计算得到最小格子的宽度,widthCount = 20
         */
        gridSize = (width / 20) / 5;

        /*
         * 大网格的数量乘以小网格的数量 = 宽(高)中网格的数量
         * （1）种能保证网格的完整，四周切掉多余的小网格，露出空白
         * （2）种是填满屏幕，会出现不是大网格粗线结尾
         */

        // (1)网格数
        heightCount = (height / (gridSize * smallGridCount)) * smallGridCount;
        //(2)网格数
        widthCount = width / gridSize;

        lineWidth = gridSize * widthCount;
        //为保持横向有20个大格子(代表是5秒心电)
        lineHeight = gridSize * heightCount;

        paddingWidth = (width - lineWidth) / 2;
        paddingHeight = (height - lineHeight) / 2;

        halfHeight = height / 2;
        super.onSizeChanged(width, height, oldWidth, oldHeight);
    }

    /**
     * 创建普通的画笔
     *
     * @param color       画笔颜色
     * @param strokeWidth 线条粗细
     * @return
     */
    private Paint createNormalPaint(int color, float strokeWidth) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        return paint;
    }

    /**
     * 初始化画笔
     */
    private void initialize() {
        // 大网格的线
        thickPaint = createNormalPaint(Color.parseColor("#1b4200"), thickLineWidth);

        // 小网格的线
        thinPaint = createNormalPaint(Color.parseColor("#092100"), thinLineWidth);
        // X 轴的线
        xAxisPaint = createNormalPaint(Color.BLACK, xAxis);
        xAxisPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * 绘制网格背景
     */
    private void drawGridBackground(Canvas canvas) {
        // 背景
        canvas.drawColor(backgroundColor);

        // 绘制小网格竖线
        int startX;
        for (int i = 0; i <= widthCount; i++) {
            startX = i * gridSize + paddingWidth;
            canvas.drawLine(startX, paddingHeight, startX, lineHeight + paddingHeight, thinPaint);
        }
        // 绘制小网格横线
        int startY;
        for (int i = 0; i <= heightCount; i++) {
            startY = i * gridSize + paddingHeight;
            canvas.drawLine(paddingWidth, startY, lineWidth + paddingWidth, startY, thinPaint);
        }

        // 绘制大网格竖线
        for (int i = 0; i <= widthCount; i += 5) {
            startX = i * gridSize + paddingWidth;
            // 每隔5个小格画一个粗线条
            canvas.drawLine(startX, paddingHeight, startX, lineHeight + paddingHeight, thickPaint);
        }

        // 绘制大网格横线
        for (int i = 0; i <= heightCount; i += 5) {
            startY = i * gridSize + paddingHeight;
            canvas.drawLine(paddingWidth, startY, lineWidth + paddingWidth, startY, thickPaint);
        }
        // x轴
//        canvas.drawLine(paddingWidth, (lineHeight + paddingHeight) / 2,
//                lineWidth + paddingWidth, (lineHeight + paddingHeight) / 2, xAxisPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawGridBackground(canvas);
    }

}
