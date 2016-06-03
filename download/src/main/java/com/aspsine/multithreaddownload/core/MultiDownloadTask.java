package com.aspsine.multithreaddownload.core;

import com.aspsine.multithreaddownload.DownloadInfo;
import com.aspsine.multithreaddownload.db.DataBaseManager;
import com.aspsine.multithreaddownload.db.ThreadInfo;
import com.aspsine.multithreaddownload.util.L;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * 支持多点续传多线程下载任务
 */
public class MultiDownloadTask extends DownloadTaskImpl {

    private DataBaseManager mDBManager;

    public MultiDownloadTask(DownloadInfo downloadInfo, ThreadInfo threadInfo, DataBaseManager dbManager, OnDownloadListener listener) {
        super(downloadInfo, threadInfo, listener);
        this.mDBManager = dbManager;
    }


    @Override
    protected void insertIntoDB(ThreadInfo info) {
        if (!mDBManager.exists(info.getTag(), info.getId())) {
            mDBManager.insert(info);
        }
    }

    @Override
    protected int getResponseCode() {
        return HttpURLConnection.HTTP_PARTIAL;
    }

    @Override
    protected void updateDB(ThreadInfo info) {
        mDBManager.update(info.getTag(), info.getId(), info.getFinished());
    }

    @Override
    protected Map<String, String> getHttpHeaders(ThreadInfo info) {
        //根据上次的完成度，设置下载Range开始和结束的位置
        Map<String, String> headers = new HashMap<String, String>();
        long start = info.getStart() + info.getFinished();
        long end = info.getEnd();
        headers.put("Range", "bytes=" + start + "-" + end);
        L.i("--------MultiDownloadTask:"+info.getTag()+"----start:"+start+"---end:"+end);
        return headers;
    }

    @Override
    protected RandomAccessFile getFile(File dir, String name, long offset) throws IOException {
        File file = new File(dir, name);
        RandomAccessFile raf = new RandomAccessFile(file, "rwd");
        raf.seek(offset);
        return raf;
    }


    @Override
    protected String getTag() {
        return this.getClass().getSimpleName();
    }
}