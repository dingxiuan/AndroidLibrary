package com.dxa.android.preference;

import android.content.Context;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 操作Preferences的接口声明
 * 
 * @author: DINGXIUAN
 */
public interface IPreference {

	IPreferenceHolder prefHolder = new IPreferenceHolder();
	/**
	 * 默认的比较器
	 */
	Comparator<String> comparator = new ComparatorImpl();

	/**
	 * IPreference的持有类
	 */
	class IPreferenceHolder {
		/**
		 * 获得一个新创建的IPreference对象
		 * 
		 * @param context
		 *            上下文对象
		 * @param fileName
		 *            文件名
		 */
		public IPreference newPreference(Context context, String fileName) {
			return new PreferenceImpl(context, fileName);
		}

		/**
		 * 获取一个IPreference对象
		 * 
		 * @param context
		 *            上下文对象
		 */
		public IPreference getPreference(Context context) {
			return PreferenceImpl.getPreference(context);
		}

		/**
		 * 获取一个IPreference对象
		 * 
		 * @param context
		 *            上下文对象
		 * @param fileName
		 *            文件名
		 */
		public IPreference getPreference(Context context, String fileName) {
			return PreferenceImpl.getPreference(context, fileName);
		}
	}

	/**
	 * 保存一个数据
	 */
	<T> void put(String key, T value);

	/**
	 * 保存一个Map集合
	 */
	<T> void putAll(Map<String, T> map);

	/**
	 * 保存一个List集合
	 */
	void putAll(String key, List<String> list);

	/**
	 * 保存一个List集合，并且自定保存顺序
	 */
	void putAll(String key, List<String> list, Comparator<String> comparator);

	/**
	 * 根据key取出一个数据
	 */
	<T> T get(String key, DataType type);
	
	/**
	 * 取出全部数据
	 */
	Map<String, ?> getAll();

	/**
	 * 取出一个List集合
	 */
	List<String> getAll(String key);

	/**
	 * 移除一个数据
	 */
	void remove(String key);

	/**
	 * 移除一个集合的数据
	 */
	void removeAll(List<String> keys);

	/**
	 * 移除一个集合的数据
	 */
	void removeAll(String[] keys);

	/**
	 * 获取String类型的数据
	 */
	String getString(String key);

	/**
	 * 获取String类型的数据
	 */
	String getString(String key, String defaultValue);

	/**
	 * 获取Float类型的数据
	 */
	float getFloat(String key);

	/**
	 * 获取Float类型的数据
	 */
	float getFloat(String key, float defaultValue);

	/**
	 * 获取int类型的数据
	 */
	int getInteger(String key);

	/**
	 * 获取int类型的数据
	 */
	int getInteger(String key, int defaultValue);

	/**
	 * 获取long类型的数据
	 */
	long getLong(String key);

	/**
	 * 获取long类型的数据
	 */
	long getLong(String key, long defaultValue);

	/**
	 * 获取Set类型的数据
	 */
	Set<String> getStringSet(String key);

	/**
	 * 获取Set类型的数据
	 */
	Set<String> getStringSet(String key, Set<String> defaultValue);

	/**
	 * 获取boolean类型的数据
	 */
	boolean getBoolean(String key);

	/**
	 * 获取boolean类型的数据
	 */
	boolean getBoolean(String key, boolean defaultValue);
	
	/**
	 * 是否存在key
	 */
	boolean contains(String key);

	/**
	 * 清除全部数据
	 */
	void clear();
	
	/**
	 * 枚举：存储或取出的数据类型
	 */
	enum DataType {
		INTEGER, LONG, BOOLEAN, FLOAT, STRING, STRING_SET, NULL
	}
}
