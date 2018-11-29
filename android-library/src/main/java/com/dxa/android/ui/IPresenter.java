package com.dxa.android.ui;

import android.content.Context;
import android.os.Handler;

import com.dxa.android.ui.handler.AsyncHandler;

/**
 * Presenter接口
 */

public interface IPresenter<V extends IView> {

    /**
     * BEGIN *************** 与Activity生命周期一致的函数
     ********************/

    void onCreate();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onRestart();

    void onDestroy();

    /** END *************** 与Activity生命周期一致的函数 ********************/

    /**
     * 获取View
     */
    V getView();

    /**
     * 获取子线程的Handler
     */
    AsyncHandler getAsyncHandler();

    /**
     * 获取默认的Handler
     */
    Handler getSyncHandler();

    /**
     * 获取上下文对象
     */
    Context getContext();
}
