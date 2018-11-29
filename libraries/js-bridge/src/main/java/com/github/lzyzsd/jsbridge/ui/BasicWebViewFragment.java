package com.github.lzyzsd.jsbridge.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dxa.android.ui.FragmentPresenter;
import com.dxa.android.ui.SuperFragment;
import com.dxa.android.utils.RUtils;
import com.github.lzyzsd.library.R;


public class BasicWebViewFragment<P extends FragmentPresenter> extends SuperFragment<P> {
    /**
     * 新建WebViewFragment
     */
    public static <T extends BasicWebViewFragment> T newInstance(T fragment, String tagName, String originalUrl) {
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

    private WebViewTemplate template;

    public BasicWebViewFragment() {
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
        getTemplate().setOriginalUrl(originalUrl);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup parent, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.webview_refresh_container, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WebViewTemplate template = getTemplate();
        template.initializeView(view);
        onInitializeToolbar(template.getToolbar());
    }

    @Override
    public void onResume() {
        super.onResume();
        getTemplate().getWebView().onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        getTemplate().getWebView().onPause();
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


    protected void onInitializeToolbar(Toolbar toolbar) {
        int colorPrimaryDark = RUtils.getColor(getContext(), "colorPrimaryDark");
        toolbar.setBackgroundColor(colorPrimaryDark > 0 ? colorPrimaryDark : Color.BLACK);
        toolbar.setVisibility(View.GONE);
    }

    public String getTagName() {
        return tagName;
    }


    public WebViewTemplate getTemplate() {
        if (template == null) {
            this.template = createTemplate();
        }
        return template;
    }

    /**
     * 创建WebView的模板
     */
    protected WebViewTemplate createTemplate() {
        return new WebViewTemplate();
    }

}
