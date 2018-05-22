package com.dxa.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 检查类
 */
public final class Checker {
    private Checker() {
    }

    /******************************************************************/
    // 对象的检查

    /**
     * 为null？
     */
    public static boolean isNull(Object o) {
        return o == null;
    }

    /**
     * 不为null？
     */
    public static boolean isNotNull(Object o) {
        return o != null;
    }

    /**
     * 全部不为null？
     */
    public static boolean isNonNull(Object... os) {
        if (os == null)
            return false;

        for (Object o : os) {
            if (o == null)
                return false;
        }
        return true;
    }

    /**
     * 存在null？
     */
    public static boolean hasNull(Object... os) {
        if (os == null)
            return true;

        for (Object o : os) {
            if (o == null)
                return true;
        }
        return false;
    }

    /**
     * 存在Blank？
     */
    public static boolean hasBlank(String... ss) {
        if (ss == null)
            return true;

        for (String s : ss) {
            if (isBlank(s))
                return true;
        }
        return false;
    }

    /**
     * 判断某些对象是否满足要求，如果不满足就返回不包含的对象的List
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> has(Callback<T> call, T... ts) {
        List<T> list = new ArrayList<>();
        for (T t : ts) {
            if (!call.call(t)) {
                list.add(t);
            }
        }
        return list;
    }

    /**
     * 是否匹配
     *
     * @param os 检查的对象数组
     * @param call 回调是否匹配
     * @return 如果存在匹配的，返回true，否则false
     */
    public static <T> boolean match(T[] os, Callback<T> call) {
        if (os == null)
            return true;

        for (T t : os) {
            if (call.call(t))
                return true;
        }
        return false;
    }

    /******************************************************************/
    // 字符串的检查

    /**
     * 不为Null，否则返回默认值
     */
    public static String getNotNull(String s, String staticValue) {
        return isNotNull(s) ? s : staticValue;
    }

    /**
     * 不为Null，否则返回空字符串
     */
    public static String getNotNull(String s) {
        return getNotNull(s, "");
    }

    /**
     * 为空？
     */
    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * 为空？
     */
    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * 不为 null 且为空？
     */
    public static boolean isBlankAndNotNull(String s) {
        return s != null && s.trim().isEmpty();
    }

    /**
     * 不为空？
     */
    public static boolean isNotEmpty(String s) {
        return s != null && s.length() == 0;
    }

    /**
     * 不为空？
     */
    public static boolean isNotBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    /**
     * 全部不为空？
     */
    public static boolean isNonEmpty(String... ss) {
        if (ss == null)
            return false;

        for (String s : ss) {
            if (s == null)
                return false;
        }
        return true;
    }

    /**
     * 全部不为空？
     */
    public static boolean isNonBlank(String... ss) {
        if (ss == null)
            return false;

        for (String s : ss) {
            if (isBlank(s))
                return false;
        }
        return true;
    }

    /******************************************************************/
    // 集合or数组的检查

    /**
     * 不为空?
     */
    public static <C extends Collection<?>> boolean isNotEmpty(C c) {
        return c != null && (!c.isEmpty());
    }

    /**
     * 为空?
     */
    public static <C extends Collection<?>> boolean isEmpty(C c) {
        return c == null || c.isEmpty();
    }

    /**
     * 不为空?
     */
    public static <M extends Map<?, ?>> boolean isNotEmpty(M m) {
        return m != null && (!m.isEmpty());
    }

    /**
     * 为空?
     */
    public static <M extends Map<?, ?>> boolean isEmpty(M m) {
        return m == null || m.isEmpty();
    }

    /**
     * 为空?
     */
    @SafeVarargs
    public static <C extends Collection<?>> boolean isEmpty(C... collections) {
        if (collections == null || collections.length <= 0) {
            return true;
        }
        for (C map : collections) {
            if (isEmpty(map)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 为空?
     */
    @SafeVarargs
    public static <M extends Map<?, ?>> boolean isEmpty(M... maps) {
        if (maps == null || maps.length <= 0) {
            return true;
        }
        for (M map : maps) {
            if (isEmpty(map)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 不为空？
     */
    @SafeVarargs
    public static <C extends Collection<?>> boolean isNonEmpty(C... maps) {
        return !isEmpty(maps);
    }

    /**
     * 不为空？
     */
    @SafeVarargs
    public static <M extends Map<?, ?>> boolean isNonEmpty(M... maps) {
        return !isEmpty(maps);
    }

    /**
     * 不为空？
     */
    public static <T> boolean isNotEmpty(T[] ts) {
        return ts != null && ts.length > 0;
    }

    /**
     * 为空？
     */
    public static <T> boolean isEmpty(T[] ts) {
        return ts == null || ts.length < 1;
    }

    /******************************************************************/
    /**
     * 是否为空
     */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * 是否为数字
     */
    public static boolean isNumeric(final CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    /******************************************************************/
    // 文件的检查

    /**
     * 存在?
     */
    public static boolean exist(File f) {
        return f != null && f.exists();
    }

    /**
     * 不存在?
     */
    public static boolean isNotExist(File f) {
        return f != null && !f.exists();
    }

    /**
     * 是否是否文件
     */
    public static boolean isFile(File f) {
        return f != null && f.exists() && f.isFile();
    }

    /**
     * 是否是否目录
     */
    public static boolean isDirectory(File d) {
        return d != null && d.exists() && d.isDirectory();
    }

    /**
     * 检查的回调
     *
     * @author DINGXIUAN
     *
     * @param <T>
     */
    public interface Callback<T> {
        boolean call(T t);
    }

}