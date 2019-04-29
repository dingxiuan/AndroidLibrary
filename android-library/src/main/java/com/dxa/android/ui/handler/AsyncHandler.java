package com.dxa.android.ui.handler;


import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Printer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.dxa.android.logger.DLogger;
import com.dxa.android.logger.LogLevel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
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

    public static String generateName() {
        return "AsyncHandler-" + COUNTER.incrementAndGet();
    }

    private final DLogger logger = new DLogger(AsyncHandler.class);

    private final CountDownLatch latch = new CountDownLatch(1);

    private volatile Thread mThread;

    private volatile Handler mHandler;


    /**
     * Default constructor associates this handler with the {@link Looper} for the
     * current thread.
     * <p>
     * If this thread does not have a looper, this handler won't be able to receive messages
     * so an exception is thrown.
     */
    public AsyncHandler() {
    }

    public void setDebug(boolean debug) {
        logger.setLevel(debug ? LogLevel.DEBUG : LogLevel.NONE);
    }

    /**
     * @return 获取Handler
     */
    @NonNull
    protected final Handler getHandler() {
        if (mHandler == null) {
            synchronized (this) {
                do {
                    try {
                        if (mThread == null) {
                            mThread = new Thread(() -> {
                                Looper.prepare();
                                mHandler = new Handler(Looper.myLooper());
                                logger.i("Looper开始");
                                Looper.loop();
                                logger.i("Looper结束");
                            });
                            mThread.setDaemon(true);
                            mThread.setName(generateName());
                            mThread.start();
                        }
                        latch.await(1, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (mHandler == null);
            }
        }
        return mHandler;
    }

    @Nullable
    public Thread getThread() {
        return mThread;
    }

    /**
     * Subclasses must implement this to receive messages.
     */
    public void handleMessage(Message msg) {
    }

    /**
     * Handle system messages here.
     */
    public void dispatchMessage(Message msg) {
        getHandler().dispatchMessage(msg);
    }

    /**
     * Returns a string representing the name of the specified message.
     * The default implementation will either return the class name of the
     * message callback if any, or the hexadecimal representation of the
     * message "what" field.
     *
     * @param message The message whose name is being queried
     */
    public String getMessageName(Message message) {
        return getHandler().getMessageName(message);
    }

    /**
     * Returns a new {@link Message Message} from the global message pool. More efficient than
     * creating and allocating new instances. The retrieved message has its handler set to this instance (Message.target == this).
     * If you don't want that facility, just call Message.obtain() instead.
     */
    public final Message obtainMessage() {
        return getHandler().obtainMessage();
    }

    /**
     * Same as {@link #obtainMessage()}, except that it also sets the what member of the returned Message.
     *
     * @param what Value to assign to the returned Message.what field.
     * @return A Message from the global message pool.
     */
    public final Message obtainMessage(int what) {
        return getHandler().obtainMessage(what);
    }

    /**
     * Same as {@link #obtainMessage()}, except that it also sets the what and obj members
     * of the returned Message.
     *
     * @param what Value to assign to the returned Message.what field.
     * @param obj  Value to assign to the returned Message.obj field.
     * @return A Message from the global message pool.
     */
    public final Message obtainMessage(int what, Object obj) {
        return getHandler().obtainMessage(what, obj);
    }

    /**
     * Same as {@link #obtainMessage()}, except that it also sets the what, arg1 and arg2 members of the returned
     * Message.
     *
     * @param what Value to assign to the returned Message.what field.
     * @param arg1 Value to assign to the returned Message.arg1 field.
     * @param arg2 Value to assign to the returned Message.arg2 field.
     * @return A Message from the global message pool.
     */
    public final Message obtainMessage(int what, int arg1, int arg2) {
        return getHandler().obtainMessage(what, arg1, arg2);
    }

    /**
     * Same as {@link #obtainMessage()}, except that it also sets the what, obj, arg1,and arg2 values on the
     * returned Message.
     *
     * @param what Value to assign to the returned Message.what field.
     * @param arg1 Value to assign to the returned Message.arg1 field.
     * @param arg2 Value to assign to the returned Message.arg2 field.
     * @param obj  Value to assign to the returned Message.obj field.
     * @return A Message from the global message pool.
     */
    public final Message obtainMessage(int what, int arg1, int arg2, Object obj) {
        return getHandler().obtainMessage(what, arg1, arg2, obj);
    }

    /**
     * Causes the Runnable r to be added to the message queue.
     * The runnable will be run on the thread to which this handler is
     * attached.
     *
     * @param r The Runnable that will be executed.
     * @return Returns true if the Runnable was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.
     */
    public final boolean post(Runnable r) {
        return getHandler().post(r);
    }

    /**
     * Causes the Runnable r to be added to the message queue, to be run
     * at a specific time given by <var>uptimeMillis</var>.
     * <b>The time-base is {@link android.os.SystemClock#uptimeMillis}.</b>
     * Time spent in deep sleep will add an additional delay to execution.
     * The runnable will be run on the thread to which this handler is attached.
     *
     * @param r            The Runnable that will be executed.
     * @param uptimeMillis The absolute time at which the callback should run,
     *                     using the {@link android.os.SystemClock#uptimeMillis} time-base.
     * @return Returns true if the Runnable was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.  Note that a
     * result of true does not mean the Runnable will be processed -- if
     * the looper is quit before the delivery time of the message
     * occurs then the message will be dropped.
     */
    public final boolean postAtTime(Runnable r, long uptimeMillis) {
        return getHandler().postAtTime(r, uptimeMillis);
    }

    /**
     * Causes the Runnable r to be added to the message queue, to be run
     * at a specific time given by <var>uptimeMillis</var>.
     * <b>The time-base is {@link android.os.SystemClock#uptimeMillis}.</b>
     * Time spent in deep sleep will add an additional delay to execution.
     * The runnable will be run on the thread to which this handler is attached.
     *
     * @param r            The Runnable that will be executed.
     * @param token        An instance which can be used to cancel {@code r} via
     *                     {@link #removeCallbacksAndMessages}.
     * @param uptimeMillis The absolute time at which the callback should run,
     *                     using the {@link android.os.SystemClock#uptimeMillis} time-base.
     * @return Returns true if the Runnable was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.  Note that a
     * result of true does not mean the Runnable will be processed -- if
     * the looper is quit before the delivery time of the message
     * occurs then the message will be dropped.
     * @see android.os.SystemClock#uptimeMillis
     */
    public final boolean postAtTime(Runnable r, Object token, long uptimeMillis) {
        return getHandler().postAtTime(r, token, uptimeMillis);
    }

    /**
     * Causes the Runnable r to be added to the message queue, to be run
     * after the specified amount of time elapses.
     * The runnable will be run on the thread to which this handler
     * is attached.
     * <b>The time-base is {@link android.os.SystemClock#uptimeMillis}.</b>
     * Time spent in deep sleep will add an additional delay to execution.
     *
     * @param r           The Runnable that will be executed.
     * @param delayMillis The delay (in milliseconds) until the Runnable
     *                    will be executed.
     * @return Returns true if the Runnable was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.  Note that a
     * result of true does not mean the Runnable will be processed --
     * if the looper is quit before the delivery time of the message
     * occurs then the message will be dropped.
     */
    public final boolean postDelayed(Runnable r, long delayMillis) {
        return getHandler().postDelayed(r, delayMillis);
    }

    /**
     * Causes the Runnable r to be added to the message queue, to be run
     * after the specified amount of time elapses.
     * The runnable will be run on the thread to which this handler
     * is attached.
     * <b>The time-base is {@link android.os.SystemClock#uptimeMillis}.</b>
     * Time spent in deep sleep will add an additional delay to execution.
     *
     * @param r           The Runnable that will be executed.
     * @param token       An instance which can be used to cancel {@code r} via
     *                    {@link #removeCallbacksAndMessages}.
     * @param delayMillis The delay (in milliseconds) until the Runnable
     *                    will be executed.
     * @return Returns true if the Runnable was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.  Note that a
     * result of true does not mean the Runnable will be processed --
     * if the looper is quit before the delivery time of the message
     * occurs then the message will be dropped.
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public final boolean postDelayed(Runnable r, Object token, long delayMillis) {
        return getHandler().postDelayed(r, token, delayMillis);
    }

    /**
     * Posts a message to an object that implements Runnable.
     * Causes the Runnable r to executed on the next iteration through the
     * message queue. The runnable will be run on the thread to which this
     * handler is attached.
     * <b>This method is only for use in very special circumstances -- it
     * can easily starve the message queue, cause ordering problems, or have
     * other unexpected side-effects.</b>
     *
     * @param r The Runnable that will be executed.
     * @return Returns true if the message was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.
     */
    public final boolean postAtFrontOfQueue(Runnable r) {
        return getHandler().postAtFrontOfQueue(r);
    }

    /**
     * Remove any pending posts of Runnable r that are in the message queue.
     */
    public final void removeCallbacks(Runnable r) {
        getHandler().removeCallbacks(r);
    }

    /**
     * Remove any pending posts of Runnable <var>r</var> with Object
     * <var>token</var> that are in the message queue.  If <var>token</var> is null,
     * all callbacks will be removed.
     */
    public final void removeCallbacks(Runnable r, Object token) {
        getHandler().removeCallbacks(r, token);
    }

    /**
     * Pushes a message onto the end of the message queue after all pending messages
     * before the current time. It will be received in {@link #handleMessage},
     * in the thread attached to this handler.
     *
     * @return Returns true if the message was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.
     */
    public final boolean sendMessage(Message msg) {
        return getHandler().sendMessage(msg);
    }

    /**
     * Sends a Message containing only the what value.
     *
     * @return Returns true if the message was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.
     */
    public final boolean sendEmptyMessage(int what) {
        return getHandler().sendEmptyMessage(what);
    }

    /**
     * Sends a Message containing only the what value, to be delivered
     * after the specified amount of time elapses.
     *
     * @return Returns true if the message was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.
     * @see #sendMessageDelayed(Message, long)
     */
    public final boolean sendEmptyMessageDelayed(int what, long delayMillis) {
        return getHandler().sendEmptyMessageDelayed(what, delayMillis);
    }

    /**
     * Sends a Message containing only the what value, to be delivered
     * at a specific time.
     *
     * @return Returns true if the message was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.
     * @see #sendMessageAtTime(Message, long)
     */

    public final boolean sendEmptyMessageAtTime(int what, long uptimeMillis) {
        return getHandler().sendEmptyMessageAtTime(what, uptimeMillis);
    }

    /**
     * Enqueue a message into the message queue after all pending messages
     * before (current time + delayMillis). You will receive it in
     * {@link #handleMessage}, in the thread attached to this handler.
     *
     * @return Returns true if the message was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.  Note that a
     * result of true does not mean the message will be processed -- if
     * the looper is quit before the delivery time of the message
     * occurs then the message will be dropped.
     */
    public final boolean sendMessageDelayed(Message msg, long delayMillis) {
        return getHandler().sendMessageDelayed(msg, delayMillis);
    }

    /**
     * Enqueue a message into the message queue after all pending messages
     * before the absolute time (in milliseconds) <var>uptimeMillis</var>.
     * <b>The time-base is {@link android.os.SystemClock#uptimeMillis}.</b>
     * Time spent in deep sleep will add an additional delay to execution.
     * You will receive it in {@link #handleMessage}, in the thread attached
     * to this handler.
     *
     * @param uptimeMillis The absolute time at which the message should be
     *                     delivered, using the
     *                     {@link android.os.SystemClock#uptimeMillis} time-base.
     * @return Returns true if the message was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.  Note that a
     * result of true does not mean the message will be processed -- if
     * the looper is quit before the delivery time of the message
     * occurs then the message will be dropped.
     */
    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        return getHandler().sendMessageAtTime(msg, uptimeMillis);
    }

    /**
     * Enqueue a message at the front of the message queue, to be processed on
     * the next iteration of the message loop.  You will receive it in
     * {@link #handleMessage}, in the thread attached to this handler.
     * <b>This method is only for use in very special circumstances -- it
     * can easily starve the message queue, cause ordering problems, or have
     * other unexpected side-effects.</b>
     *
     * @return Returns true if the message was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.
     */
    public final boolean sendMessageAtFrontOfQueue(Message msg) {
        return getHandler().sendMessageAtFrontOfQueue(msg);
    }

    /**
     * Remove any pending posts of messages with code 'what' that are in the
     * message queue.
     */
    public final void removeMessages(int what) {
        getHandler().removeMessages(what);
    }

    /**
     * Remove any pending posts of messages with code 'what' and whose obj is
     * 'object' that are in the message queue.  If <var>object</var> is null,
     * all messages will be removed.
     */
    public final void removeMessages(int what, Object object) {
        getHandler().removeMessages(what, object);
    }

    /**
     * Remove any pending posts of callbacks and sent messages whose
     * <var>obj</var> is <var>token</var>.  If <var>token</var> is null,
     * all callbacks and messages will be removed.
     */
    public final void removeCallbacksAndMessages(Object token) {
        getHandler().removeCallbacksAndMessages(token);
    }

    /**
     * Check if there are any pending posts of messages with code 'what' in
     * the message queue.
     */
    public final boolean hasMessages(int what) {
        return getHandler().hasMessages(what);
    }

    /**
     * Check if there are any pending posts of messages with code 'what' and
     * whose obj is 'object' in the message queue.
     */
    public final boolean hasMessages(int what, Object object) {
        return getHandler().hasMessages(what, object);
    }

    // if we can get rid of this method, the handler need not remember its loop
    // we could instead export a getMessageQueue() method...
    public final Looper getLooper() {
        return getHandler().getLooper();
    }

    public final void dump(Printer pw, String prefix) {
        getHandler().dump(pw, prefix);
    }

    @Override
    public String toString() {
        return "Handler (" + getClass().getName() + ") {"
                + Integer.toHexString(System.identityHashCode(this))
                + "}";
    }

}


///**
// * 异步线程
// *
// * @author DINGXIUAN
// */
//public class AsyncHandler implements Runnable {
//
//    private static final class Holder {
//        private static volatile AsyncHandler INSTANCE;
//
//        static {
//            INSTANCE = new AsyncHandler();
//        }
//    }
//
//    public static AsyncHandler getInstance() {
//        return Holder.INSTANCE;
//    }
//
//    /**
//     * 线程统计
//     */
//    private static final AtomicInteger COUNTER = new AtomicInteger(0);
//
//    public static String getThreadName() {
//        return "AsyncHandler-" + COUNTER.incrementAndGet();
//    }
//
//    private final DLogger logger = new DLogger(AsyncHandler.class);
//
//    private final Thread thread;
//    private volatile Handler handler;
//
//    private volatile Looper looper;
//    private final boolean startNow;
//
//    public AsyncHandler() {
//        this(true);
//    }
//
//    public AsyncHandler(boolean startNow) {
//        this(true, startNow);
//    }
//
//    public AsyncHandler(boolean isDaemon, boolean startNow) {
//        this.startNow = startNow;
//        this.thread = new Thread(this);
//        this.thread.setDaemon(isDaemon);
//        this.thread.setName(getThreadName());
//
//        this.logger.setTag(thread.getName());
//
//        if (startNow) {
//            // 开启当前的线程
//            this.start();
//        }
//    }
//
//    public void setDebug(boolean debug) {
//        logger.setLevel(debug ? LogLevel.DEBUG : LogLevel.NONE);
//    }
//
//    /**
//     * 获取当前的Handler
//     */
//    public Handler getHandler() {
//        if (handler != null) {
//            while (handler == null) {
//                try {
//                    wait(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return handler;
//    }
//
//    public void sendMessage(Message message) {
//        getHandler().sendMessage(message);
//    }
//
//    public void sendEmptyMessage(int what) {
//        getHandler().sendEmptyMessage(what);
//    }
//
//    public void sendEmptyMessageDelayed(int what, long delayMillis) {
//        getHandler().sendEmptyMessageDelayed(what, delayMillis);
//    }
//
//    public void post(Runnable r) {
//        getHandler().post(r);
//    }
//
//    /**
//     * 延迟发送消息
//     * @param r
//     * @param delayMillis
//     */
//    public void postDelayed(Runnable r, long delayMillis) {
//        getHandler().postDelayed(r, delayMillis);
//    }
//
//    public void start() {
//        if (looper != null) {
//            return;
//        }
//        thread.start();
//    }
//
//    public void stop() {
//        if (looper != null) {
//            looper.quitSafely();
//            looper = null;
//            logger.i("停止");
//        }
//    }
//
//
//    @Override
//    public void run() {
//        if (thread != Thread.currentThread() || looper != null) {
//            return;
//        }
//
//        if (looper == null) {
//            Looper.prepare();
//            looper = Looper.myLooper();
//            handler = new Handler(looper);
//            logger.i("Looper开始");
//            Looper.loop();
//            logger.i("Looper结束");
//        }
//    }
//
//
//}
