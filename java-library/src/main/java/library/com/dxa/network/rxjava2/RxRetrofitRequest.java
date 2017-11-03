package com.dxa.network.rxjava2;

import com.dxa.network.retrofit.RetrofitRequest;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * RxJava
 */

public abstract class RxRetrofitRequest<ServiceApi> extends RetrofitRequest<ServiceApi> {

    /**
     * RxAndroid的默认调度器，主线程中调用
     */
    private Scheduler scheduler = Schedulers.trampoline();

    public RxRetrofitRequest(String baseUrl, Class<ServiceApi> clazz) {
        super(baseUrl, clazz);
    }


    /**
     * 默认调度器
     */
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * 获取调度器
     */
    public Scheduler getScheduler() {
        return scheduler != null ? scheduler : Schedulers.trampoline();
    }

    @Override
    public Converter.Factory getConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Override
    public CallAdapter.Factory getCallAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }


    /**********************************************************************/

    /**
     * 发送请求
     *
     * @param observable Observable对象
     * @param <T>        类型
     * @return 返回结果
     */
    public <T> Observable<T> observable(Observable<T> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(getScheduler());
    }

    /**
     * 发送请求
     *
     * @param observable Observable对象
     * @param scheduler  调度器
     * @param <T>        类型
     * @return 返回结果
     */
    public <T> Observable<T> observable(Observable<T> observable, Scheduler scheduler) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(scheduler);
    }


}
