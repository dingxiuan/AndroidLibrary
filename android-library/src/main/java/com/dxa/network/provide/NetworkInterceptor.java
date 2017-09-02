package com.dxa.network.provide;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 网络请求的拦截器
 */
public class NetworkInterceptor implements Interceptor {
	private static final class NetworkInterceptorHolder {
		private static volatile NetworkInterceptor INSTANCE;
		
		static {
			INSTANCE = new NetworkInterceptor();
		}
	}
	
	public static NetworkInterceptor provideNetworkInterceptor(){
		return NetworkInterceptorHolder.INSTANCE;
	}

	private Listener listener;
	
	private NetworkInterceptor() {
		// ~
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		if (listener != null) {
			Entity entity = new Entity();
			entity.setMethod(request.method());
			
			HttpUrl url = request.url();
			entity.setUrl(url.toString());
			
			HashMap<String, String> queryMap = new HashMap<>();
			for (String name : url.queryParameterNames()) {
				queryMap.put(name, url.queryParameter(name));
			}
			entity.setQeuries(queryMap);
			
			RequestBody body = request.body();
			entity.setMediaType(body != null ? body.contentType() : null);
			
			Headers headers = request.headers();
			if (headers != null) {
				HashMap<String, String> headerMap = new HashMap<>();
				for (String headerName : headers.names()) {
					headerMap.put(headerName, headers.get(headerName));
				}
				entity.setHeaders(headerMap);
			}
			listener.onRequest(entity);
		}
		return chain.proceed(request);
	}

	/**
	 * 监听
	 */
	public void addListener(Listener listener) {
		this.listener = listener;
	}

	public interface Listener {

		void onRequest(Entity entity);
	}

	public static class Entity {
		
		/**
		 * 请求路径
		 */
		private String url;
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
		private HashMap<String, String> qeuries = new HashMap<>();
		/**
		 * 请求头
		 */
		private HashMap<String, String> headers = new HashMap<>();
		
		public Entity() {
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
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
		
		public HashMap<String, String> getQeuries() {
			return qeuries;
		}

		public void setQeuries(HashMap<String, String> qeuries) {
			this.qeuries = qeuries;
		}

		public HashMap<String, String> getHeaders() {
			return headers;
		}

		public void setHeaders(HashMap<String, String> headers) {
			this.headers = headers;
		}
		
		@Override
		public String toString() {
			return new StringBuffer()
					.append("请求路径").append(": ").append(get(url)).append("\n")
					.append("请求方法").append(": ").append(get(method)).append("\n")
					.append("请求类型").append(": ").append(get(mediaType)).append("\n")
					.append("query").append(": ").append(get(qeuries)).append("\n")
					.append("请求头").append(": ").append(get(headers))
					.toString();
		}
		
		private static String get(Object o){
			return o != null ? o.toString() : "";
		}
	}
	
	
}
