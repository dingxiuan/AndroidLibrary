package com.dxa.androidview.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dxa.android.ui.ActivityPresenter;
import com.dxa.android.ui.SuperActivity;
import com.dxa.androidview.R;
import com.dxa.androidview.widget.EcgDrawTextureView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EcgDrawTextureViewActivity extends SuperActivity {

    @BindView(R.id.texture_view)
    EcgDrawTextureView textureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecg_draw_texture_view);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean isLandscape() {
        return Configuration.ORIENTATION_LANDSCAPE == getResources().getConfiguration().orientation;
    }

    @Nullable
    @Override
    protected ActivityPresenter buildPresenter() {
        return null;
    }


}
