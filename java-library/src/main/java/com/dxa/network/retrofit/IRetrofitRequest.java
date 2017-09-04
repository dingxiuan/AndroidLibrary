package com.dxa.network.retrofit;


import io.reactivex.annotations.Nullable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;

/**
 * 请求基类
 */
public interface IRetrofitRequest<ServiceApi> {

    /**
     * 获取创建的ServiceApi
     */
    @Nullable
    ServiceApi getServiceApi();

    /**
     * 创建 OkHttpClient 对象
     */
    @Nullable
    OkHttpClient getOkHttpClient();

    /**
     * 创建网络请求的拦截器
     */
    @Nullable
    Interceptor getNetworkInterceptor();

    /**
     * 创建Converter.Factory的实现类对象，如 {@link GsonConverterFactory#create()}
     */
    Converter.Factory getConverterFactory();

    /**
     * 创建 CallAdapter.Factory 的实现类对象，如 {@link RxJavaCallAdapterFactory#create()}
     */
    @Nullable
    CallAdapter.Factory getCallAdapterFactory();

}
