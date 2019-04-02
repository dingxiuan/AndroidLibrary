package com.dxa.android.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 蓝牙扫描接收的广播
 */
public class BluetoothDiscoveryReceiver extends BroadcastReceiver {
    /**
     * 注册广播
     */
    public static BroadcastReceiver register(Context context, Callback callback) {
        if (callback == null) throw new NullPointerException("Callback's instance is null !");
        IntentFilter filter = BluetoothHelper.getDiscoverFilter();
        BluetoothDiscoveryReceiver receiver = new BluetoothDiscoveryReceiver(callback);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
        return receiver;
    }

    /**
     * 取消注册广播
     *
     * @param context  上下文对象
     * @param receiver 广播
     */
    public static void unregister(Context context, BluetoothDiscoveryReceiver receiver) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }

    private static final Map<Context, BroadcastReceiver> receiverCache = new ConcurrentHashMap<>();

    private Callback callback;

    public BluetoothDiscoveryReceiver(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            callback.onDiscoveryStarted();
        } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            BluetoothClass clazz = intent.getParcelableExtra(BluetoothDevice.EXTRA_CLASS);
            callback.onFoundDevice(device, clazz);
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            callback.onDiscoveryFinished();
        }
    }

    /**
     * 处理蓝牙扫描的回调
     */
    public interface Callback {
        /**
         * 当扫描开始时
         */
        void onDiscoveryStarted();

        /**
         * 当发现设备时
         */
        void onFoundDevice(BluetoothDevice device, BluetoothClass clazz);

        /**
         * 当扫描结束时
         */
        void onDiscoveryFinished();
    }

}
