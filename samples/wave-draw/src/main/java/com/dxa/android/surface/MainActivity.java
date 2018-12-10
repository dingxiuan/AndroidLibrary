package com.dxa.android.surface;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dxa.android.surface.view.EcgSurfaceView;
import com.dxa.android.surface.view.EcgView;
import com.dxa.android.ui.ActivityPresenter;
import com.dxa.android.ui.SuperActivity;

public class MainActivity extends SuperActivity {

    EcgSurfaceView ecgSurfaceView;
    EcgView ecgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ecgSurfaceView = findViewById(R.id.ecg_texture_view);
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
