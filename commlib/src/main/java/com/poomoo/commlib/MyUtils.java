/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.commlib;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 作者: 李苜菲
 * 日期: 2016/3/18 14:32.
 */
public class MyUtils {
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
}
