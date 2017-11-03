package com.dxa.tool.file;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;


/**
 * 文件操作的工具类
 */

public final class FileUtils {

    private FileUtils() {
        throw new IllegalAccessError();
    }

    /**
     * 获取系统编码
     */
    public static String getDefaultEncode() {
        return Charset.defaultCharset().toString();
    }

    /**
     * 写入数据
     *
     * @param file  文件
     * @param bytes 字节数组
     * @throws IOException IO异常
     */
    public static void writeBytes(File file, byte[] bytes) throws IOException {
        write(file, bytes);
    }

    /**
     * 写入数据
     *
     * @param filePath 文件
     * @return 返回读取的字节数组
     * @throws IOException IO异常
     */
    public static byte[] readBytes(String filePath) throws IOException {
        File file = new File(filePath);
        return readBytes(file);
    }

    /**
     * 写入数据
     *
     * @param file 文件
     * @return 返回读取的字节数组
     * @throws IOException IO异常
     */
    public static byte[] readBytes(File file) throws IOException {
        return read(file).toByteArray();
    }

    /**
     * 往文件中写入字符串数据
     *
     * @param file 文件
     * @param data 写入的字符串
     */
    public static void writeString(File file, String data) throws IOException {
        write(file, data.getBytes());
    }

    /**
     * 读取字符串
     *
     * @param filePath 文件
     * @return 返回读取的字符串
     * @throws IOException IO异常
     */
    public static String readString(String filePath) throws IOException {
        File file = new File(filePath);
        return readString(file, getDefaultEncode());
    }

    /**
     * 读取字符串
     *
     * @param filePath 文件
     * @return 返回读取的字符串
     * @throws IOException IO异常
     */
    public static String readString(String filePath, String encode) throws IOException {
        File file = new File(filePath);
        return readString(file, encode);
    }

    /**
     * 读取字符串
     *
     * @param file 文件
     * @return 返回读取的字符串
     * @throws IOException IO异常
     */
    public static String readString(File file, String encode) throws IOException {
        return read(file).toString(encode);
    }

    /**
     * 读取字符串
     *
     * @param file 文件
     * @return 返回读取的字符串
     * @throws IOException IO异常
     */
    public static String readString(File file) throws IOException {
        String encode = getDefaultEncode();
        return readString(file, encode);
    }

    /**
     * 读取数据
     *
     * @param file 文件
     * @return 返回保存数据的ByteArrayOutputStream
     * @throws IOException IO异常
     */
    public static ByteArrayOutputStream read(File file) throws IOException {
        checkExist(file);
        DataInputStream dis = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            dis = new DataInputStream(new FileInputStream(file));
            byte[] buff = new byte[1024];
            int length;
            while ((length = dis.read(buff)) > 0) {
                baos.write(buff, 0, length);
            }
            return baos;
        } finally {
            close(dis);
        }
    }

    /**
     * 写入数据
     *
     * @param file 文件
     * @param data 数据
     * @throws IOException IO异常
     */
    private static void write(File file, byte[] data) throws IOException {
        DataOutputStream dos = null;
        checkFileIfNotExistCreate(file);
        try {
            dos = new DataOutputStream(new FileOutputStream(file));
            dos.write(data);
        } finally {
            close(dos);
        }
    }

    /**
     * 检查文件，如果不是文件，就创建
     *
     * @param file 文件对象
     * @return 返回是否存在
     */

    public static boolean checkFileIfNotExistCreate(File file) throws IOException {
        if (file == null)
            throw new IOException("File对象为 null ！");

        boolean exist = file.exists();
        if (!exist) {
            // 检查目录
            File parentFile = file.getParentFile();
            checkDirsIfNotExistCreate(parentFile);
            // 创建文件
            exist = file.createNewFile();
        } else if (file.isDirectory()) {
            throw new IOException("不是文件!");
        }
        return exist;
    }

    /**
     * 检查目录，如果目录不存在就创建
     *
     * @param file 文件
     * @return
     */
    public static boolean checkDirsIfNotExistCreate(File file) throws IOException {
        if (file == null)
            throw new IOException("File对象为 null ！");

        if (file.exists() && file.isFile()) {
            throw new IOException("不是目录!");
        }
        return file.mkdirs();
    }

    private static void checkExist(File file) throws IOException {
        if (file == null || !file.exists())
            throw new IOException("文件不存在");
    }

    /**
     * 关闭流
     */
    public static void close(Closeable... cs) {
        if (cs == null || cs.length == 0)
            return;

        for (Closeable c : cs) {
            if (c != null){
                try {
                    c.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static boolean notEmpty(String s) {
        return !isEmpty(s);
    }

    /**
     * 读取或写入的回调
     */
    public interface Callback<Type> {
        /**
         * 处理数据
         */
        void onHandleData(Type type);
    }
}
