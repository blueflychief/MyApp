package com.infinite.myapp.utils.network.download;

import android.util.Log;

import com.infinite.myapp.utils.ThreadManagerUtil;
import com.infinite.myapp.utils.database.DownLoadDatabase;
import com.infinite.myapp.utils.database.model.DownLoadModel;
import com.infinite.myapp.utils.network.KKNetWorkRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


public class DownLoadManager {

    private final String TAG = DownLoadManager.class.getSimpleName();

    // 获取文件大小的线程池
    private static ExecutorService mGetFileLengthService;

    //所有下载Task
    private Map<String, DownLoadRequest> mTaskMap;

    private static DownLoadDatabase mDownLoadDatabase;

    private DownLoadManager() {
        mTaskMap = new ConcurrentHashMap<>();
    }

    private static DownLoadManager sInstance = new DownLoadManager();

    public static DownLoadManager getInstance() {
        init();
        return sInstance;
    }

    private static synchronized void init() {
        if (mDownLoadDatabase == null) {
            mDownLoadDatabase = new DownLoadDatabase(KKNetWorkRequest.getInstance().sContext.getApplicationContext());
        }
        if (mGetFileLengthService == null) {
            mGetFileLengthService = Executors.newFixedThreadPool(ThreadManagerUtil.getCPUCores() + 1);
        }
    }

    /**
     * 获取文件大小的call
     */
    class GetFileLengthTask implements Runnable {

        private Call<ResponseBody> mResponseCall;

        private GetFileSizeListener mGetFileSizeListener;

        public GetFileLengthTask(Call<ResponseBody> responseCall, GetFileSizeListener getFileSizeListener) {
            mResponseCall = responseCall;
            mGetFileSizeListener = getFileSizeListener;
        }

