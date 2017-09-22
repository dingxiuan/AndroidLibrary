package com.msznyl.shequ.request;

import com.msznyl.shequ.entity.AuthToken;
import com.msznyl.shequ.entity.HttpResult;
import com.msznyl.shequ.entity.Signed;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 社区580的接口
 */

public interface SheQuApi {

    String BASE_URL = "http://open.sq580.com/admin/v1_0/";
    //需要添加头
//    @Headers({"Content-Type: application/json", "Accept: application/json"})

    /**
     * 身份验证
     */
    @POST("residents/authorize")
    Observable<HttpResult<AuthToken>> authorize(@Body RequestBody body);

    /**
     * 签约数据查询
     */
    @POST("residents/signed")
    Observable<HttpResult<List<Signed>>> signed(@Body RequestBody body);

//    /**
//     * 通过居民证件查询签约数据
//     */
//    @POST("residents/signed_idsearch")
//    Observable<HttpResult<>> searchSignedId(@Body RequestBody body);


}
