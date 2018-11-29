package com.dxa.android.surface;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dxa.android.surface.view.EcgSurfaceView2;
import com.dxa.android.surface.view.EcgView;
import com.dxa.android.ui.ActivityPresenter;
import com.dxa.android.ui.SuperActivity;

public class MainActivity extends SuperActivity {

    EcgSurfaceView2 ecgTextureView;
    EcgView ecgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ecgTextureView = findViewById(R.id.ecg_texture_view);
        ecgView = findViewById(R.id.ecg_view);

//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
////        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int count = Painter.calPxCount(metrics);
//        logger.e("metrics: " + metrics);
//        logger.e("count: " + count);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    protected ActivityPresenter buildPresenter() {
        return null;
    }

}
