package com.dxa.androidview.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.TextureView;

import com.dxa.android.logger.DLogger;

/**
 * 绘制的SurfaceView：使用Timer开启TimerTask绘制图形
 */
public class EcgTextureView extends TextureView {

    private static final DLogger LOGGER = new DLogger(EcgTextureView.class);


    private int scalePoint = 0;
    private int currentPoint = 0;
    private int maxPoint = 0;
    private int minPoint = 0;
    private float previousX = 0;
    private float previousY = 0;

    private float scaleHeight = 0;
    private float scaleWidth = 0;

    private volatile boolean initFlag = false;

    private Path path = new Path();
    private Paint pathPaint;
    private Paint clearPaint;
    private Bitmap bitmapCache;
    private Canvas canvasCache = new Canvas();

    // 大网格
    private Paint thickPaint;
    // 小网格
    private Paint thinPaint;
    // 粗线条
    private Paint xAxisPaint;

    // 画横线
    private int heightCount;
    // 画竖线
    private int widthCount;

    // 小网格的宽度
    private int gridSize;
    // 每个大网格内的小网格数
    private int smallGridCount = 5;
    // 背景色，默认白色
    private int backgroundColor = Color.BLACK;
    // 线的宽度和高度
    private int lineWidth, lineHeight;
    // 空余的宽度和高度
    private int paddingWidth, paddingHeight;

    // 粗线条的宽度
    private float thickLineWidth = 3.0f;
    // 细线条的宽度
    private float thinLineWidth = 1.5f;
    // x轴线的宽度（心电图线的宽度）
    private float xAxis = 2.5f;

    private int halfHeight = 0;

    public EcgTextureView(Context context) {
        this(context, null);
    }

