/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;

import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.view.ApplicantInfoView;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/9 15:30.
 */
public class ApplicantInfoActivity extends BaseActivity implements ApplicantInfoView{

    private String nickName = "丽丽";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nickName = "丽丽123";
    }

    @Override
    protected String onSetTitle() {
        return nickName;
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_applicant_info;
    }
}
