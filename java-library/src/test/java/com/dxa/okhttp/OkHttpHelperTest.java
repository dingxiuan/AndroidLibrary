package com.dxa.okhttp;

import com.dxa.network.okhttp.body.ProgressListener;
import com.dxa.network.okhttp.builder.MultipartBodyBuilder;
import com.dxa.network.okhttp.builder.OkHttpClientBuilder;
import com.dxa.network.okhttp.interceptor.NetworkInterceptor;
import com.dxa.utils.print.DPrinter;
import com.dxa.utils.unit.Units;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 测试OkHttp的工具类
 */
public class OkHttpHelperTest implements ProgressListener/*, Interceptor*/ {
    public static void main(String[] args) {
        try {
            new OkHttpHelperTest().uploadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateProgress(long bytesLength, long contentLength, boolean done) {
        String format = String.format(Locale.getDefault(), "总长度: %f \t字节长度: %d ", contentLength * 1.0F / Units.MB, bytesLength);
        DPrinter.printer.i(format + done + "\t是否完成: " + done);
    }

    @Test
    public void uploadFile() throws IOException {
        System.out.println("10485760 ==> " + 10485760.0F / Units.MB);
        MultipartBody multipartBody = new MultipartBodyBuilder()
                .addFiles("file", new File("G:\\健康家-2017090517-v1.3.1.apk"))
                .addParams("version", "6")
                .addParams("versionName", "1.3.1")
                .addParams("description", "修复长时间连接心率减半的问题，自动重连的问题")
                .build();
        Request request = new Request.Builder()
                .url("http://127.0.0.1:8099/heart/appheart/apk")
//                .url("http://www.msznyl.com:8099/heart/appheart/apk")
                .post(multipartBody)
                .build();
        NetworkInterceptor interceptor = new NetworkInterceptor();
        NetworkInterceptor.NetworkListener listener = new NetworkInterceptor.NetworkListener() {
            @Override
            public void onRequest(NetworkInterceptor.RequestInfo requestInfo) {
                System.out.println(requestInfo.toString());
            }
        };
        interceptor.addListener(listener);
        OkHttpClient client = new OkHttpClientBuilder()
                .addResponseProgressListener(this)
                .addNetworkInterceptor(interceptor)
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        ResponseBody body = response.body();
        if (body != null) {
            String result = body.string();
            System.out.println(result);
        } else {
            System.out.println("请求体为 null");
        }
    }

}