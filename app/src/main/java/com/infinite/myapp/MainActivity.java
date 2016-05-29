package com.infinite.myapp;

import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.infinite.myapp.view.AppBar;
import com.infinite.myapp.view.LoadingHintView;

public class MainActivity extends BaseActivity {
    private AppBar appBar;
    private RelativeLayout rl_container;
    LoadingHintView loadingHintView=null;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void findViews() {
        super.findViews();
        loadingHintView=new LoadingHintView(this);
        appBar = (AppBar) findViewById(R.id.toolbar);
        rl_container = (RelativeLayout) findViewById(R.id.rl_container);
        //左侧导航按钮
        ImageButton imageButton = appBar.getNavButton();
        if (imageButton != null) {
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

        appBar.getMenuCenter().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingHintView.showHintView(LoadingHintView.LoadingStatus.LOADING);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
