package com.infinite.myapp.utils.networkutil;


public class BaseEntity {
    /**
     * 接口请求requestCode,用于区分多个请求同时发起的情况
     */
    public int requestCode;

    /**
     * 接口请求返回的状态码
     */
    public int responseCode;

    /**
     * 接口请求返回的提示信息
     */
    public String serverTip;
}
