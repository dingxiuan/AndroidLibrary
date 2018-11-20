package com.github.lzyzsd.jsbridge.ui;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.dxa.android.ui.ActivityPresenter;
import com.dxa.android.ui.SuperActivity;
import com.github.lzyzsd.library.R;


/**
 * 抽象的WebViewActivity
 *
 * @param <P>
 */
public abstract class BaseWebViewActivity<P extends ActivityPresenter> extends SuperActivity<P> {
    public static Bundle createBundle(String title, String url) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAMS_TITLE, title);
        bundle.putString(PARAMS_URL, url);
        return bundle;
    }

    public static void startActivity(Context context, Class<? extends Activity> activityClass, Bundle bundle) {
        Intent intent = new Intent(context, activityClass);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<? extends Activity> activityClass, String title, String url) {
        Bundle bundle = createBundle(title, url);
        startActivity(context, activityClass, bundle);
    }

    public static final String PARAMS_TITLE = "title";
    public static final String PARAMS_URL = "url";

    private Bundle extras;
    // fragmentName
    private String title;
    private String url;

    private WebViewTemplate template;

    public BaseWebViewActivity() {
        // ~
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_refresh_container);
        this.extras = getIntent().getExtras();
        if (this.extras != null) {
            this.title = this.extras.getString(PARAMS_TITLE);
            this.url = this.extras.getString(PARAMS_URL);
        }

        // 初始化View
        WebViewTemplate template = getTemplate();
        template.onInitialView(this);
        onInitialToolbar(template.getToolbar(), title);
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    protected P buildPresenter() {
        return null;
    }

    protected void onInitialToolbar(Toolbar toolbar, String title) {
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (!TextUtils.isEmpty(title)) {
                actionBar.setTitle(title);
            }
            actionBar.setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> onBackClick());
        }
    }

    public WebViewTemplate getTemplate() {
        if (template == null) {
            synchronized (this) {
                this.template = createWebViewTemplate();
                this.template.setOriginalUrl(url);
            }
        }
        return template;
    }

    /**
     * 创建WebView的模板
     */
    protected WebViewTemplate createWebViewTemplate() {
        return new WebViewTemplate(getContext());
    }

    public Bundle getExtras() {
        return extras;
    }

    public Object getStringExtras(String key) {
        Bundle extras = getExtras();
        return extras != null ? extras.get(key) : null;
    }
}
