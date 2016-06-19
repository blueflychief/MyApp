package com.infinite.myapp.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.infinite.myapp.R;
import com.infinite.myapp.utils.LoadingUtils;
import com.infinite.myapp.widget.AppBar;
import com.infinite.myapp.widget.LoadingLayout;

/**
 * Created by Administrator on 2016-05-29.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected LoadingLayout mContentPanel;
    protected AppBar mAppBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_layout);
        mContentPanel = (LoadingLayout) findViewById(R.id.ll_loading);
        mAppBar = (AppBar) findViewById(R.id.app_bar);
        //必须在setSupportActionBar之前调用
        mAppBar.setTitle("");
        mAppBar.setNavigationIcon(R.mipmap.icon_navigation_left);
        setSupportActionBar(mAppBar);
        getLayoutInflater().inflate(getLayoutId(), mContentPanel);
        findViews(mContentPanel);
        getData();
    }


    public void findViews(LoadingLayout contentPanel) {
    }

    public void getData() {
        Intent intent = getIntent();
        initData(intent);

    }


    public abstract int getLayoutId();

    public void initData(Intent intent) {

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoadingUtils.closeLoadingDialog();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

}
