package com.dxa.benefit.file;


import com.dxa.benefit.Checker;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 操作拷贝或剪切
 */
public class FileCopy {

    /**
     * 拷贝
     *
     * @param oldFile    原文件
     * @param newFile    新文件
     * @param bufferSize 缓存的大小
     * @return
     */
    public static boolean copy(File oldFile, File newFile, int bufferSize) {
        return operate(OperateType.COPY, oldFile, newFile, bufferSize);
    }

    /**
     * 剪切
     *
     * @param oldFile    原文件
     * @param newFile    新文件
     * @param bufferSize 缓存的大小
     * @return
     */
    public static boolean cut(File oldFile, File newFile, int bufferSize) {
        return operate(OperateType.CUT, oldFile, newFile, bufferSize);
    }

    /**
     * 剪切文件
     *
     * @param oldFile    原文件
     * @param newFile    新文件
     * @param bufferSize 缓存的大小
     * @return
     */
    private static boolean operate(OperateType type,
                                   File oldFile, File newFile, int bufferSize) {
        if (Checker.hasNull(oldFile, newFile)) {
            return false;
        }

        if (Checker.exist(oldFile) && oldFile.isFile()
                && Checker.isFile(newFile)) {
            return (type == OperateType.CUT) ?
                    cutFile(oldFile, newFile, bufferSize) :
                    copyFile(oldFile, newFile, bufferSize);
        } else if (Checker.isDirectory(oldFile)
                && Checker.isDirectory(newFile)) {
            String oldDirectoryName = oldFile.getAbsolutePath();
            String newDirectoryName = newFile.getAbsolutePath();
            StringBuffer buffer = new StringBuffer();
            for (File f : oldFile.listFiles()) {
                buffer.setLength(0);
                buffer.append(f.getAbsolutePath());
                int end = oldDirectoryName.length();
                buffer.replace(0, end, newDirectoryName);
                File tempFile = new File(buffer.toString());
                if (f.isFile()) {
                    boolean b = operate(type, f, tempFile, bufferSize);
                    if (!b) {
                        return false;
                    }
                } else if (f.isDirectory()) {
                    tempFile.mkdirs();
                    boolean result = operate(type, f, tempFile, bufferSize);
                    if (!result) {
                        return false;
                    }
                }
            }
            return type == OperateType.CUT && oldFile.delete();
        }
        return false;
    }

    /**
     * 拷贝
     *
     * @param bufferSize 缓存大小
     * @param fromFile   原文件
     * @param toFile     目标文件
     * @return
     */
    private static boolean copyFile(File fromFile, File toFile, int bufferSize) {
        if (Checker.exist(fromFile)
                && Checker.isFile(fromFile)) {
            FileInputStream fis = null;
            FileOutputStream fos = null;
            try {
                fis = new FileInputStream(fromFile);
                fos = new FileOutputStream(toFile);
                byte[] buffer = new byte[bufferSize];
                int temp;
                while ((temp = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, temp);
                }
                fos.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close(fis, fos);
            }
        }
        return false;
    }

    /**
     * 剪切
     *
     * @param bufferSize 缓存大小
     * @param fromFile   原文件
     * @param toFile     目标文件
     * @return 是否剪切成功
     */
    public static boolean cutFile(File fromFile, File toFile, int bufferSize) {
        boolean result = copyFile(fromFile, toFile, bufferSize);
        return result && fromFile.delete();
    }

    /**
     * 关闭流
     */
    public static void close(Closeable close) {
        if (close != null) {
            try {
                close.close();
            } catch (IOException e) {/* ignore */}
        }
    }

    /**
     * 关闭流
     */
    public static void close(Closeable... closes) {
        if (closes != null && closes.length > 0) {
            for (Closeable c : closes) {
                close(c);
            }
        }
    }

    /**
     * 文件操作的类型，拷贝或剪切
     */
    public enum OperateType {
        COPY, // 拷贝
        CUT // 剪切
    }


}
