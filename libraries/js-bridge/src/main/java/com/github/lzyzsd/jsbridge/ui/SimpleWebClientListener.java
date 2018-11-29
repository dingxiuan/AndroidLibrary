package com.github.lzyzsd.jsbridge.ui;


import android.support.v4.widget.SwipeRefreshLayout;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

public class SimpleWebClientListener implements BasicWebClient.Listener {


    private SwipeRefreshLayout refreshLayout;

    public SimpleWebClientListener() {
    }

    public SimpleWebClientListener(SwipeRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return refreshLayout;
    }

    /**
     * 加载开始
     *
     * @param view WebView页面
     * @param url  路径
     */
    @Override
    public void onLoadStarted(WebView view, String url) {
        setSwipeRefreshLayoutState(true);
    }

    /**
     * 加载完成
     *
     * @param view WebView页面
     * @param url  路径
     */
    @Override
    public void onLoadFinished(WebView view, String url) {
        setSwipeRefreshLayoutState(false);
    }

    /**
     * 加载错误
     *
     * @param view        WebView页面
     * @param errorCode   错误码
     * @param description 描述
     * @param failingUrl  失败的地址
     */
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        setSwipeRefreshLayoutState(false);
    }

    /**
     * @param view    WebView页面
     * @param request 请求
     * @param error   错误
     */
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        setSwipeRefreshLayoutState(false);
    }

    /**
     * 是否覆写URL加载
     *
     * @param view WebView页面
     * @param url  地址
     * @return 返回是否覆写了，返回true表示已覆写，不需要加载
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    /**
     * 设置当前的刷新状态
     *
     * @param refreshing 是否正在刷新
     */
    public void setSwipeRefreshLayoutState(boolean refreshing) {
        SwipeRefreshLayout refreshLayout = getSwipeRefreshLayout();
        if (refreshLayout != null && refreshLayout.isEnabled()) {
            refreshLayout.setRefreshing(refreshing);
        }
    }
}
