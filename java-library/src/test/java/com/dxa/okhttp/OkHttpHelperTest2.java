package com.dxa.okhttp;

import com.dxa.network.okhttp.OkHttpHelper;
import com.dxa.network.okhttp.body.ProgressListener;
import com.dxa.network.okhttp.builder.MultipartBodyBuilder;
import com.dxa.network.okhttp.builder.OkHttpClientBuilder;
import com.dxa.utils.print.DPrinter;

import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 测试OkHttp的工具类
 */
public class OkHttpHelperTest2 implements ProgressListener, Interceptor {
	/**
	 * 缓存大小
	 */
	private static final long CACHE_SIZE = 1024 << 15;

	private static final String JSON_URL = "http://192.168.10.199:8080/heart/appheart/appLogin?bhno=H201701120002&&name=%E5%BC%A0%E6%BA%A2%E8%90%B1";

	private OkHttpHelper okHttpHelper;
	
	@org.junit.Before
	public void setUp() throws Exception {
		File file = new File("D:/Temp/Cache");
		OkHttpClient client = new OkHttpClientBuilder()
				.addResponseProgressListener(this)
				.addNetworkInterceptor(this)
				.setCache(file, CACHE_SIZE)
				.build();
		okHttpHelper = new OkHttpHelper(client);
		
		client = new OkHttpClientBuilder()
			.addNetworkInterceptor(this)
			.build();
	}

	@org.junit.After
	public void tearDown() throws Exception {
		print("tearDown()...");
	}

	@Override
	public void onUpdateProgress(long bytesLength, long contentLength, boolean done) {
		String format = String.format(Locale.getDefault(), "总长度: %d \t字节长度: %d ", contentLength, bytesLength);
		DPrinter.printer.i(format + done + "\t是否完成: " + done);
	}
	

	@Test
	public void uploadFile() throws IOException {
		File file = new File("F:/健康家-v1.3.3.apk");
		HashMap<String, File> map = new HashMap<>();
		map.put(file.getName(), file);

		MultipartBody multipartBody = new MultipartBodyBuilder()
				.addFiles("file", file)
				.addParams("version", "8")
				.addParams("versionName", "1.3.3")
				.addParams("description", "修复了几个影响您使用的小bug；")
				.build();
		Request request = new Request.Builder()
				.url("http://www.msznyl.com:8099/heart/appheart/apk")
//				.url("http://218.29.120.238:8099/heart/appheart/apk")
				.post(multipartBody)
				.build();
		Response response = new OkHttpHelper().execute(request);
		String result = response.body().string();
		System.out.println(result);
	}

	@Test
	public void downloadApp() {
		Request request = new Request.Builder().get().url("http://192.168.10.199:8080//heart/appheart/upDate").build();

		BufferedOutputStream bos = null;
		try {
			Response response = okHttpHelper.execute(request);
			byte[] bytes = response.body().bytes();
			bos = new BufferedOutputStream(new FileOutputStream(new File("D:\\Temp\\ddd.apk")));
			bos.write(bytes, 0, bytes.length);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Test
	public void downloadImage() {
		Request request = new Request.Builder().get().url("http://seopic.699pic.com/photo/00012/1648.jpg_wh1200.jpg")
				.build();

		BufferedOutputStream bos = null;
		try {
			Response response = okHttpHelper.execute(request);
			byte[] bytes = response.body().bytes();
			bos = new BufferedOutputStream(new FileOutputStream(new File("D:\\Temp\\jpg_wh1200.jpg")));
			bos.write(bytes, 0, bytes.length);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 测试注册
	 */
	@Test
	public void register() {
		MultipartBody body = new MultipartBodyBuilder()
				.addParams("mobile", "18317720854")
                .addParams("gender",  "1")
                .addParams("username", "小丁")
				.build();
		Request request = new Request.Builder()
				.url("http://192.168.10.151/zn/index.php/Admin/User/register.html")
				.post(body)
				.build();
		System.out.println(request.toString());
		try {
			Response response = OkHttpHelper.getOkHttpHelper().execute(request);
			System.out.println(response.code());
			System.out.println(response.message());
			System.out.println(response.body().string());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	@Test
	public void testUploadAvatar() {
		OkHttpHelper helper = OkHttpHelper.getOkHttpHelper();
		File image = new File("F:/FaceQ1429776821737.jpg");
		MultipartBody body = new MultipartBodyBuilder()
			.addFiles("image", image)
			.addParams("bhno", "S201703240010")
			.build();
		Request request = new Request.Builder()
				.post(body)
				.url("http://192.168.10.151/zn/index.php/Admin/User/uploadicon.html")
				.build();
		
		System.out.println("0000000000000000");
		try {
			Response response = helper.execute(request);
			System.out.println("请求码: "+ response.code() +"\n是否成功: "+ response.isSuccessful());
			System.out.println(response.body().string());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void print(String msg) {
		System.out.println(msg);
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		System.out.println("请求路径: "+ request.url().toString());
		return chain.proceed(request);
	}

}