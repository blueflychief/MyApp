package com.infinite.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.infinite.myapp.utils.LoadingUtils;
import com.infinite.myapp.view.AppBar;
import com.infinite.myapp.view.LoadingLayout;

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
        getLayoutInflater().inflate(getLayoutId(), mContentPanel);
        findViews(mContentPanel);
        getData();
    }


    public void findViews(LoadingLayout contentPanel) {
    }

    public void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            initData(intent);
        } else {
            initData();
        }

    }


    public abstract int getLayoutId();

    public void initData(Intent intent) {

    }

    public void initData() {

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

}
