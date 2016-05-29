package com.infinite.myapp.utils.network.download;


import com.infinite.myapp.utils.ThreadManagerUtil;
import com.infinite.myapp.utils.database.DownLoadDatabase;
import com.infinite.myapp.utils.database.model.DownLoadModel;

import java.net.SocketException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class DownLoadRequest {

    private final String TAG = DownLoadRequest.class.getSimpleName();

    // 下载线程池
    private ExecutorService mDownLoadService;


    //多线程下载文件最低大小10mb
    private final long MULTI_LINE = 10 * 1024 * 1024;

    private final long NEW_DOWN_BEGIN = 0;

    private DownLoadTaskListener mCallBackListener;

    private DownLoadDatabase mDownLoadDatabase;

    //URL下载Task
    private Map<String, Map<Integer, Future>> mUrlTaskMap;
    //下载的任务
    private List<DownLoadModel> mList;

    public DownLoadRequest(DownLoadDatabase downLoadDatabase, DownLoadTaskListener callBackListener, List<DownLoadModel> list) {
        mDownLoadDatabase = downLoadDatabase;
        mCallBackListener = callBackListener;
        mList = list;
        mUrlTaskMap = new ConcurrentHashMap<>();
        mDownLoadService = Executors.newFixedThreadPool(ThreadManagerUtil.getCPUCores() + 1);
    }

    public void start() {
        for (DownLoadModel e : mList) {
            addDownLoadTask(e);
        }
    }

    private void addDownLoadTask(DownLoadModel downLoadModel) {
        Map<Integer, Future> downLoadTaskMap = new ConcurrentHashMap<>();
        MultiDownLoaderListener multiDownLoaderListener = new MultiDownLoaderListener(mCallBackListener);
        if (downLoadModel.multiList != null && downLoadModel.multiList.size() != 0) {
            for (int i = 0; i < downLoadModel.multiList.size(); i++) {
                DownLoadModel entity = downLoadModel.multiList.get(i);
                //当前分支是否下载完成
                if (entity.downed + entity.start >= entity.end) {
                    continue;
                }
                DownLoadTask downLoadTask = new DownLoadTask.Builder().downLoadModel(entity).taskId(entity.dataId).downLoadTaskListener(multiDownLoaderListener).build();
                executeNetWork(entity, downLoadTask, downLoadTaskMap);
            }
        } else {
            //文件不存在 直接下载
            createDownLoadTask(downLoadModel, NEW_DOWN_BEGIN, downLoadTaskMap, multiDownLoaderListener);
        }
    }

    //加入下载线程池
    private void executeNetWork(DownLoadModel entity, DownLoadTask downLoadTask, Map<Integer, Future> map) {
//        Log.d(TAG, "创建任务" + downLoadTask.getTaskId() + "");
        map.put(entity.dataId, mDownLoadService.submit(downLoadTask));
        mUrlTaskMap.put(entity.url, map);
    }


    /**
     * 创建下载任务
     *
     * @param beginSize            开始位置
     * @param downLoadTaskMap      当前url本地缓存
     * @param downLoadTaskListener
     */
    private void createDownLoadTask(DownLoadModel downLoadModel, long beginSize, final Map<Integer, Future> downLoadTaskMap, DownLoadTaskListener downLoadTaskListener) {
        long startSize, endSize;

        DownLoadTask downLoadTask;
        if (downLoadModel.total > MULTI_LINE) {
            //多线程下载
            int threadNum = (int) ((downLoadModel.total % MULTI_LINE == 0) ? downLoadModel.total / MULTI_LINE : downLoadModel.total / MULTI_LINE + 1);

            for (int i = 0; i < threadNum; i++) {
                startSize = beginSize + i * MULTI_LINE;
                endSize = startSize + MULTI_LINE - 1;
                if (i == threadNum - 1) {
                    if (endSize > downLoadModel.total) {
                        endSize = downLoadModel.total;
                    }
                }
                DownLoadModel entity = mDownLoadDatabase.insert(downLoadModel.url, (int) startSize, (int) endSize, (int) downLoadModel.total, downLoadModel.saveName);
                downLoadTask = new DownLoadTask.Builder().taskId(entity.dataId).downLoadModel(entity).downLoadTaskListener(downLoadTaskListener).build();
                executeNetWork(entity, downLoadTask, downLoadTaskMap);
            }
        } else {
            //单线程下载
            DownLoadModel entity = mDownLoadDatabase.insert(downLoadModel.url, 0, (int) downLoadModel.total - 1, (int) downLoadModel.total, downLoadModel.saveName);
            downLoadTask = new DownLoadTask.Builder().taskId(entity.dataId).downLoadModel(entity).downLoadTaskListener(downLoadTaskListener).build();
            executeNetWork(entity, downLoadTask, downLoadTaskMap);
        }
    }

    //移除map记录 只有在完成后移除
    private boolean removeTask(DownLoadModel downLoadModel) {
        Map<Integer, Future> map = getDownLoadTask(downLoadModel.url);
        if (map.isEmpty()) {
            return true;
        }
        if (map.containsKey(downLoadModel.dataId)) {
            map.remove(downLoadModel.dataId);
        }
        if (map.size() == 0) {
            mUrlTaskMap.remove(downLoadModel.url);
        }
        if (mUrlTaskMap.size() == 0) {
            return true;
        }
        return false;
    }

    public Map<Integer, Future> getDownLoadTask(String url) {
        return mUrlTaskMap.get(url);
    }


    //取消当前url所有任务
    private void cancel(String url) {
        Map<Integer, Future> downLoadMap = getDownLoadTask(url);
        if (downLoadMap != null && downLoadMap.size() > 0) {
            Iterator iterator = downLoadMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, Future> map = (Map.Entry<Integer, Future>) iterator.next();
                Future future = map.getValue();
                future.cancel(true);
                iterator.remove();
            }
            mUrlTaskMap.remove(url);
        }
    }

    //取消所有任务
    public void stop() {
        if (mUrlTaskMap.size() != 0) {
            Iterator iterator = mUrlTaskMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Map<Integer, Future>> entry = (Map.Entry<String, Map<Integer, Future>>) iterator.next();
                cancel(entry.getKey());
                iterator.remove();
            }
        }
    }

    /**
     * 子任务监听
     */
    class MultiDownLoaderListener implements DownLoadTaskListener {

        private DownLoadTaskListener downLoadTaskListener;
        //重复次数
        private int repeatCount = 10;

        public MultiDownLoaderListener(DownLoadTaskListener downLoadTaskListener) {
            this.downLoadTaskListener = downLoadTaskListener;
        }

        @Override
        public synchronized void onDownLoading(long downSize) {
            downLoadTaskListener.onDownLoading(downSize);
        }

        @Override
        public synchronized void onStart(DownLoadModel downLoadModel) {
            downLoadTaskListener.onStart(downLoadModel);
        }

        @Override
        public synchronized void onCancel(DownLoadModel downLoadModel) {
            mDownLoadDatabase.update(downLoadModel);
        }

        @Override
        public synchronized void onCompleted(DownLoadModel downLoadModel) {
            mDownLoadDatabase.update(downLoadModel);
            if (!isRepeatExecute(downLoadModel, repeatCount, this)) {
                if (removeTask(downLoadModel)) {
                    downLoadTaskListener.onCompleted(downLoadModel);
                }
            } else {
                repeatCount--;
            }
        }

        @Override
        public synchronized void onError(DownLoadModel downLoadModel, Throwable throwable) {
            mDownLoadDatabase.update(downLoadModel);
            if (throwable instanceof SocketException) {
                downLoadTaskListener.onError(downLoadModel, throwable);
                return;
            }
            if (!isRepeatExecute(downLoadModel, repeatCount, this)) {
                downLoadTaskListener.onError(downLoadModel, throwable);
            } else {
                repeatCount--;
            }
        }
    }

    //判断是否下载完整
    private synchronized boolean isRepeatExecute(DownLoadModel downLoadModel, int repeatCount, DownLoadTaskListener loadTaskListener) {
        if ((downLoadModel.downed + downLoadModel.start < downLoadModel.end) && repeatCount > 0) {
            //没下载完
            DownLoadTask downLoadTask = new DownLoadTask.Builder().taskId(downLoadModel.dataId).downLoadModel(downLoadModel).downLoadTaskListener(loadTaskListener).build();
            executeNetWork(downLoadModel, downLoadTask, mUrlTaskMap.get(downLoadModel.url));
            return true;
        }
        return false;
    }
}
