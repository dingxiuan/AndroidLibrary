package com.dxa.android.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;
import java.util.List;

/**
 * PagerAdapter的基类，主要操作View
 */

public class ViewPagerAdapter<V extends View> extends PagerAdapter {

    private List<V> views;
    private List<? extends CharSequence> titles;

    public ViewPagerAdapter(List<V> views) {
        this.views = views;
    }

    public ViewPagerAdapter(List<V> views, List<? extends CharSequence> titles) {
        if (views.size() != titles.size()) {
            throw new IllegalArgumentException("The view's size not equals title's size");
        }
        this.views = views;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View v, Object o) {
        return v == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        V itemView = getItemView(position);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(getItemView(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return notEmpty(titles) ? titles.get(position) : null;
    }

    /**
     * 根据位置获得一个Item
     */
    public V getItemView(int position) {
        return views.get(position);
    }

    /**
     * 获取整个的View集合
     */
    protected int getViewSize() {
        return views.size();
    }

    protected int getTitleSize() {
        return titles.size();
    }

    protected <T extends Collection<?>> boolean notEmpty(T t) {
        return t != null && t.size() > 0;
    }

}
