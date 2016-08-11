/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.WindowManager;

import com.baidu.android.pushservice.BasicPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.poomoo.api.HttpLoggingInterceptor;
import com.poomoo.api.Network;
import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyConfig;
import com.poomoo.commlib.SPUtils;
import com.poomoo.commlib.StatusBarUtil;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.service.LocaleService;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

import org.litepal.tablemanager.Connector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 启动界面
 * 作者: 李苜菲
 * 日期: 2016/4/20 13:54.
 */
public class SplashActivity extends BaseActivity {
    private static String DB_PATH = "/data/data/com.poomoo.parttimejob/databases/";
    private static String DB_NAME = "partTimeJob.db";
    private final static int SPLASH_DISPLAY_LENGTH = 3000;
    private boolean isIndex = false;//是否需要引导
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTransparent(this);

        //不显示日志
//        LogUtils.isDebug = false;
//        Network.level = HttpLoggingInterceptor.Level.NONE;

        //统计错误日志到友盟平台
        MobclickAgent.setDebugMode(true);
        MobclickAgent.setCatchUncaughtExceptions(true);

        MyConfig.isRun = true;

        if (!(boolean) SPUtils.get(getApplicationContext(), getString(R.string.sp_isPushBind), false))
            PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, MyConfig.pushKey);

        Resources resource = this.getResources();
        String pkgName = this.getPackageName();
        BasicPushNotificationBuilder basicPushNotificationBuilder = new BasicPushNotificationBuilder();
        basicPushNotificationBuilder.setStatusbarIcon(R.drawable.ic_stat_notify);
        basicPushNotificationBuilder.setStatusbarIcon(resource.getIdentifier("ic_logo", "drawable", pkgName));
        // 推送高级设置，通知栏样式设置为下面的ID
        PushManager.setNotificationBuilder(this, 1, basicPushNotificationBuilder);

        importDB();
        Connector.getDatabase();
        startService(new Intent(this, LocaleService.class));
        isIndex = (boolean) SPUtils.get(getApplicationContext(), getString(R.string.sp_isIndex), true);
        if (isIndex) {
            openActivity(IndexViewPagerActivity.class);
            SPUtils.put(getApplicationContext(), getString(R.string.sp_isIndex), false);
            finish();
        } else
            start();
    }

    @Override
    protected String onSetTitle() {
        return null;
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_splash;
    }

    public void start() {
        new Handler().postDelayed(() -> {
            LogUtils.d(TAG, "IsLogin-->" + (boolean) SPUtils.get(getApplicationContext(), getString(R.string.sp_isLogin), false));
            if (!(boolean) SPUtils.get(getApplicationContext(), getString(R.string.sp_isLogin), false)) {
                openActivity(LoginActivity.class);
                finish();
            } else {
                application.setUserId((Integer) SPUtils.get(getApplicationContext(), getString(R.string.sp_userId), 0));
                application.setNickName((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_nickName), ""));
                application.setRealName((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_realName), ""));
                application.setTel((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_phoneNum), ""));
                application.setIdCardNum((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_idCardNum), ""));
                application.setHeadPic((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_headPic), ""));
                application.setLogin(true);
                LogUtils.d(TAG, "bundle:" + getIntent().getStringExtra(getString(R.string.intent_bundle)));
                if (!TextUtils.isEmpty(getIntent().getStringExtra(getString(R.string.intent_bundle)))) {
                    bundle = new Bundle();
                    bundle.putString(getString(R.string.intent_bundle), getIntent().getStringExtra(getString(R.string.intent_bundle)));
                }
                openActivity(MainActivity.class, bundle);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void importDB() {
        // TODO 自动生成的方法存根
        try {
            // 获得.db文件的绝对路径
            String databaseFilename = DB_PATH + DB_NAME;
            File dir = new File(DB_PATH);
            // 如果目录不存在，创建这个目录
            if (!dir.exists())
                dir.mkdir();
            boolean isExists = (new File(databaseFilename)).exists();
            LogUtils.d(TAG, "importDB:" + isExists);
            // 如果在目录中不存在 .db文件，则从res\assets目录中复制这个文件到该目录
            if (!isExists) {
                LogUtils.d(TAG, "文件不存在");
                // 获得封装.db文件的InputStream对象
                InputStream is = getAssets().open(DB_NAME);
                FileOutputStream fos = new FileOutputStream(databaseFilename);
                byte[] buffer = new byte[7168];
                int count = 0;
                // 开始复制.db文件
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
                LogUtils.d(TAG, "导入数据库文件结束");
            }
        } catch (Exception e) {
            LogUtils.d(TAG, "EXCEPTION:" + e.getMessage());
        }
    }
}
