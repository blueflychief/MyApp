package com.infinite.myapp.utils.network;

public interface KKNetworkResponse {
    /**
     * 服务器返回成功回调
     *
     * @param baseEntity 网络请求返信息
     */
    void onDataReady(BaseEntity baseEntity);

    /**
     * 调用失败回调
     */
    void onDataError(int requestCode, String message);
}
