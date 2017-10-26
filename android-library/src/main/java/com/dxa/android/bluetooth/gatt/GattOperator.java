package com.dxa.android.bluetooth.gatt;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

import java.util.UUID;

/**
 *
 */
public interface GattOperator {

    /**
     * 获取对应UUID的BluetoothGattService
     *
     * @param serviceUUID BluetoothGattService对应的UUID
     * @return 返回对应UUID的BluetoothGattService或null
     */
    BluetoothGattService getGattService(UUID serviceUUID);

    /**
     * 获取BluetoothGattCharacteristic
     *
     * @param serviceUUID        BluetoothGattService的UUID
     * @param characteristicUUID BluetoothGattCharacteristic的UUID
     * @return 返回对应UUID的BluetoothGattCharacteristic或null
     */
    BluetoothGattCharacteristic getGattCharacteristic(UUID serviceUUID, UUID characteristicUUID);

    /**
     * @param serviceUUID        BluetoothGattService的UUID
     * @param characteristicUUID BluetoothGattCharacteristic的UUID
     * @param enableNotification 可读时是否提醒
     * @return 是否读取
     */
    boolean readCharacteristic(UUID serviceUUID, UUID characteristicUUID, boolean enableNotification);

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic     The characteristic to read from.
     * @param enableNotification 可读时是否提醒.
     * @return 是否读取
     */
    boolean readCharacteristic(BluetoothGattCharacteristic characteristic, boolean enableNotification);

    /**
     * 写入特征值
     */
    boolean writeCharacteristic(BluetoothGattCharacteristic characteristic, byte[] value);

    /**
     * 写入特征值
     */
    boolean writeCharacteristic(BluetoothGattService service, UUID characteristicUUID, byte[] value);

    /**
     * 写入特征值
     */
    boolean writeCharacteristic(UUID serviceUUID, UUID characteristicUUID, byte[] value);

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     * @return 是否设置BluetoothGattCharacteristic提醒
     */
    boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled);

    /**
     * 获取对应UUID的BluetoothGattDescriptor
     */
    BluetoothGattDescriptor getGattDescriptor(BluetoothGattCharacteristic characteristic, UUID descriptorUUID);

    /**
     * 给BluetoothGattDescriptor设置值
     * {@link BluetoothGattDescriptor#ENABLE_NOTIFICATION_VALUE}
     * {@link BluetoothGattDescriptor#ENABLE_INDICATION_VALUE}
     * {@link BluetoothGattDescriptor#DISABLE_NOTIFICATION_VALUE}
     */
    boolean setDescriptorValue(BluetoothGattCharacteristic characteristic, UUID descriptorUUID, byte[] value);

    /**
     * 给BluetoothGattDescriptor设置值
     * {@link BluetoothGattDescriptor#ENABLE_NOTIFICATION_VALUE}
     * {@link BluetoothGattDescriptor#ENABLE_INDICATION_VALUE}
     * {@link BluetoothGattDescriptor#DISABLE_NOTIFICATION_VALUE}
     */
    boolean setDescriptorValue(UUID serviceUUID, UUID characteristicUUID, UUID descriptorUUID, byte[] value);

}
