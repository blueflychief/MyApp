package com.infinite.myapp.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.infinite.myapp.R;
import com.infinite.myapp.base.BaseActivity;
import com.infinite.myapp.utils.MyClickListener;
import com.infinite.myapp.widget.LoadingLayout;

public class LoginActivity extends BaseActivity {
    private EditText et_phone_number;
    private EditText et_pwd;
    private Button bt_login;
    private TextView tv_wixin_login;
    private TextView tv_qq_login;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void findViews(LoadingLayout contentPanel) {
        super.findViews(contentPanel);
        et_phone_number = (EditText) contentPanel.findViewById(R.id.et_phone_number);
        et_pwd = (EditText) contentPanel.findViewById(R.id.et_pwd);
        bt_login = (Button) contentPanel.findViewById(R.id.bt_login);
        tv_wixin_login = (TextView) contentPanel.findViewById(R.id.tv_wixin_login);
        tv_qq_login = (TextView) contentPanel.findViewById(R.id.tv_qq_login);
        bt_login.setOnClickListener(mClick);
        tv_wixin_login.setOnClickListener(mClick);
        tv_qq_login.setOnClickListener(mClick);
    }


    @Override
    public void initData(Intent intent) {
        super.initData(intent);
    }

    private MyClickListener mClick = new MyClickListener() {
        @Override
        protected void onNotFastClick(View v) {
            switch (v.getId()) {
                case R.id.bt_login:
                    break;
                case R.id.tv_wixin_login:
                    break;
                case R.id.tv_qq_login:
                    break;

            }
        }
    };
}
