package com.dxa.android.ui;

import android.content.Context;

/**
 * Presenter接口
 */

public interface IPresenter<V extends IView> {

    /** BEGIN *************** 与Activity生命周期一致的函数 ********************/

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

    Context getContext();
}
