package com.aspsine.multithreaddownload.core;

import com.aspsine.multithreaddownload.DownloadConfiguration;
import com.aspsine.multithreaddownload.DownloadException;
import com.aspsine.multithreaddownload.DownloadInfo;
import com.aspsine.multithreaddownload.DownloadRequestInfo;
import com.aspsine.multithreaddownload.architecture.IConnectTask;
import com.aspsine.multithreaddownload.architecture.IDownloadResponse;
import com.aspsine.multithreaddownload.architecture.DownloadStatus;
import com.aspsine.multithreaddownload.architecture.IDownloadTask;
import com.aspsine.multithreaddownload.architecture.IDownloader;
import com.aspsine.multithreaddownload.db.DataBaseManager;
import com.aspsine.multithreaddownload.db.ThreadInfo;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * 下载实现类
 * Created by Aspsine on 2015/10/28.
 */
public class DownloaderImpl implements IDownloader, IConnectTask.IConnectListener, IDownloadTask.OnDownloadListener {

    private DownloadRequestInfo mRequest;

    private IDownloadResponse mResponse;

    private Executor mExecutor;

    private DataBaseManager mDBManager;

    private String mTag;

    private DownloadConfiguration mConfig;

    private IDownloaderDestroyedListener mListener;

    private int mStatus;

    private DownloadInfo mDownloadInfo;

    private IConnectTask mConnectTask;

    private List<IDownloadTask> mDownloadTasks;

    public DownloaderImpl(DownloadRequestInfo request,
                          IDownloadResponse response,
                          Executor executor,
                          DataBaseManager dbManager,
                          String key,
                          DownloadConfiguration config,
                          IDownloaderDestroyedListener listener) {
        mRequest = request;
        mResponse = response;
        mExecutor = executor;
        mDBManager = dbManager;
        mTag = key;
        mConfig = config;
        mListener = listener;
        initDownloadInfo();
    }

    /**
     * 初始化下载信息
     */
    private void initDownloadInfo() {
        mDownloadInfo = new DownloadInfo(mRequest.getFileName().toString(), mRequest.getUri(), mRequest.getSaveDir());
        mDownloadTasks = new LinkedList<>();
    }

    @Override
    public boolean isRunning() {
        return mStatus == DownloadStatus.STATUS_STARTED
                || mStatus == DownloadStatus.STATUS_CONNECTING
                || mStatus == DownloadStatus.STATUS_CONNECTED
                || mStatus == DownloadStatus.STATUS_PROGRESS;
    }

    @Override
    public void start() {
        mStatus = DownloadStatus.STATUS_STARTED;
        mResponse.onStarted();
        connectServer();
    }

    @Override
    public void pause() {
        if (mConnectTask != null) {
            mConnectTask.cancel();
        }
        for (IDownloadTask task : mDownloadTasks) {
            task.pause();
        }
    }

    @Override
    public void cancel() {
        if (mConnectTask != null) {
            mConnectTask.cancel();
        }
        for (IDownloadTask task : mDownloadTasks) {
            task.cancel();
        }
    }

    @Override
    public void onDestroy() {
        // trigger the onDestroy callback tell download manager
        mListener.onDestroyed(mTag, this);
    }

    @Override
    public void onConnecting() {
        mStatus = DownloadStatus.STATUS_CONNECTING;
        mResponse.onConnecting();
    }

    @Override
    public void onConnected(long time, long length, boolean isAcceptRanges) {
        mStatus = DownloadStatus.STATUS_CONNECTED;
        mResponse.onConnected(time, length, isAcceptRanges);
        mDownloadInfo.setAcceptRanges(isAcceptRanges);
        mDownloadInfo.setLength(length);
        
        //真正的开始下载文件
        prepareDownload(length, isAcceptRanges);


    }

    @Override
    public void onConnectFailed(DownloadException de) {
        mStatus = DownloadStatus.STATUS_FAILED;
        mResponse.onConnectFailed(de);
        onDestroy();
    }

    @Override
    public void onConnectCanceled() {
        mStatus = DownloadStatus.STATUS_CANCELED;
        mResponse.onConnectCanceled();
        onDestroy();
    }

    @Override
    public void onDownloadConnecting() {
    }

    @Override
    public void onDownloadProgress(int thread_id, long thread_finished, long all_finished, long length) {
        mStatus = DownloadStatus.STATUS_PROGRESS;
        // calculate percent
        final int percent = (int) (all_finished * 100 / length);
        mResponse.onDownloadProgress(thread_id, thread_finished, all_finished, length, percent);
    }

