package com.dxa.network.okhttp.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 网络请求的拦截器
 */
public class NetworkInterceptor implements Interceptor {

    private NetworkListener networkListener;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (networkListener != null) {
            Request copyRequest = request.newBuilder().build();
            RequestInfo info = new RequestInfo(copyRequest);

            info.setTag(request.tag());
            info.setHttps(request.isHttps());
            info.setMethod(request.method());

            HttpUrl url = request.url();
            info.setHttpUrl(url.toString());

            HashMap<String, String> queryMap = new HashMap<>();
            for (String name : url.queryParameterNames()) {
                queryMap.put(name, url.queryParameter(name));
            }
            info.setQueries(queryMap);

            RequestBody body = request.body();
            info.setMediaType(body != null ? body.contentType() : null);

            if (body != null && body instanceof MultipartBody) {
                MultipartBody multipartBody = (MultipartBody) body;
                List<MultipartBody.Part> parts = multipartBody.parts();
                for (MultipartBody.Part part : parts) {

                }
            }

            Headers headers = request.headers();
            if (headers != null) {
                HashMap<String, String> headerMap = new HashMap<>();
                for (String headerName : headers.names()) {
                    headerMap.put(headerName, headers.get(headerName));
                }
                info.setHeaders(headerMap);
            }

            networkListener.onRequest(info);
        }
        return chain.proceed(request);
    }

    /**
     * 监听
     */
    public void addListener(NetworkListener networkListener) {
        this.networkListener = networkListener;
    }

    /**
     * 网络监听
     */
    public interface NetworkListener {

        void onRequest(RequestInfo requestInfo);
    }

    /**
     * 网络请求信息
     */
    public static class RequestInfo {
        /**
         * 请求副本
         */
        private Request request;
        /**
         * 设置标签
         */
        private Object tag;
        /**
         * 是否为Https
         */
        private boolean https;
        /**
         * 请求路径
         */
        private String httpUrl;
        /**
         * 请求方法
         */
        private String method;
        /**
         * 端口
         */
        private int port;
        /**
         * 类型
         */
        private MediaType mediaType;
        /**
         * query
         */
        private HashMap<String, String> queries = new HashMap<>();
        /**
         * 请求头
         */
        private HashMap<String, String> headers = new HashMap<>();


        public RequestInfo(Request request) {
            this.request = request;
        }

        public Request getRequest() {
            return request;
        }

        public Object getTag() {
            return tag;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }

        public boolean isHttps() {
            return https;
        }

        public void setHttps(boolean https) {
            this.https = https;
        }

        public String getHttpUrl() {
            return httpUrl;
        }

        public void setHttpUrl(String httpUrl) {
            this.httpUrl = httpUrl;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public MediaType getMediaType() {
            return mediaType;
        }

        public void setMediaType(MediaType mediaType) {
            this.mediaType = mediaType;
        }

        public HashMap<String, String> getQueries() {
            return queries;
        }

        public void setQueries(HashMap<String, String> queries) {
            this.queries = queries;
        }

        public HashMap<String, String> getHeaders() {
            return headers;
        }

        public void setHeaders(HashMap<String, String> headers) {
            this.headers = headers;
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("path: ").append(get(httpUrl))
                    .append("\ntag: ").append(get(tag))
                    .append("\nhttps: ").append(get(https))
                    .append("\nmethod: ").append(get(method))
                    .append("\nmedia-type: ").append(get(mediaType))
                    .append("\nqueries: ").append(get(queries))
                    .append("\nheaders: ").append(get(headers))
                    .toString();
        }

        private static <T> String get(T o) {
            return o != null ? String.valueOf(o) : "";
        }


    }


}
