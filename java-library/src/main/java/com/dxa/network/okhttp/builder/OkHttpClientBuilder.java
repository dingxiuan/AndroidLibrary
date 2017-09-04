package com.dxa.network.okhttp.builder;


import com.dxa.network.okhttp.body.ProgressHelper;
import com.dxa.network.okhttp.body.ProgressListener;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

public class OkHttpClientBuilder {

    private OkHttpClient.Builder builder;

    public OkHttpClientBuilder() {
        builder = new OkHttpClient.Builder();
    }

    public OkHttpClientBuilder(OkHttpClient.Builder builder) {
        if (builder != null){
            this.builder = builder;
        } else {
            this.builder = new OkHttpClient.Builder();
        }
    }

    /**
     * 设置缓存
     */
    public OkHttpClientBuilder setCache(File cacheDirectory, long cacheSize) {
        Cache cache = new Cache(cacheDirectory, cacheSize);
        builder.cache(cache);
        return this;
    }

    /**
     * 连接超时时间
     */
    public OkHttpClientBuilder setConnectTimeout(
        long connectTimeout, TimeUnit unit) {
        builder.connectTimeout(connectTimeout, unit);
        return this;
    }

    /**
     * 读取超时时间
     */
    public OkHttpClientBuilder setReadTimeout(long readTimeout, TimeUnit unit) {
        builder.readTimeout(readTimeout, unit);
        return this;
    }

    /**
     * 添加拦截器
     */
    public OkHttpClientBuilder addInterceptor(Interceptor interceptor) {
        builder.addInterceptor(interceptor);
        return this;
    }

    /**
     * 添加网络拦截器
     */
    public OkHttpClientBuilder addNetworkInterceptor(Interceptor interceptor) {
        builder.addNetworkInterceptor(interceptor);
        return this;
    }

    /**
     * 添加进度更新的拦截器
     */
    public OkHttpClientBuilder addResponseProgressListener(ProgressListener progressListener) {
        Interceptor interceptor = ProgressHelper.getResponseInterceptor(progressListener);
        builder.addNetworkInterceptor(interceptor);
        return this;
    }

    /**
     * 是否重连
     */
    public OkHttpClientBuilder retryOnConnectionFailure(boolean retryOnConnectionFailure) {
        builder.retryOnConnectionFailure(retryOnConnectionFailure);
        return this;
    }

    /**
     * 构建OkHttpClient
     */
    public OkHttpClient build() {
        return builder.build();
    }

    /**
     * 获取OkHttpClient.Builder对象
     */
    public static OkHttpClient.Builder obtainBuilder(){
        return OkHttpClientHolder.client.newBuilder();
    }

    /**
     * 持有默认的OkHttpClient对象
     */
    private static final class OkHttpClientHolder {
        // 连接超时
        private static final long CONNECT_TIME_OUT = 60;
        // 读取超时
        private static final long READ_TIME_OUT = 60;
        // 默认时间单位
        private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
        // 失败后重新尝试
        private static final boolean RETRY = true;
        // 默认的OkHttpClient对象
        private static volatile OkHttpClient client;

        static {
            client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT, TIME_UNIT)
                .readTimeout(READ_TIME_OUT, TIME_UNIT)
                .retryOnConnectionFailure(RETRY)
                .build();
        }
    }
}
