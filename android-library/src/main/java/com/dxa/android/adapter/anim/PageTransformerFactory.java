package com.dxa.android.adapter.anim;


import androidx.viewpager.widget.ViewPager;

/**
 * 生成动画的工厂类
 */

public class PageTransformerFactory {

    public static ViewPager.PageTransformer newZoomOutPageTransformer(){
        return new ZoomOutPageTransformer();
    }

    public static ViewPager.PageTransformer newDepthPageTransformer(){
        return new DepthPageTransformer();
    }

}
