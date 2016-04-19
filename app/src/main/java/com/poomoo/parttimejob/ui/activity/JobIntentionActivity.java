/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;

import com.poomoo.commlib.MyUtils;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.ui.custom.FreeTimeView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 求职意向设置
 * 作者: 李苜菲
 * 日期: 2016/4/18 11:53.
 */
public class JobIntentionActivity extends BaseActivity {
    @Bind(R.id.freeTimeView)
    FreeTimeView freeTimeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        freeTimeView.setOnItemClickListener(new FreeTimeView.OnItemClickListener() {
            @Override
            public void OnItemClick(int index) {
//                MyUtils.showToast(getApplicationContext(),"点击了第"+index+"个");
            }
        });
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_jobIntention);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_job_intention;
    }
}
