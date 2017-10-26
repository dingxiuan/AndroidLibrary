package com.dxa.android.bluetooth.gatt.model;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import java.util.Arrays;
import java.util.UUID;

/**
 */

public class GattDescriptor {
    /**
     * GattDescriptor的UUID
     */
    private UUID mUuid;
    /**
     * GattDescriptor所在的GattCharacteristic
     */
    private BluetoothGattCharacteristic mCharacteristic;
    /**
     * GattDescriptor对应的BluetoothGattDescriptor
     */
    private BluetoothGattDescriptor mDescriptor;

    /**
     * Permissions for this descriptor
     */
    protected int mPermissions;
    /**
     * The value for this descriptor.
     */
    protected byte[] mValue;

    public GattDescriptor(BluetoothGattDescriptor descriptor){
        this.mDescriptor = descriptor;

        getGattDescriptorInfo(descriptor);
    }

    private void getGattDescriptorInfo(BluetoothGattDescriptor descriptor) {
        mUuid = descriptor.getUuid();
        mCharacteristic = descriptor.getCharacteristic();
        mPermissions = descriptor.getPermissions();
        mValue = descriptor.getValue();
    }

    public UUID getUuid() {
        return mUuid;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return mCharacteristic;
    }

    public BluetoothGattDescriptor getDescriptor() {
        return mDescriptor;
    }

    public int getPermissions() {
        return mPermissions;
    }

    public byte[] getValue() {
        return mValue;
    }

    @Override
    public String toString() {
        return "GattDescriptor{" +
                "uuid=" + mUuid +
                ", characteristic=" + mCharacteristic +
                ", descriptor=" + mDescriptor +
                ", permissions=" + mPermissions +
                ", value=" + Arrays.toString(mValue) +
                '}';
    }
}
