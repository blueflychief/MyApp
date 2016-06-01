package com.infinite.myapp.utils.network.download.service;


import com.infinite.myapp.utils.database.model.DownLoadModel;

import java.util.ArrayList;

public interface IDownLoad {

    void onDownLoadList(ArrayList<DownLoadModel> urls, IDownLoadCallback callback);
}
