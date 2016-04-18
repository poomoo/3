/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;

import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.base.BaseActivity;

/**
 * 实名认证
 * 作者: 李苜菲
 * 日期: 2016/4/18 11:03.
 */
public class AuthActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack();
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_auth);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_auth;
    }
}
