package com.infinite.myapp.utils;

import android.util.Log;

import com.infinite.myapp.BuildConfig;
import com.infinite.myapp.config.AppConfig;


/**
 * 日志工具类
 */
public class MyLogger {

    public static void v(String msg) {
        if (BuildConfig.MY_LOG) {
            Log.v(AppConfig.APP_LOG_TAG, msg);
        }
    }

    public static void d(String msg) {
        if (BuildConfig.MY_LOG) {
            Log.d(AppConfig.APP_LOG_TAG, msg);
        }
    }

    public static void i(String msg) {
        if (BuildConfig.MY_LOG) {
            Log.i(AppConfig.APP_LOG_TAG, msg);
        }
    }

    public static void w(String msg) {
        if (BuildConfig.MY_LOG) {
            Log.w(AppConfig.APP_LOG_TAG, msg);
        }
    }

    public static void e(String msg) {
        if (BuildConfig.MY_LOG) {
            Log.e(AppConfig.APP_LOG_TAG, msg);
        }
    }

    public static void w(Throwable tr) {
        if (BuildConfig.MY_LOG) {
            Log.w(AppConfig.APP_LOG_TAG, Log.getStackTraceString(tr));
        }
    }

    public static void e(Throwable tr) {
        if (BuildConfig.MY_LOG)
            Log.e(AppConfig.APP_LOG_TAG, Log.getStackTraceString(tr));
    }

    public static void i(Throwable tr) {
        if (BuildConfig.MY_LOG) {
            Log.i(AppConfig.APP_LOG_TAG, Log.getStackTraceString(tr));
        }
    }

}
