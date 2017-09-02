package com.dxa.android.bluetooth.le;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class BluetoothLeUtils {

    private static HashMap<Integer, String> serviceTypes = new HashMap<>();

    static {
        // Sample Services.
        serviceTypes.put(BluetoothGattService.SERVICE_TYPE_PRIMARY, "PRIMARY");
        serviceTypes.put(BluetoothGattService.SERVICE_TYPE_SECONDARY, "SECONDARY");
    }

    public static String getServiceType(int type) {
        return serviceTypes.get(type);
    }


    //-------------------------------------------    
    private static HashMap<Integer, String> charPermissions = new HashMap<>();

    static {
        charPermissions.put(0, "UNKNOW");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_READ, "READ");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED, "READ_ENCRYPTED");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED_MITM, "READ_ENCRYPTED_MITM");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE, "WRITE");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED, "WRITE_ENCRYPTED");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED_MITM, "WRITE_ENCRYPTED_MITM");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE_SIGNED, "WRITE_SIGNED");
        charPermissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE_SIGNED_MITM, "WRITE_SIGNED_MITM");
    }

    public static String getCharPermission(int permission) {
        return getHashMapValue(charPermissions, permission);
    }

    //-------------------------------------------
    private static HashMap<Integer, String> charProperties = new HashMap<>();

    static {

        charProperties.put(BluetoothGattCharacteristic.PROPERTY_BROADCAST, "BROADCAST");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_EXTENDED_PROPS, "EXTENDED_PROPS");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_INDICATE, "INDICATE");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_NOTIFY, "NOTIFY");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_READ, "READ");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE, "SIGNED_WRITE");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_WRITE, "WRITE");
        charProperties.put(BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE, "WRITE_NO_RESPONSE");
    }

    public static String getCharPropertie(int property) {
        return getHashMapValue(charProperties, property);
    }

    //--------------------------------------------------------------------------
    private static HashMap<Integer, String> descPermissions = new HashMap<>();

    static {
        descPermissions.put(0, "UNKNOW");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_READ, "READ");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_READ_ENCRYPTED, "READ_ENCRYPTED");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_READ_ENCRYPTED_MITM, "READ_ENCRYPTED_MITM");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_WRITE, "WRITE");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED, "WRITE_ENCRYPTED");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED_MITM, "WRITE_ENCRYPTED_MITM");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_WRITE_SIGNED, "WRITE_SIGNED");
        descPermissions.put(BluetoothGattDescriptor.PERMISSION_WRITE_SIGNED_MITM, "WRITE_SIGNED_MITM");
    }

    public static String getDescPermission(int property) {
        return getHashMapValue(descPermissions, property);
    }


    private static String getHashMapValue(HashMap<Integer, String> hashMap, int number) {
        String result = hashMap.get(number);
        if (TextUtils.isEmpty(result)) {
            List<Integer> numbers = getElement(number);
            result = "";
            for (int i = 0; i < numbers.size(); i++) {
                result += hashMap.get(numbers.get(i)) + "|";
            }
        }
        return result;
    }

    /**
     * 位运算结果的反推函数10 -> 2 | 8;
     */
    static private List<Integer> getElement(int number) {
        List<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < 32; i++) {
            int b = 1 << i;
            if ((number & b) > 0)
                result.add(b);
        }

        return result;
    }


    public static String bytesToHexString(byte[] src) {
        if (src == null || src.length <= 0) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder("");

        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase(Locale.getDefault());
    }

    public static final char[] DIGITAL = "0123456789ABCDEF".toCharArray();

    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 字节转换成字符串
     *
     * @param bs 字节数组
     * @return 转换后的字符串
     */
    public static String bin2hex(byte[] bs) {
        StringBuilder builder = new StringBuilder();
        byte[] var5 = bs;
        int var4 = bs.length;

        for (int var3 = 0; var3 < var4; ++var3) {
            byte b = var5[var3];
            builder.append(DIGITAL[(b & 240) >> 4]).append(DIGITAL[b & 15]);
        }

        return builder.toString();
    }

    /**
     * 16进制字符串转换成字节数组
     *
     * @param hexString 字符串
     * @return 转换的字节数组
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString != null && hexString.trim().length() > 0) {
            hexString = hexString.toUpperCase(Locale.getDefault());
            int length = hexString.length() / 2;
            char[] hexChars = hexString.toCharArray();
            byte[] d = new byte[length];

            for (int i = 0; i < length; ++i) {
                int pos = i * 2;
                d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
            }

            return d;
        } else {
            return null;
        }
    }

    /**
     * 查找指定UUID的BluetoothGattCharacteristic对象(根据两个UUID查找)
     *
     * @param gattServices       BluetoothGattService的集合
     * @param serviceUUID        BluetoothGattService的UUID
     * @param characteristicUUID BluetoothGattCharacteristic的UUID
     * @return 查找到的对象，如果没有返回null；
     */
    public static BluetoothGattCharacteristic findGattCharacteristic(
            List<BluetoothGattService> gattServices,
            UUID serviceUUID,
            UUID characteristicUUID) {
        if (gattServices == null
                || gattServices.isEmpty()
                || serviceUUID == null
                || characteristicUUID == null)
            return null;

        String sServiceUUID = serviceUUID.toString();
        String cCharacteristicUUID = characteristicUUID.toString();
        for (BluetoothGattService service : gattServices) {
            UUID sUuid = service.getUuid();
            if (sServiceUUID.equalsIgnoreCase(sUuid.toString())) {
                List<BluetoothGattCharacteristic> gattCharacteristics = service.getCharacteristics();
                for (BluetoothGattCharacteristic characteristic : gattCharacteristics) {
                    UUID cUuid = characteristic.getUuid();
                    if (cCharacteristicUUID.equalsIgnoreCase(cUuid.toString())) {
                        return characteristic;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 查找指定UUID的BluetoothGattCharacteristic对象(根据两个UUID查找)
     *
     * @param gattService        BluetoothGattService的对象
     * @param serviceUUID        BluetoothGattService的UUID
     * @param characteristicUUID BluetoothGattCharacteristic的UUID
     * @return 查找到的对象，如果没有返回null；
     */
    public static BluetoothGattCharacteristic findGattCharacteristic(
            BluetoothGattService gattService,
            UUID serviceUUID,
            UUID characteristicUUID) {
        if (gattService == null
                || serviceUUID == null
                || characteristicUUID == null)
            return null;

        String cCharacteristicUUID = characteristicUUID.toString();
        List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
        for (BluetoothGattCharacteristic characteristic : gattCharacteristics) {
            UUID cUuid = characteristic.getUuid();
            if (cCharacteristicUUID.equalsIgnoreCase(cUuid.toString())) {
                return characteristic;
            }
        }
        return null;
    }

    /**
     * 写入数据
     */
    public static boolean write(BluetoothGatt bluetoothGatt,
                                BluetoothGattCharacteristic characteristic,
                                String data,
                                boolean enabled) {
        if (data == null || "".equals(data.trim()))
            return false;
        byte[] bytes = hexStringToBytes(data);
        return write(bluetoothGatt, characteristic, bytes, enabled);
    }

    /**
     * 写入数据
     */
    public static boolean write(BluetoothGatt bluetoothGatt,
                                BluetoothGattCharacteristic characteristic,
                                byte[] bytes,
                                boolean enabled) {
        if (bluetoothGatt == null
                || characteristic == null
                || (bytes == null || bytes.length < 1))
            return false;

        characteristic.setValue(bytes);
        bluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        return bluetoothGatt.writeCharacteristic(characteristic);
    }
}
