package com.dxa.android.ui.view;

/**
 * 加载更多
 */

public interface ILoadMoreView {

    /**
     * 当加载更多时
     */
    void loadMore();

    /**
     * 刷新完成
     */
    void loadMoreCompleted();

    /**
     * 加载更多失败
     */
    void loadMoreFailed();
}
