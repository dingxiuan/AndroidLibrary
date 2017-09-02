package com.dxa.android.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.dxa.android.logger.DLogger;
import com.dxa.android.logger.LogLevel;


/**
 * Activity的基类
 */
@SuppressLint("NewApi")
public abstract class SuperActivity<P extends ActivityPresenter>
        extends AppCompatActivity implements IView {
	
	private final ActivityLifecycle LIFECYCLE = new DefaultActivityLifecycle();

    protected final DLogger logger = new DLogger("Activity");
    private final Object lock = new Object();

    /**
     * Presenter对象
     */
    private P presenter;
    /**
     * 
     */
    private final Handler handler;
    /**
     * Activity的生命周期回调
     */
    private ActivityLifecycle lifecycle = LIFECYCLE;

    public SuperActivity() {
        String tag = getClass().getSimpleName();
        logger.setTag(tag);
        handler = new Handler();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
    	logger.d("onCreate");
        super.onCreate(savedInstanceState);
        lifecycle.onCreate();
        setStatusBarColor();
        
        getPresenter().onCreate();
    }

    @Override
    protected void onStart() {
    	logger.d("onStart");
        super.onStart();
        lifecycle.onStart();
        getPresenter().onStart();
    }

    @Override
    protected void onResume() {
    	logger.d("onResume");
        super.onResume();
        lifecycle.onResume();
        getPresenter().onResume();
    }

    @Override
    protected void onPause() {
    	logger.d("onPause");
        super.onPause();
        lifecycle.onPause();
        getPresenter().onPause();
    }

    @Override
    protected void onStop() {
    	logger.d("onStop");
        super.onStop();
        lifecycle.onStop();
        getPresenter().onStop();
    }

    @Override
    protected void onRestart() {
    	logger.d("onRestart");
        super.onRestart();
        lifecycle.onRestart();
        getPresenter().onRestart();
    }

    @Override
    protected void onDestroy() {
    	logger.d("onDestroy");
        super.onDestroy();
        lifecycle.onDestroy();
        getPresenter().onDestroy();
    }

    /**
     * 设置状态栏颜色
     */
    public void setStatusBarColor() {

    }
    
    /**
     * 设置生命周期回调
     */
    public void setActivityLifecycle(ActivityLifecycle lifecycle) {
    	this.lifecycle = (lifecycle != null ? lifecycle : LIFECYCLE);
    }
    
    /**
     * 获取当前Activity的对象
     */
    public final Activity getThisActivity(){
    	return this;
    }
    
    public final Handler getHandler() {
        return handler;
    }

	@SuppressLint("NewApi")
	@Override
    public void onBackPressed() {
        super.onBackPressed();
        logger.d("onBackPressed");
    }

    @Override
    public void finishActivity() {
    	logger.d("finishActivity");
        finish();
    }

    @Override
    public void onBackClick() {
    	logger.d("onBackClick");
        finish();
    }

    /**
     * 启动Activity
     * 
     * @param clazz 	Activity类
     * @param bundle 	携带的Bundle
     * @param isFinish  是否启动后销毁Activity
     */
    protected void startAct(Class<? extends Activity> clazz,
                            Bundle bundle,
                            boolean isFinish) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
        logger.d("启动Activity: ", clazz.getName());
        if (isFinish)
            super.finish();
    }

    @Override
    public void startAct(Class<? extends Activity> clazz) {
        startAct(clazz, null, false);
    }

    @Override
    public void startAct(Class<? extends Activity> clazz, Bundle bundle) {
        startAct(clazz, bundle, false);
    }

    @Override
    public void startActAfterFinish(Class<? extends Activity> clazz) {
        startAct(clazz, null, true);
    }

    @Override
    public void startActAfterFinish(Class<? extends Activity> clazz, Bundle bundle) {
        startAct(clazz, bundle, false);
    }

    /**
     * 启动Activity
     */
    @Override
    public void startActForResult(Class<? extends Activity> clazz, int requestCode) {
        startActForResult(clazz, requestCode, null);
    }

    /**
     * 启动Activity
     */
	@Override
    public void startActForResult(Class<? extends Activity> clazz,
                                  int requestCode,
                                  @Nullable Bundle bundle) {
        if (clazz == null)
            throw new IllegalArgumentException("Activity's class object is null.");

        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(@StringRes int strResId) {
        showToast(getString(strResId));
    }

    /**
     * 创建Presenter对象
     */
    @Nullable
    protected abstract P buildPresenter();

    /**
     * 获取Presenter
     */
    public P getPresenter() {
        if (presenter == null) {
            synchronized (lock) {
                if (presenter == null) {
                    presenter = buildPresenter();
                }
                
                if (presenter == null) {
                    presenter = getDefaultPresenter();
                }
            }
        }
        return presenter;
    }

    @SuppressWarnings("unchecked")
	public P getDefaultPresenter() {
        return (P) new ActivityPresenter<IView>(this) {
        };
    }

    /**
     * 设置是否调试
     */
    public void setDebug(boolean debug) {
        if (debug) {
            logger.setLevel(LogLevel.VERBOSE);
        } else {
            logger.setLevel(LogLevel.NONE);
        }
    }

    /**
     * 打印日志
     */
    @Deprecated
    public void log(Object...msg) {
        logger.log(LogLevel.DEBUG, msg);
    }
    
    /**
     * 获取View对象
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T findView(@IdRes int id) {
        return (T) this.findViewById(id);
    }

    /**
     * 获取View对象
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T findView(Activity activity, @IdRes int id) {
        return (T) activity.findViewById(id);
    }

    /**
     * 获取View对象
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T findView(View v, @IdRes int id) {
        return (T) v.findViewById(id);
    }

}


