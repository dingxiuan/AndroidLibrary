package com.dxa.android.view;

import android.view.View;

public abstract class OnDoubleClickListener implements View.OnClickListener {

    private long lastTime = System.currentTimeMillis();
    private boolean isClick = false;

    @Override
    public void onClick(View v) {
        if (isClick && System.currentTimeMillis() - lastTime < 100) {
            onDoubleClick(v);
            isClick = false;
        } else {
            isClick = true;
        }
        lastTime = System.currentTimeMillis();
    }

    public abstract void onDoubleClick(View view);
}
