package com.dxa.android.bluetooth.le;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Binder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 蓝牙的服务
 */
public class BluetoothLeBinder extends Binder implements IBluetoothLe {

    private BluetoothLeClient client;

    public BluetoothLeBinder(@NonNull BluetoothLeClient client) {
        this.client = client;
    }

    private BluetoothLeClient getClient() {
        return client;
    }

    @Override
    public boolean connect(@NonNull String address, boolean autoConnect) {
        return getClient().connect(address, autoConnect);
    }

    @Override
    public boolean write(@NonNull BluetoothGattCharacteristic characteristics,
                         @NonNull String s,
                         boolean enabled) {
        BluetoothGatt gatt = getClient().getBluetoothGatt();
        return gatt != null && BluetoothLeUtils.write(gatt, characteristics, s, enabled);
    }

    @Override
    public boolean write(@NonNull BluetoothGattCharacteristic characteristics,
                         @NonNull byte[] bytes,
                         boolean enabled) {
        BluetoothGatt gatt = getClient().getBluetoothGatt();
        return gatt != null && BluetoothLeUtils.write(gatt, characteristics, bytes, enabled);
    }

    @Override
    public void setCharacteristicNotification(@NonNull BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        getClient().setCharacteristicNotification(characteristic, enabled);
    }

    @Override
    public boolean reconnect() {
        return getClient().reconnect();
    }

    @Override
    public void disconnect() {
        getClient().disconnect();
    }

    @Override
    public void setOnGattStatusListener(@Nullable OnGattChangedListener listener) {
        getClient().setOnGattChangedListener(listener);
    }
}
