package com.infinite.myapp.utils.networkutil.download.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.infinite.myapp.utils.MyLogger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 核心服务类,注 ： 该服务不常驻内存中，用完会自动销毁
 */
public class MyTaskService extends BaseIntentService {

    private static final String TAG = MyTaskService.class.getSimpleName();

    public MyTaskService(){
        super("KNIntentService");
    }


    public static final int DOWNLOAD_FILE = 0x01 ;

    public static final int START_DOWNLOAD = 0x02 ;

    public static final String DOWNLOAD_FILE_URL = "download_file_url";

    public static final String DOWNLOAD_FILE_SAVE_PATH = "download_file_save_path";

    public static final int RESULT_OK = 200 ;

    public static final int RESULT_PROGRESS = 100;

    public static final int RESULT_FAILURE = -1 ;


    /** 开始下载文件的后台服务，下载完成后自动销毁 */
    public static void startDownloadFileTask(Context context , Intent intent , ResultReceiver resultReceiver){
        intent.putExtra(ACTION_TYPE_KEY, DOWNLOAD_FILE);
        intent.putExtra(RESULT_RECEIVER,resultReceiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);
        if(intent != null ){
            int action = intent.getIntExtra(ACTION_TYPE_KEY, -1);
            switch (action){

                case DOWNLOAD_FILE: //下载文件
                    downloadFile(intent);
                    break;

//                case : 以后需要什么其他下载的服务，可自行扩展
//                break;

                default:
                    break;
            }
        }
    }

    private void downloadFile(Intent intent){
        ResultReceiver resultReceiver = intent.getParcelableExtra(RESULT_RECEIVER);
        String fileUrl = intent.getStringExtra(DOWNLOAD_FILE_URL);
        String savePath = intent.getStringExtra(DOWNLOAD_FILE_SAVE_PATH);

        MyLogger.e("downloadFile url == " + fileUrl);

        InputStream is = null;
        FileOutputStream outputStream = null ;
        HttpURLConnection conn = null ;
        try {
            URL url = new URL(fileUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            MyLogger.e("download response:" + response);

            if(response == HttpURLConnection.HTTP_OK) { // 请求正常

                long contentLength = conn.getContentLength();

                MyLogger.e("download content length === " + contentLength);
                long count = 0;
                Bundle resultData = new Bundle();

                is = conn.getInputStream();
                outputStream = new FileOutputStream(savePath);
                int bytesRead = -1;
                int BUFFER_SIZE = 4096;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = is.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    count += bytesRead;
                    MyLogger.e("download file content:" + count);
                    float progress = (float) count / contentLength * 100;
                    MyLogger.e("download progress:" + progress);
//                    resultData = new Bundle();
                    resultData.putFloat("progress",progress);
                    resultReceiver.send(RESULT_PROGRESS,resultData);
                }
                resultReceiver.send(RESULT_OK,null);
            }else{
                resultReceiver.send(RESULT_FAILURE, null); // 如果返回结果中不需要原因，则直接写第二个参数为null
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultReceiver.send(RESULT_FAILURE, null);
        } finally {
            try {
                if(is!= null){
                    is.close();
                }
                if(outputStream != null){
                    outputStream.close();
                }

                if(conn != null){
                    conn.disconnect();
                    conn = null ;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public String readIt(InputStream stream, int len) throws IOException {
//        Reader reader = null;
//        reader = new InputStreamReader(stream, "UTF-8");
//        char[] buffer = new char[len];
//        reader.read(buffer);
//        return new String(buffer);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyLogger.e("MyTaskService 任务完成，已销毁");
    }
}
