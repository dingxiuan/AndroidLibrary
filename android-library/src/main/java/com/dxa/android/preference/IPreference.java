package com.dxa.android.preference;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 操作Preferences的接口声明
 */
public interface IPreference {

    /**
     * 创建 IPreference
     *
     * @param context  上下文
     * @param filename 文件名
     * @return 返回 DefaultPreference 实例
     */
    static IPreference newPreference(Context context, String filename) {
        return new DefaultPreference(context, filename);
    }

    /**
     *
     */
    SharedPreferences getSharedPreferences();

    /**
     * @return 获取编辑SP文件的对象
     */
    default SharedPreferences.Editor getEditor() {
        return getSharedPreferences().edit();
    }

    /**
     * 处理器
     */
    void setHandler(Handler handler);

    /**
     * @return 获取Handler
     */
    Handler getHandler();

    /**
     * 保存一个数据
     */
    default <T> void put(String key, T value) {
        SharedPreferences.Editor editor = getEditor();
        put(editor, key, value);
        editor.apply();
    }

    /**
     * 保存一个数据
     *
     * @param editor Editor
     * @param key    键
     * @param value  值
     * @param <T>    类型
     */
    default <T> void put(SharedPreferences.Editor editor, String key, T value) {
        // key 不为null时再存入，否则不存储
        if (key != null && !key.isEmpty()) {
            DataType type;
            if (value instanceof Boolean) {
                type = DataType.BOOLEAN;
            } else if (value instanceof Integer) {
                type = DataType.INTEGER;
            } else if (value instanceof Long) {
                type = DataType.LONG;
            } else if (value instanceof Float) {
                type = DataType.FLOAT;
            } else if (value instanceof String) {
                type = DataType.STRING;
            } else if (value instanceof Set) {
                type = DataType.STRING_SET;
            } else {
                type = DataType.OTHER;
            }
            getHandler().onPutValue(editor, key, value, type);
        }
    }

    /**
     * 保存一个Map集合
     */
    default <T> void putAll(Map<String, T> map) {
        SharedPreferences.Editor editor = getEditor();
        for (Map.Entry<String, T> entry : map.entrySet()) {
            put(editor, entry.getKey(), entry.getValue());
        }
        editor.apply();
    }

    /**
     * 保存一个List集合
     */
    default void putAll(String key, List<String> list) {
        put(key, new TreeSet<>(list));
    }

    /**
     * 保存一个List集合，并且自定保存顺序
     */
    default void putAll(String key, List<String> list, Comparator<String> comparator) {
        Set<String> set = new TreeSet<>(comparator);
        set.addAll(list);
        put(key, set);
    }

    /**
     * 获取值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @param type         类型
     * @param <T>          类型
     * @return 返回获取的值
     */
    default <T> T get(String key, T defaultValue, DataType type) {
        SharedPreferences sp = getSharedPreferences();
        return getHandler().onGetValue(sp, key, defaultValue, type);
    }

    /**
     * @return 取出全部数据
     */
    @SuppressWarnings("unchecked")
    default Map<String, ?> getAll() {
        return get("all", Collections.EMPTY_MAP, DataType.ALL);
    }

    /**
     * 取出一个List集合
     */
    @SuppressWarnings("unchecked")
    default List<String> getAll(String key) {
        Set<String> set = getStringSet(key);
        if (set != null) {
            ArrayList<String> list = new ArrayList<>(set);
            Collections.sort(list, String::compareTo);
            return list;
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 获取String类型的数据
     *
     * @param key 键
     * @return 返回数据
     */
    default String getString(String key) {
        return getString(key, null);
    }

    /**
     * 获取String类型的数据
     */
    default String getString(String key, String defaultValue) {
        return get(key, defaultValue, DataType.STRING);
    }

    /**
     * 获取Float类型的数据
     *
     * @param key 键
     * @return 返回数据
     */
    default float getFloat(String key) {
        return getFloat(key, -1F);
    }

    /**
     * 获取Float类型的数据
     */
    default float getFloat(String key, float defaultValue) {
        return get(key, defaultValue, DataType.FLOAT);
    }

    /**
     * 获取int类型的数据
     *
     * @param key 键
     * @return 返回数据
     */
    default int getInteger(String key) {
        return getInteger(key, -1);
    }

    /**
     * 获取int类型的数据
     */
    default int getInteger(String key, int defaultValue) {
        return get(key, defaultValue, DataType.INTEGER);
    }

    /**
     * 获取long类型的数据
     *
     * @param key 键
     * @return 返回数据
     */
    default long getLong(String key) {
        return getLong(key, -1L);
    }

    /**
     * 获取long类型的数据
     */
    default long getLong(String key, long defaultValue) {
        return get(key, defaultValue, DataType.LONG);
    }

    /**
     * 获取Set类型的数据
     *
     * @param key 键
     * @return 返回数据
     */
    @SuppressWarnings("unchecked")
    default Set<String> getStringSet(String key) {
        return getStringSet(key, Collections.EMPTY_SET);
    }

    /**
     * 获取Set类型的数据
     */
    default Set<String> getStringSet(String key, Set<String> defaultValue) {
        return get(key, defaultValue, DataType.STRING_SET);
    }

    /**
     * 获取boolean类型的数据
     *
     * @param key 键
     * @return 返回数据
     */
    default boolean getBoolean(String key) {
        return getBoolean(key, Boolean.FALSE);
    }

    /**
     * 获取boolean类型的数据
     */
    default boolean getBoolean(String key, boolean defaultValue) {
        return get(key, defaultValue, DataType.BOOLEAN);
    }

    /**
     * 移除一个数据
     */
    default void remove(String key) {
        removeAll(Collections.singletonList(key));
    }

    /**
     * 移除一个集合的数据
     */
    default void removeAll(List<String> keys) {
        SharedPreferences.Editor editor = getEditor();
        Handler handler = getHandler();
        for (String key : keys) {
            handler.onRemove(editor, key);
        }
        editor.apply();
    }

    /**
     * 移除一个集合的数据
     */
    default void removeAll(String[] keys) {
        removeAll(Arrays.asList(keys));
    }

    /**
     * 是否存在key
     */
    default boolean contains(String key) {
        return getSharedPreferences().contains(key);
    }

    /**
     * 清除全部数据
     */
    default void clear() {
        getHandler().onClear(getEditor());
    }

    /**
     * 枚举：存储或取出的数据类型
     */
    enum DataType {
        INTEGER, LONG, BOOLEAN, FLOAT, STRING, STRING_SET, ALL, OTHER
    }


    interface Handler {
        /**
         * 被Put之前
         *
         * @param editor Editor
         * @param key    键
         * @param value  值
         */
        void onPutValue(SharedPreferences.Editor editor, String key, Object value, DataType type);

        /**
         * 获取数据
         *
         * @param sp           SharedPreferences
         * @param key          键
         * @param defaultValue 默认值
         * @param type         类型
         * @param <T>          返回值类型
         * @return 返回获取的数据
         */
        <T> T onGetValue(SharedPreferences sp, String key, T defaultValue, DataType type);

        /**
         * 被移除
         *
         * @param editor Editor
         * @param key    键
         */
        void onRemove(SharedPreferences.Editor editor, String key);

        /**
         * 被清空
         *
         * @param editor Editor
         */
        void onClear(SharedPreferences.Editor editor);
    }


}
