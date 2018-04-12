package com.dxa.common;

/**
 * 基本单位
 */
public final class Unit {
	private Unit() {
	}
	/****************************************/
	// 内存、文件大小

	/**
	 * KB
	 */
	public static final long KB = 1024L;
	/**
	 * MB
	 */
	public static final long MB = 1024L * KB;
	/**
	 * GB
	 */
	public static final long GB = 1024L * MB;
	/**
	 * TB
	 */
	public static final long TB = 1024L * GB;

	/**
	 * 计算KB
	 */
	public static float toKB(long size) {
		return (size * 1.0f) / KB;
	}

	/**
	 * 计算MB
	 */
	public static float toMB(long size) {
		return (size * 1.0f) / MB;
	}

	/**
	 * 计算GB
	 */
	public static float toGB(long size) {
		return (size * 1.0f) / GB;
	}

	/**
	 * 获取KB
	 */
	public static long KB(int size) {
		return size > 0 ? size * KB : 0;
	}

	/**
	 * 获取MB
	 */
	public static long MB(int size) {
		return size > 0 ? size * MB : 0;
	}

	/**
	 * 获取GB
	 */
	public static long GB(int size) {
		return size > 0 ? size * GB : 0;
	}

	/****************************************/
	// 时间

	/**
	 * 秒
	 */
	public static final long SECOND = 1000L;
	/**
	 * 分钟
	 */
	public static final long MINUTE = 60 * SECOND;
	/**
	 * 小时
	 */
	public static final long HOUR = 60 * MINUTE;
	/**
	 * 天
	 */
	public static final long DAY = 24 * HOUR;
	/**
	 * 周
	 */
	public static final long WEEK = 7 * DAY;

	/**
	 * 获得 N 分钟的时间长度
	 */
	public static long minutes(int minute) {
		return minute > 0 ? (minute * MINUTE) : 0;
	}

	/**
	 * 获得 N 小时的时间长度
	 */
	public static long hours(int hour) {
		return hour > 0 ? (hour * HOUR) : 0;
	}

	/**
	 * 获得 N 天的时间长度
	 */
	public static long days(int day) {
		return day > 0 ? (day * DAY) : 0;
	}

	/**
	 * 月份
	 */
	public enum Month {
		JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER;
	}
}
