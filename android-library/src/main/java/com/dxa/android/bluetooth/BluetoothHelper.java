package com.dxa.android.bluetooth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 蓝牙帮助类
 */
public final class BluetoothHelper {
    private static final String TAG = "BluetoothHelper";

    public static final String UNKNOWN = "UNKNOWN";
    public static final int DEFAULT = -1;
    /**
     * 请求蓝牙可用
     */
    public static final int REQUEST_ENABLE_BT = 0x01;
    /**
     * 请求蓝牙不可用
     */
    public static final int REQUEST_DISABLE_BT = 0x02;
    /**
     * 扫描蓝牙
     */
    public static final int REQUEST_DISCOVER_BT = 0x03;

    /**
     * 私有，不允许创建对象
     *
     * @throws IllegalAccessException
     */
    private BluetoothHelper() throws IllegalAccessException {
        throw new IllegalAccessException("BluetoothHelper cannot be new directly!");
    }

    /**
     * 获得蓝牙适配器
     */
    public static BluetoothAdapter getAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * 是否支持蓝牙
     */
    public static boolean isSupported() {
        return BluetoothAdapter.getDefaultAdapter() != null;
    }

    /**
     * 是否支持BLE功能，即蓝牙低功耗
     */
    @SuppressLint("InlinedApi")
    public static boolean isSupportBLE(Context context) {
        return context.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }


    private static boolean isEnabled(BluetoothAdapter adapter) {
        return adapter.isEnabled();
    }

    /**
     * 蓝牙是否可用
     */
    public static boolean isEnabled() {
        return getAdapter().isEnabled();
    }

    /**
     * 打开蓝牙
     *
     * @param activity    Activity对象
     * @param requestCode 请求码
     */
    public static void enable(Activity activity, int requestCode) {
        if (!isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, requestCode);
        }
    }

    /**
     * 打开蓝牙
     */
    public static boolean enable() {
        BluetoothAdapter adapter = getAdapter();
        return isEnabled(adapter) || adapter.enable();
    }

    /**
     * 关闭蓝牙设备
     */
    public static boolean disable() {
        return getAdapter().disable();
    }


    /**
     * 扫描蓝牙设备
     */
    public static boolean startDiscovery() {
        BluetoothAdapter adapter = getAdapter();
        return isEnabled(adapter) && adapter.startDiscovery();
    }

    /**
     * 取消扫描
     */
    public static boolean cancelDiscovery() {
        BluetoothAdapter adapter = getAdapter();
        return isEnabled(adapter) && adapter.cancelDiscovery();
    }

    /**
     * 是否正在扫描
     */
    public static boolean isDiscovering() {
        BluetoothAdapter adapter = getAdapter();
        return isEnabled(adapter) && adapter.isDiscovering();
    }

    /**
     * 设置蓝牙可连接和可被扫描
     *
     * @param activity    Activity对象
     * @param requestCode 请求码
     * @param duration    可被扫描到的时长
     */
    public static void setConnectableAndDiscoverable(
            Activity activity, int requestCode, int duration) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, duration);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 获取配对的蓝牙设备
     */
    public static Set<BluetoothDevice> getBondedDevices() {
        BluetoothAdapter adapter = getAdapter();
        return isEnabled(adapter) ? adapter.getBondedDevices() : new HashSet<>();
    }

    /**
     * 获取配对的蓝牙设备
     */
    public static List<BluetoothDevice> getBondedDeviceList() {
        Set<BluetoothDevice> devices = getBondedDevices();
        return new ArrayList<>(devices);
    }

    /**
     * 设置蓝牙名
     */
    public static void setName(String deviceName) {
        BluetoothAdapter adapter = getAdapter();
        if (isEnabled(adapter))
            adapter.setName(deviceName);
        else
            Log.d(TAG, "setName: 蓝牙未开启!");
    }

    /**
     * 获取蓝牙名
     */
    public static String getName() {
        BluetoothAdapter adapter = getAdapter();
        return isEnabled(adapter) ? adapter.getName() : UNKNOWN;
    }

    /**
     * 获取蓝牙的MAC地址
     */
    @SuppressLint("HardwareIds")
    public static String getAddress() {
        BluetoothAdapter adapter = getAdapter();
        return isEnabled(adapter) ? adapter.getAddress() : UNKNOWN;
    }

    /**
     * 获取蓝牙的状态
     */
    public static int getState() {
        BluetoothAdapter adapter = getAdapter();
        return isEnabled(adapter) ? adapter.getState() : DEFAULT;
    }

    /**
     * 获取蓝牙的扫描模式
     */
    public static int getScanMode() {
        BluetoothAdapter adapter = getAdapter();
        return isEnabled(adapter) ? adapter.getScanMode() : DEFAULT;
    }

    /**
     * 获取扫描的过滤器
     */
    public static IntentFilter getDiscoverFilter() {
        IntentFilter filter = new IntentFilter();
        // 开始扫描
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        // 发现设备
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        // 扫描结束
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        return filter;
    }

    /**
     * 获取配对的过滤器
     */
    public static IntentFilter getBondFilter() {
        IntentFilter filter = new IntentFilter();
        if (android.os.Build.VERSION_CODES.KITKAT < android.os.Build.VERSION.SDK_INT) {
            // 配对请求
            filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        }
        // 配对状态改变
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        return filter;
    }

    /**
     * 获取蓝牙状态改变的过滤器
     */
    public static IntentFilter getStateChangedFilter() {
        return new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
    }

    /**
     * 取消注册
     */
    public static void unregister(Context context, BroadcastReceiver receiver) {
        if (notNull(context) && notNull(receiver))
            context.unregisterReceiver(receiver);
    }

    private static boolean notNull(Object o) {
        return o != null;
    }
}
