package com.dxa.android.ui.view;

import java.util.List;

/**
 * 更新Item的View视图
 */
public interface IUpdateItemView<Item> extends IRefreshView<Item>, ILoadMoreView<Item> {

    /**
     * 刷新
     */
    @Override
    void refreshStart();

    /**
     * 刷新完成
     */
    @Override
    void refreshCompleted();

    /**
     * 刷新失败
     *
     * @param errorMsg 错误信息
     */
    @Override
    void refreshError(String errorMsg);

    /**
     * 添加Item
     *
     * @param items
     */
    @Override
    void addItems(List<Item> items);

    /**
     * 加载更多
     */
    @Override
    void loadMoreStart();

    /**
     * 加载更多完成
     */
    @Override
    void loadMoreCompleted();

    /**
     * 加载更多失败
     *
     * @param errorMsg 错误信息
     */
    @Override
    void loadMoreFailed(String errorMsg);
}
