package com.dxa.tool.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * 操作对象流的工具类，可以写入和读取已经实现序列化的对象
 */
public class ObjectStreamHelper<T extends Serializable> {
	
	/**
	 * 往文件中写入对象
	 */
	public synchronized boolean write(File file, T t) throws IOException {
		boolean isSuccessful = false;
		ObjectOutputStream oos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(t);
			isSuccessful = true;
		} finally {
			close(fos);
			close(oos);
		}
		return isSuccessful;
	}
	
	/**
	 * 往文件中写入对象
	 */
	public synchronized boolean write(String fileName, T t) {
		try {
			File file = new File(fileName);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			return write(file, t);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 读取文件中的对象
	 */
	@SuppressWarnings("unchecked")
	public synchronized T read(File file) throws IOException, ClassNotFoundException {
		FileInputStream fis;
		ObjectInputStream ois = null;
		T t = null;
		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			t = (T) ois.readObject();
		} finally {
			close(ois);
		}
		return t;
	}
	
	public synchronized  T read(String fileName) {
		try {
			return read(new File(fileName));
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void close(OutputStream stream) {
		if (isNotNull(stream))
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	private void close(InputStream stream) {
		if (isNotNull(stream))
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	private boolean isNotNull(Object o) {
		return o != null;
	}

}
