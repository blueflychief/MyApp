package com.infinite.myapp.utils.network.download.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.ResultReceiver;

import com.infinite.myapp.utils.MyLogger;
import com.infinite.myapp.utils.database.model.DownLoadModel;
import com.infinite.myapp.utils.network.download.DownLoadBackListener;
import com.infinite.myapp.utils.network.download.DownLoadManager;

import java.util.ArrayList;

/**
 * 注: 常驻后台服务,应用退出时,需要手动调用stopService()关闭
 */
public class MyPersistentService extends Service {


    private static final String TAG = MyPersistentService.class.getSimpleName();

    public static final int DOWNLOAD_FILE = 0x01;

    public static final int DOWNLOAD_START = 0x02;

    public static final int DOWNLOAD_CANCEL = 0x03;

    public static final int DOWNLOAD_SIZE = 0x04;

    public static final int DOWNLOAD_DEFAULT_SIZE = 0x05;

    public static final int RESULT_OK = 200;

    public static final int RESULT_FAILURE = -1;

    public static final String DOWNLOAD_FILE_LIST = "download_file_list";

//    public static final String DOWNLOAD_FILE_URL = "download_file_url";

//    public static final String DOWNLOAD_FILE_SAVE_PATH = "download_file_save_path";

    public static final String DOWNLOAD_SIZE_KEY = "download_size_key";

    public static final String DOWNLOAD_START_KEY = "download_start_key";

    public static final String ACTION_TYPE_KEY = "ACTION_TYPE";

    public static final String RESULT_RECEIVER = "RESULT_RECEIVER";

    private Bundle resultBundle = new Bundle();

    private ServiceHandler mServiceHandler;

    private Looper mServiceLooper;

    /**
     * 开始下载文件的后台服务，下载完成后自动销毁
     */
    public static void startDownloadFileTask(Context context, Intent intent, ResultReceiver resultReceiver) {
        intent.putExtra(ACTION_TYPE_KEY, DOWNLOAD_FILE);
        intent.putExtra(RESULT_RECEIVER, resultReceiver);
        context.startService(intent);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("MyPersistentService", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        msg.obj = intent;
        mServiceHandler.sendMessage(msg);
        return super.onStartCommand(intent, flags, startId);
    }


    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Intent intent = (Intent) msg.obj;
            if (intent != null) {
                int action = intent.getIntExtra(ACTION_TYPE_KEY, -1);
                switch (action) {

                    case DOWNLOAD_FILE: //下载文件
                        downloadFile(intent);
                        break;

//                case : 以后需要什么其他下载的服务，可自行扩展
//                break;总体

                    default:
                        break;
                }
            }
        }
    }


    private void downloadFile(Intent intent) {
        final ResultReceiver resultReceiver = intent.getParcelableExtra(RESULT_RECEIVER);
//        String fileUrl = intent.getStringExtra(DOWNLOAD_FILE_URL);
//        String savePath = intent.getStringExtra(DOWNLOAD_FILE_SAVE_PATH);

        ArrayList<DownLoadModel> list = intent.getParcelableArrayListExtra(DOWNLOAD_FILE_LIST);
        if (list == null && list.isEmpty()) {
            return;
        }


        DownLoadManager.getInstance().addDownLoadTask(list, "download", new DownLoadBackListener() {
            @Override
            public void onStart(double percent) {
                resultBundle.putDouble(DOWNLOAD_START_KEY, percent);
                resultReceiver.send(DOWNLOAD_DEFAULT_SIZE, resultBundle);
            }

            @Override
            public void onCancel() {
                resultReceiver.send(DOWNLOAD_CANCEL, null);
            }

            @Override
            public void onDownLoading(double percent) {
                resultBundle.putDouble(DOWNLOAD_SIZE_KEY, percent);
                resultReceiver.send(DOWNLOAD_SIZE, resultBundle);
            }

            @Override
            public void onCompleted() {
                resultReceiver.send(RESULT_OK, null);
            }

            @Override
            public void onError(Throwable throwable) {
                resultReceiver.send(RESULT_FAILURE, null);
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        DownLoadManager.getInstance().cancelAll();

        if (mServiceHandler != null) {
            mServiceHandler = null;
            MyLogger.i("MyPersistentService release mServiceHandler object  ");
        }

        if (mServiceLooper != null) {
            mServiceLooper.quit();
            mServiceLooper = null;
            MyLogger.i(" MyPersistentService release mServiceLooper object  ");
        }

        MyLogger.i("MyPersistentService onDestory ! ! ! ");

    }
}