    public EcgTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EcgTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        pathPaint = createNormalPaint(Color.GREEN, 2);
        currentPoint = 0;
        clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        // 大网格的线
        thickPaint = createNormalPaint(Color.parseColor("#1b4200"), thickLineWidth);
        // 小网格的线
        thinPaint = createNormalPaint(Color.parseColor("#092100"), thinLineWidth);
        // X 轴的线
        xAxisPaint = createNormalPaint(Color.BLACK, xAxis);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
//        setValue(1024, 4000, -4000);
//        setValue(2048, 8000, -8000);
//        setValue(2048, 10000, -10000);
//        setValue(width * 8, height, 0);

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
    }


    /**
     * 创建普通的画笔
     *
     * @param color       画笔颜色
     * @param strokeWidth 线条粗细
     * @return
     */
    private Paint createNormalPaint(int color, float strokeWidth) {
        Paint paint = new Paint(Paint.DITHER_FLAG);
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setDither(true);
        return paint;
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

        LOGGER.e("绘制背景网格");
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
//            clear(canvas);
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


    public void clear(Canvas canvas, Path path, Paint clearPaint) {
        canvas.drawPaint(clearPaint);
        path.reset();
        canvas.drawColor(Color.BLACK, PorterDuff.Mode.SRC);
        drawGridBackground(canvas);
        canvas.drawPath(path, pathPaint);
        currentPoint = 0;
    }

    public boolean isReady() {
        synchronized (lock) {
            return initFlag;
        }
    }


    public void updatePoint(Canvas canvas, int point) {
        if (!initFlag) {
            return;
        }

        if (bitmapCache == null) {
            bitmapCache = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            canvasCache.setBitmap(bitmapCache);
        }

        if (currentPoint == 0) {
            clear(canvasCache, path, clearPaint);
        }

        float y = this.getBottom() - ((point - minPoint) * scaleHeight);
        float x = this.getLeft() + currentPoint * scaleWidth;

        if (currentPoint == 0) {
            path.moveTo(x, y);
            currentPoint++;
            previousX = x;
            previousY = y;
        } else if (currentPoint == scalePoint) {
            canvasCache.drawPath(path, pathPaint);
            currentPoint = 0;
        } else {
            path.quadTo(previousX, previousY, x, y);
            currentPoint++;
            previousX = x;
            previousY = y;
        }
        // 绘制
        canvasCache.drawPath(path, pathPaint);
        canvas.drawBitmap(bitmapCache, 0, 0, pathPaint);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LOGGER.d("onConfigurationChanged");
        synchronized (lock) {
            initFlag = false;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LOGGER.d("onAttachedToWindow");
        synchronized (lock) {
            initFlag = false;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        LOGGER.d("onLayout");
        synchronized (lock) {
            if (!initFlag) {
                initView();
                initFlag = true;
            }
        }
    }


    /**************************************************************************************************/

//    private static final String TASK_NAME = "DrawTask";
    private final Object lock = new Object();
//    private final Timer drawTimer = new Timer(true);
//
//    @Override
//    public void surfaceCreated(final SurfaceHolder holder) {
//        LOGGER.d("surfaceCreated");
//        this.initFlag = true;
//
//        drawTimer.cancel(TASK_NAME);
//        drawTimer.scheduleAtFixedRate(new TimerTask(TASK_NAME) {
//            @Override
//            public void run() {
//                synchronized (lock) {
//                    if (initFlag) {
//                        final SurfaceHolder holder = getHolder();
//                        final Canvas canvas = holder.lockCanvas();
//                        try {
//                            updatePoint(canvas, );
//                        } finally {
//                            holder.unlockCanvasAndPost(canvas);
//                        }
//                    }
//                }
//            }
//        }, 1000, 10);
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        LOGGER.d(String.format(Locale.getDefault(), "surfaceChanged: width: %d, height: %d", width, height));
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        LOGGER.d("surfaceDestroyed");
//        this.initFlag = false;
//        drawTimer.cancel(TASK_NAME);
//    }


//    public static class ArrayQueue<T> {
//        /**
//         * 对象数组，队列最多存储a.length-1个对象
//         */
//        private final T[] array;
//        /**
//         * 数组的长度
//         */
//        private final int length;
//        /**
//         * 队首下标
//         */
//        private int front = 0;
//        /**
//         * 队尾下标
//         */
//        private int rear = 0;
//
//        public ArrayQueue() {
//            this(16);
//        }
//
//        public ArrayQueue(int length) {
//            this.length = length + 1;
//            this.array = (T[]) new Object[this.length];
//        }
//
//        /**
//         * 将一个对象追加到队列尾部
//         *
//         * @param t 值
//         * @return 队列满时返回false, 否则返回true
//         */
//        public boolean enqueue(T t) {
//            if (isFull()) {
//                return false;
//            }
//            array[rear] = t;
//            rear = (rear + 1) % length;
//            return true;
//        }
//
//        /**
//         * 将一个对象追加到队列尾部
//         *
//         * @param t   值
//         * @param pop 当队列已满时，是否弹出第一个后再存入
//         * @return 队列满时返回false, 否则返回true
//         */
//        public boolean enqueue(T t, boolean pop) {
//            if (isFull()) {
//                if (pop) {
//                    dequeue();
//                    return enqueue(t);
//                }
//                return false;
//            }
//            return enqueue(t);
//        }
//
//        /**
//         * 队列头部的第一个对象出队
//         *
//         * @return 出队的对象，队列空时返回 null
//         */
//        public T dequeue() {
//            if (isEmpty()) {
//                return null;
//            }
//            T t = array[front];
//            front = (front + 1) % array.length;
//            return t;
//        }
//
//        /**
//         * 查看队首元素
//         */
//        public T front() {
//            return this.array[this.front];
//        }
//
//        /**
//         * 查看队尾元素
//         */
//        public T tail() {
//            int index = (this.rear + this.length - 1) % this.length;
//            return this.array[index];
//        }
//
//        /**
//         * 判断队列是否为空
//         */
//        public boolean isEmpty() {
//            // 头尾索引相等表示队列满
//            return this.rear == this.front;
//        }
//
//        /**
//         * 判断队列是否为满
//         */
//        public boolean isFull() {
//            // 尾索引的下一个元素为索引时表示队列满，即将队列容量空出一个作为约定
//            return (this.rear % this.length) == this.front;
//        }
//
//        @Override
//        public String toString() {
//            return Arrays.toString(array);
//        }
//
//        public static void main(String[] args) {
//            ArrayQueue<String> q = new ArrayQueue<>(4);
//            System.out.println(q.enqueue("张三"));
//            System.out.println(q.enqueue("李斯"));
//            System.out.println(q.enqueue("王五"));
//            System.out.println("1.队列是否已满: " + q.isFull()); // 无法入队列，队列满
//            System.out.println(q.enqueue("赵六"));
//
//            if (q.isFull()) {
//                q.dequeue();
//            }
//
//            System.out.println("2.队列是否已满: " + q.isFull()); // 无法入队列，队列满
//            System.out.println(q.enqueue("孙七"));//无法入队列，队列满
//            System.out.println(q.enqueue("王一2", true));//队列满，弹出第一个后存入
//
//            System.out.println(q);
//        }
//    }
//
//
//    public static class Queue<T> {
//        /**
//         * 队列空间
//         */
//        private final T[] array;
//        /**
//         * 队列头索引
//         */
//        private int head = 0;
//        /**
//         * 队列尾索引
//         */
//        private int tail = 0;
//        /**
//         * 队列最大长度
//         */
//        private int length;
//
//        public Queue(int length) {
//            this.length = length;
//            this.array = (T[]) new Object[length];
//        }
//
//        /**
//         * 入队
//         */
//        public boolean enqueue(T t) {
//            if (isFull()) {
//                return false;
//            }
//            this.array[this.tail] = t;
//            this.tail = (this.tail + 1) % this.length;
//            return true;
//        }
//
//        /**
//         * 出队
//         */
//        public T dequeue() {
//            if (isEmpty()) {
//                return null;
//            }
//            T t = this.array[this.head];
//            this.head = (this.head + 1) % this.length;
//            return t;
//        }
//
//        /**
//         * 查看队首元素
//         */
//        public T front() {
//            return this.array[this.head];
//        }
//
//        /**
//         * 查看队尾元素
//         */
//        public T tail() {
//            return this.array[(this.tail + this.length - 1) % this.length];
//        }
//
//        /**
//         * 查看队列元素总数
//         */
//        public int size() {
//            return (this.tail + this.length - this.head) % this.length - 1;
//        }
//
//        /**
//         * 判断队列是否为空
//         */
//        public boolean isEmpty() {
//            // 头尾索引相等表示队列满
//            return this.tail == this.head;
//        }
//
//        /**
//         * 判断队列是否为满
//         */
//        public boolean isFull() {
//            // 尾索引的下一个为为索引时表示队列满，即将队列容量空出一个作为约定
//            return (this.tail + 1) % this.length == this.head;
//        }
//
//        public static void main(String[] argc) {
//            Queue q = new Queue(5);
//            q.enqueue(1);
//            q.enqueue(2);
//            q.enqueue(3);
//            q.dequeue();
//            System.out.println("首[" + q.front() + "] 尾[" + q.tail() + "] 队列元素总数" + q.size());
//            q.enqueue(4);
//            q.enqueue(5);
//            q.enqueue(6);
//            q.dequeue();
//            q.dequeue();
//            q.enqueue(7);
//            System.out.println("首[" + q.front() + "] 尾[" + q.tail() + "] 队列元素总数" + q.size());
//        }
//    }
}
