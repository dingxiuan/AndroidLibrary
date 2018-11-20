package com.github.lzyzsd.jsbridge.ui;

import android.graphics.Bitmap;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.dxa.android.logger.DLogger;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;

public class SimpleWebClient extends BridgeWebViewClient {

    protected final DLogger logger = new DLogger(SimpleWebClient.class);

    private Listener listener = _DEFAULT_LISTENER;

    public SimpleWebClient(BridgeWebView webView) {
        super(webView);
    }

    public Listener geListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        geListener().onLoadStarted(view, url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        geListener().onLoadFinished(view, url);
    }


    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        geListener().onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        geListener().onReceivedError(view, request, error);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (geListener().shouldOverrideUrlLoading(view, url)) {
            return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
    }


    public interface Listener {
        /**
         * 加载开始
         *
         * @param view WebView页面
         * @param url  路径
         */
        void onLoadStarted(WebView view, String url);

        /**
         * 加载完成
         *
         * @param view WebView页面
         * @param url  路径
         */
        void onLoadFinished(WebView view, String url);

        /**
         * 加载错误
         *
         * @param view        WebView页面
         * @param errorCode   错误码
         * @param description 描述
         * @param failingUrl  失败的地址
         */
        void onReceivedError(WebView view, int errorCode, String description, String failingUrl);

        /**
         * @param view    WebView页面
         * @param request 请求
         * @param error   错误
         */
        void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error);

        /**
         * 是否覆写URL加载
         *
         * @param view WebView页面
         * @param url  地址
         * @return 返回是否覆写了，返回true表示已覆写，不需要加载
         */
        boolean shouldOverrideUrlLoading(WebView view, String url);
    }

    private static final Listener _DEFAULT_LISTENER = new SimpleWebClientListener();
}
