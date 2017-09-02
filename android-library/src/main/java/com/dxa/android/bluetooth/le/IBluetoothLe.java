package com.dxa.android.bluetooth.le;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 蓝牙Binder的接口
 */

public interface IBluetoothLe {

    /**
     * 连接设备
     */
    boolean connect(@NonNull String address, boolean autoConnect);

    /**
     * 写入数据
     *
     * @param characteristics BluetoothGattCharacteristic对象
     * @param s               字符串
     * @param enabled         是否提醒
     * @return 是否写入
     */
    boolean write(@NonNull BluetoothGattCharacteristic characteristics,
                  String s,
                  boolean enabled);

    /**
     * 写入数据
     *
     * @param characteristics BluetoothGattCharacteristic对象
     * @param bytes           字节数组
     * @param enabled         是否提醒
     * @return 是否写入
     */
    boolean write(@NonNull BluetoothGattCharacteristic characteristics,
                  byte[] bytes,
                  boolean enabled);

    /**
     * 设置是否提醒
     *
     * @param characteristic BluetoothGattCharacteristic对象
     * @param enabled        是否提醒
     */
    void setCharacteristicNotification(@NonNull BluetoothGattCharacteristic characteristic,
                                       boolean enabled);

    /**
     * 重连
     */
    boolean reconnect();

    /**
     * 断开连接
     */
    void disconnect();

    /**
     * 设置监听
     */
    void setOnGattStatusListener(@Nullable OnGattChangedListener listener);

}
