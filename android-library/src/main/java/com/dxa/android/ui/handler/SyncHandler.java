package com.dxa.android.ui.handler;


import android.os.Handler;
import android.os.Looper;

/**
 * 同步的Handler, 默认主线程
 *
 * @author DINGXIUAN
 */
public class SyncHandler extends Handler {

    public static final class Holder {
        private static volatile SyncHandler INSTANCE;

        static {
            INSTANCE = new SyncHandler();
        }
    }

    public static SyncHandler getInstance() {
        return Holder.INSTANCE;
    }

    public SyncHandler() {
        this(Looper.getMainLooper(), null);
    }

    public SyncHandler(Callback callback) {
        this(Looper.getMainLooper(), callback);
    }

    public SyncHandler(Looper looper) {
        this(looper, null);
    }

    public SyncHandler(Looper looper, Callback callback) {
        super(looper, callback);
    }
}
