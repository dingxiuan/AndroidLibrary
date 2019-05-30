package com.dxa.android.preference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 操作SharedPreferences的工具类
 */
public class DefaultPreference implements IPreference {

    private static final Handler HANDLER_INSTANCE = new DefaultPreferenceHandler();
    /**
     * 默认的文件名
     */
    private static final String FILENAME = "shared_preferences";

    private Handler handler = HANDLER_INSTANCE;

    private SharedPreferences preferences;

    /**
     * Instantiates a new Pref manager.
     *
     * @param context the context
     */
    public DefaultPreference(Context context) {
        this(context, FILENAME);
    }

    /**
     * Instantiates a new Pref manager.
     *
     * @param context  the context
     * @param filename the file name
     */
    public DefaultPreference(Context context, String filename) {
        preferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return preferences;
    }

    @Override
    public void setHandler(Handler handler) {
        this.handler = (handler == null ? HANDLER_INSTANCE : handler);
    }

    @Override
    public Handler getHandler() {
        return handler;
    }


}
