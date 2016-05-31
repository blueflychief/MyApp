package com.infinite.myapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016-05-31.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments = null;
    private String[] mTitles;

    private Context mContext = null;

    public MainPagerAdapter(FragmentManager fm, Context ctx, List<Fragment> list, String[] titles) {
        super(fm);
        mContext = ctx;
        mFragments = list;
        mTitles = titles;
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
