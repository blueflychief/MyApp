package com.infinite.myapp.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class AppHelper {

    /**
     * 判断进程是否运行
     *
     * @return
     */
    public static boolean isProessRunning(Context context, String proessName) {

        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : lists) {
            if (info.processName.equals(proessName)) {
                isRunning = true;
            }
        }

        return isRunning;
    }


//
//    public static boolean isGpsDialogShowed() {
//        return StaticConstant.IS_OPEN_GPS_DIALOG_SHOWED;
//    }
//
//    public static void setGpsDialogShowed(boolean b) {
//        StaticConstant.IS_OPEN_GPS_DIALOG_SHOWED = b;
//    }
//
//    public static boolean isNotifacationDialogShowed() {
//        return StaticConstant.IS_OPEN_NOTIFICATION_DIALOG_SHOWED;
//    }
//
//    public static void setNotifacationDialogShowed(boolean b) {
//        StaticConstant.IS_OPEN_NOTIFICATION_DIALOG_SHOWED = b;
//    }

    /**
     * 重置GPS弹窗和通知弹窗提示
     */
//    public static void resetDialogShowStatus() {
//        setGpsDialogShowed(false);
//        setNotifacationDialogShowed(false);
//    }
}
