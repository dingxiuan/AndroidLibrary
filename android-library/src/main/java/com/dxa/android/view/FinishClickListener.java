package com.dxa.android.view;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

/**
 * 点击销毁的监听
 */
public class FinishClickListener implements View.OnClickListener {
	private static final class FinishClickListenerHolder {
		private static volatile FinishClickListener INSTANCE;
		
		static{
			INSTANCE = new FinishClickListener();
		}
	}
	
	public static FinishClickListener get(){
		return FinishClickListenerHolder.INSTANCE;
	}
	
	@Override
	public void onClick(View v) {
		Context context = v.getContext();
        if (context instanceof Activity) {
            ((Activity) context).finish();
            Log.e("TAG", "finish current Activity...");
        } else {
            Log.e("TAG", "cannot finish current view: " + context.getClass());
        }
	}

}
