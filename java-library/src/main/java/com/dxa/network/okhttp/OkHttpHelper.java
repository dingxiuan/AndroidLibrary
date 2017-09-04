package com.dxa.network.okhttp;


import com.dxa.network.okhttp.builder.OkHttpClientBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * OkHttp的工具类
 */
public class OkHttpHelper implements Interceptor {
    public static OkHttpHelper getOkHttpHelper(){
        return OkHttpHelperHolder.HELPER;
    }

    private static final class OkHttpHelperHolder{
        private static final OkHttpHelper HELPER;

        static {
            HELPER = new OkHttpHelper();
        }
    }

    private OkHttpClient client;

    public OkHttpHelper() {
        client = OkHttpClientBuilder.obtainBuilder()
        		.addNetworkInterceptor(this)
        		.build();
    }

    public OkHttpHelper(OkHttpClient client) {
        if (client != null){
            this.client = client;
        } else {
            throw new NullPointerException("OkHttpClient's instance is null !");
        }
    }

    public OkHttpHelper(long connectTimeout, long readTimeout, TimeUnit unit) {
        client = new OkHttpClientBuilder()
            .setConnectTimeout(connectTimeout, unit)
            .setReadTimeout(readTimeout, unit)
            .build();
    }

    public OkHttpClient getClient() {
        return client;
    }

    public Response execute(Request request) throws IOException {
        return client.newCall(request).execute();
    }

    public void enqueue(Request request, Callback responseCallback) {
        client.newCall(request).enqueue(responseCallback);
    }

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		System.out.println("请求路径: "+ request.url().toString());
		return chain.proceed(request);
	}
    
}
