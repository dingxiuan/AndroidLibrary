package com.dxa.android.permission;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;

/**
 * 权限帮助类
 */
@SuppressLint("NewApi")
public class PermissionHelper {


    private PermissionHelper() {
    }

    /**
     * 检查SDK版本
     */
    public static boolean checkSDK() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }


    /**
     * 获取权限
     */
    public static boolean isGranted(int code) {
        return PackageManager.PERMISSION_GRANTED == code;
    }

    /**
     * 获取权限
     */
    public static boolean isGranted(Context c, String permission) {
        int code = getPermissionResult(c, permission);
        return isGranted(code);
    }

    /**
     * 未获取权限
     */
    public static boolean isDenied(int code) {
        return PackageManager.PERMISSION_DENIED == code;
    }

    /**
     * 未获取权限
     */
    public static boolean isDenied(Context c, String permission) {
        int code = getPermissionResult(c, permission);
        return isDenied(code);
    }

    /**
     * 是否需要解释获取权限原因
     */
    public static boolean shouldShow(Activity activity, String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }


    /**
     * 获取权限的结果码
     *
     * @param c          上下文对象
     * @param permission 权限
     * @return 结果码
     */
    public static int getPermissionResult(Context c, String permission) {
        return checkSDK() ? c.checkSelfPermission(permission) : 0;
    }

    /**
     * 获取所有已授权的权限
     *
     * @param c           上下文对象
     * @param permissions 权限
     * @return 已授权的权限集合
     */
    public static String[] getGrantedPermissions(Context c, String[] permissions) {
        required(c, permissions);

        ArrayList<String> grantedPermissions = new ArrayList<>();
        for (String p : permissions) {
            if (isGranted(c, p))
                grantedPermissions.add(p);
        }
        int size = grantedPermissions.size();
        return size > 0 ? grantedPermissions.toArray(new String[size]) : new String[0];
    }

    /**
     * 获取所有需要解释原因的权限
     */
    public static String[] getShouldShowRequestPermission(Activity c, String[] permissions) {
        required(c, permissions);

        ArrayList<String> shouldShowPermissions = new ArrayList<>();
        for (String p : permissions) {
            if (shouldShow(c, p))
                shouldShowPermissions.add(p);
        }
        int size = shouldShowPermissions.size();
        return size > 0 ? shouldShowPermissions.toArray(new String[size]) : new String[0];
    }


    /**
     * 获取所有未授权的权限
     *
     * @param c           上下文对象
     * @param permissions 权限
     * @return 未授权的权限集合
     */
    public static String[] getDeniedPermissions(Context c, String[] permissions) {
        required(c, permissions);

        ArrayList<String> deniedPermissions = new ArrayList<>();
        for (String p : permissions) {
            if (isDenied(c, p))
                deniedPermissions.add(p);
        }
        int size = deniedPermissions.size();
        return size > 0 ? deniedPermissions.toArray(new String[size]) : new String[0];
    }

    /**
     * 获取非Granted类型的权限
     *
     * @param c           上下文对象
     * @param permissions 权限
     */
    public static String[] getNonGrantedPermissions(Context c, String[] permissions) {
        required(c, permissions);

        ArrayList<String> nonGrantedPermissions = new ArrayList<>();
        for (String p : permissions) {
            if (!isGranted(c, p))
                nonGrantedPermissions.add(p);
        }
        int size = nonGrantedPermissions.size();
        return size > 0 ? nonGrantedPermissions.toArray(new String[size]) : new String[0];
    }

    /**
     * 请求权限
     *
     * @param c           Activity
     * @param requestCode 请求码
     * @param permissions 请求的权限
     */
    public static boolean requests(Activity c, int requestCode, String[] permissions) {
        if (checkSDK() && notNull(c) && nonEmpty(permissions)) {
            String[] ps = getNonGrantedPermissions(c, permissions);
            if (ps.length > 0) {
                c.requestPermissions(ps, requestCode);
                return true;
            }
        }
        return false;
    }

    /**
     * 请求权限
     */
    public static boolean request(Activity c, int requestCode, String permission) {
        String[] permissions = new String[]{permission};
        required(c, permissions);
        if (checkSDK() && !isGranted(c, permission)) {
            c.requestPermissions(permissions, requestCode);
            return true;
        }
        return false;
    }


    private static void required(Context c, String[] permissions) {
        if (isNull(c))
            throw new NullPointerException("Context's object is null !");
        else if (isNull(permissions))
            throw new NullPointerException("permissions is null");
        else if (haveEmpty(permissions))
            throw new IllegalStateException("permissions have empty");
    }

    private static <T> boolean notNull(T o) {
        return o != null;
    }

    private static <T> boolean isNull(T o) {
        return o == null;
    }

    private static boolean haveEmpty(String... ts) {
        if (ts == null)
            return true;

        for (String o : ts) {
            if (o == null || "".equals(o.trim()))
                return true;
        }
        return false;
    }

    private static boolean nonEmpty(String... ts) {
        if (ts == null)
            return false;

        for (String o : ts) {
            if (o == null || "".equals(o.trim()))
                return false;
        }
        return true;
    }
}
