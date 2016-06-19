package com.infinite.myapp.controller;

import android.content.Context;

import com.infinite.myapp.config.CommonConstants;
import com.infinite.myapp.utils.nohttp.CallServer;
import com.infinite.myapp.utils.nohttp.HttpListener;

/**
 * Created by Administrator on 2016-06-18.
 */
public class LoginController {

    public LoginController() {
    }

    private static class LoginHolder {
        private static final LoginController INSTANCE = new LoginController();

    }

    public static LoginController getInstance() {
        return LoginHolder.INSTANCE;
    }


    public void onTeleLogin(Context context) {
        CallServer.getRequestInstance().httpGet(context, CommonConstants.HTTP_TYPE_LOGIN,mListener,true,true,CommonConstants.HTTP_URL_LOGIN);
    }


    private HttpListener mListener=new HttpListener() {

        @Override
        public void onSucceed(int what, String response) {

        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

        }
    };
}
