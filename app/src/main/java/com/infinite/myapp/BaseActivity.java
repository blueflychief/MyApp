package com.infinite.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.infinite.myapp.utils.LoadingUtils;

/**
 * Created by Administrator on 2016-05-29.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        findViews();
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    public abstract int getLayoutId();


    public void findViews() {
    }

    public void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            initData(intent);
        } else {
            initData();
        }

    }


    public void initData(Intent intent) {

    }

    public void initData() {

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
