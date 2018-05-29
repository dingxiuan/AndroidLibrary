package com.dxa.android.logger;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 打印日志
 */

public class DLogger {
    /**
     * 默认的日志对象
     */
    public static final DLogger logger = new DLogger();

    /**
     * 显式日志的等级
     */
    private static LogLevel LEVEL = LogLevel.NONE;
    /**
     * 是否为Debug模式
     */
    private static boolean DEBUG = false;


    /**
     * 设置是否打印日志
     */
    public static void setDebug(boolean d) {
        DEBUG = d;
        LEVEL = d ? LogLevel.VERBOSE : LogLevel.NONE;
    }

    /**
     * 是否为Debug模式
     */
    public static boolean isDebug() {
        return DEBUG;
    }

    /**********************************************************/
    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 格式化日期
     */
    private final SimpleDateFormat sdf = new SimpleDateFormat(PATTERN, Locale.getDefault());
    /**
     * 拼接字符串
     */
    private final StringBuffer buffer = new StringBuffer();

    /**
     * tag
     */
    private String tag;
    private LogLevel level;

    public DLogger() {
        this("DLogger", LogLevel.DEBUG);
    }

    public DLogger(String tag) {
        this(tag, LogLevel.DEBUG);
    }

    public DLogger(String tag, LogLevel level) {
        this.tag = tag;
        this.level = level;
    }

    public DLogger(Class<?> clazz) {
        this(clazz, LogLevel.DEBUG);
    }

    public DLogger(Class<?> clazz, LogLevel level) {
        this(clazz.getSimpleName(), level);
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    /**
     * 日期格式
     */
    public void setFormat(String pattern) {
        sdf.applyPattern(pattern);
    }

    /**
     * 是否打印日志
     */
    public boolean isPrintLog(LogLevel l) {
        return this.level.getLevel() <= l.getLevel();
    }

    /**
     * 是否打印日志
     */
    public boolean isPrintLog() {
        return DEBUG && (LEVEL != LogLevel.NONE)
                && (LEVEL.getLevel() <= level.getLevel());
    }

    public void v(Object... msg) {
        log(LogLevel.VERBOSE, tag, msg);
    }

    public void d(Object... msg) {
        log(LogLevel.DEBUG, tag, msg);
    }

    public void i(Object... msg) {
        log(LogLevel.INFO, tag, msg);
    }

    public void w(Object... msg) {
        log(LogLevel.WARN, tag, msg);
    }

    public void e(Object... msg) {
        log(LogLevel.ERROR, tag, msg);
    }

    /**
     * 打印Throwable
     */
    public void e(String msg, Throwable e) {
        if (isPrintLog()) {
            Log.e(tag, msg, e);
        }
    }

    public void t(LogLevel level, Object... os) {
        t(tag, level, os);
    }

    public void t(String tag, LogLevel level, Object... os) {
        if (isPrintLog()) {
            String s;
            synchronized (buffer) {
                buffer.setLength(0);
                for (Object o : os) {
                    buffer.append(o);
                }
                s = buffer.append("\nThread: ").append(currentThreadName())
                        .append("\nTime: ").append(sdf.format(System.currentTimeMillis()))
                        .toString();
            }
            print(level, tag, s);
        }
    }

    public void log(LogLevel level, Object... os) {
        log(level, tag, os);
    }

    public void log(LogLevel level, String tag, Object... os) {
        if (isPrintLog() && isPrintLog(level)) {
            String s;
            synchronized (buffer) {
                buffer.setLength(0);
                for (Object o : os) {
                    buffer.append(o);
                }
                s = buffer.append("......")
                        .append(sdf.format(System.currentTimeMillis()))
                        .toString();
            }
            print(level, tag, s);
        }
    }

    private void print(LogLevel level, String tag, String msg) {
        if (isPrintLog(level) && notEmpty(msg)) {
            switch (level) {
                case VERBOSE:
                    Log.v(tag, msg);
                    break;
                case DEBUG:
                    Log.d(tag, msg);
                    break;
                case INFO:
                    Log.i(tag, msg);
                    break;
                case WARN:
                    Log.w(tag, msg);
                    break;
                case ERROR:
                    Log.e(tag, msg);
                    break;
                case NONE:
                    // nothing done!
                    break;
            }
        }
    }

    private static boolean notEmpty(String msg) {
        return msg != null && msg.trim().length() > 0;
    }

    private static String currentThreadName() {
        return Thread.currentThread().getName();
    }

}
