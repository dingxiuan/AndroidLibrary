package com.dxa.android.ui.view;

/**
 * Dialog的操作
 */
public interface IDialogView {
    /**
     * 显示Dialog
     */
    void showDialog();

    /**
     * 隐藏Dialog
     */
    void hideDialog();

    /**
     * 隐藏Dialog
     *
     * @param successful 是否成功
     */
    default void hideDialog(boolean successful) {
        hideDialog();
    }
}
