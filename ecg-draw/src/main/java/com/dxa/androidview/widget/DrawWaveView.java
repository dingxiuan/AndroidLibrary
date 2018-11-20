package com.dxa.androidview.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.dxa.android.logger.DLogger;

/**
 * @author DINGXIUAN
 */
public class DrawWaveView extends View {
    private static final DLogger LOGGER = new DLogger(DrawWaveView.class);

    private Path path;
    private Paint paint = null;
    private Canvas cacheCanvas = null;
    private int scalePoint = 0;
    private int currentPoint = 0;
    private int maxPoint = 0;
    private int minPoint = 0;
    private float previousX = 0;
    private float previousY = 0;

    private float scaleHeight = 0;
    private float scaleWidth = 0;

    private boolean initFlag = false;

    private final Paint clearPaint = new Paint();

    public DrawWaveView(Context context) {
        super(context);
    }

    public DrawWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);

        cacheCanvas = new Canvas();
        path = new Path();

        paint = new Paint(Paint.DITHER_FLAG);
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
        paint.setDither(true);

        currentPoint = 0;
        clearPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        setValue(1024, 4000, -4000);
//        setValue(2048, 8000, -8000);
//        setValue(2048, 10000, -10000);
//        setValue(w * 8, h, 0);
    }

    public void setValue(int scalePoint, int maxPoint, int minPoint) {
        this.scalePoint = scalePoint;
        this.maxPoint = maxPoint;
        this.minPoint = minPoint;
    }

    public void updateDensity() {
        if (isReady()) {
            scaleHeight = (float) this.getHeight() / (maxPoint - minPoint);
            scaleWidth = (float) this.getWidth() / scalePoint;
            clear();
        }
    }

    public void initView() {
        scaleHeight = (float) this.getHeight() / (maxPoint - minPoint);
        scaleWidth = (float) this.getWidth() / scalePoint;
        currentPoint = 0;

        LOGGER.e("left: ", getLeft(), ", top: ", getTop(), ", right: ", getRight(), ", bottom: ", getBottom());
        LOGGER.e("width: ", getWidth(), ", height: ", getHeight());
        LOGGER.e("scaleHeight: ", scaleHeight, ", scaleWidth: ", scaleWidth);
    }


    public void clear() {
        cacheCanvas.drawPaint(clearPaint);
        clearPaint.setXfermode(new PorterDuffXfermode(Mode.SRC));
        currentPoint = 0;
        path.reset();
        invalidate();
    }

    public boolean isReady() {
        return initFlag;
    }

    public void updatePoint(int point) {
        if (!initFlag) {
            return;
        }

        float y = this.getBottom() - ((point - minPoint) * scaleHeight);
        float x = this.getLeft() + currentPoint * scaleWidth;
        LOGGER.i("point(", point, ") ==>: (", x, ", ", y, ")");

        if (currentPoint == 0) {
            path.moveTo(x, y);
            currentPoint++;
            previousX = x;
            previousY = y;
        } else if (currentPoint == scalePoint) {
            cacheCanvas.drawPath(path, paint);
            currentPoint = 0;
        } else {
            path.quadTo(previousX, previousY, x, y);
            currentPoint++;
            previousX = x;
            previousY = y;
        }
        invalidate();
        if (currentPoint == 0) {
            clear();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LOGGER.d("onConfigurationChanged");
        initFlag = false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LOGGER.d("onAttachedToWindow");
        initFlag = false;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        LOGGER.d("onLayout");
        if (!initFlag) {
            initView();
            initFlag = true;
        }
    }


}
