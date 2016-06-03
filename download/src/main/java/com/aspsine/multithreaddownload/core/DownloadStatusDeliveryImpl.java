package com.aspsine.multithreaddownload.core;

import android.os.Handler;

import com.aspsine.multithreaddownload.IDownloadCallBack;
import com.aspsine.multithreaddownload.DownloadException;
import com.aspsine.multithreaddownload.architecture.DownloadStatus;
import com.aspsine.multithreaddownload.architecture.DownloadStatusDelivery;

import java.util.concurrent.Executor;

/**
 * 下载状态回调
 */
public class DownloadStatusDeliveryImpl implements DownloadStatusDelivery {
    private Executor mDownloadStatusPoster;

    public DownloadStatusDeliveryImpl(final Handler handler) {

        //初始化时execute不执行，当DownloadResponseImpl调用mDelivery.post(mDownloadStatus)方法时，执行第30行的post()方法，调用execute()，然后就走run();
        mDownloadStatusPoster = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
    }

    @Override
    public void post(DownloadStatus status) {
        mDownloadStatusPoster.execute(new DownloadStatusDeliveryRunnable(status));
    }

    private static class DownloadStatusDeliveryRunnable implements Runnable {
        private final DownloadStatus mDownloadStatus;
        private final IDownloadCallBack mCallBack;

        public DownloadStatusDeliveryRunnable(DownloadStatus downloadStatus) {
            this.mDownloadStatus = downloadStatus;
            this.mCallBack = mDownloadStatus.getCallBack();
        }

        @Override
        public void run() {
            switch (mDownloadStatus.getStatus()) {
                case DownloadStatus.STATUS_CONNECTING:
                    mCallBack.onConnecting();
                    break;
                case DownloadStatus.STATUS_CONNECTED:
                    mCallBack.onConnected(mDownloadStatus.getTime(),mDownloadStatus.getLength(), mDownloadStatus.isAcceptRanges());
                    break;
                case DownloadStatus.STATUS_PROGRESS:
                    mCallBack.onProgress(mDownloadStatus.getThreadId(),mDownloadStatus.getThreadFinished(),mDownloadStatus.getAllFinished(), mDownloadStatus.getLength(), mDownloadStatus.getPercent());
                    break;
                case DownloadStatus.STATUS_COMPLETED:
                    mCallBack.onCompleted();
                    break;
                case DownloadStatus.STATUS_PAUSED:
                    mCallBack.onDownloadPaused();
                    break;
                case DownloadStatus.STATUS_CANCELED:
                    mCallBack.onDownloadCanceled();
                    break;
                case DownloadStatus.STATUS_FAILED:
                    mCallBack.onFailed((DownloadException) mDownloadStatus.getException());
                    break;
            }
        }
    }
}
