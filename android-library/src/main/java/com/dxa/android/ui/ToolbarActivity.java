package com.dxa.android.ui;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;

import com.dxa.android.R;

/**
 * ToolbarActivity
 */

public abstract class ToolbarActivity<P extends ActivityPresenter<IView>> extends SuperActivity<P> {

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View containerParent = getLayoutInflater().inflate(layoutResID, null);
        this.setContentView(containerParent);
    }

    @Override
    public void setContentView(View containerParent) {
        int toolbarID = containerParent.getResources()
                .getIdentifier("toolbar", "id", getPackageName());
        // 设置Toolbar
        Toolbar toolbar = (Toolbar) findViewById(toolbarID);
        if (toolbar == null) {
            LinearLayout linearLayout = new LinearLayout(this);
            LinearLayoutCompat.LayoutParams layoutParams =
                    new LinearLayoutCompat.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
            linearLayout.setLayoutParams(layoutParams);

            linearLayout.setOrientation(LinearLayout.VERTICAL);
            toolbar = new Toolbar(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toolbar.setPopupTheme(android.R.style.Widget_Material_PopupMenu_Overflow);
            } else {
                toolbar.setPopupTheme(android.R.style.Widget_ActionButton_Overflow);
            }
            linearLayout.addView(toolbar);
            linearLayout.addView(containerParent);
            containerParent = linearLayout;
        }

        toolbar.setBackgroundColor(Color.parseColor("#303F9F"));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        super.setContentView(containerParent);
    }
}
