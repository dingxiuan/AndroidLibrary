package com.dxa.android.ui.handler;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.dxa.android.logger.DLogger;
import com.dxa.android.logger.LogLevel;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 异步线程
 *
 * @author DINGXIUAN
 */
public class AsyncHandler {

    private static final class Holder {
        private static volatile AsyncHandler INSTANCE;

        static {
            INSTANCE = new AsyncHandler();
        }
    }

    public static AsyncHandler getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 线程统计
     */
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    public static String getThreadName() {
        return "AsyncHandler-" + COUNTER.incrementAndGet();
    }

    private final DLogger logger = new DLogger();

    private Thread currentThread;
    private Handler handler;

    private volatile Looper looper;
    private final boolean startNow;

    public AsyncHandler() {
        this(true);
    }

    public AsyncHandler(boolean startNow) {
        this(true, startNow);
    }

    public AsyncHandler(boolean isDaemon, boolean startNow) {
        this.startNow = startNow;
        this.currentThread = new Thread(r);
        this.currentThread.setDaemon(isDaemon);
        this.currentThread.setName(getThreadName());

        this.logger.setTag(currentThread.getName());

        if (startNow) {
            // 开启当前的线程
            this.start();
        }
    }

    public void setDebug(boolean debug) {
        logger.setLevel(debug ? LogLevel.DEBUG : LogLevel.NONE);
    }

    /**
     * 获取当前的Handler
     */
    public Handler getHandler() {
        return handler;
    }

    public void sendMessage(Message message) {
        getHandler().sendMessage(message);
    }

    public void sendEmptyMessage(int what) {
        getHandler().sendEmptyMessage(what);
    }

    public void sendEmptyMessageDelayed(int what, long delayMillis) {
        getHandler().sendEmptyMessageDelayed(what, delayMillis);
    }

    public void post(Runnable r) {
        getHandler().post(r);
    }

    public void postDelayed(Runnable r, long delayMillis) {
        getHandler().postDelayed(r, delayMillis);
    }

    public void start() {
        if (looper != null) {
            return;
        }

        if (startNow) {
            return;
        }

        currentThread.start();
    }

    public void stop() {
        if (looper != null) {
            looper.quitSafely();
            looper = null;
            logger.i("停止");
        }
    }

    private final Runnable r = new Runnable() {
        @Override
        public void run() {
            if (currentThread != Thread.currentThread() || looper != null) {
                return;
            }

            if (looper == null) {
                Looper.prepare();
                looper = Looper.myLooper();
                handler = new Handler(looper);
                logger.i("Looper开始");
                Looper.loop();
                logger.i("Looper结束");
            }
        }
    };

}
