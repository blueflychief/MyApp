package com.infinite.myapp.utils;

import android.view.View;

import java.util.Calendar;

/**
 * 防止快速点击，
 * 所有View的点击事件都继承这个类即可
 */
public abstract class KKClickListener implements View.OnClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 500;
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onKKClick(v);
        }
    }

    protected abstract void onKKClick(View v);
}