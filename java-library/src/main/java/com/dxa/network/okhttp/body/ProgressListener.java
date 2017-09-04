package com.dxa.network.okhttp.body;

/**
 * 响应体进度回调接口，比如用于文件下载中
 */
public interface ProgressListener {
    /**
     * 进度更新
     * @param bytesLength 字节的长度
     * @param contentLength 内容的长度
     * @param done 是否完成
     */
    void onUpdateProgress(long bytesLength, long contentLength, boolean done);
}
