package com.dxa.network.provide;

import java.io.File;

import okhttp3.Cache;

class CacheProvider {

    // lazy instantiate
    private static volatile Cache mCache;
    // 4MB
    public static final long CACHE_SIZE_4MB = 1024 << 12;
    // 8MB
    public static final long CACHE_SIZE_8MB = 1024 << 13;
    // 16MB
    public static final long CACHE_SIZE_16MB = 1024 << 14;

    private CacheProvider() {
    }

    synchronized static Cache provideCache(File cacheDir, long size) {
        if (mCache == null){
            mCache = new Cache(cacheDir, size);
        }
        return mCache;
    }

    synchronized static Cache provideCache(File cacheDir) {
        if (mCache == null){
            mCache = new Cache(cacheDir, CACHE_SIZE_16MB);
        }
        return mCache;
    }
}
