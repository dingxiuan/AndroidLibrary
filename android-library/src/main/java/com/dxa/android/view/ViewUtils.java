package com.dxa.android.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

public class ViewUtils {

    public static boolean notNull(Object o) {
        return o != null;
    }

    public static boolean isNull(Object o) {
        return o == null;
    }

    /**
     * 设置点击销毁
     */
    public static void setOnClickFinish(View view) {
        view.setOnClickListener(FinishClickListener.get());
    }

    public static float getScaledDensity(DisplayMetrics metrics) {
        return metrics.scaledDensity;
    }

    public static float getScaledDensity(Context context) {
        return getDisplayMetrics(context).scaledDensity;
    }

    public static int getDensityDpi(DisplayMetrics metrics) {
        return metrics.densityDpi;
    }

    public static int getDensityDpi(Context context) {
        return getDisplayMetrics(context).densityDpi;
    }

    /**
     * 获取DisplayMetrics
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * 获取DisplayMetrics对象
     */
    public static DisplayMetrics getDisplayMetrics(Activity activity) {
        Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        defaultDisplay.getMetrics(metrics);
        return metrics;
    }

    @SuppressWarnings("unchecked")
    public static <V extends View> V find(Activity act, @IdRes int id) {
        return (V) act.findViewById(id);
    }
    
    @SuppressWarnings("unchecked")
	public static <V extends View> V find(View v, @IdRes int id){
    	return (V) v.findViewById(id);
    }
    
    /**
     * 设置文本内容
     */
    public static <V extends TextView> void setText(V view, String s) {
        if (notNull(view) && notNull(s)) {
            view.setText(s);
        }
    }
    
    /**
     * 设置文本内容
     */
    public static <V extends TextView> void setText(Activity act, @IdRes int id, String s) {
        TextView view = find(act, id);
        setText(view, s);
    }
    
    /**
     * 获取文本内容
     *
     * @param v 继承自TextView
     */
    public static <V extends TextView> String getText(V v) {
        return notNull(v) ? v.getText().toString().trim() : "";
    }

    /**
     * 获取文本内容
     */
    public static String getText(Activity activity, @IdRes int idRes) {
        TextView v = find(activity, idRes);
        return getText(v);
    }

    public static <V extends TextView> boolean isEmpty(V v) {
        return getText(v).equals("");
    }

    public static boolean isEmpty(Activity activity, @IdRes int idRes) {
        TextView v = find(activity, idRes);
        return isEmpty(v);
    }


    @SafeVarargs
    public static <T extends TextView> void clear(T... ts) {
        if (notNull(ts)) {
            for (TextView tv : ts) {
                if (notNull(tv)) tv.setText("");
            }
        }
    }

    /**
     * 设置View的监听
     *
     * @param v   给View设置监听
     * @param l   监听器对象
     */
    public static <V extends View, L extends View.OnClickListener> void setOnClickListener(V v, L l) {
        if (notNull(v) && notNull(l)) {
            v.setOnClickListener(l);
        } else {
            Log.e("TAG", "Can't set click listener!");
        }
    }

    /**
     * 设置View的监听
     *
     * @param l   监听器对象
     * @param <L> Listener
     */
    public static <L extends View.OnClickListener> void setOnClickListener(Activity activity, @IdRes int idRes, L l) {
        View v = find(activity, idRes);
        if (notNull(v) && notNull(l)) {
            v.setOnClickListener(l);
        } else {
            Log.e("TAG", "Can't set click listener!");
        }
    }


}
