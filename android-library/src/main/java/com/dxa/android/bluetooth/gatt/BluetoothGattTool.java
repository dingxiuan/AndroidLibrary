package com.dxa.android.bluetooth.gatt;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * 蓝牙4.0的工具类
 */
public class BluetoothGattTool {

    private static final String UNKNOWN = "UNKNOWN";
    private static final String EMPTY = "";
    private static final String NULL = null;

    /********************************************************/
    /**
     * 16进制和2进制转换
     */

    private static final String HEX_UPPER_CASE = "0123456789ABCDEF";
    private static final String HEX_LOWER_CASE = "0123456789abcdef";

    private static final String[] _BINARY = {
            "0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111",
            "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"
    };

    /**
     * 二进制转换成二进制字符串
     *
     * @param bin 二进制字节数组
     * @return 返回二进制字符串
     */
    public static String binToBin(byte[] bin) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bin) {
            //高四位
            builder.append(_BINARY[(b & 0xF0) >> 4]);
            //低四位
            builder.append(_BINARY[b & 0x0F]);
        }
        return builder.toString();
    }

    /**
     * 二进制转换成16进制字符串
     *
     * @param bin 二进制字节数组
     * @return 返回16进制字符串或空
     */
    public static String binToHex(byte[] bin) {
        return binToHex(bin, false);
    }

    /**
     * 二进制转换成16进制字符串
     *
     * @param bin       二进制字节数组
     * @param lowerCase 是否为小写字母
     * @return 返回16进制字符串或空
     */
    public static String binToHex(byte[] bin, boolean lowerCase) {
        if (isEmpty(bin)) {
            return NULL;
        }

        String hex = lowerCase ? HEX_LOWER_CASE : HEX_UPPER_CASE;
        StringBuilder builder = new StringBuilder();
        for (byte b : bin) {
            //字节高4位
            builder.append(hex.charAt((b & 0xF0) >> 4));
            //字节低4位
            builder.append(hex.charAt(b & 0x0F));
        }
        return builder.toString();
    }

    /**
     * 16进制字符串转换成字节数组
     *
     * @param hex 字符串
     * @return 转换的字节数组
     */
    public static byte[] hexToBin(String hex) {
        return hexToBin(hex, null);
    }

    /**
     * 16进制字符串转换成字节数组
     *
     * @param hex          字符串
     * @param defaultValue 默认值
     * @return 转换的字节数组
     */
    public static byte[] hexToBin(String hex, byte[] defaultValue) {
        if (notEmpty(hex)) {
            int length = hex.length() / 2;
            char[] ch = hex.toUpperCase().toCharArray();
            byte[] bin = new byte[length];

            char high, low;
            for (int i = 0; i < length; ++i) {
                high = ch[i * 2];
                low = ch[i * 2 + 1];
                bin[i] = (byte) (charToByte(high) << 4 | charToByte(low));
            }
            return bin;
        }
        return defaultValue;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /******************************************************************/

    @SuppressLint("UseSparseArrays")
    public enum Gatt {
        /**
         * GATT 服务
         */
        SERVICE {
            private HashMap<Integer, String> serviceTypes = new HashMap<>();

            {
                serviceTypes.put(BluetoothGattService.SERVICE_TYPE_PRIMARY, "PRIMARY");
                serviceTypes.put(BluetoothGattService.SERVICE_TYPE_SECONDARY, "SECONDARY");
            }

            public String getServiceType(int type) {
                return serviceTypes.get(type);
            }
        },

        /**
         * GattCharacteristic的属性和权限
         */
        CHARACTERISTIC {
            private HashMap<Integer, String> permissions = new HashMap<>();
            private HashMap<Integer, String> properties = new HashMap<>();

            {
                properties.put(BluetoothGattCharacteristic.PROPERTY_BROADCAST, "BROADCAST");
                properties.put(BluetoothGattCharacteristic.PROPERTY_EXTENDED_PROPS, "EXTENDED_PROPS");
                properties.put(BluetoothGattCharacteristic.PROPERTY_INDICATE, "INDICATE");
                properties.put(BluetoothGattCharacteristic.PROPERTY_NOTIFY, "NOTIFY");
                properties.put(BluetoothGattCharacteristic.PROPERTY_READ, "READ");
                properties.put(BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE, "SIGNED_WRITE");
                properties.put(BluetoothGattCharacteristic.PROPERTY_WRITE, "WRITE");
                properties.put(BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE, "WRITE_NO_RESPONSE");

                permissions.put(0, UNKNOWN);
                permissions.put(BluetoothGattCharacteristic.PERMISSION_READ, "READ");
                permissions.put(BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED, "READ_ENCRYPTED");
                permissions.put(BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED_MITM, "READ_ENCRYPTED_MITM");
                permissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE, "WRITE");
                permissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED, "WRITE_ENCRYPTED");
                permissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED_MITM, "WRITE_ENCRYPTED_MITM");
                permissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE_SIGNED, "WRITE_SIGNED");
                permissions.put(BluetoothGattCharacteristic.PERMISSION_WRITE_SIGNED_MITM, "WRITE_SIGNED_MITM");
            }

            public String getProperty(int property) {
                return getHashMapValue(properties, property);
            }

            /**
             * 获取权限
             */
            public String getPermission(int permission) {
                return getHashMapValue(permissions, permission);
            }

        },

        /**
         * GattDescriptor的权限
         */
        DESCRIPTOR {
            private HashMap<Integer, String> permissions = new HashMap<>();
            private HashMap<byte[], String> valueTypes = new HashMap<>();

            {
                permissions.put(0, UNKNOWN);
                permissions.put(BluetoothGattDescriptor.PERMISSION_READ, "READ");
                permissions.put(BluetoothGattDescriptor.PERMISSION_READ_ENCRYPTED, "READ_ENCRYPTED");
                permissions.put(BluetoothGattDescriptor.PERMISSION_READ_ENCRYPTED_MITM, "READ_ENCRYPTED_MITM");
                permissions.put(BluetoothGattDescriptor.PERMISSION_WRITE, "WRITE");
                permissions.put(BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED, "WRITE_ENCRYPTED");
                permissions.put(BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED_MITM, "WRITE_ENCRYPTED_MITM");
                permissions.put(BluetoothGattDescriptor.PERMISSION_WRITE_SIGNED, "WRITE_SIGNED");
                permissions.put(BluetoothGattDescriptor.PERMISSION_WRITE_SIGNED_MITM, "WRITE_SIGNED_MITM");

                valueTypes.put(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE, "ENABLE_NOTIFICATION_VALUE");
                valueTypes.put(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE, "DISABLE_NOTIFICATION_VALUE");
                valueTypes.put(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE, "ENABLE_INDICATION_VALUE");
            }

            /**
             * 获取权限
             * @param permission 权限
             * @return 权限
             */
            public String getPermission(int permission) {
                return getHashMapValue(permissions, permission);
            }

            /**
             * 获取值的类型
             */
            public String getValueType(byte[] value) {
                String type = valueTypes.get(value);
                if (isEmpty(type) && notEmpty(value)) {
                    byte[] temp = {value[0], value[1]};
                    type = valueTypes.get(temp);
                }
                return isEmpty(type) ? UNKNOWN : type;
            }
        };

        private static String getHashMapValue(HashMap<Integer, String> hashMap, int number) {
            String result = hashMap.get(number);
            if (isEmpty(result)) {
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
        private static List<Integer> getElement(int number) {
            List<Integer> result = new ArrayList<>();
            for (int i = 0; i < 32; i++) {
                int b = 1 << i;
                if ((number & b) > 0)
                    result.add(b);
            }
            return result;
        }
    }

    /*****************************************************************************/

    /**
     * 获取UUID
     */
    public static UUID getUUID(String uuid) {
        return UUID.fromString(uuid);
    }

    /**
     * 获取BluetoothGattService集合
     */
    public static List<BluetoothGattService> getGattServices(BluetoothGatt gatt) {
        if (gatt == null)
            return new ArrayList<>();
        return gatt.getServices();
    }

    /**
     * 获取对应UUID的BluetoothGattService
     */
    public static BluetoothGattService getGattService(BluetoothGatt gatt, UUID serviceUUID) {
        return gatt != null ? gatt.getService(serviceUUID) : null;
    }

    /**
     * 获取对应UUID的BluetoothGattService
     */
    public static BluetoothGattService getGattService(List<BluetoothGattService> gattServices, UUID serviceUUID) {
        if (isEmpty(gattServices) || serviceUUID == null)
            return null;

        for (BluetoothGattService service : gattServices) {
            UUID sUUID = service.getUuid();
            if (sUUID.equals(serviceUUID)) {
                return service;
            }
        }
        return null;
    }

    /**
     * 获取对应UUID的BluetoothGattCharacteristic
     */
    public static BluetoothGattCharacteristic getGattCharacteristic(BluetoothGattService service, UUID characteristicUUID) {
        if (!nonNull(service, characteristicUUID))
            return null;
        return service.getCharacteristic(characteristicUUID);
    }

    /**
     * 获取对应UUID的BluetoothGattCharacteristic
     */
    public static BluetoothGattCharacteristic getGattCharacteristic(BluetoothGatt gatt, UUID serviceUUID, UUID characteristicUUID) {
        BluetoothGattService gattService = getGattService(gatt, serviceUUID);
        return getGattCharacteristic(gattService, characteristicUUID);
    }

    /**
     * 获取对应UUID的BluetoothGattDescriptor
     */
    public static BluetoothGattDescriptor getGattDescriptor(BluetoothGattService gattService, UUID characteristicUUID, UUID descriptorUUID) {
        BluetoothGattCharacteristic characteristic = getGattCharacteristic(gattService, characteristicUUID);
        return getGattDescriptor(characteristic, descriptorUUID);
    }

    /**
     * 获取对应UUID的BluetoothGattDescriptor
     */
    public static BluetoothGattDescriptor getGattDescriptor(BluetoothGatt gatt, UUID serviceUUID, UUID characteristicUUID, UUID descriptorUUID) {
        BluetoothGattCharacteristic characteristic = getGattCharacteristic(gatt, serviceUUID, characteristicUUID);
        return getGattDescriptor(characteristic, descriptorUUID);
    }

    /**
     * 获取对应UUID的BluetoothGattDescriptor
     */
    public static BluetoothGattDescriptor getGattDescriptor(BluetoothGattCharacteristic characteristic, UUID descriptorUUID) {
        return characteristic != null ? characteristic.getDescriptor(descriptorUUID) : null;
    }

    /**
     * 设置特征值改变时的提醒
     */
    public static boolean setCharacteristicNotification(BluetoothGatt gatt,
                                                        BluetoothGattCharacteristic characteristic,
                                                        boolean enable) {
        return nonNull(gatt, characteristic)
                && gatt.setCharacteristicNotification(characteristic, enable);
    }

    /**
     * 设置特征值改变时的提醒
     */
    public static boolean setCharacteristicNotification(
            BluetoothGatt gatt, UUID serviceUUID, UUID characteristicUUID, boolean enable) {
        BluetoothGattCharacteristic characteristic =
                getGattCharacteristic(gatt, serviceUUID, characteristicUUID);
        return setCharacteristicNotification(gatt, characteristic, enable);
    }

    /**
     * 设置描述符的值
     * {@link BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE}
     * {@link BluetoothGattDescriptor.ENABLE_INDICATION_VALUE}
     * {@link BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE}
     */
    public static boolean setDescriptorValue(BluetoothGatt gatt,
                                             BluetoothGattDescriptor descriptor,
                                             byte[] value) {
        return nonNull(gatt, descriptor) && descriptor.setValue(value);
    }

    public static boolean notification(
            BluetoothGatt gatt, UUID serviceUUID, UUID characteristicUUID) {
        BluetoothGattService service = gatt.getService(serviceUUID);
        if (service != null) {
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUUID);
            if (characteristic != null) {
                gatt.setCharacteristicNotification(characteristic, true);
                // 适配部分机型
                gatt.readCharacteristic(characteristic);
                for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                    BluetoothGattTool.writeDescriptorValue(
                            gatt, descriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 是否写入描述符的值
     */
    public static boolean writeDescriptorValue(BluetoothGatt gatt,
                                               BluetoothGattDescriptor descriptor,
                                               byte[] value) {
        return setDescriptorValue(gatt, descriptor, value)
                && gatt.writeDescriptor(descriptor);
    }

    /**
     * 是否读取描述符的值
     */
    public static boolean readDescriptorValue(BluetoothGatt gatt,
                                              BluetoothGattDescriptor descriptor,
                                              byte[] value) {
        return setDescriptorValue(gatt, descriptor, value)
                && gatt.readDescriptor(descriptor);
    }

    /**
     * 写入特征值
     */
    public static boolean writeCharacteristic(BluetoothGatt gatt,
                                              BluetoothGattCharacteristic characteristic,
                                              String value) {
        byte[] bin = hexToBin(value);
        return writeCharacteristic(gatt, characteristic, bin);
    }

    /**
     * 写入特征值
     */
    public static boolean writeCharacteristic(BluetoothGatt gatt,
                                              BluetoothGattCharacteristic characteristic,
                                              byte[] value) {
        return characteristic != null
                && characteristic.setValue(value)
                && gatt.writeCharacteristic(characteristic);
    }


    /**
     * 打印BluetoothGattService的信息
     */
    public static void printGattInfo(List<BluetoothGattService> getServices, String tag) {
        StringBuilder builder = new StringBuilder();
        int sIndex = 0;
        for (BluetoothGattService service : getServices) {
            sIndex++;
            Log.e(tag, "\n-------- start -------- " + sIndex + " ------------------------");
            builder.append("Service uuid: ").append(service.getUuid().toString());
            builder.append("; type: ").append(service.getType());
            Log.i(tag, builder.toString());
            builder.setLength(0);

            int cIndex = 0;
            Log.w(tag, "==>: characteristic ----> START");
            for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                cIndex++;
                builder.append(cIndex).append("、characteristic ");
                builder.append("uuid: ").append(characteristic.getUuid());
                builder.append("; permissions: ").append(characteristic.getPermissions());
                builder.append("; properties: ").append(characteristic.getProperties());
                builder.append("; writeType: ").append(characteristic.getWriteType());
                builder.append("; value: ")
                        .append(BluetoothGattTool.binToHex(characteristic.getValue()));
                Log.i(tag, builder.toString());
                builder.setLength(0);

                Log.i(tag, cIndex + " ==>: descriptor ----> START");
                int dIndex = 0;
                for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                    dIndex++;
                    builder.append(cIndex).append(".").append(dIndex).append("、descriptor ");
                    builder.append("uuid: ").append(descriptor.getUuid());
                    builder.append("; permissions: ").append(descriptor.getPermissions());
                    builder.append("; value: ")
                            .append(BluetoothGattTool.binToHex(characteristic.getValue()));
                    Log.d(tag, builder.toString());
                    builder.setLength(0);
                }
                Log.i(tag, "==>: descriptor ----> END");
            }
            Log.w(tag, "==>: characteristic ----> END");
            Log.e(tag, "\n-------- end -------- " + sIndex + " ------------------------");
        }
        builder.setLength(0);
    }


    /*****************************************************************************/


    private static boolean notEmpty(byte[] s) {
        return s != null && s.length > 0;
    }

    private static boolean nonNull(Object... objects) {
        for (Object o : objects) {
            if (o == null)
                return false;
        }
        return true;
    }

    private static boolean notEmpty(String s) {
        return s != null && s.trim().length() > 0;
    }

    private static boolean isEmpty(byte[] bytes) {
        return bytes == null || bytes.length <= 0;
    }

    private static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }
}
