package com.dxa.android.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.dxa.android.logger.DLogger;
import com.dxa.android.logger.LogLevel;

import java.util.Stack;

/**
 * Activity的管理类
 */
@RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class DActivityManager {

    private final String TAG = getClass().getSimpleName();
    private final DLogger logger = new DLogger(TAG);
    private final StringBuffer buffer = new StringBuffer();

    private final Stack<Activity> stack = new Stack<>();

    private Application.ActivityLifecycleCallbacks callbacks;

    public DActivityManager() {
    }

    public void setActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    private final Application.ActivityLifecycleCallbacks appCallbacks = new Application.ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            i(append("onActivityCreated>>>: ", getComponentName(activity)));
            push(activity);
            if (callbacks != null) {
                callbacks.onActivityCreated(activity, savedInstanceState);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            i(append("onActivityStarted: ", getComponentName(activity)));
            if (callbacks != null) {
                callbacks.onActivityStarted(activity);
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
            i(append("onActivityResumed: ", getComponentName(activity)));
            if (callbacks != null) {
                callbacks.onActivityResumed(activity);
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            i(append("onActivityPaused: ", getComponentName(activity)));
            if (callbacks != null) {
                callbacks.onActivityPaused(activity);
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {
            i(append("onActivityStopped: ", getComponentName(activity)));
            if (callbacks != null) {
                callbacks.onActivityStopped(activity);
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            i(append("onActivitySaveInstanceState: ", getComponentName(activity)));
            if (callbacks != null) {
                callbacks.onActivitySaveInstanceState(activity, outState);
            }
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            i(append("onActivityDestroyed: ", remove(activity)));
            if (callbacks != null) {
                callbacks.onActivityDestroyed(activity);
            }
        }
    };

    /**
     * 设备debug
     */
    public void setDebug(boolean debug) {
        LogLevel level = debug ? LogLevel.DEBUG : LogLevel.NONE;
        logger.setLevel(level);
    }

    public void setTag(String tag) {
        logger.setTag(tag);
    }

    /**
     * Activity集合的大小
     *
     * @return
     */
    public int size() {
        return stack.size();
    }

    /**
     * 移除栈顶Activity
     */
    public Activity pop() {
        return stack.pop();
    }

    /**
     * 弹出栈顶Activity对象，不移除
     */
    public Activity peek() {
        return stack.peek();
    }

    /**
     * 将Activity添加到栈底
     */
    public boolean add(Activity activity) {
        return notNull(activity) && stack.add(activity);
    }

    /**
     * 往栈顶压入一个Activity
     */
    public Activity push(Activity activity) {
        if (notNull(activity)) {
            activity = stack.push(activity);
        }
        return activity;
    }

    /**
     * 移除Activity
     */
    public boolean remove(Activity activity) {
        return stack.remove(activity);
    }

    /**
     * 销毁Activity
     */
    public boolean finish(Activity activity) {
        if (notNull(activity) && stack.contains(activity)) {
            boolean remove = stack.remove(activity);
            activity.finish();
            logger.i("是否移除Activity: " + remove);
            return remove;
        }
        return false;
    }

    /**
     * 销毁全部
     */
    public void finishAll() {
        synchronized (stack) {
            StringBuilder builder = new StringBuilder();
            int size = stack.size();
            builder.append("Activity的数量: ")
                    .append(size)
                    .append("\n");
            for (int i = 0; i < size; i++) {
                Activity activity = stack.pop();
                activity.finish();
                builder.append(i)
                        .append(" : ")
                        .append(activity.getClass().getName())
                        .append("\n");
            }
            i(builder.toString());
            builder.setLength(0);
        }
    }

    /**
     * 清空栈
     */
    public void clear() {
        stack.clear();
    }

    private void i(String msg) {
        if (logger != null) {
            logger.i(msg);
        }
    }

    /**
     * 是否包含Activity
     */
    public boolean contains(Activity activity) {
        return notNull(activity) && stack.contains(activity);
    }

    /**
     * 获取组件名
     */
    private String getComponentName(Context context) {
        return append(context.getPackageName(), "@",
                context.getClass().getSimpleName());
    }

    private String append(Object... os) {
        String msg;
        synchronized (buffer) {
            buffer.setLength(0);
            for (Object o : os) {
                buffer.append(o.toString());
            }
            msg = buffer.toString();
        }
        return msg;
    }

    private static boolean notNull(Object o) {
        return o != null;
    }

}
