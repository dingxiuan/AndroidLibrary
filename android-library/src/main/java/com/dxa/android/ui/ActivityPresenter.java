package com.dxa.android.ui;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.StringRes;

import com.dxa.android.logger.DLogger;
import com.dxa.android.logger.LogLevel;
import com.dxa.android.ui.handler.AsyncHandler;

/**
 * Presenter的基类
 */
public class ActivityPresenter<V extends IView> implements IPresenter<V> {
    protected final DLogger logger = new DLogger("Presenter");

    private V baseView;

    public ActivityPresenter(V baseView) {
        this.baseView = baseView;

        String tag = getClass().getSimpleName();
        logger.setTag(tag);
    }

    @Override
    public void onCreate() {
        // NOTHING DONE
    }

    @Override
    public void onStart() {
        // NOTHING DONE
    }

    @Override
    public void onResume() {
        // NOTHING DONE
    }

    @Override
    public void onPause() {
        // NOTHING DONE
    }

    @Override
    public void onStop() {
        // NOTHING DONE
    }

    @Override
    public void onRestart() {
        // NOTHING DONE
    }

    @Override
    public void onDestroy() {
        baseView = null;
        log("onDestroy()");
    }

    /**
     * 获取View
     */
    @Override
    public V getView() {
        return baseView;
    }

    @Override
    public AsyncHandler getAsyncHandler() {
        return getView().getAsyncHandler();
    }

    @Override
    public Handler getSyncHandler() {
        return getView().getSyncHandler();
    }

    @Override
    public Context getContext() {
        return getView().getContext();
    }

    public void run() {
        // NOTHING DONE !
    }

    public String getString(@StringRes int id) {
        return getView().getContext().getString(id);
    }

    protected void setDebug(boolean debug) {
        if (debug) {
            logger.setLevel(LogLevel.DEBUG);
        } else {
            logger.setLevel(LogLevel.NONE);
        }
    }

    protected void log(String msg) {
        logger.i(msg);
    }


}
