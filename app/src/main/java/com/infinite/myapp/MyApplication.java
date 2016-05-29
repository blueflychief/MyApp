package com.infinite.myapp;

import android.app.Application;
import android.content.Context;

import com.infinite.myapp.config.AppConfig;
import com.infinite.myapp.utils.network.Excalibur;


/**
 * Created by Administrator on 2016-05-29.
 */
public class MyApplication extends Application {

    private static MyApplication sINSTANCE;

    public static String S_REGISTION_ID = "";

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
        Excalibur.getInstance().init(this, AppConfig.APP_ROOT_URL);
//        //JPush config
//        initJPushConfig();
//        LeakCanary.install(this);
//        MyLogger.e("UserAgent-->>" + DeviceUtil.getUser_Agent());
    }

//    private void initJPushConfig() {
//        // 使用模拟器时注释掉这两句，否则崩溃。设置开启日志,发布时请关闭日志
//        JPushInterface.setDebugMode(BuildConfig.KK_LOG);
//        // 初始化 JPush
//        JPushInterface.init(this);
//    }

}
