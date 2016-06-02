package com.aspsine.multithreaddownload.architecture;

/**
 * Created by Aspsine on 2015/10/29.
 */
public interface IDownloader {

    public interface IDownloaderDestroyedListener {
        void onDestroyed(String key, IDownloader downloader);
    }

    boolean isRunning();

    void start();

    void pause();

    void cancel();

    void onDestroy();

}
