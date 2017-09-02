package com.dxa.network.provide;

import java.io.File;

import com.google.gson.Gson;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 提供基本的需要依赖
 */

public class ProviderFactory {

	// 16MB
	public static final long CACHE_SIZE = 1024 << 14;

	/**
	 * 获取单例的Gson对象
	 */
	public static Gson getGson() {
		return GsonProvider.provideGson();
	}

	/**
	 * 获取单例的Cache对象
	 */
	public static Cache getCache(File cacheDir) {
		return CacheProvider.provideCache(cacheDir);
	}

	/**
	 * 获取单例的Cache对象
	 */
	public static Cache getCache(File cacheDir, long size) {
		return CacheProvider.provideCache(cacheDir, size);
	}

	/**
	 * 获取单例的OkHttpClient对象
	 */
	public static OkHttpClient getOkHttpClient(Cache cache, Interceptor networkInterceptor) {
		return OkHttpClientProvider.provideHttpClient(cache, networkInterceptor);
	}

	/**
	 * 获取单例的OkHttpClient对象
	 */
	public static OkHttpClient getOkHttpClient(File cacheDir, Interceptor networkInterceptor) {
		Cache cache = getCache(cacheDir);
		return OkHttpClientProvider.provideHttpClient(cache, networkInterceptor);
	}
	
	/**
	 * 获取单例的OkHttpClient对象
	 */
	public static OkHttpClient newOkHttpClient(File cacheDir, Interceptor networkInterceptor) {
		Cache cache = new Cache(cacheDir, CACHE_SIZE);
		return OkHttpClientProvider.createOkHttpClient(cache, networkInterceptor);
	}

	/**
	 * 创建NetworkInterceptor对象
	 */
	public static Interceptor getNetworkInterceptor() {
		return NetworkInterceptor.provideNetworkInterceptor();
	}

	/**
	 * 获取GsonConverterFactory对象
	 */
	public static GsonConverterFactory getGsonConverterFactory() {
		return GsonConverterFactory.create();
	}

	/**
	 * 获取GsonConverterFactory对象
	 */
	public static GsonConverterFactory getGsonConverterFactory(Gson gson) {
		return GsonConverterFactory.create(gson);
	}

	/**
	 * 获取RxJavaCallAdapterFactory对象
	 */
	public static RxJavaCallAdapterFactory getRxJavaCallConverterFactory() {
		return RxJavaCallAdapterFactory.create();
	}

	/**
	 * 获取Retrofit对象
	 * 
	 * @param baseUrl
	 *            基地址
	 * @param cacheDir
	 *            缓存目录
	 * @return
	 */
	public static Retrofit getRetrofit(String baseUrl, File cacheDir) {
		Cache cache = getCache(cacheDir);
		Interceptor networkInterceptor = getNetworkInterceptor();
		OkHttpClient okHttpClient = getOkHttpClient(cache, networkInterceptor);
		Gson gson = getGson();
		GsonConverterFactory gsonConverterFactory = getGsonConverterFactory(gson);
		RxJavaCallAdapterFactory rxJavaCallConverterFactory = getRxJavaCallConverterFactory();
		return RetrofitProvider.provideRetrofit(baseUrl, okHttpClient, gsonConverterFactory,
				rxJavaCallConverterFactory);
	}

	/**
	 * 获取Retrofit对象
	 * 
	 * @param baseUrl
	 *            基地址
	 * @param okHttpClient
	 *            OkHttpClient对象
	 * @return
	 */
	public static Retrofit getRetrofit(String baseUrl, OkHttpClient okHttpClient) {
		Gson gson = getGson();
		GsonConverterFactory gsonConverterFactory = getGsonConverterFactory(gson);
		RxJavaCallAdapterFactory rxJavaCallConverterFactory = getRxJavaCallConverterFactory();
		return RetrofitProvider.provideRetrofit(baseUrl, okHttpClient, gsonConverterFactory,
				rxJavaCallConverterFactory);
	}

	/**
	 * 获取Retrofit对象
	 * 
	 * @param baseUrl
	 *            基地址
	 * @param okHttpClient
	 *            OkHttpClient对象
	 * @param jsonConverterFactory
	 *            Converter.Factory对象
	 * @param callAdapterFactory
	 *            CallAdapter.Factory对象
	 * @return
	 */
	public static Retrofit getRetrofit(String baseUrl, OkHttpClient okHttpClient,
			Converter.Factory jsonConverterFactory, CallAdapter.Factory callAdapterFactory) {
		return RetrofitProvider.provideRetrofit(baseUrl, okHttpClient, jsonConverterFactory, callAdapterFactory);
	}

}
