package com.aspsine.multithreaddownload.architecture;

import com.aspsine.multithreaddownload.DownloadException;

/**
 *
 * 连接任务接口
 */
public interface IConnectTask extends Runnable {

    public interface IConnectListener {
        void onConnecting();

        void onConnected(long connect_time, long length, boolean isAcceptRanges);

        void onConnectCanceled();

        void onConnectFailed(DownloadException de);
    }

    void cancel();

    boolean isConnecting();

    boolean isConnected();

    boolean isCanceled();

    boolean isFailed();

    @Override
    void run();
}
