package com.dxa.android.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * FragmentPagerAdapter的实现
 */

class CommonFragmentPagerAdapter<T extends Fragment> extends FragmentPagerAdapter {

	private List<String> titles;
	private List<T> fragments;

	public CommonFragmentPagerAdapter(FragmentManager fm, List<T> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	public CommonFragmentPagerAdapter(FragmentManager fm, List<T> fragments, String[] titles) {
		this(fm, fragments, Arrays.asList(titles));
	}

	public CommonFragmentPagerAdapter(FragmentManager fm, List<T> fragments, List<String> titles) {
		super(fm);
		if (fragments.size() != titles.size()) {
			throw new IllegalArgumentException("Page's size not equals title's size");
		}
		this.fragments = fragments;
		this.titles = titles;
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return notEmpty(titles) ? titles.get(position) : null;
	}

	private <T extends Collection<?>> boolean notEmpty(T t) {
		return t != null && t.size() > 0;
	}

}
