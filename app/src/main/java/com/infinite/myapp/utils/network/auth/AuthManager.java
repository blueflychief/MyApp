package com.infinite.myapp.utils.network.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.infinite.myapp.utils.MyLogger;

import org.json.JSONException;
import org.json.JSONObject;


public class AuthManager {

    private static final String TAG = AuthManager.class.getSimpleName();

    private static final String TOKEN_LOGIN_ACTIVITY = "token.login";
    private static final String TOKEN_UPDATE = "token.update";
    private static final String TOKEN_LOCALE_CHANGE = "token.locale.change";
    private static final String TOKEN_FEED_BACK_DIALOG = "token.dialog.exit";
    private static final String TOKEN_HTTP_TASK_BASE = "token.http.base";

    private static final String PREF_NAME = "user_list";
    private static final String PREF_KEY_CURR_LOGIN = "curr_login";
    private static final String PREF_KEY_PSW = "psw_";

    private static final String SP_AUTH = "auth.sp";
    private static final String KEY_USER_NAME = "_uname";
    private static final String KEY_DISPLAY_NAME = "_displayname";
    private static final String KEY_USER_PSWD = "_upswd";
    private static final String KEY_AUTO_LOGIN = "_auto";
    private static final String KEY_PSWD_LENGTH = "_upswd_length";

    private static final String SP_LOGIN_STATE = "login.state";
    private static final String KEY_SAVE_PASSWORD = "_savepassword";

    private static AuthManager sGlobalInstance = null;

    private boolean mLogined = false;

    private String mTempUserName = null;
    private String mTempPassword = null;

    public static AuthManager getInstance() {
        if (sGlobalInstance == null) {
            sGlobalInstance = new AuthManager();
        }
        return sGlobalInstance;
    }

    private Context mContext;

    public void init(Context context) {
        mContext = context;
    }

    private AuthManager() {
    }

    public boolean isExplicitLogin() {
        if (!TextUtils.isEmpty(mTempUserName) && !TextUtils.isEmpty(mTempPassword)) {
            return true;
        }
        return false;
    }

    public String getTempUserName() {
        return mTempUserName;
    }

    public String getTempPassword() {
        return mTempPassword;
    }

    public void setTempUserInfo(String username, String password) {
        mTempUserName = username;
        mTempPassword = password;
    }

    public void clearTempeUserInfo() {
        mTempPassword = null;
        mTempUserName = null;
    }

    public UserInfo getLoginUser(boolean getDefault) {
        if (getDefault) {
            UserInfo res = new UserInfo();
            res.mName = "Name";
            res.mUID = "ID";
            return res;
        }
        UserInfo res = null;
        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        String jStr = sp.getString(PREF_KEY_CURR_LOGIN, "");
        if (!TextUtils.isEmpty(jStr)) {
            try {
                JSONObject jObj = new JSONObject(jStr);
                res = UserInfo.getFromJSONObject(jObj);
                if (res == null) {
                    MyLogger.e("Can not create UserInfo from jObj: " + jObj);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    public boolean resetLoginUser(String token, UserInfo tUI) {
        if (!TextUtils.equals(token, TOKEN_LOGIN_ACTIVITY) &&
                !TextUtils.equals(token, TOKEN_UPDATE) &&
                !TextUtils.equals(token, TOKEN_FEED_BACK_DIALOG) &&
                !TextUtils.equals(token, TOKEN_LOCALE_CHANGE)) {
            MyLogger.d("resetCurrUser: Wrong TOKEN: " + token);
            return false;
        }

        if (tUI != null && TextUtils.isEmpty(tUI.mUID)) {
            MyLogger.d("resetCurrUser: Wrong para tUI: " + tUI);
            return false;
        }

        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        String preKey = PREF_KEY_CURR_LOGIN;
        if (tUI == null) {
            ed.remove(preKey);
        } else {
            ed.putString(preKey, tUI.toJSONObj().toString());
        }

        return ed.commit();
    }

    public String getPswByUID(String token, String tUID, boolean getDefault) {
        if (getDefault) {
            return "psw_default";
        }
        if (!TextUtils.equals(token, TOKEN_LOGIN_ACTIVITY) &&
                !TextUtils.equals(token, TOKEN_HTTP_TASK_BASE)) {
            MyLogger.d("getPswByUID: Wrong TOKEN: " + token);
            return null;
        }
        if (TextUtils.isEmpty(tUID)) {
            MyLogger.d("getPswByUID: Wrong para tUID: " + tUID);
            return null;
        }

        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);

        String preKey = PREF_KEY_PSW + tUID;

        String pswRes = sp.getString(preKey, "");
        if (!TextUtils.isEmpty(pswRes)) {
            pswRes = deCodePsw(pswRes);
        }
        return pswRes;
    }

    public boolean resetPswByUID(String token, String tUID, String psw) {
        if (!TextUtils.equals(token, TOKEN_LOGIN_ACTIVITY)) {
            MyLogger.d("resetPswByUID: Wrong TOKEN: " + token);
            return false;
        }
        if (TextUtils.isEmpty(tUID)) {
            MyLogger.d("resetPswByUID: Wrong para tUID: " + tUID);
            return false;
        }

        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        String preKey = PREF_KEY_PSW + tUID;
        if (TextUtils.isEmpty(psw)) {
            ed.remove(preKey);
        } else {
            ed.putString(preKey, enCodePsw(psw));
        }

        return ed.commit();
    }

    private String deCodePsw(String oldPsw) {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String android_id = tm.getDeviceId();
        String strRes = AESUtils.decrypt(android_id, oldPsw);
        return strRes;
    }

    private String enCodePsw(String oldPsw) {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String android_id = tm.getDeviceId();
        String strRes = AESUtils.encrypt(android_id, oldPsw);
        return strRes;
    }


    /**
     * Set login status
     *
     * @param login
     */
    public synchronized void setLoginStatus(boolean login) {
        mLogined = login;
    }

    public synchronized void setSavePasswd(boolean b) {

        SharedPreferences.Editor editor = mContext.getSharedPreferences(SP_LOGIN_STATE, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.putBoolean(KEY_SAVE_PASSWORD, b);
        editor.commit();
    }

    /**
     * Update account
     * Note: GUEST account will not be updated.
     *
     * @param name
     * @param pswd
     */
    public synchronized void updateAccount(String name, String displayname, String pswd, boolean auto) {
        addAccount(name, displayname, pswd, auto);
    }

    /**
     * Add account
     *
     * @param name
     * @param pswd
     */
    private synchronized void addAccount(String name, String displayname, String pswd, boolean auto) {
        SharedPreferences.Editor editor = mContext
                .getSharedPreferences(SP_AUTH, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_DISPLAY_NAME, displayname);

        editor.putString(KEY_USER_PSWD,
                AuthUtility.base64Encode(pswd.getBytes()));
        editor.putBoolean(KEY_AUTO_LOGIN, auto);
        editor.putInt(KEY_PSWD_LENGTH, pswd.length());
        editor.commit();
    }
}
