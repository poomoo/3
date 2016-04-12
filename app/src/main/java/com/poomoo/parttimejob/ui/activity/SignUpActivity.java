/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;

import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.view.SignUpView;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/9 12:08.
 */
public class SignUpActivity extends BaseActivity implements SignUpView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_signUp);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_signup;
    }
}
