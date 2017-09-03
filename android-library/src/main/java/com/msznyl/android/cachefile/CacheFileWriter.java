package com.msznyl.android.cachefile;

import com.dxa.android.timer.TimerTask;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 缓存文件的任务
 */

public class CacheFileWriter extends TimerTask implements CacheFile {
    /**
     * This ID is used to generate thread names.
     */
    private final static AtomicInteger nextSerialNumber = new AtomicInteger(0);

    private static int serialNumber() {
        return nextSerialNumber.getAndIncrement();
    }

    private final Object lock = new Object();

    private Callback callback;

    /**
     * 当前的文件
     */
    private File currentFile;
    /**
     * 是否取消
     */
    private boolean canceled;

    private BufferedWriter bufferedWriter;

    public CacheFileWriter() {
        super("FileCacheTask-" + serialNumber());
    }

    public CacheFileWriter(Callback callback) {
        this();
        this.callback = callback;
    }

    /**
     * 设置回调
     */
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        if (callback != null && !canceled) {
            try {
                File oldFile;
                synchronized (lock) {
                    oldFile = currentFile;
                    currentFile = callback.createNewFile();
                    close();
                    FileWriter fileWriter = new FileWriter(currentFile);
                    bufferedWriter = new BufferedWriter(fileWriter);

                    // 创建BufferedWriter后可能会需要写入数据
                    callback.onCreateWriter(currentFile, bufferedWriter);
                }

                // 防止初次运行 File 为 null
                if (oldFile != null) {
                    callback.onHandleFile(oldFile, false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(String line) {
        if (bufferedWriter != null && !canceled) {
            try {
                bufferedWriter.write(line);
                bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean cancel() {
        synchronized (lock) {
            if (!canceled) {
                canceled = super.cancel();
                close();
                if (callback != null) {
                    callback.onHandleFile(currentFile, true);
                }
            }
        }
        return canceled;
    }

    private void close() {
        if (bufferedWriter != null) {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bufferedWriter = null;
        }
    }

    /**
     * 缓存文件的回调
     */

    public interface Callback {

        /**
         * 创建新的文件
         */
        File createNewFile();

        /**
         * 当创建Writer对象后
         */
        void onCreateWriter(File newFile, Writer writer) throws IOException;

        /**
         * 处理文件
         *
         * @param file       文件
         * @param isCanceled 是否被取消
         */
        void onHandleFile(File file, boolean isCanceled);

    }
}
