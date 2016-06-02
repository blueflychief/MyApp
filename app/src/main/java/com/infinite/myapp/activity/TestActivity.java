package com.infinite.myapp.activity;

import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.aspsine.multithreaddownload.IDownloadCallBack;
import com.aspsine.multithreaddownload.DownloadException;
import com.aspsine.multithreaddownload.DownloadManager;
import com.aspsine.multithreaddownload.DownloadRequestInfo;
import com.infinite.myapp.R;
import com.infinite.myapp.base.BaseActivity;
import com.infinite.myapp.utils.MyClickListener;
import com.infinite.myapp.utils.MyLogger;
import com.infinite.myapp.view.LoadingLayout;

import java.io.File;


public class TestActivity extends BaseActivity {

    private Button tv_test_download;
    private String url;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_layout;
    }


    @Override
    public void findViews(LoadingLayout contentPanel) {
        super.findViews(contentPanel);

        tv_test_download = (Button) contentPanel.findViewById(R.id.tv_test_download);
        tv_test_download.setOnClickListener(mClick);

    }

    private MyClickListener mClick = new MyClickListener() {
        @Override
        protected void onNotFastClick(View v) {
            switch (v.getId()) {
                case R.id.tv_test_download:
//                    IDownLoad iDownLoad = DownLoadService.getService();
//                    List<String> list = new ArrayList<>();
//                    list.add("http://sw.bos.baidu.com/sw-search-sp/software/50a1366f748/jre_8u91_windows_i586_8.0.910.15.exe");
//                    list.add("http://sw.bos.baidu.com/sw-search-sp/software/7811f6cde4b/QQ_8.3.18038.0_setup.exe");
                    String download_path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "myapp" + File.separator;
//                    ArrayList<DownLoadModel> tempList = buildDownloadList(list,download_path);
//                    iDownLoad.onDownLoadList(download_path,tempList, mDownloadVideoCallback);


//                    url = "http://sw.bos.baidu.com/sw-search-sp/software/50a1366f748/jre_8u91_windows_i586_8.0.910.15.exe";
                    url = "http://sw.bos.baidu.com/sw-search-sp/software/7811f6cde4b/QQ_8.3.18038.0_setup.exe";
                    final DownloadRequestInfo requestInfo = new DownloadRequestInfo.Builder()
                            .setFileName("jre_8u91_windows_i586_8.0.910.15.exe")
                            .setUri(url)
                            .setSavePath(new File(download_path))
                            .build();

                    DownloadManager.getInstance().download(requestInfo, new DownloadCallbackImpl());
                    break;
            }
        }
    };


    //下载回调实现类
    private class DownloadCallbackImpl implements IDownloadCallBack {


        @Override
        public void onStarted() {
            MyLogger.i("------onStarted");
        }

        @Override
        public void onConnecting() {
            MyLogger.i("------onConnecting");
        }

        @Override
        public void onConnected(long connect_time, long total, boolean isRangeSupport) {
            MyLogger.i("------onConnected:" + "---connect_time:" + connect_time + "--total:" + total + "---isRangeSupport:" + isRangeSupport);
        }

        @Override
        public void onProgress(int thread_id, long thread_finished, long finished, long total, int progress) {
            MyLogger.i("------onProgress:" + "---thread_id:" + thread_id + "---thread_finished:" + thread_finished + "---finished:" + finished + "---total:" + total + "----progress:" + progress);
        }

        @Override
        public void onCompleted() {
            MyLogger.i("------onCompleted");
        }

        @Override
        public void onDownloadPaused() {
            MyLogger.i("------onDownloadPaused");
        }

        @Override
        public void onDownloadCanceled() {
            MyLogger.i("------onDownloadCanceled");
        }

        @Override
        public void onFailed(DownloadException e) {
            MyLogger.i("------onFailed");
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        pause(url);
    }

    private void pause(String tag) {
        DownloadManager.getInstance().pause(tag);
    }

//    private ArrayList<DownLoadModel> buildDownloadList(List<String> urls,String path) {
//        if (TextUtils.isEmpty(path)) {
//            MyLogger.e("------下载路径为空！！！");
//            return null;
//        }
//
//        ArrayList<DownLoadModel> list = new ArrayList<>(urls.size());
//        for (String url : urls) {
//            int index = url.lastIndexOf("/");
//            String filename = url.substring(index + 1);
////                File file = new File(download_path + filename);
//            DownLoadModel entity = new DownLoadModel();
//            entity.url = url;
//            entity.saveName = path + filename;
//            list.add(entity);
//        }
//        return list;
//    }
//
//    private IDownLoadCallback mDownloadVideoCallback = new IDownLoadCallback() {
//
//        @Override
//        public void onStartDownload() {
//            MyLogger.i("------onStartDownload");
//        }
//
//        @Override
//        public void onStartDefaultSize(int size) {
//            MyLogger.i("------onStartDefaultSize:"+size);
//        }
//
//        @Override
//        public void onDownLoadSuccess() {
//            MyLogger.i("------onDownLoadSuccess:");
//        }
//
//        @Override
//        public void onDownloadFailed() {
//            MyLogger.i("------onDownloadFailed:");
//        }
//
//        @Override
//        public void onDownloadSize(int size) {
//            MyLogger.i("--------onDownloadSizesize:"+size);
//        }
//    };
}
