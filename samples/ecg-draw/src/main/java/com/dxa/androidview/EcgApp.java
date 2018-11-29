package com.dxa.androidview;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;

import com.dxa.android.logger.DLogger;

/**
 * 心电图绘制
 */
public class EcgApp extends Application {

    private static final DLogger logger = DLogger.getLogger(EcgApp.class);

    static {
        DLogger.setDebug(true);
        DLogger.setGlobalTag(EcgApp.class.getSimpleName());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        logger.i("attachBaseContext");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        logger.i("onCreate");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        logger.i("onTerminate");
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        logger.i("onConfigurationChanged: ", newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        logger.i("onLowMemory");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        logger.i("onTrimMemory: ", level);
    }

}
