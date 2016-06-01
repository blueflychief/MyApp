package com.infinite.myapp.utils.networkutil.download;


import com.infinite.myapp.utils.databaseutil.DownLoadModel;


public interface DownLoadTaskListener {
    void onStart(DownLoadModel downLoadModel);

    void onCancel(DownLoadModel downLoadModel);

    void onDownLoading(long downSize);

    void onCompleted(DownLoadModel downLoadModel);

    void onError(DownLoadModel downLoadModel, Throwable throwable);
}
