package com.dxa.android.bluetooth.le;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

/**
 * 当状态改变时的监听
 */

public interface OnGattChangedListener {

    /**
     * 连接设备
     */
    void onConnect(BluetoothGatt gatt);

    /**
     * 发送服务
     */
    void onServiceDiscover(BluetoothGatt gatt);

    /**
     * 数据可读取
     */
    void onCharacteristicRead(BluetoothGatt gatt,
                              BluetoothGattCharacteristic characteristic,
                              int status);

    /**
     * 数据可写入
     */
    void onCharacteristicWrite(BluetoothGatt gatt,
                               BluetoothGattCharacteristic characteristic);

    /**
     * Characteristic改变时回调
     */
    void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic);

    /**
     * 断开连接
     */
    void onDisconnect(BluetoothGatt gatt);
}
