package com.infinite.myapp.activity;

import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.infinite.myapp.R;
import com.infinite.myapp.base.BaseActivity;
import com.infinite.myapp.utils.KKClickListener;
import com.infinite.myapp.utils.database.model.DownLoadModel;
import com.infinite.myapp.utils.network.download.service.DownLoadService;
import com.infinite.myapp.utils.network.download.service.IDownLoad;
import com.infinite.myapp.utils.network.download.service.IDownLoadCallback;
import com.infinite.myapp.view.LoadingLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luo Shuiquan on 6/1/2016.
 */
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

    private KKClickListener mClick = new KKClickListener() {
        @Override
        protected void onKKClick(View v) {
            switch (v.getId()) {
                case R.id.tv_test_download:
                    IDownLoad iDownLoad = DownLoadService.getService();
                    List<String> list = new ArrayList<>();
                    ArrayList<DownLoadModel> tempList = buildDownloadList(list);
                    iDownLoad.onDownLoadList(tempList, mDownloadVideoCallback);
                    break;
            }
        }
    };


    private ArrayList<DownLoadModel> buildDownloadList(List<String> urls) {
        ArrayList<DownLoadModel> list = new ArrayList<>(urls.size());
        final String download_path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "myapp" + File.separator;
        for (String url : urls) {
            int index = url.lastIndexOf("/");
            String filename = url.substring(index + 1);
//                File file = new File(download_path + filename);
            DownLoadModel entity = new DownLoadModel();
            entity.url = url;
            entity.saveName = download_path + filename;
            list.add(entity);
        }
        return list;
    }

    private IDownLoadCallback mDownloadVideoCallback = new IDownLoadCallback() {

        @Override
        public void onStartDownload() {

        }

        @Override
        public void onStartDefaultSize(int size) {

        }

        @Override
        public void onDownLoadSuccess() {

        }

        @Override
        public void onDownloadFailed() {

        }

        @Override
        public void onDownloadSize(int size) {

        }
    };
}
