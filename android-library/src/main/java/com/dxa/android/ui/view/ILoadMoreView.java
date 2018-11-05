package com.dxa.android.ui.view;

import java.util.List;

/**
 * 加载更多
 */

public interface ILoadMoreView<Item> {
    /**
     * 加载更多
     */
    void loadMoreStart();

    /**
     * 当加载更多时
     */
    void addItems(List<Item> items);

    /**
     * 加载更多完成
     */
    void loadMoreCompleted();

    /**
     * 加载更多失败
     *
     * @param errorMsg 错误信息
     */
    void loadMoreFailed(String errorMsg);
}
