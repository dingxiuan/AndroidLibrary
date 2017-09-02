package com.dxa.android.cache;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * 文件的工具类
 */
class FileUtils {
	/**
	 * 获取InputStream对象
	 * @param file 文件
	 * @return 返回InputStream对象
	 * @throws FileNotFoundException 找不到文件
	 */
	public static InputStream getInputStream(File file) 
			throws FileNotFoundException {
		required(file);
		return new FileInputStream(file);
	}
	
	/**
	 * 获取InputStreamReader对象
	 * @param file 文件
	 * @return 返回InputStreamReader对象
	 * @throws FileNotFoundException 找不到文件
	 */
	public static InputStreamReader getInputStreamReader(File file) 
			throws FileNotFoundException {
		InputStream stream = getInputStream(file);
		return new InputStreamReader(stream);
	}
	
	/**
	 * 获取BufferedReader对象
	 * @param file 文件
	 * @return 返回BufferedReader对象
	 * @throws FileNotFoundException 找不到文件
	 */
	public static BufferedReader getBufferedReader(File file) 
			throws FileNotFoundException {
		InputStreamReader reader = getInputStreamReader(file);
		return new BufferedReader(reader);
	}
	
	/**
	 * 获取FileOutputStream对象
	 * @param file 文件
	 * @return 返回FileOutputStream对象
	 * @throws FileNotFoundException 找不到文件
	 */
	public static FileInputStream getFileInputStream(File file) 
			throws FileNotFoundException {
		return new FileInputStream(file);
	}
	
	/*****************************************************/
	/**
	 * 输出流
	 */
	
	/**
	 * 获取OutputStream对象
	 * @param file 文件
	 * @return 返回OutputStream对象
	 * @throws FileNotFoundException 找不到文件
	 */
	public static OutputStream getOutputStream(File file)
			throws FileNotFoundException {
		required(file);
		return new FileOutputStream(file);
	}
	
	/**
	 * 获取OutputStreamWriter对象
	 * @param file 文件
	 * @return 返回OutputStreamWriter对象
	 * @throws FileNotFoundException 找不到文件
	 */
	public static OutputStreamWriter getOutputStreamWriter(File file)
			throws FileNotFoundException {
		OutputStream stream = getOutputStream(file);
		return new OutputStreamWriter(stream);
	}

	/**
	 * 获取BufferedWriter对象
	 * @param file 文件
	 * @return 返回BufferedWriter对象
	 * @throws FileNotFoundException 找不到文件
	 */
	public static BufferedWriter getBufferedWriter(File file) 
			throws FileNotFoundException {
		OutputStreamWriter writer = getOutputStreamWriter(file);
		return new BufferedWriter(writer);
	}

	/**
	 * 获取FileOutputStream对象
	 * @param file 文件
	 * @return 返回FileOutputStream对象
	 * @throws FileNotFoundException 找不到文件
	 */
	public static FileOutputStream getFileOutputStream(File file) 
			throws FileNotFoundException {
		return new FileOutputStream(file);
	}

	/***************************************************************/

	public static void required(File file) {
		if (!Checker.exist(file))
			throw new IllegalAccessError("文件不存在!");
	}
	
	/**
	 * 关闭流
	 */
	public static void close(Closeable os){
		if (Checker.notNull(os)) {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
