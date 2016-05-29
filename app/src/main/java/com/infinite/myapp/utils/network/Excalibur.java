package com.infinite.myapp.utils.network;

import android.content.Context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Excalibur {

    private Map<String, Scheme> mSchemeMap = new ConcurrentHashMap<>();

    private static Excalibur sInstance = new Excalibur();

    private Excalibur() {
    }

    public static Excalibur getInstance() {
        return sInstance;
    }

    public void init(Context context, String baseUrl) {
        KKNetWorkRequest.getInstance().init(context, baseUrl);
    }


    public void subscribe(String[] modules) {
        if (modules == null || modules.length <= 0) {
            throw new IllegalArgumentException("modules is not available");
        }
        for (String module : modules) {
            if (!mSchemeMap.containsKey(module)) {
                mSchemeMap.put(module, new Scheme(module));
            }
        }
    }

    public void get(String module, String method, Object... params) {
        if (mSchemeMap.containsKey(module)) {
            mSchemeMap.get(module).get(method, params);
        } else {
            throw new IllegalArgumentException(module + "module not subscribe");
        }
    }

    public void unSubscribe(String[] modules) {
        if (modules == null || modules.length <= 0) {
            throw new IllegalArgumentException("modules is not available");
        }
        for (String module : modules
                ) {
            if (mSchemeMap.containsKey(module)) {
                mSchemeMap.get(module).cancelSchemeNetWork();
                mSchemeMap.remove(module);
            } else {
                throw new IllegalArgumentException(module + "module not subscribe");
            }
        }
    }

    public void cancelNetWork(String tag) {
        KKNetWorkRequest.getInstance().removeTagCall(tag);
    }

    public void cancelNetWork(String tag, int requestCode) {
        KKNetWorkRequest.getInstance().removeCall(tag, requestCode);
    }
}
