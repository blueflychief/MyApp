


package com.infinite.myapp.utils;


import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 检测输入的电话号码和邮箱是否合法
 */

public class InfoCheck {

    /**
     * 检测手机号是否合法
     */
    public static boolean checkPhoneNum(String phoneNum) {
        String pattern = "^(((13[0-9])|(14[5,7])|(15([0-3]|[5-9]))|(17[0-9])|(18[0-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$";
        return phoneNum.matches(pattern) || TextUtils.isEmpty(phoneNum);
    }

    /**
     * 检测固话号是否合法
     */
    public static boolean checkLandLine(String phoneNo) {
        String pattern = "^(0[0-9]{2,3}-)?([2-9][0-9]{6,7})+(-[0-9]{1,4})?";
        return phoneNo.matches(pattern) || TextUtils.isEmpty(phoneNo);
    }

    /**
     * 检测邮箱是否合法
     */
    public static boolean checkEmail(String emailName) {
        String pattern = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        return emailName.matches(pattern) || TextUtils.isEmpty(emailName);
    }

    /**
     * 检测密码是否6-16数字或者字母组合
     *
     * @param psd
     * @return
     */
    public static boolean checkPsd(String psd) {
        Pattern pat = Pattern.compile("[0-9a-zA-Z]{6,16}");
        Matcher mat = pat.matcher(psd);
        return mat.matches();
    }

    /**
     * 检测身份证号
     *
     * @param s
     * @return
     */
//    public static boolean checkIdCard(String s) {
//        String pattern = "(^\\d{15}$)|(^\\d{17}([0-9xX])$)";
//        return s.matches(pattern);
//    }
    public static boolean checkIdCard(String s) {
        String pattern15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
        String pattern18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9xX])$";
        return s.matches(pattern18) || s.matches(pattern15);
    }

    /**
     * 判定输入汉字,英文，数字，下划线
     *
     * @param c
     * @return
     */

    public static boolean isReg(char c) {

        String reg = "[a-zA-Z0-9\u4E00-\u9FA5_]";
        return String.valueOf(c).matches(reg);

    }

    /**
     * 检测昵称是否全符合规则
     *
     * @param nikename
     * @return
     */

    public static boolean checkNameChese(String nikename) {
        boolean res = true;
        char[] cTemp = nikename.toCharArray();
        for (int i = 0; i < nikename.length(); i++) {
            if (!isReg(cTemp[i])) {
                res = false;
                break;
            }
        }
        return res;

    }

}
