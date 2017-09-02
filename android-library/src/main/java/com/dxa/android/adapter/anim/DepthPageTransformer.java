package com.dxa.android.adapter.anim;

import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

/**
 * @description: 这个Page Transformer使用默认动画的屏幕左滑动画。但是为右滑使用一种“潜藏”效
 * 果的动画。潜藏动画将page淡出，并且线性缩小它。
 * <p/>
 * {@link http://hukai.me/android-training-course-in-chinese/animations/screen-slide.html#Depth Page Transformer}
 */

class DepthPageTransformer implements PageTransformer {
    private static final float MIN_SCALE = 0.75f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
            view.setAlpha(1);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);

        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            view.setAlpha(1 - position);

            // Counteract the default slide transition
            view.setTranslationX(pageWidth * -position);

            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}