package com.dxa.android.ui;


import androidx.annotation.StringRes;

/**
 * 显示Toast
 */

public interface IShowToast {

    /**
     * 显示Toast
     */
    void showToast(String msg);

    /**
     * 显示Toast
     */
    void showToast(@StringRes int strResId);
}
