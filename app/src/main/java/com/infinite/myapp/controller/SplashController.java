package com.infinite.myapp.controller;

import android.content.Context;

/**
 * Created by Administrator on 2016-06-18.
 */
public class SplashController {

    public SplashController() {
    }

    private static class SplashHolder {
        private static final SplashController INSTANCE = new SplashController();

    }

    public static SplashController getInstance() {
        return SplashHolder.INSTANCE;
    }


    public void getAdInfo(Context context) {
//        CallServer.getRequestInstance().httpGet(context, )
    }
}
