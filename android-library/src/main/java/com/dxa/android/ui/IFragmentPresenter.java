package com.dxa.android.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dxa.android.ui.handler.AsyncHandler;

/**
 * Fragment的Presenter
 */
public interface IFragmentPresenter<V extends IView> extends IPresenter<V> {

    /**
     * BEGIN *****************  与Fragment生命周期函数一致
     ********************/

    void onAttach();

    void onActivityCreated(@Nullable Bundle savedInstanceState);

    @Override
    void onCreate();

    void onViewCreated(@Nullable Bundle savedInstanceState);

    @Override
    void onStart();

    @Override
    void onResume();

    @Override
    void onPause();

    @Override
    void onStop();

    @Override
    void onRestart();

    void onDestroyView();

    @Override
    void onDestroy();

    void onDetach();

    /**
     * END *****************  与Fragment生命周期函数一致 ********************/

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onSaveInstanceState(Bundle outState);

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

    void onConfigurationChanged(Configuration newConfig);

    @Override
    V getView();

    @Override
    AsyncHandler getAsyncHandler();

    @Override
    Handler getSyncHandler();

    @Override
    Context getContext();

}
