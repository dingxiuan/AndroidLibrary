package com.dxa.common;

import java.lang.ref.SoftReference;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日期格式化：
 * <p>
 * 默认: 横线，没有后缀
 * HR: 代表横线，默认
 * BACKSLASH: 代表反斜杠
 * BLANK: 代表空格
 * CN: 代表中文
 */
public final class DateFormatter {

    // 横线
    public static final String yMd = "yyyy-MM-dd";
    // 反斜杠
    public static final String yMd_BACKSLASH = "yyyy/MM/dd";
    // 没有空格
    public static final String yMd_NO = "yyyyMMdd";
    // 中文
    public static final String yMd_CN = "yyyy年MM月dd";

    // ************************************************/
    // 横线
    public static final String yMdHm = "yyyy-MM-dd HH:mm";
    // 反斜杠
    public static final String yMdHm_BACKSLASH = "yyyy/MM/dd HH:mm";
    // 没有空格
    public static final String yMdHm_NO = "yyyyMMddHHmm";
    // 中文
    public static final String yMdHm_CN = "yyyy年MM月dd HH时mm分";

    // ************************************************/
    // 横线
    public static final String yMdHms = "yyyy-MM-dd HH:mm:ss";
    // 反斜杠
    public static final String yMdHms_BACKSLASH = "yyyy/MM/dd HH:mm:ss";
    // 没有空格
    public static final String yMdHms_NO = "yyyyMMddHHmmss";
    // 中文
    public static final String yMdHms_CN = "yyyy年MM月dd HH时mm分ss秒";

    // ************************************************/
    // 横线
    public static final String yMdHmsS = "yyyy-MM-dd HH:mm:ss.SSS";
    // 反斜杠
    public static final String yMdHmsS_BACKSLASH = "yyyy/MM/dd HH:mm:ss.SSS";
    // 没有空格
    public static final String yMdHmsS_NO = "yyyyMMddHHmmssSSS";
    // 中文
    public static final String yMdHmsS_CN = "yyyy年MM月dd HH时mm分ss秒.SSS";

    // ************************************************/
    // 横线
    public static final String Hms = "HH:mm:ss";
    // 中文
    public static final String Hms_CN = "HH时mm分ss秒";
    // 横线
    public static final String HmsS = "HH:mm:ss.SSS";
    // 中文
    public static final String HmsS_CN = "HH时mm分ss秒.SSS";


    private static final String EMPTY = "";
    /**
     * 缓存时间格式化
     */
    private static final ConcurrentHashMap<String, SoftReference<SecureDateFormat>> FORMAT_CACHE;

    static {
        FORMAT_CACHE = new ConcurrentHashMap<>();
    }

    private DateFormatter() {
    }

    /**
     * 获取SimpleDateFormat
     */
    public static SecureDateFormat getSDF(String pattern) {
        SoftReference<SecureDateFormat> reference = FORMAT_CACHE.get(pattern);
        SecureDateFormat sdf = null;
        if (reference != null) {
            sdf = reference.get();
        }

        synchronized (DateFormatter.class) {
            if (sdf == null) {
                sdf = new SecureDateFormat(pattern);
                reference = new SoftReference<>(sdf);
                FORMAT_CACHE.putIfAbsent(pattern, reference);
            }
        }
        return sdf;
    }

    /**
     * 解析字符串类型的时间
     */
    public static Date parseOrThrows(String time, SecureDateFormat sdf) throws ParseException {
        if (Checker.isBlank(time) || Checker.isNull(sdf))
            return null;
        synchronized (sdf.lock) {
            return sdf.parse(time);
        }
    }

    /**
     * 解析字符串类型的时间
     */
    public static Date parseOrThrows(String time, String pattern) throws ParseException {
        if (Checker.hasBlank(time, pattern))
            return null;
        SecureDateFormat sdf = getSDF(pattern);
        return parseOrThrows(time, sdf);
    }

    /**
     * 解析字符串类型的时间
     */
    public static Date parse(String time, SecureDateFormat sdf) {
        if (Checker.hasNull(time, sdf))
            return null;
        try {
            return parseOrThrows(time, sdf);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 解析字符串类型的时间
     */
    public static long parseToLong(String time, String pattern) {
        return getTime(time, pattern);
    }

    /**
     * 解析字符串类型的时间
     */
    public static Date parse(String time, String pattern) {
        if (Checker.hasNull(time, pattern))
            return null;
        SecureDateFormat sdf = getSDF(pattern);
        return parse(time, sdf);
    }

    /**
     * 获取long
     *
     * @param time    字符串时间
     * @param pattern 格式
     * @return 返回时间，如果格式有问题，返回 0
     */
    public static long getTime(String time, String pattern) {
        return getTime(time, pattern, 0L);
    }

    /**
     * 获取long
     *
     * @param time         字符串时间
     * @param pattern      格式
     * @param defaultValue 默认值
     * @return 返回时间，如果格式有问题，返回 0
     */
    public static long getTime(String time, String pattern, long defaultValue) {
        Date date = parse(time, pattern);
        return date != null ? date.getTime() : defaultValue;
    }

    /**
     * 格式化日期
     */
    public static String fomatNow(String pattern) {
        if (Checker.isBlank(pattern))
            return null;
        long current = System.currentTimeMillis();
        SecureDateFormat sdf = getSDF(pattern);
        return format(current, sdf);
    }

    /**
     * 格式化日期
     */
    public static String fomat(Date time, String pattern) {
        if (Checker.hasNull(time, pattern))
            return null;
        SecureDateFormat sdf = getSDF(pattern);
        return format(time, sdf);
    }

    /**
     * 格式化日期
     */
    public static String fomat(long time, String pattern) {
        if (Checker.isNull(pattern))
            return null;
        SecureDateFormat sdf = getSDF(pattern);
        return format(time, sdf);
    }

    /**
     * 格式化日期
     */
    public static String format(Date time, SecureDateFormat sdf) {
        synchronized (sdf.lock) {
            return sdf.format(time);
        }
    }

    /**
     * 格式化日期
     */
    public static String format(long time, SecureDateFormat sdf) {
        synchronized (sdf.lock) {
            return sdf.format(time);
        }
    }

    /**
     * 格式化当前时间
     *
     * @param pattern 格式
     * @return 返回格式化好的时间字符串
     */
    public static String formatNow(String pattern) {
        SecureDateFormat sdf = getSDF(pattern);
        return format(System.currentTimeMillis(), sdf);
    }

    /**
     * 格式化当前时间
     *
     * @return 返回格式化好的时间字符串
     */
    public static String formatNow() {
        SecureDateFormat sdf = getSDF(yMdHms);
        return format(System.currentTimeMillis(), sdf);
    }

    public static class SecureDateFormat extends SimpleDateFormat {

        /**
         *
         */
        private static final long serialVersionUID = -2122594483902414276L;

        private final Object lock = new Object();

        public SecureDateFormat() {
            super();
        }

        public SecureDateFormat(String pattern, DateFormatSymbols formatSymbols) {
            super(pattern, formatSymbols);
        }

        public SecureDateFormat(String pattern, Locale locale) {
            super(pattern, locale);
        }

        public SecureDateFormat(String pattern) {
            super(pattern);
        }

        /**
         * 获取当前对象的锁
         */
        public final synchronized Object getLock() {
            return lock;
        }
    }

}
