package com.dxa.android.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.dxa.android.logger.DLogger;
import com.dxa.android.logger.LogLevel;
import com.dxa.android.ui.handler.AsyncHandler;


/**
 * Fragment的Presenter
 */


public abstract class FragmentPresenter<V extends IView>
        implements IFragmentPresenter<V> {

    protected final DLogger logger = new DLogger("FragmentPresenter");
    private V baseView;

    public FragmentPresenter(V baseView) {
        this.baseView = baseView;
        String tag = getClass().getSimpleName();
        this.logger.setTag(tag);
    }

    @Override
    public void onAttach() {
        // NOTHING DONE !
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // NOTHING DONE !
    }

    @Override
    public void onCreate() {
        // NOTHING DONE !
    }

    @Override
    public void onViewCreated(@Nullable Bundle savedInstanceState) {
        // NOTHING DONE !
    }

    @Override
    public void onStart() {
        // NOTHING DONE !
    }

    @Override
    public void onResume() {
        // NOTHING DONE !
    }

    @Override
    public void onPause() {
        // NOTHING DONE !
    }

    @Override
    public void onStop() {
        // NOTHING DONE !
    }

    @Override
    public void onRestart() {
        // NOTHING DONE !
    }

    @Override
    public void onDestroyView() {
        // NOTHING DONE !
    }

    @Override
    public void onDestroy() {
        // NOTHING DONE !
    }

    @Override
    public void onDetach() {
        // NOTHING DONE !
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // NOTHING DONE !
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // NOTHING DONE !
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // NOTHING DONE !
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // NOTHING DONE !
    }

    @Override
    public V getView() {
        return baseView;
    }

    @Override
    public AsyncHandler getAsyncHandler() {
        return getView().getAsyncHandler();
    }

    @Override
    public Handler getSyncHandler() {
        return getView().getSyncHandler();
    }

    @Override
    public Context getContext() {
        return getView().getContext();
    }

    protected void run() {
        // NOTHING DONE !
    }

    public void setDebug(boolean debug) {
        logger.setLevel(debug ? LogLevel.DEBUG : LogLevel.NONE);
    }

    public Resources getResources() {
        return baseView.getResources();
    }

    @SuppressWarnings("deprecation")
    public Drawable getDrawable(@DrawableRes int drawId) {
        return getResources().getDrawable(drawId);
    }

    @SuppressWarnings("deprecation")
    public int getColor(@ColorRes int id) {
        return getResources().getColor(id);
    }

    public String getString(@StringRes int id) {
        return baseView.getContext().getString(id);
    }
}
