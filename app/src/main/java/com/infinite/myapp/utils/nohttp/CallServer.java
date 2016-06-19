/*
 * Copyright 2015 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.infinite.myapp.utils.nohttp;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.download.DownloadQueue;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

import java.net.InetSocketAddress;
import java.net.Proxy;

import javax.net.ssl.SSLContext;


/**
 * Created in Oct 23, 2015 1:00:56 PM.
 *
 * @author Yan Zhenjie.
 */
public class CallServer {

    public enum RequestType {
        STRING_REQUEST,
        IMAGE_REQUEST,
    }


    private static CallServer callServer;

    /**
     * 请求队列.
     */
    private RequestQueue requestQueue;

    /**
     * 下载队列.
     */
    private static DownloadQueue downloadQueue;

    private CallServer() {
        requestQueue = NoHttp.newRequestQueue();
    }

    /**
     * 请求队列.
     */
    public synchronized static CallServer getRequestInstance() {
        if (callServer == null)
            callServer = new CallServer();
        return callServer;
    }

    /**
     * 下载队列.
     */
    public static DownloadQueue getDownloadInstance() {
        if (downloadQueue == null)
            downloadQueue = NoHttp.newDownloadQueue();
        return downloadQueue;
    }


    /**
     * 普通get请求
     *
     * @param context
     * @param what
     * @param callback
     * @param canCancel
     * @param isLoading
     * @param url
     * @param parmas
     * @param <T>
     */
    public <T> Request<T> httpGet(Context context, int what, HttpListener<T> callback, boolean canCancel, boolean isLoading, String url, String... parmas) {
        Request request = parseParams(url, RequestType.STRING_REQUEST, RequestMethod.GET, parmas);
        requestQueue.add(what, request, new HttpResponseListener<T>(context, request, callback, canCancel, isLoading));
        return request;
    }

    /**
     * 普通 post请求
     *
     * @param context
     * @param what
     * @param callback
     * @param canCancel
     * @param isLoading
     * @param url
     * @param parmas
     * @param <T>
     */
    public <T> Request<T> httpPost(Context context, int what, HttpListener<T> callback, boolean canCancel, boolean isLoading, String url, String... parmas) {
        Request request = parseParams(url, RequestType.STRING_REQUEST, RequestMethod.POST, parmas);
        requestQueue.add(what, request, new HttpResponseListener<T>(context, request, callback, canCancel, isLoading));
        return request;
    }


    /**
     * 请求Bitmap
     *
     * @param context
     * @param what
     * @param callback
     * @param method
     * @param canCancel
     * @param isLoading
     * @param url
     * @return
     */
    public Request<Bitmap> httpRequestImage(Context context, int what, HttpListener<Bitmap> callback, RequestMethod method, boolean canCancel, boolean isLoading, String url) {
        return httpRequestImage(context, what, callback, method, canCancel, isLoading, 1000, 1000, Bitmap.Config.ARGB_8888, ImageView.ScaleType.CENTER_INSIDE, url);

    }


    /**
     * 自定义图片参数请求Bitmap
     *
     * @param context
     * @param what
     * @param callback
     * @param method
     * @param canCancel
     * @param isLoading
     * @param maxWidth
     * @param maxHeight
     * @param config
     * @param scaleType
     * @param url
     * @return
     */
    public Request<Bitmap> httpRequestImage(Context context, int what, HttpListener<Bitmap> callback, RequestMethod method, boolean canCancel, boolean isLoading,
                                            int maxWidth, int maxHeight, Bitmap.Config config, ImageView.ScaleType scaleType,
                                            String url) {
        Request<Bitmap> request = NoHttp.createImageRequest(url, method, maxWidth, maxHeight, config, scaleType);
        requestQueue.add(what, request, new HttpResponseListener<Bitmap>(context, request, callback, canCancel, isLoading));
        return request;
    }


