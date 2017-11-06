package com.msznyl.shequ.request;

import com.dxa.network.okhttp.interceptor.NetworkInterceptor;
import com.dxa.network.rxjava2.RxRetrofitRequest;
import com.dxa.tool.unit.Units;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 社区580的请求
 */

public class SheQuRequest extends RxRetrofitRequest<SheQuApi> {

    public SheQuRequest(String baseUrl, Class<SheQuApi> clazz) {
        super(baseUrl, clazz);
    }

    private final Object lock = new Object();
    private volatile OkHttpClient client;

    @Override
    public OkHttpClient getOkHttpClient() {
        if (client == null) {
            synchronized (lock) {
                if (client == null) {
                    NetworkInterceptor networkInterceptor = getNetworkInterceptor();
                    networkInterceptor.addListener(networkListener);
                    client = new OkHttpClient.Builder()
                            .addNetworkInterceptor(networkInterceptor)
                            .cache(new Cache(new File("E:/OkHttp/cache"), 16 * Units.MB))
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .readTimeout(60, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(false)
                            .build();
                }
            }
        }
        return client;
    }

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public NetworkInterceptor getNetworkInterceptor() {
        return new NetworkInterceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                // 添加请求头
                Request.Builder builder = request.newBuilder();
                builder.addHeader("Content-Type", "application/json; charset=utf-8");
                if (token != null && token.length() > 0) {
                    builder.addHeader("Token", token);
                } else {
                    System.out.println("token为空?");
                }
                return chain.proceed(request);
            }
        };
    }

    @Override
    public Converter.Factory getConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Override
    public CallAdapter.Factory getCallAdapterFactory() {
        return RxJava2CallAdapterFactory.createAsync();
    }


    private final NetworkInterceptor.NetworkListener networkListener = System.out::println;
}