        @Override
        public void run() {
            try {
                Response response = mResponseCall.execute();
                if (response.isSuccessful()) {
                    if (mGetFileSizeListener != null) {
                        mGetFileSizeListener.success(Long.parseLong(response.headers().get("Content-Range").split("/")[1]));
                    }
                } else {
                    if (mGetFileSizeListener != null) {
                        mGetFileSizeListener.error();
                    }
                }
                if (response.body() != null) {
                    ((ResponseBody) response.body()).close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private interface GetFileSizeListener {
        void success(Long fileSize);

        void error();
    }

    //取消所有任务
    public void cancelAll() {
        if (!mTaskMap.isEmpty()) {
            Iterator iterator = mTaskMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, DownLoadRequest> entry = (Map.Entry<String, DownLoadRequest>) iterator.next();
                cancelTag(entry.getKey());
            }
        }
        releaseDatabase();
    }

    //取消当前context所有任务
    public void cancelTag(String tag) {
        if (!mTaskMap.isEmpty()) {
            if (mTaskMap.containsKey(tag)) {
                mTaskMap.get(tag).stop();
                mTaskMap.remove(tag);
            }
        }
    }

    //    //通过url找到context
//    private Context getContextFromURL(String url) {
//        Context context = null;
//        if (mTaskMap != null && mTaskMap.size() > 0) {
//
//        }
//        return context;
//    }
    //释放数据库
    private synchronized void releaseDatabase() {
        if (mDownLoadDatabase != null) {
            mDownLoadDatabase.destroy();
            mDownLoadDatabase = null;
        }
    }

    /**
     * @param list
     * @param downLoadTaskListener 页面的回调
     */
    public synchronized void addDownLoadTask(ArrayList<DownLoadModel> list, String tag, DownLoadBackListener downLoadTaskListener) {
        List<DownLoadModel> myList = queryDownLoadData(list);
        Iterator iterator = myList.iterator();
        DownCallBackListener urlListener = new DownCallBackListener(downLoadTaskListener);
        long totalFileSize = 0;
        long hasDownSize = 0;
        while (iterator.hasNext()) {
            DownLoadModel downLoadModel = (DownLoadModel) iterator.next();
            hasDownSize += downLoadModel.downed;
            totalFileSize += downLoadModel.total;
        }
        if (hasDownSize >= totalFileSize) {
            downLoadTaskListener.onCompleted();
            return;
        }
        urlListener.setTotalSize(totalFileSize);
        urlListener.setHasDownSize(hasDownSize);
        DownLoadRequest downLoadRequest = new DownLoadRequest(mDownLoadDatabase, urlListener, myList);
        mTaskMap.put(tag, downLoadRequest);
        downLoadRequest.start();
    }

    public synchronized void addDownLoadTask(DownLoadModel downLoadModel, String tag, DownLoadBackListener downLoadTaskListener) {
        ArrayList<DownLoadModel> list = new ArrayList<>();
        list.add(downLoadModel);
        addDownLoadTask(list, tag, downLoadTaskListener);
    }

    //汇总url实体的下载信息
    private synchronized ArrayList<DownLoadModel> queryDownLoadData(ArrayList<DownLoadModel> list) {
        final Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            final DownLoadModel downLoadModel = (DownLoadModel) iterator.next();
            List<DownLoadModel> dataList = mDownLoadDatabase.query(downLoadModel.url);
            if (dataList.size() > 0) {
                File file = new File(downLoadModel.saveName);
                if (file.exists()) {
                    //文件存在 下载剩余
                    downLoadModel.multiList = dataList;
                    Iterator dataIterator = dataList.iterator();
                    while (dataIterator.hasNext()) {
                        DownLoadModel dataEntity = (DownLoadModel) dataIterator.next();
                        downLoadModel.downed += dataEntity.downed;
                        downLoadModel.total = dataEntity.total;
                    }
                } else {
                    //文件不存在 删除数据库 重新下载
                    mDownLoadDatabase.deleteAllByUrl(downLoadModel.url);
                    downLoadModel.total = dataList.get(0).total;
                }
            } else {
                mDownLoadDatabase.deleteAllByUrl(downLoadModel.url);
                Call<ResponseBody> mResponseCall = KKNetWorkRequest.getInstance().getApiService().downloadFile(downLoadModel.url, "bytes=" + 0 + "-" + 0);
                mGetFileLengthService.submit(new GetFileLengthTask(mResponseCall, new GetFileSizeListener() {
                    @Override
                    public void success(Long fileSize) {
                        downLoadModel.total = fileSize;
                    }

                    @Override
                    public void error() {
                    }
                }));
            }
            if (!iterator.hasNext()) {
                mGetFileLengthService.shutdown();
            }
        }
        while (!mGetFileLengthService.isShutdown() || !mGetFileLengthService.isTerminated()) {

        }
        mGetFileLengthService = Executors.newFixedThreadPool(ThreadManagerUtil.getCPUCores() + 1);
        return list;
    }


    /**
     * 总回调代理
     */
    class DownCallBackListener implements DownLoadTaskListener {

        private DownLoadBackListener mBackListener;
        long mTotalSize;
        long mHasDownSize;

        private boolean isReturnStart = false;
        private boolean isReturnErr = false;
        private boolean isReturnCancel = false;

        public void setTotalSize(long totalSize) {
            this.mTotalSize = totalSize;
        }

        public void setHasDownSize(long hasDownSize) {
            this.mHasDownSize = hasDownSize;
        }

        public DownCallBackListener(DownLoadBackListener downLoadBackListener) {
            this.mBackListener = downLoadBackListener;
        }

        @Override
        public synchronized void onStart(DownLoadModel downLoadModel) {
            if (!isReturnStart) {
                Log.d("dd", "mTotalSize" + mTotalSize + "__" + "mHasDownSize" + mHasDownSize);
                mBackListener.onStart((double) mHasDownSize / mTotalSize);
            }
            isReturnStart = true;
        }

        @Override
        public synchronized void onCancel(DownLoadModel downLoadModel) {
            if (!isReturnCancel) {
                mBackListener.onCancel();
            }
            isReturnCancel = true;
        }

        @Override
        public synchronized void onDownLoading(long downSize) {
            mHasDownSize += downSize;
            mBackListener.onDownLoading((double) mHasDownSize / mTotalSize);
            Log.d("dd", "onDownLoading" + "—————downSize" + downSize + "—————mTotalSize" + mTotalSize + "—————mHasDownSize" + mHasDownSize);
        }

        @Override
        public synchronized void onCompleted(DownLoadModel downLoadModel) {
            mBackListener.onCompleted();
        }

        @Override
        public synchronized void onError(DownLoadModel downLoadModel, Throwable throwable) {
            if (!isReturnErr) {
                mBackListener.onError(throwable);
            }
            isReturnErr = true;
        }
    }
}
