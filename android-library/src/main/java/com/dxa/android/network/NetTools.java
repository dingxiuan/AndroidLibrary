package com.dxa.android.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络工具类
 */
public class NetTools {

    private NetTools() {
        throw new IllegalAccessError("Cannot new instance.");
    }

    public static ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 获取网络状态信息
     */
    public static NetworkInfo getActiveNetworkInfo(Context context) {
        return getConnectivityManager(context).getActiveNetworkInfo();
    }

    /**
     * 是否为某类型的网络
     */
    public static boolean isNetworkType(Context context, int type) {
        NetworkInfo activeInfo = getActiveNetworkInfo(context);
        return isConnected(activeInfo) && (type == activeInfo.getType());
    }

    /**
     * 网络是否可用
     */
    public static boolean isConnected(NetworkInfo info) {
        return info != null && info.isAvailable() && info.isConnected();
    }

    /**
     * 网络是否可用
     */
    public static boolean isConnected(Context context) {
        return isConnected(getActiveNetworkInfo(context));
    }

    /**
     * 是否为手机网络
     */
    public static boolean isMobile(Context context) {
        return isNetworkType(context, ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * 是否为WiFi
     */
    public static boolean isWiFi(Context context) {
        return isNetworkType(context, ConnectivityManager.TYPE_WIFI);
    }

    /**
     * 是否为手机网络或WiFi
     */
    public static boolean isMobileOrWiFi(Context context) {
        return isMobile(context) || isWiFi(context);
    }
}
