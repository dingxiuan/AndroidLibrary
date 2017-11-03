package com.dxa.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.dxa.android.logger.DLogger;
import com.dxa.android.logger.LogLevel;

/**
 * Service的超类
 */

public abstract class SuperService<T extends ServiceDelegate> extends Service {

    protected final DLogger logger = new DLogger(getClass().getSimpleName(), LogLevel.NONE);
    protected T delegate;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        delegate = createDelegate();
        delegate = (delegate == null) ? getDefaultDelegate() : delegate;
        delegate.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        delegate.onStartCommand(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        delegate.onDestroy();
        super.onDestroy();
    }

    /**
     * 创建ServiceDelegate对象
     */
    protected abstract T createDelegate();

    /**
     * 获得Delegate对象
     */
    public ServiceDelegate getDelegate() {
        return delegate;
    }

    @SuppressWarnings("unchecked")
    public T getDefaultDelegate() {
        return (T) new ServiceDelegate(this){};
    }

}
