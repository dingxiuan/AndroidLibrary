package com.dxa.android.service;


import android.content.Context;
import android.content.Intent;

import com.dxa.android.logger.DLogger;
import com.dxa.android.logger.LogLevel;

/**
 * Service的代理类
 */

public class ServiceDelegate {

    protected final DLogger logger = new DLogger(
    		"Service", LogLevel.NONE);
    private Context context;

    public ServiceDelegate(Context context) {
        this.context = context;
        logger.setTag(getClass().getSimpleName());
    }

    public void onCreate() {
        log("onCreate()");
    }

    public void onStartCommand(Intent intent) {
    	log("onStartCommand()");
    }

    public void onDestroy() {
    	log("onDestroy()");
    }

    public Context getContext() {
        return context;
    }

    public void setDebug(boolean debug){
    	LogLevel level = debug ? LogLevel.VERBOSE : LogLevel.NONE;
        logger.setLevel(level);
    }
    
    public void log(String msg) {
		logger.i(msg);
	}

}
