package com.dxa.common.encrypt;


import com.dxa.common.hex.HexTool;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Locale;

import static com.dxa.common.hex.HexTool.binToHex;

public class SecretHelper {
    private static final String _HEX = "0123456789ABCDEF";
    private static final char[] DIGITAL = _HEX.toCharArray();

    private SecretHelper() {
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    /**
     * 16进制转换成二进制
     *
     * @param source 数据源
     * @return 返回二进制字节数组
     */
    public static byte[] hexToBin(String source) {
        source = source.toUpperCase(Locale.getDefault());
        int length = source.length() / 2;
        char[] hexChars = source.toCharArray();
        byte[] bytes = new byte[length];

        for (int i = 0; i < length; ++i) {
            int pos = i * 2;
            char high = hexChars[pos];
            char low = hexChars[pos + 1];
            bytes[i] = (byte) (charToByte(high) << 4 | charToByte(low));
        }

        return bytes;
    }

    /**
     * 获取BASE64编码对象
     */
    public static Base64.Encoder getBase64Encoder() {
        return Base64.getEncoder();
    }

    /**
     * 对数据进行BASE64编码
     *
     * @param source 数据源
     * @return 返回BASE64的编码数据
     */
    public static String base64Encode(String source) {
        byte[] bytes = source.getBytes();
        return getBase64Encoder().encodeToString(bytes);
    }

    /**
     * 对数据进行BASE64编码
     *
     * @param source 数据源
     * @return 返回BASE64的编码数据
     */
    public static String base64Encode(byte[] source) {
        return getBase64Encoder().encodeToString(source);
    }

    /**
     * 对数据进行BASE64编码
     *
     * @param source 数据源
     * @param encode 指定的编码
     * @return 返回BASE64的编码数据
     * @throws UnsupportedEncodingException 不支持的编码异常
     */
    public static String base64Encode(String source, String encode) throws UnsupportedEncodingException {
        byte[] bytes = source.getBytes(encode);
        return getBase64Encoder().encodeToString(bytes);
    }

    /**
     * 获取Base64解码对象
     */
    public static Base64.Decoder getBase64Decoder() {
        return Base64.getDecoder();
    }

    /**
     * BASE64解码
     *
     * @param source 数据源
     * @return 返回解码后的数据
     * @throws IOException IO异常
     */
    public static byte[] base64Decode(String source) throws IOException {
        return getBase64Decoder().decode(source);
    }

    /**
     * BASE64解码
     *
     * @param source 数据源
     * @param encode 编码格式
     * @return 返回解码后的数据
     * @throws IOException IO异常
     */
    public static String base64Decode(String source, String encode) throws IOException {
        byte[] bytes = base64Decode(source);
        return new String(bytes, encode);
    }

    /**
     * 对数据进行加密
     *
     * @param source    加密源
     * @param algorithm 算法名
     * @return 返回加密后的字节数组
     */
    public static byte[] getMessageDigest(byte[] source, String algorithm) {
        if (source != null && source.length > 0) {
            try {
                MessageDigest digest = MessageDigest.getInstance(algorithm);
                return digest.digest(source);
            } catch (NoSuchAlgorithmException var3) {
                var3.printStackTrace();
            }
        }
        return new byte[0];
    }

    /**
     * 对数据进行加密
     *
     * @param source    加密源
     * @param algorithm 算法名
     * @return 返回加密后的字节数组
     */
    public static byte[] getMessageDigest(byte[] source, MdAlgorithm algorithm) {
        return getMessageDigest(source, algorithm.getAlgorithm());
    }

    /**
     * 对数据进行MD5加密
     *
     * @param source 加密源
     * @return 返回加密后的字节数组
     */
    public static byte[] md5(byte[] source) {
        return getMessageDigest(source, MdAlgorithm.MD5.getAlgorithm());
    }

    /**
     * 对数据进行MD5加密
     *
     * @param source 加密源
     * @return 返回加密后的字节数组
     */
    public static byte[] md5(String source) {
        byte[] bytes = source.getBytes();
        return md5(bytes);
    }

    /**
     * 对数据进行MD5加密
     *
     * @param source 加密源
     * @param encode 指定编码
     * @return 返回加密后的字节数组
     */
    public static byte[] md5(String source, String encode) throws UnsupportedEncodingException {
        byte[] bytes = source.getBytes(encode);
        return md5(bytes);
    }

    /**
     * 将MD5转换成16进制
     *
     * @param source MD5数据源
     * @return 返回转换后的16进制数据
     */
    public static String md5ToHex(String source) {
        byte[] bytes = source.getBytes();
        byte[] md5 = md5(bytes);
        return HexTool.binToHex(md5);
    }

    /**
     * 将MD5转换成16进制
     *
     * @param source MD5数据源
     * @param encode 指定编码
     * @return 返回转换后的16进制数据
     */
    public static String md5ToHex(String source, String encode) throws UnsupportedEncodingException {
        byte[] bytes = source.getBytes(encode);
        byte[] md5 = md5(bytes);
        return HexTool.binToHex(md5);
    }

    /**
     * 将MD5转换成BASE64编码
     *
     * @param source MD5数据源
     * @return 返回转换后的BASE64数据
     */
    public static String md5ToBase64(String source) {
        byte[] bytes = source.getBytes();
        byte[] md5 = md5(bytes);
        return base64Encode(md5);
    }

    /**
     * 将MD5转换成BASE64编码
     *
     * @param source MD5数据源
     * @param encode 指定编码
     * @return 返回转换后的BASE64数据
     * @throws UnsupportedEncodingException 不支持的编码异常
     */
    public static String md5ToBase64(String source, String encode) throws UnsupportedEncodingException {
        byte[] bytes = source.getBytes(encode);
        byte[] md5 = md5(bytes);
        return base64Encode(md5);
    }


}
