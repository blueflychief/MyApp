package com.infinite.myapp.utils.networkutil.download;


public interface DownLoadBackListener {
    void onStart(double percent);

    void onCancel();

    void onDownLoading(double percent);

    void onCompleted();

    void onError(Throwable throwable);
}
