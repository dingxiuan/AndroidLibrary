package com.dxa.android.adapter;


import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * 默认的OnPageChangeListener实现
 */
public class OnPagerChangedListenerImpl implements ViewPager.OnPageChangeListener {

    protected PagerAdapter adapter;

    public OnPagerChangedListenerImpl() {
    }

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

    public PagerAdapter getAdapter() {
        return adapter;
    }
}
