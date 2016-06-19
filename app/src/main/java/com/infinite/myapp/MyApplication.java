package com.infinite.myapp;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;


/**
 * Created by Administrator on 2016-05-29.
 */
public class MyApplication extends Application {

    private static MyApplication sINSTANCE;

    public static String S_REGISTION_ID = "";

    private float oldFontScale;
    private Locale oldLocale;

    public static synchronized MyApplication getInstance() {
        return sINSTANCE;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sINSTANCE = this;

        oldFontScale = getResources().getConfiguration().fontScale;
        oldLocale = getResources().getConfiguration().locale;
//        //JPush config
//        initJPushConfig();
//        LeakCanary.install(this);
//        MyLogger.e("UserAgent-->>" + DeviceUtil.getUser_Agent());
    }

    /**
     * 修改系统字号和地区时，会导致crash
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        if (oldFontScale != newConfig.fontScale || !oldLocale.equals(newConfig.locale)) {
//            System.exit(0);
//        }
    }
}
