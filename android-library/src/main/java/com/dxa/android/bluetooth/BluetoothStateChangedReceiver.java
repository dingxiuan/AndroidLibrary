package com.dxa.android.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 蓝牙状态的广播
 */

public abstract class BluetoothStateChangedReceiver extends BroadcastReceiver {

    /**
     * 获取IntentFilter
     */
    public static IntentFilter makeFilter() {
        return new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            int state = intent.getIntExtra(
                    BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            switch (state) {
            	case BluetoothAdapter.STATE_TURNING_ON:
	                onBluetoothStateTurnOn();
	                break;
                case BluetoothAdapter.STATE_ON:
                    onBluetoothStateOn();
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    onBluetoothStateTurnOff();
                    break;
                case BluetoothAdapter.STATE_OFF:
                    onBluetoothStateOff();
                    break;
            }
        }
    }

    /**
     * 蓝牙已关闭
     */
    protected abstract void onBluetoothStateOff();

    /**
     * 蓝牙已打开
     */
    protected abstract void onBluetoothStateOn();

    /**
     * 蓝牙打开时
     */
    protected abstract void onBluetoothStateTurnOn();

    /**
     * 蓝牙关闭时
     */
    protected abstract void onBluetoothStateTurnOff();
}
