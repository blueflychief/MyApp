package com.infinite.myapp.utils.network;

import android.content.Context;

import com.infinite.myapp.utils.network.okhttp.AuthenticatorManager;
import com.infinite.myapp.utils.network.okhttp.CommonInterceptor;
import com.infinite.myapp.utils.network.okhttp.CookieManager;
import com.infinite.myapp.utils.network.okhttp.LoggingInterceptor;
import com.infinite.myapp.utils.network.retrofit.FastJsonConverterFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class KKNetWorkRequest {

    public static final String TAG = KKNetWorkRequest.class.getSimpleName();

    private KKNetWorkRequest() {

    }

    private static KKNetWorkRequest sInstance = new KKNetWorkRequest();

    public static KKNetWorkRequest getInstance() {
        return sInstance;
    }

    private Retrofit mRetrofit;
    private ApiService mApiService;

    private Map<String, Map<Integer, Call>> mRequestMap = new ConcurrentHashMap<>();

    public Context sContext;

    /**
     * 初始化Retrofit
     *
     * @param context
     */
    public void init(Context context, String baseurl) {
        sContext = context;
        synchronized (KKNetWorkRequest.this) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .cache(new Cache(new File(context.getExternalCacheDir(), "http_cache"), 1024 * 1024 * 100))
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .addInterceptor(new CommonInterceptor())
                    .addInterceptor(new LoggingInterceptor())
                    .cookieJar(new CookieManager())
                    .authenticator(new AuthenticatorManager())
                    .build();
            mRetrofit = new Retrofit.Builder()
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .baseUrl(baseurl)//主机地址
                    .client(okHttpClient)
                    .build();
            mApiService = mRetrofit.create(ApiService.class);
        }
    }


    public ApiService getApiService() {
        return mApiService;
    }

    /**
     * 异步请求
     *
     * @param TAG
     * @param requestCall
     * @param responseListener
     * @param <T>
     * @return
     */
    public <T extends BaseEntity> void asyncNetWork(final String TAG, final int requestCode, final Call<T> requestCall, final KKNetworkResponse responseListener) {
        if (responseListener == null) {
            return;
        }

        Call<T> call = null;

        if (requestCall.isExecuted()) {
            call = requestCall.clone();
        } else {
            call = requestCall;
        }
        addCall(TAG, requestCode, call);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    BaseEntity result = response.body();
                    result.requestCode = requestCode;
                    result.serverTip = response.message();
                    result.responseCode = response.code();
                    responseListener.onDataReady(result);
                } else {
                    responseListener.onDataError(requestCode, response.message());
                }
                removeCall(TAG, requestCode);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                responseListener.onDataError(requestCode, t.getMessage());

                removeCall(TAG, requestCode);
            }
        });
    }

    /**
     * 同步请求
     *
     * @param TAG
     * @param requestCall
     * @param responseListener
     * @param <T>
     * @return
     */
    public <T extends BaseEntity> void syncNetWork(final String TAG, final int requestCode, final Call<T> requestCall, final KKNetworkResponse responseListener) {
        if (responseListener == null) {
            return;
        }
        Call call = null;
        try {
            if (requestCall.isExecuted()) {
                call = requestCall.clone();
            } else {
                call = requestCall;
            }

            Response<T> response = call.execute();
            addCall(TAG, requestCode, call);
            if (response.isSuccessful()) {
                BaseEntity result = response.body();
                result.responseCode = response.code();
                result.serverTip = response.message();
                result.requestCode = requestCode;
                responseListener.onDataReady(result);
            } else {
                responseListener.onDataError(requestCode, response.message());
            }
        } catch (IOException e) {
            responseListener.onDataError(requestCode, e.getMessage());
            e.printStackTrace();
        } finally {
            removeCall(TAG, requestCode);
        }
    }

    /**
     * 添加call到Map
     *
     * @param TAG
     * @param call
     */
    private void addCall(String TAG, Integer code, Call call) {
        if (TAG == null) {
            return;
        }
        if (mRequestMap.get(TAG) == null) {
            Map<Integer, Call> map = new ConcurrentHashMap<>();
            map.put(code, call);
            mRequestMap.put(TAG, map);
        } else {
            mRequestMap.get(TAG).put(code, call);
        }
    }

    /**
     * 取消某个call
     *
     * @param TAG
     * @param code
     */
    public boolean removeCall(String TAG, Integer code) {
        if (TAG == null) {
            return false;
        }
        Map<Integer, Call> map = mRequestMap.get(TAG);
        if (map == null) {
            return false;
        }
        if (code == null) {
            //取消整个context请求
            Iterator iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                Integer key = (Integer) iterator.next();

                Call call = map.get(key);
                if (call == null) {
                    continue;
                }
                call.cancel();
            }
            mRequestMap.remove(TAG);
            return false;
        } else {
            //取消一个请求
            if (map.containsKey(code)) {
                Call call = map.get(code);
                if (call != null) {
                    call.cancel();
                }
                map.remove(code);
            }
            if (map.size() == 0) {
                mRequestMap.remove(TAG);
                return false;
            }
        }
        return true;
    }

    /**
     * 取消整个tag请求，关闭页面时调用
     *
     * @param TAG
     */
    public void removeTagCall(String TAG) {
        removeCall(TAG, null);
    }
}
