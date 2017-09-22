package com.msznyl.shequ;

import com.msznyl.shequ.bodyentity.Authorization;
import com.msznyl.shequ.entity.AuthToken;
import com.msznyl.shequ.entity.HttpResult;
import com.msznyl.shequ.request.RequestHelper;
import com.msznyl.shequ.request.SheQuApi;
import com.msznyl.shequ.request.SheQuRequest;

import junit.framework.TestCase;

import org.junit.Test;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 测试
 */

public class TestMain extends TestCase {

    @Override
    protected void setUp() throws Exception {
    }

    @Test
    public void testSheQuRequest() {
        SheQuRequest request = new SheQuRequest(SheQuApi.BASE_URL, SheQuApi.class);
        SheQuApi serviceApi = request.getServiceApi();

        Authorization authorization = new Authorization();
        authorization.setAppKey("6926ZK6J1Y77D9BX");
        authorization.setAccount("hndxgh");
        authorization.setPassword("123366");
        authorization.setRequestSequence("");
        authorization.setSignature("1");

        RequestBody body = RequestHelper.createBody(authorization);
        serviceApi.authorize(body)
                .filter(authTokenHttpResult -> authTokenHttpResult.getData() == null)
                .concatMap(new Function<HttpResult<AuthToken>, ObservableSource<AuthToken>>() {
                    @Override
                    public ObservableSource<AuthToken> apply(@NonNull HttpResult<AuthToken> authTokenHttpResult) throws Exception {
                        return Observable.just(authTokenHttpResult.getData());
                    }
                })
                .subscribeOn(Schedulers.trampoline())
                .observeOn(Schedulers.trampoline())
                .subscribe(new DefaultObserver<AuthToken>() {
                    @Override
                    public void onNext(@NonNull AuthToken authToken) {
                        System.out.println(Thread.currentThread().getName() + "认证的Token: " + authToken);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("哦哦，出错了!"+ Thread.currentThread().getName());
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("完成: "+ Thread.currentThread().getName());
                    }
                });
    }

    @Test
    public void testRxJava() {
        Observable.just(1, 2, 3, 4, 10, 20, 30)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return integer > 10;
                    }
                })
                .observeOn(Schedulers.trampoline())
                .subscribeOn(Schedulers.trampoline())
                .subscribe(new DefaultObserver<Integer>() {
                    @Override
                    public void onNext(@NonNull Integer integer) {
                        System.out.println(Thread.currentThread().getName() + "---> onNext: " + integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println(Thread.currentThread().getName() + "---> onComplete ");
                    }
                });
    }

    @Test
    public void testRequest() {
        String postBody = ""
                + "Releases\n"
                + "--------\n"
                + "\n"
                + " * _1.0_ May 6, 2013\n"
                + " * _1.1_ June 15, 2013\n"
                + " * _1.2_ August 11, 2013\n";

        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://open.sq580.com/v1_0/residents/signed")
                .post(RequestBody.create(MEDIA_TYPE_JSON, postBody))
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void tearDown() throws Exception {

    }
}