    /**
     * 代理请求
     *
     * @param context
     * @param what
     * @param callback
     * @param canCancel
     * @param method
     * @param isLoading
     * @param url
     * @param proxyUrl
     * @param port
     * @param parmas
     * @param <T>
     * @return
     */
    public <T> Request<T> httpProxy(Context context, int what, HttpListener<T> callback, RequestMethod method, boolean canCancel, boolean isLoading, String url, String proxyUrl, int port, String... parmas) {
        Request request = parseParams(url, RequestType.STRING_REQUEST, method, parmas);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyUrl, port));
        request.setProxy(proxy);
        requestQueue.add(what, request, new HttpResponseListener<T>(context, request, callback, canCancel, isLoading));
        return request;
    }


    /**
     * 带参数请求Bitmap
     *
     * @param context
     * @param what
     * @param callback
     * @param canCancel
     * @param isLoading
     * @param url
     * @param parmas
     * @return
     */
    public Request<Bitmap> httpGetImage(Context context, int what, HttpListener<Bitmap> callback, boolean canCancel, boolean isLoading, String url, String... parmas) {
        Request request = parseParams(url, RequestType.IMAGE_REQUEST, RequestMethod.GET, parmas);
        requestQueue.add(what, request, new HttpResponseListener<Bitmap>(context, request, callback, canCancel, isLoading));
        return request;
    }


    /**
     * Https请求，带证书。
     * 需要在SSLContextUtil配置证书
     */
    private <T> void httpsPostWithVerify(Context context, int what, HttpListener<T> callback, boolean canCancel, boolean isLoading, String url, String... parmas) {
        Request httpsRequest = parseParams(url, RequestType.STRING_REQUEST, RequestMethod.POST, parmas);
        SSLContext sslContext = SSLContextUtil.getSSLContext();
        if (sslContext != null)
            httpsRequest.setSSLSocketFactory(sslContext.getSocketFactory());
        requestQueue.add(what, httpsRequest, new HttpResponseListener(context, httpsRequest, callback, canCancel, isLoading));
    }

    /**
     * Https请求，不带证书。
     * 需要在SSLContextUtil配置证书
     */
    private <T> void httpsPostWithoutVerify(Context context, int what, HttpListener<T> callback, boolean canCancel, boolean isLoading, String url, String... parmas) {
        Request<String> httpsRequest = parseParams(url, RequestType.STRING_REQUEST, RequestMethod.POST, parmas);
        SSLContext sslContext = SSLContextUtil.getDefaultSLLContext();
        if (sslContext != null)
            httpsRequest.setSSLSocketFactory(sslContext.getSocketFactory());
        httpsRequest.setHostnameVerifier(SSLContextUtil.HOSTNAME_VERIFIER);
        requestQueue.add(what, httpsRequest, new HttpResponseListener(context, httpsRequest, callback, canCancel, isLoading));
    }

    @NonNull
    private Request parseParams(String url, RequestType type, RequestMethod methed, String[] parmas) {
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("url error!url can't be null");
        }
        Request request = null;
        switch (type) {
            case STRING_REQUEST:
                request = NoHttp.createStringRequest(url, methed);
                break;

            case IMAGE_REQUEST:
                request = NoHttp.createImageRequest(url, methed);
                break;
        }

        if (parmas != null) {
            if (parmas.length % 2 == 0) {
                for (int i = 0; i < parmas.length; i = i + 2) {
                    String key = parmas[i];
                    String value = parmas[i + 1];
                    request.add(key, value);
                }
            } else {
                throw new IllegalArgumentException("parmas error!parmas must be in pairs");
            }
        }
        return request;
    }


    /**
     * 添加一个请求到请求队列.
     *
     * @param context   context用来实例化dialog.
     * @param what      用来标志请求, 当多个请求使用同一个{@link HttpListener}时, 在回调方法中会返回这个what.
     * @param request   请求对象.
     * @param callback  结果回调对象.
     * @param canCancel 是否允许用户取消请求.
     * @param isLoading 是否显示dialog.
     */
    public <T> void add(Context context, int what, Request<T> request, HttpListener<T> callback, boolean canCancel, boolean isLoading) {
        requestQueue.add(what, request, new HttpResponseListener<T>(context, request, callback, canCancel, isLoading));
    }

    /**
     * 取消这个sign标记的所有请求.
     */
    public void cancelBySign(Object sign) {
        requestQueue.cancelBySign(sign);
    }

    /**
     * 取消队列中所有请求.
     */
    public void cancelAll() {
        requestQueue.cancelAll();
    }

    /**
     * 退出app时停止所有请求.
     */
    public void stopAll() {
        requestQueue.stop();
    }


}
