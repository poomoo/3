/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.commlib;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
     * 获取绑定的百度云推送ChannelId
     *
     * @param context
     * @return
     */
    public static String getChannelId(Context context) {
//        TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
//        return tm.getChannelId();
        return (String) SPUtils.get(context, "sp_channelId", "");
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
    public static String[] formatPay(String pay) {
        String[] ss = new String[2];
//        ss = new SpannableString(pay);
        int len = pay.length();
        int pos = 0;
        for (int i = 0; i < len; i++) {
            char a = pay.charAt(i);
//            LogUtils.d(TAG, "formatPay:" + i + "a:" + a);
            if (!(a >= '0' && a <= '9')) {
//                LogUtils.d(TAG, "数字结束的位置:" + i);
                pos = i;
                break;
            }
        }
//        if (color) {
//            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#1fa3e7")), 0, pos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#8e8e8e")), pos, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//        ss.setSpan(new RelativeSizeSpan(1f), 0, pos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(new RelativeSizeSpan(0.7f), pos, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss[0] = pay.substring(0, pos);
        ss[1] = pay.substring(pos, len);
        return ss;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 判断应用是否已经启动
     *
     * @param context     一个context
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public static boolean isAppAlive(Context context, String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
                Log.i("NotificationLaunch", String.format("the %s is running, isAppAlive return true", packageName));
                return true;
            }
        }
        Log.i("NotificationLaunch", String.format("the %s is not running, isAppAlive return false", packageName));
        return false;
    }
}
