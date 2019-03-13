package com.dxa.android.preference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 操作SharedPreferences的工具类
 *
 * @author: DINGXIUAN
 */
class PreferenceImpl implements IPreference {

    private static IPreference instance;

    /**
     * 对象锁
     */
    private static final Object lock = new Object();

    /**
     * 获取PrefManager对象
     *
     * @param context
     * @return
     */
    public static IPreference getPreference(Context context) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    initPreference(context, FILE_NAME);
                }
            }
        }
        return instance;
    }

    /**
     * 获取PrefManager对象
     *
     * @param context
     * @param fileName
     * @return
     */
    public static IPreference getPreference(Context context, String fileName) {
        synchronized (lock) {
            if (instance == null) {
                initPreference(context, fileName);
            }
        }
        return instance;
    }

    /**
     * 初始化PrefManager对象
     *
     * @param context
     * @param fileName
     */
    private static synchronized void initPreference(Context context, String fileName) {
        if (instance == null) {
            instance = new PreferenceImpl(context, fileName);
        }
    }

    /**
     * 默认的文件名
     */
    private static final String FILE_NAME = "shared_preferences";

    private SharedPreferences preferences;

    /**
     * Instantiates a new Pref manager.
     *
     * @param context the context
     */
    public PreferenceImpl(Context context) {
        preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Instantiates a new Pref manager.
     *
     * @param context  the context
     * @param fileName the file name
     */
    public PreferenceImpl(Context context, String fileName) {
        preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    private SharedPreferences getPreferences() {
        return preferences;
    }

    private SharedPreferences.Editor getEditor() {
        return getPreferences().edit();
    }

    @Override
    public <T> void put(String key, T value) {
        SharedPreferences.Editor edit = getPreferences().edit();
        put(edit, key, value);
        edit.apply();
    }

    /**
     * 保存一个Map集合
     *
     * @param map
     */
    @Override
    public <T> void putAll(Map<String, T> map) {
        SharedPreferences.Editor edit = getEditor();
        for (Map.Entry<String, T> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            put(edit, key, value);
        }
        edit.apply();
    }

    @Override
    public void putAll(String key, List<String> list) {
        putAll(key, list, new ComparatorImpl());
    }

    @Override
    public void putAll(String key, List<String> list, Comparator<String> comparator) {
        Set<String> set = new TreeSet<String>(comparator);
        for (String value : list) {
            set.add(value);
        }
        getEditor().putStringSet(key, set).apply();
    }

    /**
     * 根据key取出一个数据
     *
     * @param key 键
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key, DataType type) {
        return (T) getValue(key, type);
    }

    @Override
    public Map<String, ?> getAll() {
        return getPreferences().getAll();
    }

    @Override
    public List<String> getAll(String key) {
        Set<String> set = getStringSet(key);
        return new ArrayList<>(set);
    }

    @Override
    public void remove(String key) {
        getEditor().remove(key).apply();
    }

    @Override
    public void removeAll(List<String> keys) {
        SharedPreferences.Editor edit = getEditor();
        for (String k : keys) {
            edit.remove(k);
        }
        edit.apply();
    }

    @Override
    public void removeAll(String[] keys) {
        removeAll(Arrays.asList(keys));
    }

    @Override
    public boolean contains(String key) {
        return getPreferences().contains(key);
    }

    @Override
    public void clear() {
        getEditor().clear().apply();
    }

    @Override
    public String getString(String key) {
        return getPreferences().getString(key, null);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return getPreferences().getString(key, defaultValue);
    }

    @Override
    public float getFloat(String key) {
        return getPreferences().getFloat(key, -1F);
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        return getPreferences().getFloat(key, defaultValue);
    }

    @Override
    public int getInteger(String key) {
        return getPreferences().getInt(key, -1);
    }

    @Override
    public int getInteger(String key, int defaultValue) {
        return getPreferences().getInt(key, defaultValue);
    }

    @Override
    public long getLong(String key) {
        return getPreferences().getLong(key, -1L);
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return getPreferences().getLong(key, defaultValue);
    }

    @Override
    public Set<String> getStringSet(String key) {
        return getPreferences().getStringSet(key, null);
    }

    @Override
    public Set<String> getStringSet(String key, Set<String> defaultValue) {
        return getPreferences().getStringSet(key, defaultValue);
    }

    @Override
    public boolean getBoolean(String key) {
        return getPreferences().getBoolean(key, false);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return getPreferences().getBoolean(key, defaultValue);
    }

    /**
     * 保存数据
     */
    @SuppressWarnings("unchecked")
    private <T> void put(SharedPreferences.Editor editor, String key, T o) {
        // key 不为null时再存入，否则不存储
        if (key != null || "".equals(key)) {
            if (o instanceof Integer) {
                editor.putInt(key, (Integer) o);
            } else if (o instanceof Long) {
                editor.putLong(key, (Long) o);
            } else if (o instanceof Boolean) {
                editor.putBoolean(key, (Boolean) o);
            } else if (o instanceof Float) {
                editor.putFloat(key, (Float) o);
            } else if (o instanceof Set) {
                editor.putStringSet(key, (Set<String>) o);
            } else if (o instanceof String) {
                editor.putString(key, String.valueOf(o));
            }
        }
    }

    /**
     * 根据key和类型取出数据
     *
     * @param key
     * @return
     */
    private Object getValue(String key, DataType type) {
        switch (type) {
            case INTEGER:
                return getPreferences().getInt(key, -1);
            case FLOAT:
                return getPreferences().getFloat(key, -1f);
            case BOOLEAN:
                return getPreferences().getBoolean(key, false);
            case LONG:
                return getPreferences().getLong(key, -1L);
            case STRING:
                return getPreferences().getString(key, "");
            case STRING_SET:
                return getPreferences().getStringSet(key, new HashSet<String>());
            default:
                return null;
        }
    }

}
