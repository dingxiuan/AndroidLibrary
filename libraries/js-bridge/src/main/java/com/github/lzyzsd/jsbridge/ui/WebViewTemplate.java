package com.github.lzyzsd.jsbridge.ui;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.dxa.android.utils.RUtils;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.github.lzyzsd.library.R;

public class WebViewTemplate implements BridgeHandler{

    private Toolbar toolbar;
    private BridgeWebView bridgeWebView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String originalUrl;

    public WebViewTemplate() {
    }

    public void initializeView(Object o) {
        if (o instanceof View) {
            View view = (View) o;
            toolbar = view.findViewById(R.id.toolbar);
            bridgeWebView = view.findViewById(R.id.bwv_container);
            swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        } else if (o instanceof Activity) {
            Activity activity = (Activity) o;
            toolbar = activity.findViewById(R.id.toolbar);
            bridgeWebView = activity.findViewById(R.id.bwv_container);
            swipeRefreshLayout = activity.findViewById(R.id.swipe_refresh_layout);
        } else {
            throw new IllegalArgumentException("不支持的参数!");
        }

        // 初始化SwipeRefreshLayout
        initialSwipeRefreshLayout(swipeRefreshLayout);
        // 初始化WebView
        initialWebView(bridgeWebView);
    }

    /**
     * 初始化Activity的Toolbar
     */
    public void initializeToolbar(AppCompatActivity activity, String title) {
        Toolbar toolbar = getToolbar();
        int colorPrimaryDark = RUtils.getColor(activity, "colorPrimaryDark");
        toolbar.setBackgroundColor(colorPrimaryDark != -1 ? colorPrimaryDark : Color.BLACK);
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            if (!TextUtils.isEmpty(title)) {
                actionBar.setTitle(title);
            }
            actionBar.setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> activity.finish());
        }
    }

    /**
     * 初始化SwipeRefreshLayout
     */
    public void initialSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setOnRefreshListener(createSwipeRefreshLayoutListener());
        swipeRefreshLayout.setEnabled(false);
    }

    /**
     * 创建SwipeRefreshLayout刷新的监听
     */
    public SwipeRefreshLayout.OnRefreshListener createSwipeRefreshLayoutListener() {
        return () -> {
            if (getSwipeRefreshLayout().isEnabled()) {
                reloadWebView();
            }
        };
    }

    /**
     * 初始化WebView
     */
    public void initialWebView(BridgeWebView webView) {
        WebSettings settings = webView.getSettings();
        // 1、LayoutAlgorithm.NARROW_COLUMNS ： 适应内容大小
        // 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setLoadWithOverviewMode(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setInitialScale(50); // 为50%，最小缩放等级

        webView.setDefaultHandler(createWebViewHandler());
        webView.setOnLongClickListener(v -> true);

        WebViewClient webViewClient = createWebViewClient(webView);
        webView.setWebViewClient(webViewClient);

        // 重新加载页面
        getWebView().registerHandler("reloadPage", this);
    }

    /**
     * 创建WebViewClient
     */
    public WebViewClient createWebViewClient(BridgeWebView webView) {
        BasicWebClient client = new BasicWebClient(webView);
        SwipeRefreshLayout refreshLayout = getSwipeRefreshLayout();
        client.setListener(new SimpleWebClientListener(refreshLayout));
        return client;
    }

    /**
     * 创建WebView的默认处理器
     */
    public BridgeHandler createWebViewHandler() {
        return new DefaultHandler();
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

    /**
     * 重新加载WebView
     */
    public void reloadWebView() {
        getWebView().loadUrl(getOriginalUrl());
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public BridgeWebView getWebView() {
        return bridgeWebView;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    /**
     * 设置URL
     */
    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    /**
     * WebView页面的地址
     */
    public String getOriginalUrl() {
        return originalUrl;
    }

    @Override
    public void handler(String data, CallBackFunction function) {

    }

}
