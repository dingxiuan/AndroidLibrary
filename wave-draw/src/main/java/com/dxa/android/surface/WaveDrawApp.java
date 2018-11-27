package com.dxa.android.surface;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.dxa.android.logger.DLogger;

import java.util.Arrays;

public class WaveDrawApp extends Application {
    static {
        DLogger.setDebug(true);
        DLogger.setGlobalTag("WaveDrawApp");
    }

    private static final DLogger logger = DLogger.getLogger(WaveDrawApp.class);

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
        logger.i("onConfigurationChanged newConfig: ", newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        logger.i("onLowMemory");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        logger.i("onTrimMemory level: ", level);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        logger.i("onCreate");
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        logger.i("startActivity, intent: ", intent);
    }

    @Override
    public void startActivity(Intent intent, Bundle options) {
        super.startActivity(intent, options);
        logger.i("startActivity, intents", intent, "， options: ", options);
    }

    @Override
    public void startActivities(Intent[] intents) {
        super.startActivities(intents);
        logger.i("startActivities, intents", Arrays.toString(intents));
    }

    @Override
    public void startActivities(Intent[] intents, Bundle options) {
        super.startActivities(intents, options);
        logger.i("startActivities, intents", Arrays.toString(intents), "， options: ", options);
    }
}
