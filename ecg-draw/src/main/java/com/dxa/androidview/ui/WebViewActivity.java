package com.dxa.androidview.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dxa.androidview.R;
import com.github.lzyzsd.jsbridge.ui.BaseWebViewActivity;

public class WebViewActivity extends BaseWebViewActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTemplate().getSwipeRefreshLayout().setEnabled(true);
    }

    @Override
    protected void onInitialToolbar(Toolbar toolbar, String title) {
        toolbar.setVisibility(View.VISIBLE);
        super.onInitialToolbar(toolbar, title);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    }

}
