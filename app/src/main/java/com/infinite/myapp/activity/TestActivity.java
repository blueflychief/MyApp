package com.infinite.myapp.activity;

import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.infinite.myapp.R;
import com.infinite.myapp.base.BaseActivity;
import com.infinite.myapp.utils.MyClickListener;
import com.infinite.myapp.utils.MyLogger;
import com.infinite.myapp.utils.databaseutil.DownLoadModel;
import com.infinite.myapp.utils.networkutil.download.DownLoadService;
import com.infinite.myapp.utils.networkutil.download.IDownLoad;
import com.infinite.myapp.utils.networkutil.download.IDownLoadCallback;
import com.infinite.myapp.view.LoadingLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class TestActivity extends BaseActivity {

    private Button tv_test_download;

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
                    IDownLoad iDownLoad = DownLoadService.getService();
                    List<String> list = new ArrayList<>();
//                    list.add("http://sw.bos.baidu.com/sw-search-sp/software/50a1366f748/jre_8u91_windows_i586_8.0.910.15.exe");
//                    list.add("http://sw.bos.baidu.com/sw-search-sp/software/7811f6cde4b/QQ_8.3.18038.0_setup.exe");
                    String download_path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "myapp" + File.separator;
                    ArrayList<DownLoadModel> tempList = buildDownloadList(list,download_path);
                    iDownLoad.onDownLoadList(download_path,tempList, mDownloadVideoCallback);
                    break;
            }
        }
    };


    private ArrayList<DownLoadModel> buildDownloadList(List<String> urls,String path) {
        if (TextUtils.isEmpty(path)) {
            MyLogger.e("------下载路径为空！！！");
            return null;
        }

        ArrayList<DownLoadModel> list = new ArrayList<>(urls.size());
        for (String url : urls) {
            int index = url.lastIndexOf("/");
            String filename = url.substring(index + 1);
//                File file = new File(download_path + filename);
            DownLoadModel entity = new DownLoadModel();
            entity.url = url;
            entity.saveName = path + filename;
            list.add(entity);
        }
        return list;
    }

    private IDownLoadCallback mDownloadVideoCallback = new IDownLoadCallback() {

        @Override
        public void onStartDownload() {
            MyLogger.i("------onStartDownload");
        }

        @Override
        public void onStartDefaultSize(int size) {
            MyLogger.i("------onStartDefaultSize:"+size);
        }

        @Override
        public void onDownLoadSuccess() {
            MyLogger.i("------onDownLoadSuccess:");
        }

        @Override
        public void onDownloadFailed() {
            MyLogger.i("------onDownloadFailed:");
        }

        @Override
        public void onDownloadSize(int size) {
            MyLogger.i("--------onDownloadSizesize:"+size);
        }
    };
}
