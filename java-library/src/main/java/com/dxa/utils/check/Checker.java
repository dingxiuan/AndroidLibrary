package com.dxa.utils.check;

import java.io.File;
import java.util.Collection;
import java.util.Map;

/**
 * 检查
 */
public class Checker {
	private Checker() {
		// ~
	}

	/**
	 * 为null
	 */
	public static boolean isNull(Object o){
		return o == null;
	}
	
	/**
	 * 有null值
	 */
	public static boolean haveNull(Object... os){
		if (isNull(os)) {
			return true;
		}
		
		for (Object o : os) {
			if (isNull(o))
				return true;
		}
		return false;
	}
	
	/**
	 * 不为null
	 */
	public static boolean notNull(Object o){
		return o != null;
	}
	
	/**
	 * 无null值
	 */
	public static boolean nonNull(Object... os){
		if (isNull(os)) {
			return false;
		}
		
		for (Object o : os) {
			if (isNull(o))
				return false;
		}
		return true;
	}
	
	
	/**
	 * 不为空
	 */
	public static boolean notEmpty(String s){
		return notNull(s) && s.length() > 0;
	}
	
	/**
	 * 非空
	 */
	public static boolean nonEmpty(String... ss) {
		if (isNull(ss)) {
			return false;
		}
		
		for (String s : ss) {
			if (isEmpty(s)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 非空
	 */
	public static boolean nonEmptyWithTrim(String... ss) {
		if (isNull(ss)) {
			return false;
		}
		
		for (String s : ss) {
			if (isEmptyWithTrim(s))
				return false;
		}
		return true;
	}
	
	/**
	 * 不为空
	 */
	public static boolean notEmptyWithTrim(String s){
		return notNull(s) && s.trim().length() > 0;
	}
	
	/**
	 * 为空
	 */
	public static boolean isEmpty(String s){
		return isNull(s) || s.isEmpty();
	}
	
	/**
	 * 为空
	 */
	public static boolean isEmptyWithTrim(String s){
		return isNull(s) || s.trim().isEmpty();
	}
	
	/**
	 * 有空值
	 */
	public static boolean haveEmpty(String... ts) {
		if (ts == null)
			return true;

		for (String o : ts) {
			if (o == null || "".equals(o.trim()))
				return true;
		}
		return false;
	}
	
	
	/**
	 * 相等
	 */
	public static boolean isEquals(String s1, String s2) {
		return nonEmpty(s1, s2) && s1.equals(s2);
	}
	
	/**
	 * 相等
	 */
	public static boolean isEquals(Object o1, Object o2) {
		return nonNull(o1, o2) && o1 == o2;
	}
	
	/**
	 * 相等
	 */
	public static boolean isEqualsWithTrim(String s1, String s2) {
		return nonNull(s1, s2) && s1.trim().equals(s2.trim()); 
	}
	
	
	/**
	 * 包含
	 */
	public static <E> boolean contains(Collection<E> c, E e) {
		return nonNull(c, e) && c.contains(e);
	}
	
	/**
	 * 包含Value
	 */
	public static <K, V> boolean containsValue(Map<K, V> map, V v) {
		return nonNull(map, v) && map.containsValue(v);
	}
	
	/**
	 * 包含Key
	 */
	public static <K, V> boolean containsKey(Map<K, V> map, K k) {
		return nonNull(map, k) && map.containsKey(k);
	}
	
	/**
	 * 存在
	 */
	public static boolean exist(File file){
		return notNull(file) && file.exists();
	}
	
	/**
	 * 不存在
	 */
	public static boolean notExist(File file){
		return isNull(file) || !file.exists();
	}

	/*
	 * 是否为目录
	 * 当参数不为null，且不存在或为目录时才返回true，
	 * 如果文件不存在，不管文件名是否添加后缀，都可以创建为目录
	 */
	public static boolean isDirectory(File directory) {
		return notNull(directory) && 
				((!directory.exists()) || 
						directory.isDirectory());
	}
	
	/**
	 * 是否为文件
	 * 文件不存在或不为file时返回false
	 */
	public static boolean isFile(File... files) {
		if (files == null) 
			return false;
		
		for (File file : files) {
			if (exist(file) && (!file.isFile())) 
				return false;
		}
		return true;
	}
	
	
	/**
	 * 要求不为空
	 * @param o 对象
	 * @param errorMsg 错误信息
	 */
	public static void requiredNotNull(Object o, String errorMsg) {
		if (isNull(o))
			throw new NullPointerException(errorMsg);
	}
	
	/**
	 * 要求存在
	 * @param f 文件
	 * @param errorMsg 错误信息
	 */
	public static void requiredExist(File f) {
		requiredExist(f, "File must exist !");
	}
	
	/**
	 * 要求存在
	 * @param f 文件
	 * @param errorMsg 错误信息
	 */
	public static void requiredExist(File f, String errorMsg) {
		if (notExist(f)) 
			throw new IllegalAccessError(errorMsg);
	}

}
