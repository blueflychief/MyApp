package com.aspsine.multithreaddownload.architecture;

import com.aspsine.multithreaddownload.DownloadException;

/**
 * Created by Aspsine on 2015/7/22.
 */
public interface IDownloadTask extends Runnable {

    interface OnDownloadListener {
        void onDownloadConnecting();

        void onDownloadProgress(int thread_id,long thread_finished,long all_finished, long length);

        void onDownloadCompleted();

        void onDownloadPaused();

        void onDownloadCanceled();

        void onDownloadFailed(DownloadException de);
    }

    void cancel();

    void pause();

    boolean isDownloading();

    boolean isComplete();

    boolean isPaused();

    boolean isCanceled();

    boolean isFailed();

    @Override
    void run();
}
