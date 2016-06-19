package com.infinite.myapp.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.infinite.myapp.R;
import com.infinite.myapp.base.BaseActivity;
import com.infinite.myapp.utils.MyClickListener;
import com.infinite.myapp.widget.LoadingLayout;
import com.infinite.myapp.widget.MyWebView;


public class TestActivity extends BaseActivity {

    private Button tv_test_download;
    private Button tv_test_webview;
    private Button tv_test_login;
    private String url;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_layout;
    }


    @Override
    public void findViews(LoadingLayout contentPanel) {
        super.findViews(contentPanel);

        tv_test_download = (Button) contentPanel.findViewById(R.id.tv_test_download);
        tv_test_webview = (Button) contentPanel.findViewById(R.id.tv_test_webview);
        tv_test_login = (Button) contentPanel.findViewById(R.id.tv_test_login);
        tv_test_download.setOnClickListener(mClick);
        tv_test_webview.setOnClickListener(mClick);
        tv_test_login.setOnClickListener(mClick);

    }

    private MyClickListener mClick = new MyClickListener() {
        @Override
        protected void onNotFastClick(View v) {
            switch (v.getId()) {
                case R.id.tv_test_download:
                    startActivity(new Intent(TestActivity.this, SVGAnimationActivity.class));
                    break;
                case R.id.tv_test_login:
                    startActivity(new Intent(TestActivity.this, LoginActivity.class));
                    break;
                case R.id.tv_test_webview:
                    Intent intent = new Intent(TestActivity.this, MyWebView.class);
                    intent.putExtra("url", "http://www.baidu.com");
                    startActivity(intent);
                    break;
            }
        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
