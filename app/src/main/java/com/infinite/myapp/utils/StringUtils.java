package com.infinite.myapp.utils;


import android.text.Html;
import android.text.Spanned;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

/**
 * Created by Administrator on 2015/9/29.
 */
public class StringUtils {

    public static final int STRING_TYPE_START = 0;
    public static final int STRING_TYPE_MIDDLE = 1;
    public static final int STRING_TYPE_END = 2;

    /**
     * 将JsonArray转换成String数组
     *
     * @param str
     * @return
     */
    public static String[] getJsonToStringArray(JSONArray str) {
        String[] arr = new String[str.size()];
        for (int i = 0; i < str.size(); i++) {
            try {
                arr[i] = str.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arr;
    }


    /**
     * 过滤非数字字符串
     *
     * @param str
     * @return
     */
    public static String filterUnNumber(String str) {
        // 只允数字
//        String regEx = "[^0-9]";
//        Pattern p = Pattern.compile(regEx);
//        Matcher m = p.matcher(str);
        return str.replaceAll("[^0-9]", "");
    }


    /**
     * list 转换  数组
     *
     * @param array
     * @return
     */
    public static int[] toArray(List<Integer> array) {

        int[] ints = new int[array.size()];

        for (int i = 0; i < array.size(); i++) {
            ints[i] = array.get(i);
        }
        return ints;
    }

    /**
     * 保留两位小数
     *
     * @param f
     * @return
     */
    public static String DFTwoPont(double f) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(f);
    }

    /**
     * 保留两位小数不四舍五入
     *
     * @param
     * @return
     */
    public static String DFTwoPont2(double f) {
        DecimalFormat formater = new DecimalFormat();
        formater.setMaximumFractionDigits(2);
        formater.setGroupingSize(0);
        formater.setRoundingMode(RoundingMode.FLOOR);
        return formater.format(f);
    }

    /**
     * 保留一位小数
     *
     * @param f
     * @return
     */
    public static String DFOnePont(double f) {
        DecimalFormat df = new DecimalFormat("#0.0");
        return df.format(f);
    }

    /**
     * 改变部分文字的颜色和大小
     *
     * @param str
     * @param color
     * @return
     */
    public static Spanned ChangeColorAndBigSize(String str, String color, int type) {
        Spanned temp = null;
        if (str.contains("%")) {
            String[] a = str.split("%");
            switch (type) {
                case STRING_TYPE_START: {
                    String source = "<big><big><font color=" + color + ">" + a[0] + "</font></big></big>" + a[1];
                    temp = Html.fromHtml(source);
                    break;
                }
                case STRING_TYPE_MIDDLE: {
                    String source = a[0] + "<big><big><font color=" + color + ">" + a[1] + "</font></big></big>" + a[2];
                    temp = Html.fromHtml(source);
                    break;
                }
                case STRING_TYPE_END: {
                    String source = a[0] + "<big><big><font color=" + color + ">" + a[1] + "</font></big></big>";
                    temp = Html.fromHtml(source);
                    break;
                }
                default:
                    temp = Html.fromHtml(str);
                    break;
            }
        } else {
            temp = Html.fromHtml(str);
        }
        return temp;
    }

    /**
     * 改变部分文字的颜色和大小
     *
     * @param str
     * @param color
     * @return
     */
    public static Spanned ChangeColorAndBigSize(String str, String color) {
        Spanned temp = null;
        if (str.contains("%")) {
            String[] a = str.split("%");
            String source = a[0] + "<big><big><big><font color=" + color + ">" + a[1] + "</font></big></big></big>" + a[2];
            temp = Html.fromHtml(source);
        } else {
            temp = Html.fromHtml(str);
        }
        return temp;
    }

    /**
     * 改变部分字体颜色
     *
     * @param str
     * @param color
     * @return
     */
    public static Spanned ChangeColor(String str, String color, int type) {
        Spanned temp = null;
        if (str.contains("%")) {
            String[] a = str.split("%");
            switch (type) {
                case STRING_TYPE_START: {
                    String source = "<font color=" + color + ">" + a[0] + "</font>" + a[1];
                    temp = Html.fromHtml(source);
                    break;
                }
                case STRING_TYPE_MIDDLE: {
                    String source = a[0] + "<font color=" + color + ">" + a[1] + "</font>" + a[2];
                    temp = Html.fromHtml(source);
                    break;
                }
                case STRING_TYPE_END: {
                    String source = a[0] + "<font color=" + color + ">" + a[1] + "</font>";
                    temp = Html.fromHtml(source);
                    break;
                }
                default:
                    temp = Html.fromHtml(str);
                    break;
            }
        } else {
            temp = Html.fromHtml(str);
        }
        return temp;
    }

    /**
     * 部分字体显示下划线
     *
     * @param str
     * @param type 0,开始部分；1，中间部分；2，结束部分
     * @return
     */
    public static Spanned ShowUnderline(String str, int type) {
        Spanned temp = null;
        if (str.contains("%")) {
            String[] a = str.split("%");
            switch (type) {
                case STRING_TYPE_START: {
                    String source = "<u>" + a[0] + "</u>" + a[1];
                    temp = Html.fromHtml(source);
                    break;
                }
                case STRING_TYPE_MIDDLE: {
                    String source = a[0] + "<u>" + a[1] + "</u>" + a[2];
                    temp = Html.fromHtml(source);
                    break;
                }
                case STRING_TYPE_END: {
                    String source = a[0] + "<u>" + a[1] + "</u>";
                    temp = Html.fromHtml(source);
                    break;
                }
                default:
                    temp = Html.fromHtml(str);
                    break;
            }
        } else {
            temp = Html.fromHtml(str);
        }
        return temp;
    }

    /**
     * 部分字体改变颜色和显示下划线
     *
     * @param str
     * @param color
     * @return
     */
    public static Spanned ChangeColorAndUnderline(String str, String color, int type) {
        Spanned temp = null;
        if (str.contains("%")) {
            String[] a = str.split("%");
            switch (type) {
                case STRING_TYPE_START: {
                    String source = "<u><font color=" + color + ">" + a[0] + "</font></u>" + a[1];
                    temp = Html.fromHtml(source);
                    break;
                }
                case STRING_TYPE_MIDDLE: {
                    String source = a[0] + "<u><font color=" + color + ">" + a[1] + "</font></u>" + a[2];
                    temp = Html.fromHtml(source);
                    break;
                }
                case STRING_TYPE_END: {
                    String source = a[0] + "<u><font color=" + color + ">" + a[1] + "</font></u>";
                    temp = Html.fromHtml(source);
                    break;
                }
                default:
                    temp = Html.fromHtml(str);
                    break;
            }
        } else {
            temp = Html.fromHtml(str);
        }
        return temp;
    }


    public static String[] split(String original, String regex) {
        if (original == null || regex == null) {
            return null;
        }
        int startIndex = 0;
        Vector<String> v = new Vector<String>();
        String[] str = null;
        int index = 0;

        startIndex = original.indexOf(regex);
        if (startIndex == -1) {
            v.addElement(original);
        } else {
            while (startIndex < original.length() && startIndex != -1) {
                String temp = original.substring(index, startIndex);
                v.addElement(temp);
                index = startIndex + regex.length();
                startIndex = original.indexOf(regex,
                        startIndex + regex.length());
            }
            v.addElement(original.substring(index));
        }
        str = new String[v.size()];
        for (int i = 0; i < v.size(); i++) {
            str[i] = v.elementAt(i);
        }
        return str;
    }


    public static String inputStream2String(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

}
