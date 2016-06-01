package com.infinite.myapp.utils.networkutil.download;


public interface IDownLoadCallback {

    void onStartDownload();

    void onStartDefaultSize(int size);

    void onDownLoadSuccess();

    void onDownloadFailed();

    void onDownloadSize(int size);
}
