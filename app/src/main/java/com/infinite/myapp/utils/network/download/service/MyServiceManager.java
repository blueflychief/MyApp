package com.infinite.myapp.utils.network.download.service;

import android.content.Intent;
import android.os.ResultReceiver;
import android.text.TextUtils;

import com.infinite.myapp.utils.database.model.DownLoadModel;
import com.infinite.myapp.utils.network.KKNetWorkRequest;

import java.util.ArrayList;

/**
 * 下载服务管理类
 */
public class MyServiceManager {

    /**
     * 是否需要在后台下载文件
     */
    private boolean isNeedBackgroundDownload = false;

    /**
     * 需要下载文件的url地址 跟 保存路径
     */
    private String url, savePath;

    /**
     * 是否是普通的Service true : 是普通Service, false : 不是普通Service
     */
    private boolean isNormalService = false;

    /**
     * 接收在服务中执行任务后的结果回调
     */
    private ResultReceiver resultReceiver = null;

    private ArrayList<DownLoadModel> list = null;

    private MyServiceManager() {
    }

    private static MyServiceManager serviceManager = null;

    public static synchronized MyServiceManager getInstance() {
        if (serviceManager == null) {
            serviceManager = new MyServiceManager();
        }
        return serviceManager;
    }


    /**
     * 设置是否需要在后台开启下载,通过此变量来设置需不需要支持断点下载
     *
     * @param isNeedBackground
     * @return
     */
    public MyServiceManager setIsNeedBackgroundDownload(final boolean isNeedBackground) {
        this.isNeedBackgroundDownload = isNeedBackground;
        return this;
    }

    /**
     * 设置在服务中执行任务后返回的结果回调
     *
     * @param resultReceiver
     */
    public MyServiceManager setResultCallback(final ResultReceiver resultReceiver) {
        this.resultReceiver = resultReceiver;
        return this;
    }

    /**
     * 设置下载文件的保存路径及需要下载文件的URL地址
     *
     * @param savePath
     * @param url
     * @return
     */
    public MyServiceManager setDownloadSavePathAndUrl(String savePath, String /*...*/url) {
        this.url = url;
        this.savePath = savePath;
        return this;
    }

    /**
     * 设置需要批量下载的文件Url集合
     *
     * @param list
     * @return
     */
    public MyServiceManager setDownloadEntityList(ArrayList<DownLoadModel> list) {
        this.list = list;
        return this;
    }

    /**
     * 设置需要启动的服务是否是普通服务
     *
     * @param isNormalService
     */
    public void setIsNormalService(boolean isNormalService) {
        this.isNormalService = isNormalService;
    }


    /**
     * 参数配置齐全后正式执行任务服务
     *
     * @return
     */
    public void execute() {

        if (isNormalService) { // 是否是普通的Service

        } else { // 不是普通Service,需要规定一些特殊的、必须的参数
            if (isNeedBackgroundDownload) { // 需要后台下载
                Intent intent = new Intent(KKNetWorkRequest.getInstance().sContext, MyPersistentService.class);
//                intent.putExtra(MyTaskService.DOWNLOAD_FILE_URL,url);
//                intent.putExtra(MyTaskService.DOWNLOAD_FILE_SAVE_PATH,savePath);
                intent.putParcelableArrayListExtra(MyPersistentService.DOWNLOAD_FILE_LIST, list);
                MyPersistentService.startDownloadFileTask(KKNetWorkRequest.getInstance().sContext, intent, resultReceiver);
            } else { // 不需要后台下载
                if (url == null || /*url.length == 0 || */TextUtils.isEmpty(savePath)) {
                    throw new IllegalArgumentException("下载文件的url 或者 下载文件存储路径 savePath 为空 ");
                } else {
                    Intent intent = new Intent(KKNetWorkRequest.getInstance().sContext, MyTaskService.class);
                    intent.putExtra(MyTaskService.DOWNLOAD_FILE_URL, url);
                    intent.putExtra(MyTaskService.DOWNLOAD_FILE_SAVE_PATH, savePath);
                    MyTaskService.startDownloadFileTask(KKNetWorkRequest.getInstance().sContext, intent, resultReceiver);
                }
            }
        }
    }

    public void release() {
        resultReceiver = null;
        // KKBuzInterface.mContext.stopService(new Intent(KKBuzInterface.mContext,MyPersistentService.class));
    }

}
