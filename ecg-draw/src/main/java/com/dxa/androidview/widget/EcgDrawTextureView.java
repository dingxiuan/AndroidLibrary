package com.dxa.androidview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.dxa.android.logger.DLogger;

public class EcgDrawTextureView extends View {
    private static final DLogger logger = new DLogger(EcgDrawTextureView.class);

    private final GridPainter gridPainter;

    public EcgDrawTextureView(Context context) {
        this(context, null);
    }

    public EcgDrawTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EcgDrawTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.gridPainter = new GridPainter(context.getResources().getDisplayMetrics());
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        gridPainter.onSizeChanged(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制背景网格
        gridPainter.drawBackground(canvas);
    }

}
