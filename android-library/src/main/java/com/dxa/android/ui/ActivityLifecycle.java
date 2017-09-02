package com.dxa.android.ui;

/**
 * Activity的生命周期
 */
public interface ActivityLifecycle {
	
	/**
	 * 创建
	 */
	void onCreate();
	
	/**
	 * 开始
	 */
	void onStart();
	
	/**
	 * 唤醒
	 */
	void onResume();
	
	/**
	 * 暂停
	 */
	void onPause();
	
	/**
	 * 停止
	 */
	void onStop();
	
	/**
	 * 重启
	 */
	void onRestart();
	
	/**
	 * 销毁
	 */
	void onDestroy();
	
}
