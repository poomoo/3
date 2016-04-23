/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;

import com.poomoo.commlib.LogUtils;
import com.poomoo.model.response.RApplyJobBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.ui.fragment.MyApplyFragment;
import com.poomoo.parttimejob.ui.fragment.TabFragment;

/**
 * 我的申请
 * 作者: 李苜菲
 * 日期: 2016/4/15 10:18.
 */
public class MyApplyActivity extends BaseActivity {
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack();
        type = getIntent().getIntExtra(getString(R.string.intent_value), 0);
        setDefaultMenuItem();
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_myApply);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_my_apply;
    }

    /**
     * 设置默认的页面
     */
    private void setDefaultMenuItem() {
        LogUtils.d(TAG, "setDefaultMenuItem");
        TabFragment mTab = new TabFragment() {
            @Override
            public void onSetupTabs() {
                addTab(getResources().getString(R.string.tab_apply_all), MyApplyFragment.class, RApplyJobBO.JOB_ALL);
                addTab(getResources().getString(R.string.tab_apply_signed), MyApplyFragment.class, RApplyJobBO.JOB_SIGNED);
                addTab(getResources().getString(R.string.tab_apply_hired), MyApplyFragment.class, RApplyJobBO.JOB_HIRED);
                addTab(getResources().getString(R.string.tab_apply_toPost), MyApplyFragment.class, RApplyJobBO.JOB_TOPOST);
                addTab(getResources().getString(R.string.tab_apply_settlement), MyApplyFragment.class, RApplyJobBO.JOB_SETTLEMENT);
            }
        };
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_container, mTab)
                .commit();
        mTab.setPage(type);
    }
}
