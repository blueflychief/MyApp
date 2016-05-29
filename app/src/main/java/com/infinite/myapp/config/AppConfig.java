package com.infinite.myapp.config;

import com.infinite.myapp.BuildConfig;

/**
 * Created by Administrator on 2016-05-29.
 */
public class AppConfig {
    /***
     * 规则:
     * app_developing=true，属于开发阶段，只有开发人员能看到。
     * app_developed=true，属于测试阶段，测试和开发人员都能看到。
     * <p/>
     * 开发过程中，新功能和现有功能的入口相同时，新功能使用if(app_developing)来控制。
     * 开发完成并提交测试时，新功能使用if(app_developed)来控制。
     */
    public static final boolean APP_DEVELOPING = BuildConfig.DEVELOPING;
    public static final boolean APP_DEVELOPED = BuildConfig.DEVELOPED;

    //QQ登录平台ID
    public static final String QQ_OPEN_PLATFORM_ID = "1104753333";
    public static final String WX_APP_ID = "wx86ad26d7a2f37c7d";
    public static final String MCH_ID = "1267310001";
    public static final String WX_AUTH_STATE = "wechat_kk_auth";
    public static final String WX_AUTH_SCOPE = "snsapi_userinfo";

    /*
     * log tag
     */
    public static final String APP_LOG_TAG = "[MyApp]";


    /*
     * Internal test url
     */
    public static final String APP_ROOT_URL = BuildConfig.API_HOST;
}
