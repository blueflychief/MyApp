package com.infinite.myapp.utils;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.infinite.myapp.view.MyDialogFragment;

/**
 * Created by Administrator on 2016-05-29.
 */
public class LoadingUtils {
    private final String TAG = LoadingUtils.class.getSimpleName();

    private static MyDialogFragment mDialogFragment;

    public static synchronized MyDialogFragment showLoadingDialog(FragmentActivity activity, String message) {
        showLoadingDialog(activity, message, false);
        return mDialogFragment;
    }

    /**
     * 显示加载提示框
     *
     * @param message 提示信息
     * @return
     */
    public static synchronized MyDialogFragment showLoadingDialog(FragmentActivity activity, String message, boolean cancelable) {
        if (!isShowing()) {
            mDialogFragment = MyDialogFragment.createDialogFragment(message);
//            mDialogFragment.show(activity.getSupportFragmentManager(), "Loading");
            mDialogFragment.setCancelable(cancelable);
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.add(mDialogFragment, "Loading");
            ft.commitAllowingStateLoss();
        } else {
            mDialogFragment.setMessage(message);
        }
        return mDialogFragment;
    }

    /**
     * 关闭加载提示框
     */
    public static synchronized void closeLoadingDialog() {
        if (isShowing()) {
            mDialogFragment.dismissAllowingStateLoss();
            mDialogFragment = null;
        }
    }

    /**
     * 判断是否显示中
     *
     * @return
     */
    public static synchronized boolean isShowing() {
        return mDialogFragment != null && mDialogFragment.getDialog() != null && mDialogFragment.getDialog().isShowing();
    }
}
