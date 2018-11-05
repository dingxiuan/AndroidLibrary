package com.dxa.android.ui.view;

import java.util.List;

/**
 * 刷新
 */
public interface IRefreshView<T> {

    /**
     * 刷新
     */
    void refreshStart();

    /**
     * 添加Item
     */
    void addItems(List<T> items);

    /**
     * 刷新完成
     */
    void refreshCompleted();

    /**
     * 刷新失败
     *
     * @param errorMsg 错误信息
     */
    void refreshError(String errorMsg);

}
