
package com.dxa.android.bluetooth.le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.text.TextUtils;

import com.dxa.android.logger.DLogger;
import com.dxa.android.logger.LogLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
public class BluetoothLeClient {
    private final static String TAG = BluetoothLeClient.class.getSimpleName();
    private final DLogger logger = new DLogger(TAG, LogLevel.DEBUG);

    private final Object lock = new Object();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;

    private OnGattChangedListener listener;
    private Context mContext;

    /**
     * 是否连接
     */
    private boolean connect;

    public BluetoothLeClient(Context context) {
        mContext = context;
    }

    public void setOnGattChangedListener(OnGattChangedListener listener) {
        this.listener = listener;
    }

    /**
     * Initializes a reference to the local Bluetooth adapter.
     * @param force 是否强制初始化，当蓝牙被重新打开时，可以强制再次初始化
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize(boolean force) {
        if (force) {
            mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
            if (isNull(mBluetoothManager)) {
                logger.e("Unable to initialize BluetoothManager.");
                return false;
            }

        } else {
            if (isNull(mBluetoothManager)) {
                mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
                if (isNull(mBluetoothManager)) {
                    logger.e("Unable to initialize BluetoothManager.");
                    return false;
                }
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (isNull(mBluetoothAdapter)) {
            logger.e("Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address, boolean autoConnect) {
        if (isNull(mBluetoothAdapter) || TextUtils.isEmpty(address)) {
            logger.w("BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (notEmpty(mBluetoothDeviceAddress) && notNull(mBluetoothGatt)
                && mBluetoothDeviceAddress.equalsIgnoreCase(address)) {

            logger.d("Trying to use an existing mBluetoothGatt for connection.");
            return mBluetoothGatt.connect();
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (isNull(device)) {
            logger.w("Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(mContext, autoConnect, mGattCallback);
        logger.d("Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        return true;
    }

    public boolean reconnect() {
        if (isNull(mBluetoothAdapter) || TextUtils.isEmpty(mBluetoothDeviceAddress)) {
            logger.w("BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (notNull(mBluetoothGatt)) {
            logger.d("Trying to use an existing mBluetoothGatt for connection.");
            return mBluetoothGatt.connect();
        } else {
            logger.d("BluetoothGatt's object is null.");
            return false;
        }

    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (isNull(mBluetoothAdapter) || isNull(mBluetoothGatt)) {
            logger.w("BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (isNull(mBluetoothGatt)) {
            return;
        }
        disconnect();
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * 是否已连接
     */
    public boolean isConnected() {
        return connect;
    }

    /**
     * 获取BluetoothGatt对象
     */
    public BluetoothGatt getBluetoothGatt() {
        return mBluetoothGatt;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (isNull(mBluetoothAdapter) || isNull(mBluetoothGatt)) {
            logger.w("BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (isNull(mBluetoothAdapter) || isNull(mBluetoothGatt)) {
            logger.w("BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        mBluetoothGatt.writeCharacteristic(characteristic);
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (isNull(mBluetoothGatt)) return new ArrayList<>();

        return mBluetoothGatt.getServices();
    }


    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                synchronized (lock) {
                    logger.i("Connected to GATT server.");
                    connect = true;
                    if (listener != null)
                        listener.onConnect(gatt);
                    // Attempts to discover services after successful connection.
                    logger.i("Attempting to start service discovery:" +
                            mBluetoothGatt.discoverServices());
                }
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                synchronized (lock) {
                    logger.i("Disconnected from GATT server.");
                    connect = false;
                    if (listener != null) {
                        listener.onDisconnect(gatt);
                    }
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                synchronized (lock) {
                    if (listener != null) {
                        listener.onServiceDiscover(gatt);
                    }
                }
            } else {
                logger.w("onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            synchronized (lock) {
                if (listener != null) {
                    listener.onCharacteristicRead(gatt, characteristic, status);
                }
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            synchronized (lock) {
                if (listener != null) {
                    listener.onCharacteristicChanged(gatt, characteristic);
                }
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            byte[] value = characteristic.getValue();
            logger.i("写入数据: " + BluetoothLeUtils.bytesToHexString(value));

            synchronized (lock) {
                if (listener != null) {
                    listener.onCharacteristicWrite(gatt, characteristic);
                }
            }
        }
    };


    /***********************************************************************/

    private static boolean notNull(Object o) {
        return o != null;
    }

    private static boolean isNull(Object o) {
        return o == null;
    }

    private static boolean notEmpty(String s) {
        return s != null && !"".equals(s);
    }

}
