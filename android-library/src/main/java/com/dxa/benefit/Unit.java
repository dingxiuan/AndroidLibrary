package com.dxa.benefit;

import java.util.Date;
import java.util.concurrent.TimeUnit;

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
     * 返回当前Date
     */
    public static Date nowDate() {
        return new Date();
    }

    /**
     * 返回当前时间
     */
    public static long now() {
        return System.currentTimeMillis();
    }

    /**
     * 获得 N 秒的时间长度
     */
    public static long seconds(int second) {
        return second > 0 ? (second * SECOND) : 0;
    }

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
     * 当前时间加上一个固定的时间，设置负值会被取正后再加
     *
     * @param time 相加的时间
     * @param unit 时间的单位
     * @return 返回相加后的时间
     */
    public static long nowPlus(int time, TimeUnit unit) {
        time = time >= 0 ? time : -time;
        long tmp = 0;
        switch (unit) {
            case MILLISECONDS:
                tmp = time;
                break;
            case SECONDS:
                tmp = seconds(time);
                break;
            case MINUTES:
                tmp = minutes(time);
                break;
            case HOURS:
                tmp = hours(time);
                break;
            case DAYS:
                tmp = days(time);
                break;
            default:
                break;
        }
        return tmp + System.currentTimeMillis();
    }

    /**
     * 当前时间减上一个固定的时间, 设置正/负值都会被取正后再减
     *
     * @param time 相减的时间
     * @param unit 时间的单位
     * @return 返回相减后的时间
     */
    public static long nowSubtract(int time, TimeUnit unit) {
        // 先取绝对值，为正后再减
        time = time < 0 ? -time : time;
        long tmp = 0;
        switch (unit) {
            case MILLISECONDS:
                tmp = time;
                break;
            case SECONDS:
                tmp = seconds(time);
                break;
            case MINUTES:
                tmp = minutes(time);
                break;
            case HOURS:
                tmp = hours(time);
                break;
            case DAYS:
                tmp = days(time);
                break;
            default:
                break;
        }
        return System.currentTimeMillis() - tmp;
    }

    /**
     * 月份
     */
    public enum Month {
        /**
         * 一月
         */
        JANUARY,
        /**
         * 二月
         */
        FEBRUARY,
        /**
         * 三月
         */
        MARCH,
        /**
         * 四月
         */
        APRIL,
        /**
         * 五月
         */
        MAY,
        /**
         * 六月
         */
        JUNE,
        /**
         * 七月
         */
        JULY,
        /**
         * 八月
         */
        AUGUST,
        /**
         * 九月
         */
        SEPTEMBER,
        /**
         * 十月
         */
        OCTOBER,
        /**
         * 十一月
         */
        NOVEMBER,
        /**
         * 十二月
         */
        DECEMBER;
    }
}