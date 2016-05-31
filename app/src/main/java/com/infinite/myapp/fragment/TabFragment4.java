package com.infinite.myapp.fragment;

import android.widget.TextView;

import com.infinite.myapp.R;
import com.infinite.myapp.base.BaseFragment;

public class TabFragment4 extends BaseFragment {
    private String mTitle = "Default";
    private TextView textView;


    public TabFragment4() {
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
    }
}
