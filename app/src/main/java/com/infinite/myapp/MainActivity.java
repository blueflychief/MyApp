package com.infinite.myapp;

import android.content.Intent;
import android.os.Handler;

import com.infinite.myapp.view.LoadingLayout;

public class MainActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_main_layout;
    }

    @Override
    public void findViews(LoadingLayout contentPanel) {
        super.findViews(contentPanel);
        mAppBar.setToolbarTitle("我是主页");
//        mAppBar.setRightMenu(this,R.layout.menu_view);
//        mAppBar.setVisibility(View.GONE);
    }

    @Override
    public void initData(Intent intent) {
        super.initData(intent);
        mContentPanel.showLoading();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mContentPanel.showContent();
            }
        }, 4000);
    }
}
