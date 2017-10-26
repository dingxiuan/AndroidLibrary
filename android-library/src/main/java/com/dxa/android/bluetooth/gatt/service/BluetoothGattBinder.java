package com.dxa.android.bluetooth.gatt.service;

import android.content.Context;
import android.os.Binder;

import com.dxa.android.bluetooth.gatt.impl.BluetoothGattManager;

/**
 *
 */
public class BluetoothGattBinder extends Binder {

    private final BluetoothGattManager gattManager;

    public BluetoothGattBinder(BluetoothGattManager gattManager) {
        this.gattManager = gattManager;
    }

    public BluetoothGattBinder(Context context){
        this.gattManager = new BluetoothGattManager(context);
    }

    public BluetoothGattManager getGattManager() {
        return gattManager;
    }

}
