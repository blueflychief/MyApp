package com.infinite.myapp.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.infinite.myapp.MainActivity;
import com.infinite.myapp.R;
import com.infinite.myapp.base.BaseActivity;
import com.infinite.myapp.config.CommonConstants;
import com.infinite.myapp.utils.ImageLoader;
import com.infinite.myapp.utils.PackageUtils;
import com.infinite.myapp.utils.SPUtils;
import com.infinite.myapp.widget.LoadingLayout;


/**
 * 可以添加网络广告图
 */
public class SplashActivity extends BaseActivity {
    private ImageView iv_backgound;
    private final long DELAY_SHOW_MAIN_TIME = 2000;
    private long mStart = 0;
    private final String image_url = "http://img234.ph.126.net/HfqH_UDDlPezziaEMkKUSA==/2124010174260547302.jpg";

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }


    @Override
    public void findViews(LoadingLayout contentPanel) {
        super.findViews(contentPanel);
        mAppBar.setVisibility(View.GONE);
        iv_backgound = (ImageView) contentPanel.findViewById(R.id.iv_backgound);
    }


    @Override
    public void initData(Intent intent) {
        super.initData(intent);
        mStart = System.currentTimeMillis();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if ((boolean) SPUtils.get(SplashActivity.this, CommonConstants.ACTION_SKIP_GUIDE + PackageUtils.getVersionCode(), false)) {
                    jumpToMain();
                } else {
                    jumpToGuide();
                }
            }
        }, DELAY_SHOW_MAIN_TIME);

        ImageLoader.loadImageCenterCrop(this, image_url, -1, iv_backgound);
    }


    private void jumpToGuide() {
        startActivity(new Intent(this, GuideActivity.class));
        finish();
    }

    private void jumpToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
