package com.aspsine.multithreaddownload.architecture;

import com.aspsine.multithreaddownload.IDownloadCallBack;
import com.aspsine.multithreaddownload.DownloadException;

/**
 *线程下载状态
 */
public class DownloadStatus {
    public static final int STATUS_STARTED = 101;
    public static final int STATUS_CONNECTING = 102;
    public static final int STATUS_CONNECTED = 103;
    public static final int STATUS_PROGRESS = 104;
    public static final int STATUS_COMPLETED = 105;
    public static final int STATUS_PAUSED = 106;
    public static final int STATUS_CANCELED = 107;
    public static final int STATUS_FAILED = 108;

    private int status;
    private long time;
    private int thread_id;
    private long thread_finished;
    private long length;
    private long all_finished;
    private int percent;
    private boolean acceptRanges;
    private DownloadException exception;

    private IDownloadCallBack callBack;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public int getThreadId() {
        return thread_id;
    }

    public void setThreadId(int thread_id) {
        this.thread_id = thread_id;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getAllFinished() {
        return all_finished;
    }

    public void setAllFinished(long finished) {
        this.all_finished = finished;
    }

    public long getThreadFinished() {
        return thread_finished;
    }

    public void setThreadFinished(long finished) {
        this.thread_finished = finished;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public boolean isAcceptRanges() {
        return acceptRanges;
    }

    public void setAcceptRanges(boolean acceptRanges) {
        this.acceptRanges = acceptRanges;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(DownloadException exception) {
        this.exception = exception;
    }

    public IDownloadCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(IDownloadCallBack callBack) {
        this.callBack = callBack;
    }
}
