package com.infinite.myapp.utils;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.infinite.myapp.view.MyDialogFragment;

/**
 * Created by Administrator on 2016-05-29.
 */
public class LoadingUtils {
    private final String TAG = LoadingUtils.class.getSimpleName();

    private static MyDialogFragment mKKDialogFragment;

    public static synchronized MyDialogFragment showLoadingDialog(FragmentActivity activity, String message) {
        showLoadingDialog(activity, message, false);
        return mKKDialogFragment;
    }

    /**
     * 显示加载提示框
     *
     * @param message 提示信息
     * @return
     */
    public static synchronized MyDialogFragment showLoadingDialog(FragmentActivity activity, String message, boolean cancelable) {
        if (!isShowing()) {
            mKKDialogFragment = MyDialogFragment.createDialogFragment(message);
//            mKKDialogFragment.show(activity.getSupportFragmentManager(), "Loading");
            mKKDialogFragment.setCancelable(cancelable);
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.add(mKKDialogFragment, "Loading");
            ft.commitAllowingStateLoss();
        } else {
            mKKDialogFragment.setMessage(message);
        }
        return mKKDialogFragment;
    }

    /**
     * 关闭加载提示框
     */
    public static synchronized void closeLoadingDialog() {
        if (isShowing()) {
            mKKDialogFragment.dismissAllowingStateLoss();
            mKKDialogFragment = null;
        }
    }

    /**
     * 判断是否显示中
     *
     * @return
     */
    public static synchronized boolean isShowing() {
        return mKKDialogFragment != null && mKKDialogFragment.getDialog() != null && mKKDialogFragment.getDialog().isShowing();
    }
}
