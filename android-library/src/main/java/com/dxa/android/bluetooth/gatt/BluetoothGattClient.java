package com.dxa.android.bluetooth.gatt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;

/**
 * 蓝牙4.0客户端
 */

public interface BluetoothGattClient extends GattOperator {

    /**
     * 获取 BluetoothAdapter
     */
    BluetoothAdapter getAdapter();

    /**
     * 蓝牙是否可用
     */
    boolean isEnabled();

    /**
     * 获取当前的设备
     *
     * @return 返回当前连接的设备或null
     */
    BluetoothDevice getCurrentDevice();

    /**
     * 连接设备
     *
     * @param context     上下文对象
     * @param address     设备的MAC地址
     * @param autoConnect 是否自动重连，设备休眠或超过距离断开后，当设备可以重新连接时，系统会自动重新连接
     * @return 是否连接
     */
    boolean connect(Context context, String address, boolean autoConnect);

    /**
     * 连接设备
     *
     * @param context     上下文对象
     * @param device      即将连接的设备
     * @param autoConnect 是否自动重连，设备休眠或超过距离断开后，当设备可以重新连接时，系统会自动重新连接
     * @return 是否连接
     */
    boolean connect(Context context, BluetoothDevice device, boolean autoConnect);

    /**
     * 重新连接，如果之前连接过设备
     *
     * @return 是否重新连接
     */
    boolean reconnect();

    /**
     * 获取BluetoothGatt对象
     *
     * @return 返回 BluetoothGatt 或 null
     */
    BluetoothGatt getBluetoothGatt();

    /**
     * 断开连接
     */
    void disconnect();

    /**
     * 断开连接
     *
     * @param close 是否顺便关闭资源
     */
    void disconnect(boolean close);

    /**
     * 使用BLE设备后，app必须调用此方法以确保资源被合理释放
     */
    void close();

    /**
     * 设置OnGattChangedListener监听
     *
     * @param listener OnGattChangedListener实现对象
     */
    void setOnGattChangedListener(OnGattChangedListener listener);

    /**
     * 是否已连接
     */
    boolean isConnected();

}