    @Override
    public void onDownloadCompleted() {
        if (isAllComplete()) {
            deleteFromDB();
            mStatus = DownloadStatus.STATUS_COMPLETED;
            mResponse.onDownloadCompleted();
            onDestroy();
        }
    }

    @Override
    public void onDownloadPaused() {
        if (isAllPaused()) {
            mStatus = DownloadStatus.STATUS_PAUSED;
            mResponse.onDownloadPaused();
            onDestroy();
        }
    }

    @Override
    public void onDownloadCanceled() {
        if (isAllCanceled()) {
            deleteFromDB();
            mStatus = DownloadStatus.STATUS_CANCELED;
            mResponse.onDownloadCanceled();
            onDestroy();
        }
    }

    @Override
    public void onDownloadFailed(DownloadException de) {
        if (isAllFailed()) {
            mStatus = DownloadStatus.STATUS_FAILED;
            mResponse.onDownloadFailed(de);
            onDestroy();
        }
    }

    //第一步，开启子线程去连接服务器，获取文件的长度及是否支持分块下载
    private void connectServer() {
        mConnectTask = new ConnectTaskImpl(mRequest.getUri(), this);
        mExecutor.execute(mConnectTask);
    }


    //第二步，准备去下载文件
    private void prepareDownload(long length, boolean acceptRanges) {

        if (mConfig.isLuanchServiceDownload()) {


        } else {
            initDownloadTasks(length, acceptRanges);
            // start tasks
            for (IDownloadTask downloadTask : mDownloadTasks) {
                mExecutor.execute(downloadTask);
            }
        }
    }

    private void initDownloadTasks(long length, boolean acceptRanges) {
        mDownloadTasks.clear();
        //分块下载
        if (acceptRanges) {
            List<ThreadInfo> threadInfos = getMultiThreadInfos(length);
            int finished = 0;

            //计算所有线程的累加完成度，以便界面显示进度
            for (ThreadInfo threadInfo : threadInfos) {
                finished += threadInfo.getFinished();
            }
            mDownloadInfo.setFinished(finished);

            for (ThreadInfo info : threadInfos) {
                //MultiDownloadTask创建之后或执行其抽象类DownloadTaskImpl的run()方法，run()开始执行下载
                mDownloadTasks.add(new MultiDownloadTask(mDownloadInfo, info, mDBManager, this));
            }
        } else {
            //不分块下载
            ThreadInfo info = getSingleThreadInfo();
            mDownloadTasks.add(new SingleDownloadTask(mDownloadInfo, info, this));
        }
    }

    //获取多线程下载的分片信息
    private List<ThreadInfo> getMultiThreadInfos(long length) {
        //首先从数据库查询线程的信息
        final List<ThreadInfo> threadInfos = mDBManager.getThreadInfos(mTag);

        //如果没有下载过，则将线程分配信息写入数据库
        if (threadInfos.isEmpty()) {
            final int threadNum = mConfig.getThreadNum();
            final long average = length / threadNum;  //每个线程下载的大小
            for (int i = 0; i < threadNum; i++) {
                // calculate average
                final long start = average * i;
                final long end;
                if (i == threadNum - 1) {
                    end = length;
                } else {
                    end = start + average - 1;
                }
                ThreadInfo threadInfo = new ThreadInfo(i, mTag, mRequest.getUri(), start, end, 0);
                threadInfos.add(threadInfo);
            }
        }
        return threadInfos;
    }

    private ThreadInfo getSingleThreadInfo() {
        ThreadInfo threadInfo = new ThreadInfo(0, mTag, mRequest.getUri(), 0);
        return threadInfo;
    }


    //遍历所有的线程信息，所有的都完成了则下载任务完成
    private boolean isAllComplete() {
        boolean allFinished = true;
        for (IDownloadTask task : mDownloadTasks) {
            if (!task.isComplete()) {
                allFinished = false;
                break;
            }
        }
        return allFinished;
    }

    private boolean isAllFailed() {
        boolean allFailed = true;
        for (IDownloadTask task : mDownloadTasks) {
            if (task.isDownloading()) {
                allFailed = false;
                break;
            }
        }
        return allFailed;
    }

    private boolean isAllPaused() {
        boolean allPaused = true;
        for (IDownloadTask task : mDownloadTasks) {
            if (task.isDownloading()) {
                allPaused = false;
                break;
            }
        }
        return allPaused;
    }

    private boolean isAllCanceled() {
        boolean allCanceled = true;
        for (IDownloadTask task : mDownloadTasks) {
            if (task.isDownloading()) {
                allCanceled = false;
                break;
            }
        }
        return allCanceled;
    }

    private void deleteFromDB() {
        mDBManager.delete(mTag);
    }
}
