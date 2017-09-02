package com.dxa.android.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;

public class ServiceHelper {

    /**
     * 开启服务
     */
    public static void startService(Context context, Class<? extends Service> clazz){
    	if (notNull(context) && notNull(clazz)){
            Intent serviceIntent = new Intent(context, clazz);
            context.startService(serviceIntent);
        }
    }

    /**
     * 停止服务
     */
    public static void stopService(Context context, Class<? extends Service> clazz){
    	if (notNull(context) && notNull(clazz)){
            Intent serviceIntent = new Intent(context, clazz);
            context.stopService(serviceIntent);
        }
    }
    
    private static boolean notNull(Object o){
        return o != null;
    }
}
