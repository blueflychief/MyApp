package com.infinite.myapp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;


import com.infinite.myapp.MyApplication;
import com.infinite.myapp.config.CommonConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.util.List;

/**
 * Created by Administrator on 4/14/2016.
 */
public class PackageUtils {
    /**
     * Compare component's require version and container's version.
     *
     * @return
     */
    public static boolean verifyCompatibility(String containerVersion,
                                              String requireVersion) {
        // get container version
        String[] versions = containerVersion.split("-");
        if (versions != null
                && versions.length == CommonConstants.HOTPOT_CONTAINER_VERSION_SEGMENT_COUNT) {
            return compareVersion(versions[0], requireVersion);
        }
        return false;
    }

    private static boolean compareVersion(String a, String b) {
        String[] a1 = StringUtils.split(a, ".");
        if (a == null || a.length() == 0 || a1 == null || a1.length == 0) {
            return true; // can not get container version anyway
        }
        String[] b1 = StringUtils.split(b, ".");
        if (b == null || b.length() == 0 || b1 == null || b1.length == 0) {
            return true; // can not get require version anyway
        }
        int compareLength = a1.length < b1.length ? a1.length : b1.length;
        for (int i = 0; i < compareLength; i++) { // version *.*.*
            if (Integer.parseInt(a1[i]) < Integer.parseInt(b1[i])) {
                return false;
            } else if (Integer.parseInt(a1[i]) > Integer.parseInt(b1[i])) {
                return true;
            }
        }
        return true;
    }


    /**
     * Get container's version
     *
     * @return
     */
    public static String getContainerVersion() {
        Context context = MyApplication.getInstance().getApplicationContext();
        String version = null;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    CommonConstants.HOTPOT_MAIN_APP_PACKAGENAME, 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * Get component require's verison
     *
     * @return
     */
    public static String getComponentRequireVersion() {
        Context context = MyApplication.getInstance().getApplicationContext();
        if (context == null) {
            return null;
        }

        String version = null;
        AssetManager assmgr = context.getAssets();
        XmlResourceParser parser = null;
        try {
            parser = assmgr.openXmlResourceParser("AndroidManifest.xml");
            parser.next(); // skip start document
            parser.next();
            parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE,
                    "manifest");
            while (parser.nextTag() == XmlPullParser.START_TAG) {
                String nodeName1 = parser.getName();
                parser.require(XmlPullParser.START_TAG,
                        XmlPullParser.NO_NAMESPACE, nodeName1);
                if (CommonConstants.HOTPOT_COMPAT_TAG_NAME.equals(nodeName1)) {
                    version = parser.getAttributeValue(
                            CommonConstants.HOTPOT_NAME_SPACE,
                            CommonConstants.HOTPOT_COMPAT_ATTR_NAME);
                    parser.next();
                } else {
                    while (parser.next() > 0) {
                        // skip xml tag we don't care
                        if (parser.getEventType() == XmlPullParser.END_TAG
                                && parser.getName().equals(nodeName1))
                            break;
                    }
                }
                parser.require(XmlPullParser.END_TAG,
                        XmlPullParser.NO_NAMESPACE, nodeName1);
            }
            parser.require(XmlPullParser.END_TAG, XmlPullParser.NO_NAMESPACE,
                    "manifest");
            parser.close();
        } catch (Exception e) {
            if (parser != null) {
                parser.close();
            }
            e.printStackTrace();
            version = null;
        }
        return version;
    }

    public static String getAllModuleVersionDescription(String intent_filter) {
        JSONArray moduleArray = new JSONArray();
        Context context = MyApplication.getInstance().getApplicationContext();
        PackageManager pm = context.getPackageManager();

        // add all server module
        Intent queryIntent = new Intent(intent_filter);
        List<ResolveInfo> list = pm.queryIntentActivities(queryIntent,
                PackageManager.MATCH_DEFAULT_ONLY);
        long now = System.currentTimeMillis();
        if (list != null && list.size() > 0) {
            for (ResolveInfo info : list) {
                PackageInfo pinfo;
                try {
                    pinfo = pm.getPackageInfo(info.activityInfo.packageName, 0);
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("app_id", info.activityInfo.packageName);
                        obj.put("app_build", "" + pinfo.versionCode);
                        moduleArray.put(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        // add main app
        try {
            PackageInfo pinfo = pm.getPackageInfo(
                    CommonConstants.HOTPOT_MAIN_APP_PACKAGENAME, 0);
            JSONObject main = new JSONObject();
            main.put("app_id", context.getPackageName());
            main.put("app_build", "" + pinfo.versionCode);
            moduleArray.put(main);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return moduleArray.toString();
    }

    public static String getVersionCode() {
        Context context = MyApplication.getInstance().getApplicationContext();
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo;
        String versionCodeDesc = "unknown";
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
            versionCodeDesc = String.valueOf(packInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCodeDesc;
    }
}
