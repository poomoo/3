/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.commlib;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 作者: 李苜菲
 * 日期: 2016/3/18 14:32.
 */
public class MyUtils {
    private static final String TAG = "MyUtils";

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 校验输入的手机号码是否合法
     *
     * @return
     */
    public static boolean checkPhoneNum(String tel) {
        int len = tel.length();
        if (len != 11)
            return false;
        return true;
    }

    /**
     * 校验输入的密码是否合法
     *
     * @param passWord
     * @return
     */
    public static boolean checkPassWord(String passWord) {
        int len = passWord.length();
        if (len < 6)
            return false;
        return true;
    }

    /**
     * 获取设备的IMEI
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }


    /**
     * 把字符串转为日期
     *
     * @param strDate
     * @return
     * @throws Exception
     */
    public static Date ConvertToDate(String strDate) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.parse(strDate);
    }


    private static ConnectivityManager mCnnManager;

    public static ConnectivityManager getCnnManager(Context context) {
        if (mCnnManager == null)
            mCnnManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return mCnnManager;
    }

    /**
     * 检测是否有网络
     *
     * @return
     */
    public static boolean hasInternet(Context context) {
        return getCnnManager(context).getActiveNetworkInfo() != null && getCnnManager(context).getActiveNetworkInfo().isAvailable();
    }

    /**
     * 格式化报酬显示
     *
     * @param pay
     * @return
     */
    public static SpannableString formatPay(Context context,String pay) {
        SpannableString ss;
        ss = new SpannableString(pay);
        int len = pay.length();
        int pos = 0;
        for (int i = 0; i < len; i++) {
            char a = pay.charAt(i);
            LogUtils.d(TAG, "formatPay:" + i + "a:" + a);
            if (!(a >= '0' && a <= '9')) {
                LogUtils.d(TAG, "数字结束的位置:" + i);
                pos = i;
                break;
            }
        }
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#1fa3e7")), 0, pos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new RelativeSizeSpan(1f), 0, pos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#8e8e8e")), pos, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new RelativeSizeSpan(0.7f), pos, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
