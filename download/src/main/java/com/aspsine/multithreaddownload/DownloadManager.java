package com.aspsine.multithreaddownload;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.aspsine.multithreaddownload.architecture.IDownloadResponse;
import com.aspsine.multithreaddownload.architecture.DownloadStatusDelivery;
import com.aspsine.multithreaddownload.architecture.IDownloader;
import com.aspsine.multithreaddownload.core.DownloadResponseImpl;
import com.aspsine.multithreaddownload.core.DownloadStatusDeliveryImpl;
import com.aspsine.multithreaddownload.core.DownloaderImpl;
import com.aspsine.multithreaddownload.db.DataBaseManager;
import com.aspsine.multithreaddownload.db.ThreadInfo;
import com.aspsine.multithreaddownload.util.L;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Aspsine on 2015/7/14.
 */
public class DownloadManager implements IDownloader.IDownloaderDestroyedListener {
    public static final String TAG = DownloadManager.class.getSimpleName();

    private static DownloadManager sDownloadManager;

    private DataBaseManager mDBManager;


    //正在下载的队列
    private Map<String, IDownloader> mDownloaderMap;

    private DownloadConfiguration mDownloadConfig;

    private ExecutorService mExecutorService;

    private DownloadStatusDelivery mDelivery;

    public static DownloadManager getInstance() {
        if (sDownloadManager == null) {
            synchronized (DownloadManager.class) {
                sDownloadManager = new DownloadManager();
            }
        }
        return sDownloadManager;
    }


    private DownloadManager() {
        mDownloaderMap = new LinkedHashMap<String, IDownloader>();
    }

    public void init(Context context) {
        init(context, new DownloadConfiguration());
    }

    public void init(Context context, @NonNull DownloadConfiguration config) {
        if (config.getThreadNum() > config.getMaxThreadNum()) {
            throw new IllegalArgumentException("thread num must < max thread num");
        }
        mDownloadConfig = config;
        mDBManager = DataBaseManager.getInstance(context);
        mExecutorService = Executors.newFixedThreadPool(mDownloadConfig.getMaxThreadNum());
        mDelivery = new DownloadStatusDeliveryImpl(new Handler(Looper.getMainLooper()));
    }

    @Override
    public void onDestroyed(String key, IDownloader downloader) {
        if (mDownloaderMap.containsKey(key)) {
            mDownloaderMap.remove(key);
        }
    }

    public void download(DownloadRequestInfo request, IDownloadCallBack callBack) {
        if (TextUtils.isEmpty(request.getUri())) {
            throw new NullPointerException("-------下载uri不能为空！！！！！");
        }
        //将下载url进行hash计算出唯一tag
        final String mUrlKey = createKey(request.getUri());
        //如果未在下载
        if (!isRuning(mUrlKey)) {
            IDownloadResponse response = new DownloadResponseImpl(mDelivery, callBack);
            IDownloader downloader = new DownloaderImpl(request, response, mExecutorService, mDBManager, mUrlKey, mDownloadConfig, this);
            mDownloaderMap.put(mUrlKey, downloader);
            downloader.start();
        }
    }

    public void pause(String tag) {
        String key = createKey(tag);
        if (mDownloaderMap.containsKey(key)) {
            IDownloader downloader = mDownloaderMap.get(key);
            if (downloader != null) {
                if (downloader.isRunning()) {
                    downloader.pause();
                }
            }
            mDownloaderMap.remove(key);
        }
    }

    public void cancel(String tag) {
        String key = createKey(tag);
        if (mDownloaderMap.containsKey(key)) {
            IDownloader downloader = mDownloaderMap.get(key);
            if (downloader != null) {
                downloader.cancel();
            }
            mDownloaderMap.remove(key);
        }
    }

    public void pauseAll() {
        for (IDownloader downloader : mDownloaderMap.values()) {
            if (downloader != null) {
                if (downloader.isRunning()) {
                    downloader.pause();
                }
            }
        }
    }

    public void cancelAll() {
        for (IDownloader downloader : mDownloaderMap.values()) {
            if (downloader != null) {
                if (downloader.isRunning()) {
                    downloader.cancel();
                }
            }
        }
    }

//    public void delete(String tag) {
//        String key = createKey(tag);
//        if (mDownloaderMap.containsKey(key)) {
//            IDownloader downloader = mDownloaderMap.get(key);
//            downloader.cancel();
//        } else {
//            List<DownloadInfo> infoList = mDBManager.getDownloadInfos(tag);
//            for (DownloadInfo info : infoList) {
//                FileUtils.delete(info.getD);
//            }
//        }
//    }
//
//    public void deleteAll() {
//
//    }

    public DownloadInfo getDownloadProgress(String tag) {
        String key = createKey(tag);
        List<ThreadInfo> threadInfos = mDBManager.getThreadInfos(key);
        DownloadInfo downloadInfo = null;
        if (!threadInfos.isEmpty()) {
            int finished = 0;
            int progress = 0;
            int total = 0;
            for (ThreadInfo info : threadInfos) {
                finished += info.getFinished();
                total += (info.getEnd() - info.getStart());
            }
            progress = (int) ((long) finished * 100 / total);
            downloadInfo = new DownloadInfo();
            downloadInfo.setFinished(finished);
            downloadInfo.setLength(total);
            downloadInfo.setProgress(progress);
        }
        return downloadInfo;
    }

    //检查是否正在下载
    private boolean isRuning(String key) {
        if (mDownloaderMap.containsKey(key)) {
            IDownloader downloader = mDownloaderMap.get(key);
            if (downloader != null) {
                if (downloader.isRunning()) {
                    L.w("Task has been started!");
                    return true;
                } else {
                    throw new IllegalStateException("Downloader instance with same tag has not been destroyed!");
                }
            }
        }
        return false;
    }

    private static String createKey(String tag) {
        if (tag == null) {
            throw new NullPointerException("Tag can't be null!");
        }
        return String.valueOf(tag.hashCode());
    }


}
