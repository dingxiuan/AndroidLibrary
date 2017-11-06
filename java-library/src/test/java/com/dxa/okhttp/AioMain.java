//package com.dxa.okhttp;
//
//import com.dxa.network.okhttp.OkHttpHelper;
//import com.dxa.tool.file.FileUtils;
//
//import java.io.File;
//import java.io.IOException;
//
//import okhttp3.MediaType;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//import okhttp3.ResponseBody;
//
//public class AioMain {
//
//    public static void main(String[] args) throws IOException {
//        String URL = "http://127.0.0.1/heart/aio-machine/json";
//
//        File file = new File("E:/test/test.json");
//        String json = FileUtils.readString(file);
//
//        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
//
//        Request request = new Request.Builder()
//                .url(URL)
//                .post(body)
//                .build();
//        Response response = new OkHttpHelper().execute(request);
//        System.out.println("状态码: " + response.code());
//
//        ResponseBody responseBody = response.body();
//        if (responseBody != null) {
//            String s = responseBody.string();
//            System.out.println(s);
//        } else {
//            System.out.println("请求体为null");
//        }
//
//    }
//
//
//}
