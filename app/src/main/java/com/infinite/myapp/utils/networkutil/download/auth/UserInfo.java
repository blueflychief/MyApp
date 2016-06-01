package com.infinite.myapp.utils.networkutil.download.auth;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfo {
    public String mUID = null;
    public String mUUID = null;
    public String mName = null;
    public String mGender = null;
    public String mAvatar = null;
    public int mGenderNum = 0;
    public int mAge = 0;
    public String mCoachInfo = null;

    public static UserInfo getFromJSONObject(JSONObject object) {
        if (object == null)
            return null;

        UserInfo result = new UserInfo();

        result.mUID = object.optString("mUID");

        result.mUUID = object.optString("mUUID");

        result.mName = object.optString("mName");
        
        result.mGender = object.optString("mGender");
        
        result.mAvatar = object.optString("mAvandar");
        
        result.mGenderNum = object.optInt("mGenderNum");
        
        result.mAge = object.optInt("mAge");

        result.mCoachInfo = object.optString("mCoachInfo");
        

        if (TextUtils.isEmpty(result.mUID)) {
            return null;
        } else {
            return result;
        }
    }

    public JSONObject toJSONObj() {
        JSONObject jObjRes = new JSONObject();
        try {
            if (!TextUtils.isEmpty(mUID))
                jObjRes.put("mUID", mUID);
            if (!TextUtils.isEmpty(mUUID))
                jObjRes.put("mUUID", mUUID);
            if (!TextUtils.isEmpty(mName))
                jObjRes.put("mName", mName);
            if (!TextUtils.isEmpty(mGender))
                jObjRes.put("mGender", mGender);
            if (!TextUtils.isEmpty(mAvatar))
                jObjRes.put("mAvandar", mAvatar);
//            if (!TextUtils.isEmpty(mGenderNum))
                jObjRes.put("mGenderNum", mGenderNum);
//            if (!TextUtils.isEmpty(mAge))
                jObjRes.put("mAge", mAge);
            if(!TextUtils.isEmpty(mCoachInfo)){
                jObjRes.put("mCoachInfo", mCoachInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jObjRes;
    }

    @Override
    public String toString() {
        String strRes = String.format("mUID:%s\r\n" + "mName:%s\r\n", mUID, mName);
        return strRes;
    }
}
