package com.dxa.android.ui.view;

import java.util.List;

/**
 * 当更新Item时
 */

public interface IUpdateItemView<T> {

    /**
     * 刷新Item
     */
    void refreshItems(List<T> items);

    /**
     * 添加Item
     */
    void addItems(List<T> items);
}
