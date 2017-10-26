package com.dxa.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dxa.android.logger.DLogger;
import com.dxa.android.logger.LogLevel;

/**
 * Fragment基类
 */
public abstract class SuperFragment<P extends IFragmentPresenter>
		extends Fragment implements IView {

	private final Object lock = new Object();
	/**
	 * 打印日志
	 */
	protected DLogger logger = new DLogger("Fragment");

	private P presenter;

	public SuperFragment() {
		String tag = getClass().getSimpleName();
		logger.setTag(tag);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onAttach(Activity activity) {
		logger.d("onAttach");
		super.onAttach(activity);
		getPresenter().onAttach();
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		logger.d("onCreate");
		super.onCreate(savedInstanceState);
		getPresenter().onCreate();
	}

	@Nullable
	@Override
	public abstract View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState);
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		logger.d("onViewCreated");
		getPresenter().onViewCreated(savedInstanceState);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		logger.d("onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		getPresenter().onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		logger.d("onStart");
		super.onStart();
		getPresenter().onStart();
	}

	@Override
	public void onResume() {
		logger.d("onResume");
		super.onResume();
		getPresenter().onResume();
	}

	@Override
	public void onPause() {
		logger.d("onPause");
		super.onPause();
		getPresenter().onPause();
	}

	@Override
	public void onStop() {
		logger.d("onStop");
		super.onStop();
		getPresenter().onStop();
	}

	@Override
	public void onDestroyView() {
		logger.d("onDestroyView");
		super.onDestroyView();
		getPresenter().onDestroyView();
	}

	@Override
	public void onDestroy() {
		logger.d("onDestroy");
		super.onDestroy();
		getPresenter().onDestroy();
	}

	@Override
	public void onDetach() {
		logger.d("onDetach");
		super.onDetach();
		getPresenter().onDetach();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		logger.d("onActivityResult");
		getPresenter().onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		logger.d("onSaveInstanceState");
		getPresenter().onSaveInstanceState(outState);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
			@NonNull String[] permissions, @NonNull int[] grantResults) {
		logger.d("onRequestPermissionsResult");
		getPresenter().onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		logger.d("onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
		getPresenter().onConfigurationChanged(newConfig);
	}

	@Override
	public void showToast(String msg) {
		Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
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
		return (P) new FragmentPresenter<IView>(this) {
		};
	}

	@Override
	public void finishActivity() {
		log("finishActivity()");
		if (getActivity() != null)
			getActivity().finish();
	}

	@Override
	public void startAct(Class<? extends Activity> clazz) {
		FragmentActivity activity = getActivity();
		Intent intent = new Intent(activity, clazz);
		startActivity(intent);
	}

	@Override
	public void startAct(Class<? extends Activity> clazz, Bundle bundle) {
		FragmentActivity activity = getActivity();
		Intent intent = new Intent(activity, clazz);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	public void startActAfterFinish(Class<? extends Activity> clazz) {
		startAct(clazz);
		getActivity().finish();
	}

	@Override
	public void startActAfterFinish(Class<? extends Activity> clazz,
			Bundle bundle) {
		startAct(clazz, bundle);
		getActivity().finish();
	}

	@Override
	public void startActForResult(Class<? extends Activity> clazz,
			int requestCode) {
		startActForResult(clazz, requestCode, null);
	}

	@Override
	public void startActForResult(Class<? extends Activity> clazz,
			int requestCode, Bundle bundle) {
		Intent intent = new Intent(getActivity(), clazz);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	public void onBackClick() {
		getActivity().finish();
	}

	/**
	 * 获取View对象
	 */
	public static <T extends View> T findView(View v, @IdRes int id) {
		return (T) v.findViewById(id);
	}

	/**********************************************/

	/**
	 * 打印日志
	 */
	protected void log(String msg) {
		logger.i(msg);
	}

	/**
	 * 设置是否打印日志
	 */
	public void setDebug(boolean debug) {
		if (debug) {
			logger.setLevel(LogLevel.DEBUG);
		} else {
			logger.setLevel(LogLevel.NONE);
		}
	}
}