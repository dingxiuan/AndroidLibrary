package com.dxa.android.ui;

import android.support.annotation.DrawableRes;

/**
 * 显示Dialog
 */

public interface IShowDialog {
    /**
     * 显示Dialog
     */
    void showDialog(@DrawableRes int drawableResId, String title, String content);

    /**
     * 隐藏Dialog
     */
    void dismissDialog();
}
