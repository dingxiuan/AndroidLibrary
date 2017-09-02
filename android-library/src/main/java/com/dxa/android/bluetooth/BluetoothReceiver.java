package com.dxa.android.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 蓝牙广播（蓝牙开启关闭的状态、蓝牙扫描的状态）
 */
public abstract class BluetoothReceiver extends BroadcastReceiver {

	public BluetoothReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
			// 蓝牙状态改变
			int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
					BluetoothAdapter.ERROR);
			switch (state) {
				case BluetoothAdapter.STATE_TURNING_ON:
					onBluetoothStateTurnOn();
					break;
				case BluetoothAdapter.STATE_OFF:
					onBluetoothStateOff();
					break;
				case BluetoothAdapter.STATE_ON:
					onBluetoothStateOn();
					break;
				case BluetoothAdapter.STATE_TURNING_OFF:
					onBluetoothStateTurnOff();
					break;
			}
		} else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
			// 开始扫描
			onDiscoveryStarted();
		} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
			// 扫描结束
			onDiscoveryFinished();
		} else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
			// 扫描到设备
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			BluetoothClass clazz = intent.getParcelableExtra(BluetoothDevice.EXTRA_CLASS);
			onFoundDevice(device, clazz);
		}
	}
	
	/**
	 * 蓝牙被打开
	 */
	protected abstract void onBluetoothStateTurnOn();
	
	/**
	 * 蓝牙已开启
	 */
	protected abstract void onBluetoothStateOn();

	/**
	 * 蓝牙关闭
	 */
	protected abstract void onBluetoothStateTurnOff();
	
	/**
	 * 蓝牙已关闭
	 */
	protected abstract void onBluetoothStateOff();

	/**
	 * 开始扫描
	 */
	protected abstract void onDiscoveryStarted();
	
	/**
	 * 发现设备时
	 * @param device 设备
	 * @param clazz 设备的class
	 */
	protected abstract void onFoundDevice(
			BluetoothDevice device, BluetoothClass clazz);
	
	/**
	 * 扫描结束
	 */
	protected abstract void onDiscoveryFinished();



	/**
	 * 过滤器：蓝牙状态改变、开始扫描，扫描到设备
	 */
	public static IntentFilter makeFilter() {
		IntentFilter filter = new IntentFilter();
		// 蓝牙状态改变
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		// 开始扫描
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		// 发现设备
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		// 扫描结束
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		return filter;
	}

}
