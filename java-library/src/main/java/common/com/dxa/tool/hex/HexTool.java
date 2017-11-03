package com.dxa.tool.hex;

/**
 * 16进制转换
 */
public class HexTool {

    private static final String NULL = null;

    /**
     * 转换成整数
     *
     * @param b 字节
     * @return 返回一个整数
     */
    public static int binToInt(byte b) {
        return (b & 0xFF) * 256 + (b & 0xFF);
    }

    /**
     * 取低字节
     */
    public static int binToIntLow(byte b) {
        return (b & 0xFF);
    }

    /**
     * 取高字节
     */
    public static int binToIntHigh(byte b) {
        return (b & 0xFF) * 256;
    }


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

    private static boolean notEmpty(String s) {
        return s != null && s.trim().length() > 0;
    }

    private static boolean isEmpty(byte[] bytes) {
        return bytes == null || bytes.length <= 0;
    }

}
