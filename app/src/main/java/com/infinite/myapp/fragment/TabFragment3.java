package com.infinite.myapp.fragment;

import android.widget.TextView;

import com.infinite.myapp.R;
import com.infinite.myapp.base.BaseFragment;
import com.infinite.myapp.utils.MyLogger;

public class TabFragment3 extends BaseFragment {
    private String mTitle = "Default";
    private TextView textView;


    public TabFragment3() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab;
    }

    @Override
    protected void initView() {
        textView = findView(R.id.tv_content);

    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            mTitle = getArguments().getString("title");
        }
        textView.setText(mTitle);
        MyLogger.i("*****lazyLoadData--finish first load*****" + this.getClass().getSimpleName());
    }
}
