package com.dxa.android.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by Administrator on 2016/12/26.
 */

public class OnPagerChangedListenerImpl implements ViewPager.OnPageChangeListener {

    protected PagerAdapter adapter;

    public OnPagerChangedListenerImpl(PagerAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
