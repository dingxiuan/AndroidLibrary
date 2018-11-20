package com.dxa.androidview.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.dxa.android.ui.ActivityPresenter;
import com.dxa.androidview.R;
import com.dxa.android.ui.SuperActivity;
import com.github.lzyzsd.jsbridge.BridgeWebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends SuperActivity {

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Nullable
    @Override
    protected ActivityPresenter buildPresenter() {
        return null;
    }

    @OnClick({
            R.id.btn_learn_view,
            R.id.btn_ecg_draw,
            R.id.btn_surface_view,
            R.id.btn_draw_surface_view,
            R.id.btn_test_webview,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_learn_view:
                startAct(LearnViewActivity.class);
                break;
            case R.id.btn_ecg_draw:
                startAct(EcgDrawActivity.class);
                break;
            case R.id.btn_surface_view:
                startAct(SurfaceViewActivity.class);
                break;
            case R.id.btn_draw_surface_view:
                startAct(DrawSurfaceViewActivity.class);
                break;
            case R.id.btn_test_webview:
                Bundle bundle = WebViewActivity.createBundle("测试WebView", "http://www.baidu.com");
                startAct(WebViewActivity.class, bundle);
                break;
            default:
                break;
        }
    }
}
