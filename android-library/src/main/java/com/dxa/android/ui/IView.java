package com.dxa.android.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

/**
 * View的基本接口
 */
public interface IView extends IShowToast {

    /**
     * 销毁当前的Activity
     */
    void finishActivity();

    /**
     * 当返回的时候
     */
    void onBackClick();

    /**
     * 启动Activity
     */
    void startAct(Class<? extends Activity> clazz);

    /**
     * 自动Activity
     */
    void startAct(Class<? extends Activity> clazz, Bundle bundle);

    /**
     * 启动其他Activity，并销毁当前的Activity
     */
    void startActAfterFinish(Class<? extends Activity> clazz);

    /**
     * 启动其他Activity，并销毁当前的Activity
     */
    void startActAfterFinish(Class<? extends Activity> clazz, Bundle bundle);

    /**
     * 启动其他Activity，并返回结果
     */
    void startActForResult(Class<? extends Activity> clazz, int requestCode);

    /**
     * 启动其他Activity，并返回结果
     */
    void startActForResult(Class<? extends Activity> clazz, int requestCode, Bundle bundle);

    /**
     * 获取当前的Activity
     */
    Context getContext();

    /**
     * 获取资源对象
     */
    Resources getResources();

}