package com.infinite.myapp.utils.networkutil.download;


import com.infinite.myapp.utils.databaseutil.DownLoadModel;

import java.util.ArrayList;

public interface IDownLoad {

    void onDownLoadList(String download_path,ArrayList<DownLoadModel> urls, IDownLoadCallback callback);
}
