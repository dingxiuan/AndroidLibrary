package com.dxa.android.bluetooth.gatt.model;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 */

public class GattCharacteristic {

    private UUID mUUID;
    private BluetoothGattCharacteristic mCharacteristic;
    private Map<String, BluetoothGattDescriptor> mDescriptorMap;

    public GattCharacteristic(BluetoothGattCharacteristic characteristic){
        this.mCharacteristic = characteristic;
        getCharacteristicInfo(characteristic);
    }

    private void getCharacteristicInfo(BluetoothGattCharacteristic characteristic) {
        List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();

    }

    public UUID getUUID() {
        return mUUID;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return mCharacteristic;
    }

    public Map<String, BluetoothGattDescriptor> getDescriptorMap() {
        return mDescriptorMap;
    }

}
