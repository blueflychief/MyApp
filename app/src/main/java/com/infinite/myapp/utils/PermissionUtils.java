package com.infinite.myapp.utils;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;


import com.infinite.myapp.MyApplication;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class PermissionUtils {

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    /**
     * 是否有录音权限
     *
     * @return
     */
    public static boolean hasRecordAudioPermission() {
        return PackageManager.PERMISSION_GRANTED == getPackageManager().checkPermission("android.permission.RECORD_AUDIO", "com.kk.user");
    }

    /**
     * 是否有摄像头权限
     *
     * @return
     */
    public static boolean hasCameraPermission() {
        return PackageManager.PERMISSION_GRANTED == getPackageManager().checkPermission("android.permission.CAMERA", "com.kk.user");
    }

    private static PackageManager getPackageManager() {
        return MyApplication.getInstance().getPackageManager();
    }

    /**
     * 通知是否被禁
     *
     * @return
     */
    public static boolean isNotificationEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ApplicationInfo appInfo = MyApplication.getInstance().getApplicationInfo();
            String pkg = MyApplication.getInstance().getApplicationContext().getPackageName();
            int uid = appInfo.uid;
            try {
                //sdk在19以上才行
                AppOpsManager mAppOps = (AppOpsManager) MyApplication.getInstance().getSystemService(Context.APP_OPS_SERVICE);
                Class appOpsClass = null;
                appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
                int value = (int) opPostNotificationValue.get(Integer.class);
                return ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 打开当前应用的设置界面
     *
     * @return
     */
    public static Intent getAppDetailSettingIntent() {
        try {
            Intent localIntent = new Intent();
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", MyApplication.getInstance().getApplicationContext().getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.setAction(Intent.ACTION_VIEW);
                localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                localIntent.putExtra("com.android.settings.ApplicationPkgName", MyApplication.getInstance().getApplicationContext().getPackageName());
            }
            return localIntent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 打开GPS的设置界面
     *
     * @return
     */
    public static Intent getLocationSettingIntent() {
        try {
            Intent intent = new Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            return intent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 是否打开了gps
     *
     * @return
     */
    public static boolean isGPSEnable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LocationManager locationManager = (LocationManager) MyApplication.getInstance().getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                return false;
            }
        } else {
            String provider = Settings.Secure.getString(MyApplication.getInstance().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (!provider.contains("gps")) {
                return false;
            }
        }
        return true;
    }
}
