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
    public static final String QQ_OPEN_PLATFORM_ID = "00000000000000";
    public static final String WX_APP_ID = "00000000000000";
    public static final String MCH_ID = "00000000000000";
    public static final String WX_AUTH_STATE = "00000000000000";
    public static final String WX_AUTH_SCOPE = "00000000000000";

    /*
     * log tag
     */
    public static final String APP_LOG_TAG = "[MyApp]";




    /*
     * Internal test url
     */
    public static final String APP_ROOT_URL = BuildConfig.API_HOST;

    // 在线缓存在1分钟内可读取
    public static final int ONLINE_CACHE_READ_TIMEOUT = 60;

    // 离线时缓存保存4周
    public static final int OFFLINE_CACHE_SAVE_TIMEOUT = 60 * 60 * 24 * 4 * 7;

    // 离线时缓存10M
    public static final int OFFLINE_CACHE_SIZE = 10 * 1024 * 1024;

    //okhttp授权user
    public static final String OKHTTP_AUTHORIZATION_USER = "13912345678";
}
