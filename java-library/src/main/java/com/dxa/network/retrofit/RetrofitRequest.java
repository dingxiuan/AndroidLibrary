package com.dxa.network.retrofit;


import com.dxa.network.okhttp.interceptor.NetworkInterceptor;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 请求基类
 */
public abstract class RetrofitRequest<ServiceApi> implements IRetrofitRequest<ServiceApi> {

    /**
     * 请求接口
     */
    private ServiceApi serviceApi;
    /**
     * 请求基地址
     */
    private final String baseUrl;
    /**
     * 请求的接口 Class 对象
     */
    private final Class<ServiceApi> clazz;

    public RetrofitRequest(String baseUrl, Class<ServiceApi> clazz) {
        this.baseUrl = baseUrl;
        this.clazz = clazz;
    }

    private void requiredInit() {
        Converter.Factory converterFactory = getConverterFactory();
        CallAdapter.Factory adapterFactory = getCallAdapterFactory();
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(adapterFactory)
                .addConverterFactory(converterFactory)
                .baseUrl(getBaseUrl())
                .client(getOkHttpClient())
                .build();
        serviceApi = retrofit.create(clazz);
    }

    /**
     * 获取基地址
     */
    public final String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public ServiceApi getServiceApi() {
        synchronized (this) {
            if (serviceApi == null) {
                requiredInit();
            }
        }
        return serviceApi;
    }

    @Override
    public abstract OkHttpClient getOkHttpClient();

    @Override
    public Interceptor getNetworkInterceptor() {
        return new NetworkInterceptor();
    }

    @Override
    public abstract Converter.Factory getConverterFactory();

    @Override
    public abstract CallAdapter.Factory getCallAdapterFactory();
}
