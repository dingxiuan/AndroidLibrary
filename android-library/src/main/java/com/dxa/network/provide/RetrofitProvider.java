package com.dxa.network.provide;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 获取Retrofit对象
 */
public class RetrofitProvider {

    private RetrofitProvider() {
    }
    
    public static Retrofit provideRetrofit(
			String baseUrl, OkHttpClient okHttpClient) {
		return new Retrofit.Builder()
				.baseUrl(baseUrl)
				.client(okHttpClient)
				// .addConverterFactory(jsonConverterFactory)
				// .addCallAdapterFactory(callAdapterFactory)
				.build();
	}

    /**
     * 获取Retrofit对象
     * @param baseUrl 基地址
     * @param okHttpClient OkHttpClient的客户端
     * @param jsonConverterFactory Json对象转换工厂
     * @param callAdapterFactory 
     * @return
     */
    public static Retrofit provideRetrofit(
			String baseUrl, OkHttpClient okHttpClient, 
			Converter.Factory jsonConverterFactory,
			CallAdapter.Factory callAdapterFactory) {
    	
        return new Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(jsonConverterFactory)
            .addCallAdapterFactory(callAdapterFactory)
            .build();
    }


}
