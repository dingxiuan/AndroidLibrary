package com.dxa.android.bluetooth.gatt.impl;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;

import com.dxa.android.bluetooth.gatt.BluetoothGattClient;
import com.dxa.android.bluetooth.gatt.BluetoothGattTool;
import com.dxa.android.bluetooth.gatt.OnGattChangedListener;
import com.dxa.android.logger.DLogger;
import com.dxa.android.logger.LogLevel;

import java.util.UUID;

/**
 *
 */
public class BluetoothGattManager {

    private final DLogger logger = new DLogger(getClass().getSimpleName());
    private final Context context;
    private final BluetoothGattClient client;

    private BluetoothGattService mService;
    private BluetoothGattCharacteristic mCharacteristic;

    public BluetoothGattManager(Context context) {
        this.context = context;
        GattCallback gattCallback = new GattCallback();
        this.client = new DefaultBluetoothGattClient(gattCallback);
    }

    public BluetoothGattManager(Context context, BluetoothGattClient client) {
        this.context = context;
        this.client = client;
    }

    public void setDebug(boolean debug) {
        logger.i(debug ? LogLevel.DEBUG : LogLevel.NONE);
    }

    public BluetoothGattClient getClient() {
        return client;
    }

    /**
     * 连接设备
     */
    public boolean connect(String address, boolean autoConnect) {
        return getClient().connect(context, address, autoConnect);
    }

    /**
     * 连接设备
     */
    public boolean connect(BluetoothDevice device, boolean autoConnect) {
        return getClient().connect(context, device, autoConnect);
    }

    /**
     * 写入数据
     *
     * @param characteristic BluetoothGattCharacteristic对象
     * @param hex            16进制字符串
     * @return 是否写入
     */
    public boolean write(BluetoothGattCharacteristic characteristic, String hex) {
        byte[] value = BluetoothGattTool.hexToBin(hex);
        return getClient().writeCharacteristic(characteristic, value);
    }

    /**
     * 写入数据
     *
     * @param characteristic BluetoothGattCharacteristic对象
     * @param value          字节数组
     * @return 是否写入
     */
    public boolean write(BluetoothGattCharacteristic characteristic, byte[] value) {
        return getClient().writeCharacteristic(characteristic, value);
    }

    /**
     * 写入数据
     *
     * @param value 字符串
     * @return 是否写入
     */
    public boolean write(String value) {
        required();
        return write(mCharacteristic, value);
    }

    /**
     * 写入数据
     *
     * @param value 数据
     * @return 是否写入
     */
    public boolean write(byte[] value) {
        required();
        return write(mCharacteristic, value);
    }

    /**
     * 重连
     */
    public boolean reconnect() {
        return getClient().reconnect();
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        getClient().disconnect();
    }

    /**
     * 断开连接
     *
     * @param close 是否关闭GATT
     */
    public void disconnect(boolean close) {
        getClient().disconnect(close);
    }

    public void setOnGattChangedListener(OnGattChangedListener listener) {
        getClient().setOnGattChangedListener(listener);
    }

    /**
     * 设置默认的服务和特征
     */
    public void setDefaultServiceWithCharacteristic(
            BluetoothGattService gattService, BluetoothGattCharacteristic gattCharacteristic) {
        this.mService = gattService;
        this.mCharacteristic = gattCharacteristic;
    }

    public void setDefaultServiceWithCharacteristic(UUID serviceUUID, UUID characteristicUUID) {
        this.mService = getClient().getGattService(serviceUUID);
        this.mCharacteristic = getClient().getGattCharacteristic(serviceUUID, characteristicUUID);
    }

    /**
     * 获取默认的服务
     */
    public BluetoothGattService getDefaultService() {
        return mService;
    }

    /**
     * 获取默认的特征
     */
    public BluetoothGattCharacteristic getDefaultCharacteristic() {
        return mCharacteristic;
    }

    /**
     * 检查是否有无默认值
     */
    private void required() {
        if (mService == null)
            throw new IllegalStateException("没有默认的BluetoothGattService");

        if (mCharacteristic == null)
            throw new IllegalStateException("没有默认的BluetoothGattCharacteristic");
    }


}
