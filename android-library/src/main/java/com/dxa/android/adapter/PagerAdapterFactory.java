package com.dxa.android.adapter;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

/**
 * Adapter工厂
 */
public class PagerAdapterFactory {

	private PagerAdapterFactory() {
		throw new IllegalAccessError("不允许直接new对象");
	}

	public static <V extends View> ViewPagerAdapter<V> newPagerAdapter(List<V> views) {
		return new ViewPagerAdapter<>(views);
	}


	public static <V extends View> ViewPagerAdapter<V> newPagerAdapter(
			List<V> views, List<? extends CharSequence> titles) {
		return new ViewPagerAdapter<>(views, titles);
	}

	public static <V extends View> ViewPagerAdapter<V> newLeftSlidePagerAdapter(List<V> views) {
		return new UnlimitedLeftSlidePagerAdapter<>(views);
	}

	public static <V extends View> ViewPagerAdapter<V> newLeftSlidePagerAdapter(
			List<V> views, List<? extends CharSequence> titles) {
		return new UnlimitedLeftSlidePagerAdapter<>(views, titles);
	}

	public static CommonFragmentPagerAdapter newCommonFragmentPagerAdapter(
			FragmentManager fm, List<Fragment> views) {
		return new CommonFragmentPagerAdapter<>(fm, views);
	}

	public static CommonFragmentPagerAdapter newCommonFragmentPagerAdapter(
			FragmentManager fm, List<Fragment> views, List<String> titles) {
		return new CommonFragmentPagerAdapter<>(fm, views, titles);
	}

	public static CommonFragmentPagerAdapter newCommonFragmentPagerAdapter(
			FragmentManager fm, List<Fragment> views, String[] titles) {
		return new CommonFragmentPagerAdapter<>(fm, views, titles);
	}
}
