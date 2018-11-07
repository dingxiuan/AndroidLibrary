package com.dxa.android.adapter;

import android.view.View;

import java.util.List;

/**
 * 无限左划的PagerAdapter
 */
class UnlimitedLeftSlidePagerAdapter<V extends View> extends ViewPagerAdapter<V> {

    public UnlimitedLeftSlidePagerAdapter(List<V> views) {
        super(views);
    }

    public UnlimitedLeftSlidePagerAdapter(List<V> views, List<? extends CharSequence> titles) {
        super(views, titles);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        position %= getTitleSize();
        return super.getPageTitle(position);
    }

    @Override
    public V getItemView(int position) {
        position %= getViewSize();
        return super.getItemView(position);
    }

}
