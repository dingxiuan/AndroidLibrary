package com.msznyl.shequ.request;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * RequestBody的工具类
 */

public class RequestHelper {

    /**
     * 创建RequestBody
     *
     * @param t   转换成Json的对象
     * @param <T> 类型
     * @return 返回RequestBody
     */
    public static <T> RequestBody createBody(T t) {
        String json = convertToJson(t);
        return createBody(json);
    }

    /**
     * 创建RequestBody
     *
     * @param json json数据
     * @return 返回RequestBody
     */
    public static RequestBody createBody(String json) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
    }

    /**
     * 转换成Json
     *
     * @param t   对象
     * @param <T> 类型
     * @return 返回Json字符串
     */
    public static <T> String convertToJson(T t) {
        return getGson().toJson(t);
    }

    /**
     * 获取Gson
     */
    public static Gson getGson() {
        return GsonHolder.INSTANCE;
    }

    private static class GsonHolder {
        private static volatile Gson INSTANCE;

        static {
            INSTANCE = new Gson();
        }
    }
}
