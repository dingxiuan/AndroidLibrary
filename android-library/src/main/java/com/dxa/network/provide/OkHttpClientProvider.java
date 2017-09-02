package com.dxa.network.provide;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * OkHttpClient
 */
class OkHttpClientProvider {

    // lazy instantiate
    private static volatile OkHttpClient mOkHttpClient;

    private OkHttpClientProvider() {
    }

    synchronized static OkHttpClient provideHttpClient(
        Cache cache, Interceptor networkInterceptor) {

        if (mOkHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addNetworkInterceptor(networkInterceptor);
            builder.cache(cache);
            mOkHttpClient = builder.build();
        }
        return mOkHttpClient;
    }

	synchronized static OkHttpClient createOkHttpClient(Cache cache, Interceptor networkInterceptor) {
		return new OkHttpClient.Builder()
				.addNetworkInterceptor(networkInterceptor)
				.readTimeout(60, TimeUnit.SECONDS)
				.connectTimeout(60, TimeUnit.SECONDS)
				.cache(cache)
				.build();
	}
}
