/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;

import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.ui.view.JobInfoView;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/9 11:48.
 */
public class JobInfoActivity extends BaseActivity implements JobInfoView{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack();
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_jobInfo);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_job_info;
    }
}
