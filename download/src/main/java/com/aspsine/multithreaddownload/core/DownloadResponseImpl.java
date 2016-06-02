package com.aspsine.multithreaddownload.core;

import com.aspsine.multithreaddownload.IDownloadCallBack;
import com.aspsine.multithreaddownload.DownloadException;
import com.aspsine.multithreaddownload.architecture.IDownloadResponse;
import com.aspsine.multithreaddownload.architecture.DownloadStatus;
import com.aspsine.multithreaddownload.architecture.DownloadStatusDelivery;

/**
 * Created by Aspsine on 2015/10/29.
 */
public class DownloadResponseImpl implements IDownloadResponse {
    private DownloadStatusDelivery mDelivery;

    private DownloadStatus mDownloadStatus;

    public DownloadResponseImpl(DownloadStatusDelivery delivery, IDownloadCallBack callBack) {
        mDelivery = delivery;
        mDownloadStatus = new DownloadStatus();
        mDownloadStatus.setCallBack(callBack);
    }

    @Override
    public void onStarted() {
        mDownloadStatus.setStatus(DownloadStatus.STATUS_STARTED);
        mDownloadStatus.getCallBack().onStarted();
    }

    @Override
    public void onConnecting() {
        mDownloadStatus.setStatus(DownloadStatus.STATUS_CONNECTING);
        mDelivery.post(mDownloadStatus);
    }

    @Override
    public void onConnected(long time, long length, boolean acceptRanges) {
        mDownloadStatus.setTime(time);
        mDownloadStatus.setAcceptRanges(acceptRanges);
        mDownloadStatus.setStatus(DownloadStatus.STATUS_CONNECTED);
        mDelivery.post(mDownloadStatus);
    }

    @Override
    public void onConnectFailed(DownloadException e) {
        mDownloadStatus.setException(e);
        mDownloadStatus.setStatus(DownloadStatus.STATUS_FAILED);
        mDelivery.post(mDownloadStatus);
    }

    @Override
    public void onConnectCanceled() {
        mDownloadStatus.setStatus(DownloadStatus.STATUS_CANCELED);
        mDelivery.post(mDownloadStatus);
    }

    @Override
    public void onDownloadProgress(int thread_id, long thread_finished, long finished, long length, int percent) {
        mDownloadStatus.setAllFinished(finished);
        mDownloadStatus.setLength(length);
        mDownloadStatus.setPercent(percent);
        mDownloadStatus.setThreadId(thread_id);
        mDownloadStatus.setThreadFinished(thread_finished);
        mDownloadStatus.setStatus(DownloadStatus.STATUS_PROGRESS);
        mDelivery.post(mDownloadStatus);
    }

    @Override
    public void onDownloadCompleted() {
        mDownloadStatus.setStatus(DownloadStatus.STATUS_COMPLETED);
        mDelivery.post(mDownloadStatus);
    }

    @Override
    public void onDownloadPaused() {
        mDownloadStatus.setStatus(DownloadStatus.STATUS_PAUSED);
        mDelivery.post(mDownloadStatus);
    }

    @Override
    public void onDownloadCanceled() {
        mDownloadStatus.setStatus(DownloadStatus.STATUS_CANCELED);
        mDelivery.post(mDownloadStatus);
    }

    @Override
    public void onDownloadFailed(DownloadException e) {
        mDownloadStatus.setException(e);
        mDownloadStatus.setStatus(DownloadStatus.STATUS_FAILED);
        mDelivery.post(mDownloadStatus);
    }
}
