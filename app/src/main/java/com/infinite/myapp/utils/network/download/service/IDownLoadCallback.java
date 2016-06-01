package com.infinite.myapp.utils.network.download.service;

/**
 * Created by guoning on 16-1-18.
 */
public interface IDownLoadCallback {

    void onStartDownload();

    void onStartDefaultSize(int size);

    void onDownLoadSuccess();

    void onDownloadFailed();

    void onDownloadSize(int size);
}
