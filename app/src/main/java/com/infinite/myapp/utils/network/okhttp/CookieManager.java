package com.infinite.myapp.utils.network.okhttp;


import com.infinite.myapp.utils.network.KKNetWorkRequest;
import com.infinite.myapp.utils.network.okhttp.cookie.PersistentCookieStore;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class CookieManager implements CookieJar {

    private final PersistentCookieStore cookieStore = new PersistentCookieStore(KKNetWorkRequest.getInstance().sContext.getApplicationContext());

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }


//    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
//
//    @Override
//    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//        cookieStore.put(url, cookies);
//    }
//
//    @Override
//    public List<Cookie> loadForRequest(HttpUrl url) {
//        List<Cookie> cookies = cookieStore.get(url);
//        return cookies != null ? cookies : new ArrayList<Cookie>();
//    }
}
