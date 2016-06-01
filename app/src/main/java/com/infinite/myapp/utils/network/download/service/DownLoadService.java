package com.infinite.myapp.utils.network.download.service;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ResultReceiver;

import com.infinite.myapp.utils.MyLogger;
import com.infinite.myapp.utils.database.model.DownLoadModel;

import java.io.File;
import java.util.ArrayList;

/**
 * Description : 重构后的下载核心类
 */
public class DownLoadService implements IDownLoad {

    private static DownLoadService sService = null;

    private static MyHandler myHandler = null ;

    private static IDownLoadCallback mCallback = null ;

    private ArrayList<DownLoadModel> list = null ;

    private DownLoadService(){ }

    public static DownLoadService getService(){
        if(sService == null){
            sService = new DownLoadService();
        }

        if(myHandler == null){
            myHandler = new MyHandler();
        }

        return sService;
    }

    private static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

                case MyTaskService.START_DOWNLOAD:
                    mCallback.onStartDownload();
                    break;

                case MyTaskService.RESULT_OK:
                    mCallback.onDownLoadSuccess();
                    break;

                case MyTaskService.RESULT_FAILURE:
                    mCallback.onDownloadFailed();
                    break;

                case MyPersistentService.DOWNLOAD_SIZE:
                    int downloadSize = msg.arg1;
                    mCallback.onDownloadSize(downloadSize);
                    break;

                case MyPersistentService.DOWNLOAD_DEFAULT_SIZE:
                    int defaultSize = msg.arg1;
                    mCallback.onStartDefaultSize(defaultSize);
                    break;

            }
        }
    }

    @Override
    public void onDownLoadList(final ArrayList<DownLoadModel> urls, final IDownLoadCallback callback) {
        list = new ArrayList<>();
        mCallback = callback ;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                download(urls,callback);
                Looper.loop();
            }
        }).start();
    }


    private void download(final ArrayList<DownLoadModel> urls, final IDownLoadCallback mCallback){

        myHandler.sendEmptyMessage(MyTaskService.START_DOWNLOAD);

        if(urls == null || urls.isEmpty()){
            throw new IllegalArgumentException("下载文件url集合不能为空");
        }

        MyLogger.e(" DownLoadRealizer.java down load url:" + urls.toString());
        final String download_path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "kkvideo" + File.separator;
        File dic = new File(download_path);
        if(!dic.exists()){
            dic.mkdirs();
        }

//        if(checkUrl2FileExists(urls)){
//            myHandler.sendEmptyMessage(MyTaskService.RESULT_OK);
//            return;
//        }

        MyServiceManager.getInstance()
                .setIsNeedBackgroundDownload(true) // 通过设置为true标识,表示可后台下载,而且支持断点下载,默认为false : 不可后台下载/不支持断点下载
                .setDownloadEntityList(urls)
                .setResultCallback(new ResultReceiver(new Handler()){
                    @Override
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        super.onReceiveResult(resultCode, resultData);
                        switch (resultCode){
                            case MyTaskService.RESULT_OK: // 下载成功
                                if(/*checkUrl2FileExists(urls) && */mCallback != null){
                                    myHandler.sendEmptyMessage(MyTaskService.RESULT_OK);
                                }else{
                                    myHandler.sendEmptyMessage(MyTaskService.RESULT_FAILURE);
                                }
                                break;

                            case MyTaskService.RESULT_FAILURE: // 下载失败
                                myHandler.sendEmptyMessage(MyTaskService.RESULT_FAILURE);
                                break;

                            case MyPersistentService.DOWNLOAD_SIZE: // 下载的字节数

                                double size = resultData.getDouble(MyPersistentService.DOWNLOAD_SIZE_KEY);
                                int downloadSize = (int)(size * 100) ;
                                MyLogger.i("onDownloadSSSSS downloadSize = "+ downloadSize);
                                Message msg = myHandler.obtainMessage();
                                msg.what = MyPersistentService.DOWNLOAD_SIZE ;
                                msg.arg1 = downloadSize ;
                                myHandler.sendMessage(msg);
                                break;

                            case MyPersistentService.DOWNLOAD_DEFAULT_SIZE:
                                double start = resultData.getDouble(MyPersistentService.DOWNLOAD_START_KEY);
                                int startSize = (int)(start * 100) ;
                                MyLogger.i("onDownloadSSSSS startSize = "+ startSize);
                                Message msgStart = myHandler.obtainMessage();
                                msgStart.what = MyPersistentService.DOWNLOAD_DEFAULT_SIZE ;
                                msgStart.arg1 = startSize ;
                                myHandler.sendMessage(msgStart);
                                break;
                        }
                    }
                }).execute();

    }


    /***
     * 检测url指定的文件是否已经被下载过
     * @param urls
     * @return

    private boolean checkUrl2FileExists(List<String> urls){
        final String download_path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "kkvideo" + File.separator;
        boolean isDownload = false ;
        for (String url : urls){
            int index = url.lastIndexOf("/");
            String filename = url.substring(index + 1);
            File file = new File(download_path + filename);
            if(file.exists()){
                isDownload = true ;
            }else{
                isDownload = false;
                DownLoadModel entity = new DownLoadModel();
                entity.url = url ;
                entity.saveName = download_path + filename ;
                list.add(entity);
            }
        }
        return isDownload ;
    }*/
}
