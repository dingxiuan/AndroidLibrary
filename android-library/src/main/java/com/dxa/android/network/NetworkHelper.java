package com.dxa.android.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络工具类，不要使用了
 */
@Deprecated
public class NetworkHelper {

    private NetworkHelper() {
        throw new IllegalAccessError("Cannot new instance.");
    }

    /**
     * 获取网络状态信息
     */
    public static NetworkInfo getNetworkInfo(Context context) {
        if (context == null)
            return null;

        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo();
    }

    /**
     * 是否为某类型的网络
     */
    public static boolean isNetworkType(Context context, int type) {
        NetworkInfo activeInfo = getNetworkInfo(context);
        return isConnected(activeInfo) && type == activeInfo.getType();
    }

    /**
     * 网络是否可用
     */
    public static boolean isConnected(NetworkInfo networkInfo) {
        return networkInfo != null && networkInfo.isAvailable()
                && networkInfo.isConnected();
    }

    /**
     * 网络是否可用
     */
    public static boolean isConnected(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        return isConnected(networkInfo);
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

    /**
     * 获取网络实体
     */
    public static NetworkEntity getNetworkEntity(Context context) {
        NetworkInfo activeInfo = getNetworkInfo(context);
        boolean availableAndConnected = isConnected(activeInfo);
        NetworkEntity.Builder builder = new NetworkEntity.Builder();
        if (availableAndConnected) {
            int type = activeInfo.getType();
            builder.setConnected(true);
            if (type == ConnectivityManager.TYPE_WIFI) {
                builder.setType(ConnectivityManager.TYPE_WIFI);
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                builder.setConnected(true).setType(ConnectivityManager.TYPE_MOBILE);
            } else {
                builder.setType(type);
            }
        } else {
            builder.setConnected(false).setType(-1);
        }
        return builder.build();
    }

    /**
     * 网络实体
     */
    public static class NetworkEntity {

        private boolean connected;
        private int type;

        private NetworkEntity() {
        }

        public boolean isConnected() {
            return connected;
        }

        void setConnected(boolean connected) {
            this.connected = connected;
        }

        public int getType() {
            return type;
        }

        void setType(int type) {
            this.type = type;
        }

        public static class Builder {

            private NetworkEntity entity;

            public Builder() {
                this.entity = new NetworkEntity();
            }

            public Builder setConnected(boolean connected) {
                entity.setConnected(connected);
                return this;
            }

            public Builder setType(int type) {
                entity.setType(type);
                return this;
            }

            public NetworkEntity build() {
                return entity;
            }
        }
    }

}
