/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.SPUtils;
import com.poomoo.commlib.StatusBarUtil;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.database.DataBaseHelper;
import com.poomoo.parttimejob.service.LocaleService;
import com.poomoo.parttimejob.ui.base.BaseActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTransparent(this);
        importDB();
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
        startService(new Intent(this, LocaleService.class));
        new Handler().postDelayed(() -> {
            if (!(boolean) SPUtils.get(getApplicationContext(), getString(R.string.sp_isLogin), false)) {
                openActivity(LoginActivity.class);
                application.setCurrCityId((Integer) SPUtils.get(getApplicationContext(), getString(R.string.sp_currCityId), 1));
                application.setCurrCity((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_currCityId), "贵阳"));
                finish();
            } else {
                application.setCurrCityId((Integer) SPUtils.get(getApplicationContext(), getString(R.string.sp_currCityId), 1));
                application.setCurrCity((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_currCityId), "贵阳"));
                application.setUserId((Integer) SPUtils.get(getApplicationContext(), getString(R.string.sp_userId), 0));
                application.setNickName((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_nickName), ""));
                application.setRealName((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_realName), ""));
                application.setTel((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_phoneNum), ""));
                application.setIdCardNum((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_idCardNum), ""));
                application.setHeadPic((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_headPic), ""));
                LogUtils.d(TAG, "HeadPic:" + application.getHeadPic());
                LogUtils.d(TAG, "CurrCityId:" + application.getCurrCityId());
                application.setLogin(true);
                openActivity(MainActivity.class);
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
