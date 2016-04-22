/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.os.Handler;

import com.poomoo.commlib.SPUtils;
import com.poomoo.commlib.StatusBarUtil;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.base.BaseActivity;

/**
 * 启动界面
 * 作者: 李苜菲
 * 日期: 2016/4/20 13:54.
 */
public class SplashActivity extends BaseActivity {

    private final static int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTransparent(this);
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
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!(boolean) SPUtils.get(getApplicationContext(), getString(R.string.sp_isLogin), false)) {
                    openActivity(LoginActivity.class);
                    finish();
                } else {
                    application.setUserId((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_userId), ""));
                    application.setNickName((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_nickName), ""));
                    application.setRealName((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_realName), ""));
                    application.setTel((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_phoneNum), ""));
                    application.setIdCardNum((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_idCardNum), ""));
                    openActivity(MainActivity.class);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
