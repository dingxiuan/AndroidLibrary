package com.dxa.network.okhttp.body;

import okhttp3.Interceptor;
import okhttp3.RequestBody;

/**
 * 进度回调辅助类
 */
public class ProgressHelper {

    private ProgressHelper() {
    }

    /**
     * 包装OkHttpClient，用于下载文件的回调
     *
     * @param progressListener 进度回调接口
     * @return 包装后的OkHttpClient，使用clone方法返回
     */
    public static Interceptor getResponseInterceptor(ProgressListener progressListener) {
        return new ProgressInterceptor(progressListener);
    }

    /**
     * 包装请求体用于上传文件的回调
     *
     * @param requestBody      请求体RequestBody
     * @param progressListener 进度回调接口
     * @return 包装后的进度回调请求体
     */
    public static ProgressRequestBody getRequestBody(RequestBody requestBody,
                                                     ProgressListener progressListener) {
        //包装请求体
        return new ProgressRequestBody(requestBody, progressListener);
    }
}
