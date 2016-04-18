/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;

import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.presentation.PubPresenter;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.ui.view.PubView;

/**
 * 意见反馈
 * 作者: 李苜菲
 * 日期: 2016/4/16 17:00.
 */
public class FeedBackActivity extends BaseActivity implements PubView {
    private PubPresenter pubPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack();
        pubPresenter = new PubPresenter(this);
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_myFeedBack);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_feed_back;
    }

    @Override
    public void failed(String msg) {

    }

    @Override
    public void succeed() {

    }
}
