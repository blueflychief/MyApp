package com.infinite.myapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.infinite.myapp.MainActivity;
import com.infinite.myapp.R;
import com.infinite.myapp.base.BaseActivity;
import com.infinite.myapp.config.CommonConstants;
import com.infinite.myapp.utils.PackageUtils;
import com.infinite.myapp.utils.SPUtils;
import com.infinite.myapp.widget.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity {
    private ViewPager vp_guide;
    private List<ImageView> mImageViews;
    private ImageView iv_loading;
    private Button bt_experience;
    private TextView tv_jump;
    private int[] imageId = new int[]{R.mipmap.guide_loading_1,
            R.mipmap.guide_loading_2,
            R.mipmap.guide_loading_3,
            R.mipmap.guide_loading_4,
    };

    @Override
    public int getLayoutId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        return R.layout.activity_guide;
    }

    @Override
    public void findViews(LoadingLayout contentPanel) {
        super.findViews(contentPanel);
        mAppBar.setVisibility(View.GONE);
        vp_guide = (ViewPager) contentPanel.findViewById(R.id.vp_guide);
        iv_loading = (ImageView) contentPanel.findViewById(R.id.iv_loading);
        bt_experience = (Button) contentPanel.findViewById(R.id.bt_experience);
        tv_jump = (TextView) contentPanel.findViewById(R.id.tv_jump);
    }

    @Override
    public void initData(Intent intent) {
        super.initData(intent);
        initImages();
        initViewPager();
    }

    private void initViewPager() {
        vp_guide.setAdapter(new MyAdatper());

        vp_guide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                iv_loading.setImageResource(imageId[arg0]);
                if (arg0 == mImageViews.size() - 1) {
                    bt_experience.setVisibility(View.VISIBLE);
                    tv_jump.setVisibility(View.INVISIBLE);
                    bt_experience.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            jumpToMain();
                        }
                    });
                } else {
                    bt_experience.setVisibility(View.INVISIBLE);
                    tv_jump.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void initImages() {
        ImageView imageView1 = new ImageView(this);
        ImageView imageView2 = new ImageView(this);
        ImageView imageView3 = new ImageView(this);
        ImageView imageView4 = new ImageView(this);

        imageView1.setBackgroundColor(Color.GREEN);
        imageView2.setBackgroundColor(Color.BLUE);
        imageView3.setBackgroundColor(Color.YELLOW);
        imageView4.setBackgroundColor(Color.CYAN);

//        imageView1.setBackgroundResource(R.mipmap.guide1);
//        imageView2.setBackgroundResource(R.mipmap.guide3);
//        imageView3.setBackgroundResource(R.mipmap.guide2);
//        imageView4.setBackgroundResource(R.mipmap.guide4);

        mImageViews = new ArrayList();

        mImageViews.add(imageView1);
        mImageViews.add(imageView2);
        mImageViews.add(imageView3);
        mImageViews.add(imageView4);
    }

    class MyAdatper extends PagerAdapter {
        @Override
        public int getCount() {
            return mImageViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViews.get(position));
            return mImageViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void jumpToMain() {
        SPUtils.put(this, CommonConstants.ACTION_SKIP_GUIDE + PackageUtils.getVersionCode(), true);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        jumpToMain();
    }
}
