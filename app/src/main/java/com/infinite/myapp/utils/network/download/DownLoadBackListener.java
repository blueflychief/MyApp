package com.infinite.myapp.utils.network.download;

public interface DownLoadBackListener {
    void onStart(double percent);

    void onCancel();

    void onDownLoading(double percent);

    void onCompleted();

    void onError(Throwable throwable);
}
