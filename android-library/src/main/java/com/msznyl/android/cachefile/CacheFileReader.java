package com.msznyl.android.cachefile;

import com.dxa.android.timer.TimerTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 读取文件
 */

public class CacheFileReader extends TimerTask implements CacheFile {

    private final Object lock = new Object();
    /**
     * 读取的文件
     */
    private final File readFile;
    /**
     * 读取数据
     */
    private BufferedReader reader;
    /**
     * 行号
     */
    private int lineNum = 1;
    /**
     * 被取消
     */
    private boolean canceled = false;

    private Callback callback;

    public CacheFileReader(File readFile) throws IOException {
        this.readFile = readFile;
        FileReader fileReader = new FileReader(readFile);
        reader = new BufferedReader(fileReader);
    }

    public CacheFileReader(File readFile, Callback callback) throws IOException {
        this(readFile);
        this.callback = callback;
    }

    public File getReadFile() {
        return readFile;
    }

    public int getLineNum() {
        return lineNum;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        if (canceled) {
            return;
        }

        if (callback != null) {
            String line = null;
            try {

            	if(reader != null) {
            		line = reader.readLine();
            	}
                
                if (line != null) {
                    if (lineNum == 1) {
                        callback.onReadFirstLine(line);
                    } else {
                        callback.onReadLine(lineNum, line);
                    }
                    lineNum++;
                } else {
                    callback.onCompleted(lineNum);
                    cancel();
                }
                
            } catch (IOException e) {
            	e.printStackTrace();
                callback.onError(e);
                cancel();
                return;
            }
        }
    }

    public void close() {
        synchronized (lock) {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean cancel() {
        if (!canceled) {
            canceled = super.cancel();
            close();
        }
        return canceled;
    }

    public interface Callback {

        /**
         * 读取第一行数据
         *
         * @param firstLine 第一行数据
         */
        void onReadFirstLine(String firstLine);

        /**
         * 读取行
         *
         * @param lineNum 行号
         * @param line    一行数据
         */
        void onReadLine(int lineNum, String line);

//        /**
//         * 读取行
//         *
//         * @param lineNum 行号
//         * @param line    一行数据
//         * @param type    类型
//         * @param data    数据
//         * @param time    时间
//         */
//        void onReadLine(int lineNum, String line, int type, int data, long time);

        /**
         * 当读取完成
         *
         * @param lineNum 行号
         */
        void onCompleted(int lineNum);

        /**
         * 当出现错误时
         */
        <E extends Exception> void onError(E e);
    }
}
