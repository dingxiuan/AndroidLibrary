package com.github.lzyzsd.jsbridge.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dxa.android.ui.FragmentPresenter;
import com.dxa.android.ui.SuperFragment;
import com.github.lzyzsd.library.R;


public abstract class BaseWebViewFragment<P extends FragmentPresenter> extends SuperFragment<P> {
    /**
     * 新建WebViewFragment
     */
    public static <T extends BaseWebViewFragment> T newInstance(T fragment, String tagName, String originalUrl) {
        Bundle args = new Bundle();
        args.putString(TAG_NAME, tagName);
        args.putString(TAG_URL, originalUrl);
        fragment.setArguments(args);
        return fragment;
    }

    private static final String TAG_NAME = "tagName";
    private static final String TAG_URL = "originalUrl";

    // fragmentName
    private String tagName;
    private String originalUrl;

    private WebViewTemplate webViewTemplate;

    public BaseWebViewFragment() {
        // ~
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            tagName = arguments.getString(TAG_NAME);
            originalUrl = arguments.getString(TAG_URL);
        }
        getWebViewTemplate().setOriginalUrl(originalUrl);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup parent, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.webview_refresh_container, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getWebViewTemplate().onInitialView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        getWebViewTemplate().getWebView().onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        getWebViewTemplate().getWebView().onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Nullable
    @Override
    protected P buildPresenter() {
        return null;
    }

    public String getTagName() {
        return tagName;
    }

    public WebViewTemplate getWebViewTemplate() {
        if (webViewTemplate == null) {
            synchronized (this) {
                webViewTemplate = new WebViewTemplate(getContext());
            }
        }
        return webViewTemplate;
    }
}
