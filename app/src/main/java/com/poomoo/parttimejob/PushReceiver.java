/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyConfig;
import com.poomoo.commlib.SPUtils;
import com.poomoo.parttimejob.ui.activity.MainActivity;

import java.util.List;

/**
 * 消息推送
 * 作者: 李苜菲
 * 日期: 2016/6/12 15:45.
 */
public class PushReceiver extends PushMessageReceiver {
    @Override
    public void onBind(Context context, int errorCode, String appid, String userId, String channelId, String requestId) {
        String responseString = "onBind errorCode=" + errorCode + " appid="
                + appid + " userId=" + userId + " channelId=" + channelId
                + " requestId=" + requestId;
        LogUtils.d(TAG, responseString);
        SPUtils.put(context, context.getString(R.string.sp_isPushBind), true);
        SPUtils.put(context, context.getString(R.string.sp_channelId), channelId);
        if (errorCode == 0) {
            // 绑定成功
            LogUtils.d(TAG, "绑定成功");
        }
    }

    @Override
    public void onUnbind(Context context, int errorCode, String s) {
        String responseString = "onUnbind errorCode=" + errorCode + " s=" + s;
        LogUtils.d(TAG, responseString);
        if (errorCode == 0) {
            // 绑定成功
            LogUtils.d(TAG, "解绑定成功");
        }
    }

    @Override
    public void onSetTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onDelTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onListTags(Context context, int i, List<String> list, String s) {

    }

    @Override
    public void onMessage(Context context, String s, String s1) {

    }

    @Override
    public void onNotificationClicked(Context context, String s, String s1, String s2) {
    }

    @Override
    public void onNotificationArrived(Context context, String title, String description, String customContentString) {
        String notifyString = "通知到达 onNotificationArrived  title=\"" + title
                + "\" description=\"" + description + "\" customContent="
                + customContentString;
        LogUtils.d(TAG, notifyString);
        initNotify(context, title, description);
        showIntentActivityNotify(context);
    }


    NotificationManager mNotificationManager;
    /**
     * Notification构造器
     */
    NotificationCompat.Builder mBuilder;
    /**
     * Notification的ID
     */
    int notifyId = 100;

    /**
     * 初始化通知栏
     */
    private void initNotify(Context context, String title, String content) {
        mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(title)
                .setContentText(content)
                .setTicker("兼职GO推送:" + content)//通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                .setSmallIcon(R.drawable.ic_stat_notify)//
                .setLargeIcon(((BitmapDrawable) context.getResources().getDrawable(R.mipmap.ic_logo)).getBitmap());
    }

    /**
     * 显示通知栏点击跳转到指定Activity
     */
    public void showIntentActivityNotify(Context context) {
        //判断app进程是否存活
        PendingIntent pendingIntent;
        if (MyConfig.isRun) {
            LogUtils.i(TAG, "the app process is alive");
            Intent resultIntent = new Intent(context, MainActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle args = new Bundle();
            args.putString(context.getString(R.string.intent_bundle), "launcher");
            resultIntent.putExtras(args);
            pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            LogUtils.i(TAG, "the app process is dead" + context.getPackageName());
            Intent resultIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            Bundle args = new Bundle();
            args.putString(context.getString(R.string.intent_bundle), "launcher");
            resultIntent.putExtras(args);
            pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(notifyId, mBuilder.build());
    }
}
