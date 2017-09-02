package com.dxa.android.view;

import android.view.View;

/**
 * 单击
 */
public abstract class OnSingleClickListener  implements View.OnClickListener {
    
	private long lastTime = 0;
	private long delayTime = 1000;
	
	public OnSingleClickListener(){}
	
	public OnSingleClickListener(long delayTime){
		this.delayTime = delayTime;
	}

    @Override
    public void onClick(View v) {
        if (System.currentTimeMillis() - lastTime < delayTime) {
            return;
        }
        onSingleClick(v);
        lastTime = System.currentTimeMillis();
    }

    public abstract void onSingleClick(View view);
}