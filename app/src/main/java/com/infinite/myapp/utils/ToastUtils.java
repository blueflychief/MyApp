package com.infinite.myapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;


public class ToastUtils {
    private static String oldMsg;
    protected static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;

    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }

    public static void showToast(final Context context, String s) {
//        if (!PermissionUtils.isNotificationEnabled()&&!AppHelper.isNotifacationDialogShowed()) {
//            if (context instanceof Activity) {
//                showDialog(context);
//                AppHelper.setNotifacationDialogShowed(true);
//            }
//        } else {
        show(context, s);
//        }
    }

    private static void showDialog(final Context context) {
        new AlertDialog.Builder(context)
                .setTitle("应用通知权限被禁用")
                .setMessage("开启通知权限后才能收到应用提示哦")
                .setPositiveButton("现在开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = PermissionUtils.getAppDetailSettingIntent();
                        if (intent != null) {
                            context.startActivity(intent);
                        }
                    }
                })
                .setNegativeButton("下次开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(false)
                .show();

    }

    private static void show(Context context, String s) {
        if (TextUtils.isEmpty(s)) {
            s = "";
        }
        if (toast == null) {
            toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
        oneTime = twoTime;
    }
}
