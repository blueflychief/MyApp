package com.infinite.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;

import com.infinite.myapp.activity.TestActivity;
import com.infinite.myapp.base.BaseActivity;
import com.infinite.myapp.fragment.TabFragment1;
import com.infinite.myapp.fragment.TabFragment2;
import com.infinite.myapp.fragment.TabFragment3;
import com.infinite.myapp.fragment.TabFragment4;
import com.infinite.myapp.utils.MyClickListener;
import com.infinite.myapp.utils.MyLogger;
import com.infinite.myapp.utils.ToastUtils;
import com.infinite.myapp.widget.LoadingLayout;
import com.infinite.myapp.widget.TabNavigationButton;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    private MainPagerAdapter mAdapter;
    private List<Integer> mTabIds = new ArrayList<>();
    private List<TabNavigationButton> mTabButtons = new ArrayList<TabNavigationButton>();
    private int mCurrentId = 0;

    private String[] mTitles = new String[]{
            "First Fragment!",
            "Second Fragment!",
            "Third Fragment!",
            "Fourth Fragment!"};


    @Override
    public int getLayoutId() {
        return R.layout.activity_main_layout;
    }

    @Override
    public void findViews(LoadingLayout contentPanel) {
        super.findViews(contentPanel);
        setOverflowShowingAlways();
        setToolbar();
        mViewPager = (ViewPager) contentPanel.findViewById(R.id.id_viewpager);

    }

    private void setToolbar() {
        mAppBar.setToolbarTitle("我是主页");
//        mAppBar.setRightMenu(this,R.layout.menu_view);
//        mAppBar.setVisibility(View.GONE);

        mAppBar.setOnMenuItemClickListener(this);


        mAppBar.getNavButton().setOnClickListener(new MyClickListener() {
            @Override
            protected void onNotFastClick(View v) {
//                Intent intent=new Intent(MainActivity.this, MyWebView.class);
//                intent.putExtra("url","http://www.baidu.com");
//                startActivity(intent);

                startActivity(new Intent(MainActivity.this, TestActivity.class));
            }
        });
    }

    private void initTabIndicator() {
        TabNavigationButton one = (TabNavigationButton) findViewById(R.id.id_indicator_one);
        TabNavigationButton two = (TabNavigationButton) findViewById(R.id.id_indicator_two);
        TabNavigationButton three = (TabNavigationButton) findViewById(R.id.id_indicator_three);
        TabNavigationButton four = (TabNavigationButton) findViewById(R.id.id_indicator_four);
        mTabIds.add(R.id.id_indicator_one);
        mTabIds.add(R.id.id_indicator_two);
        mTabIds.add(R.id.id_indicator_three);
        mTabIds.add(R.id.id_indicator_four);
        mTabButtons.add(one);
        mTabButtons.add(two);
        mTabButtons.add(three);
        mTabButtons.add(four);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        one.setIconAlpha(1.0f);
        mCurrentId = mTabIds.get(0);
    }

    @Override
    public void onPageSelected(int arg0) {
        MyLogger.i("----onPageSelected" + arg0);
        mCurrentId = mTabIds.get(arg0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        // Log.e("TAG", "position = " + position + " , positionOffset = "
        // + positionOffset);

        if (positionOffset > 0) {
            TabNavigationButton left = mTabButtons.get(position);
            TabNavigationButton right = mTabButtons.get(position + 1);

            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        changeTab(v);
    }

    private void changeTab(View v) {
        MyLogger.i("----onClick" + v.getId());
        if (mCurrentId == v.getId()) {
            return;
        }
        resetOtherTabs();
        switch (v.getId()) {
            case R.id.id_indicator_one:
                mTabButtons.get(0).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.id_indicator_two:
                mTabButtons.get(1).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.id_indicator_three:
                mTabButtons.get(2).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.id_indicator_four:
                mTabButtons.get(3).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(3, false);
                break;
        }
        mCurrentId = v.getId();
    }

    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置其他的Tab
     */
    private void resetOtherTabs() {
        for (int i = 0; i < mTabButtons.size(); i++) {
            mTabButtons.get(i).setIconAlpha(0);
        }
    }

    @Override
    public void initData(Intent intent) {
        super.initData(intent);
        initViewPager();
        mContentPanel.showContent();
    }

    private void initViewPager() {
        TabFragment1 tabFragment1 = new TabFragment1();
        Bundle args1 = new Bundle();
        args1.putString("title", mTitles[0]);
        tabFragment1.setArguments(args1);
        mTabs.add(tabFragment1);

        TabFragment2 tabFragment2 = new TabFragment2();
        Bundle args2 = new Bundle();
        args2.putString("title", mTitles[1]);
        tabFragment2.setArguments(args2);
        mTabs.add(tabFragment2);

        TabFragment3 tabFragment3 = new TabFragment3();
        Bundle args3 = new Bundle();
        args3.putString("title", mTitles[2]);
        tabFragment3.setArguments(args3);
        mTabs.add(tabFragment3);

        TabFragment4 tabFragment4 = new TabFragment4();
        Bundle args4 = new Bundle();
        args4.putString("title", mTitles[3]);
        tabFragment4.setArguments(args4);
        mTabs.add(tabFragment4);

        mAdapter = new MainPagerAdapter(getSupportFragmentManager(), this, mTabs, mTitles);

        initTabIndicator();
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_right_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                ToastUtils.showToast(this, "setting");
                break;
            case R.id.action_share:
                ToastUtils.showToast(this, "share");
                break;
            case R.id.ab_search:
                ToastUtils.showToast(this, "search");
                break;
            default:
                break;
        }
        return true;
    }
}
