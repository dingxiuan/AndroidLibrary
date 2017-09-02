package com.dxa.network.okhttp.body;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 更新进度的拦截器
 */
class ProgressInterceptor implements Interceptor {

    private ProgressListener progressListener;

    public ProgressInterceptor(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        //拦截
        Request request = chain.request();
        Response response = chain.proceed(request);
        //包装响应体并返回
        ResponseBody responseBody = response.body();
        ProgressResponseBody progressResponseBody = new ProgressResponseBody(responseBody, progressListener);
        return response.newBuilder()
            .body(progressResponseBody)
            .build();
    }
}
