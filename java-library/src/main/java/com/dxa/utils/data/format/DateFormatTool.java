package com.dxa.utils.data.format;

import com.dxa.utils.check.Checker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期格式化工具
 */
public class DateFormatTool {
    public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String YMDHMSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String YMD = "yyyy-MM-dd";
    public static final String HMS = "HH:mm:ss";
    public static final String HM = "HH:mm";
    public static final String MS = "mm:ss";
    public static final String YMDHMS_C = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String YMDHMSS_C = "yyyy年MM月dd日 HH时mm分ss秒.SSS";
    public static final String MD_C = "yyyy年MM月dd日";
    public static final String HMS_C = "HH时mm分ss秒";
    public static final String HM_C = "HH时mm分";

    private final SimpleDateFormat sdf;
    private String pattern = YMDHMS;

    public DateFormatTool() {
        sdf = new SimpleDateFormat(pattern, Locale.getDefault());
    }

    public DateFormatTool(String pattern) {
        this.pattern = pattern;
        sdf = new SimpleDateFormat(pattern, Locale.getDefault());
    }

    public void applyPattern(String pattern) {
        if (Checker.notEmptyWithTrim(pattern))
            sdf.applyPattern(pattern);
    }

    public String format(long time) {
        return sdf.format(time);
    }

    public String format(Date time) {
        return sdf.format(time);
    }

    public Date parse(String time) {
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**********************************************************/

    private static final SimpleDateFormat SDF = new SimpleDateFormat();
    private static final SimpleDateFormat SDF2 = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());

    /**
     * 格式化日期
     */
    public static String getFormat(long time, String pattern) {
        synchronized (SDF) {
            SDF.applyPattern(pattern);
            return SDF.format(time);
        }
    }

    /**
     * 解析时间
     */
    public static Date parseDate(String time, String pattern) {
        synchronized (SDF) {
            try {
                SDF.applyPattern(pattern);
                return SDF.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 解析时间
     */
    public static long parseTime(String time, String pattern) {
        synchronized (SDF) {
            try {
                SDF.applyPattern(pattern);
                return SDF.parse(time).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    /**
     * 格式化日期
     */
    public static String getFormat(long time) {
        synchronized (SDF2) {
            return SDF2.format(time);
        }
    }


    /**
     * 解析时间
     */
    public static Date parseDate(String time) {
        synchronized (SDF2) {
            try {
                return SDF2.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static long parseTime(String time) {
        synchronized (SDF2) {
            try {
                return SDF2.parse(time).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

}
