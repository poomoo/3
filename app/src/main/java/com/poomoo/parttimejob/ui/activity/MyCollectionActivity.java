/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.poomoo.commlib.LogUtils;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.ui.fragment.CommodityCollectFragment;
import com.poomoo.parttimejob.ui.fragment.JobCollectFragment;
import com.poomoo.parttimejob.ui.fragment.TabFragment;

/**
 * 我的收藏
 * 作者: 李苜菲
 * 日期: 2016/4/15 10:18.
 */
public class MyCollectionActivity extends BaseActivity {
    private TabFragment mTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack();
        setDefaultMenuItem();
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_myCollection);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_my_tab;
    }

    /**
     * 设置默认的页面
     */
    @SuppressLint("ValidFragment")
    private void setDefaultMenuItem() {
        LogUtils.d(TAG, "setDefaultMenuItem");
        mTab = new TabFragment() {
            @Override
            public void onSetupTabs() {
                addTab(getResources().getString(R.string.tab_collection_job), JobCollectFragment.class, 0);
                addTab(getResources().getString(R.string.tab_collection_buy), CommodityCollectFragment.class, 0);
            }
        };
//        mTab.setPage(type);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, mTab)
                .commit();
    }
}
