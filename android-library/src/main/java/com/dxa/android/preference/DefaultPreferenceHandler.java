package com.dxa.android.preference;

import android.content.SharedPreferences;

import java.util.Set;

public class DefaultPreferenceHandler implements IPreference.Handler {

    /**
     * 被Put之前
     *
     * @param editor Editor
     * @param key    键
     * @param value  值
     */
    public void onPutValue(SharedPreferences.Editor editor, String key, Object value, IPreference.DataType type) {
        switch (type) {
            case BOOLEAN:
                editor.putBoolean(key, (Boolean) value);
                break;
            case INTEGER:
                editor.putInt(key, (Integer) value);
                break;
            case LONG:
                editor.putLong(key, (Long) value);
                break;
            case FLOAT:
                editor.putFloat(key, (Float) value);
                break;
            case STRING:
                editor.putString(key, (String) value);
                break;
            case STRING_SET:
                editor.putStringSet(key, (Set<String>) value);
                break;
            case OTHER:
                editor.remove(key);
                break;
            default:
                break;
        }
    }

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
    @SuppressWarnings("unchecked")
    public <T> T onGetValue(SharedPreferences sp, String key, T defaultValue, IPreference.DataType type) {
        Object v;
        switch (type) {
            case BOOLEAN:
                v = sp.getBoolean(key, (Boolean) defaultValue);
                break;
            case INTEGER:
                v = sp.getInt(key, (Integer) defaultValue);
                break;
            case LONG:
                v = sp.getLong(key, (Long) defaultValue);
                break;
            case FLOAT:
                v = sp.getFloat(key, (Float) defaultValue);
                break;
            case STRING:
                v = sp.getString(key, (String) defaultValue);
                break;
            case STRING_SET:
                v = sp.getStringSet(key, (Set<String>) defaultValue);
                break;
            case ALL:
                v = sp.getAll();
                break;
            default:
                v = defaultValue;
                break;
        }
        return (T) v;
    }

    /**
     * 被移除
     *
     * @param editor Editor
     * @param key    键
     */
    public void onRemove(SharedPreferences.Editor editor, String key) {
        editor.remove(key);
    }

    /**
     * 被清空
     *
     * @param editor Editor
     */
    public void onClear(SharedPreferences.Editor editor) {
        editor.clear();
        editor.apply();
    }

}
