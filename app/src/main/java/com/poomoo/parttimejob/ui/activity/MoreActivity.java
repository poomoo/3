/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.base.BaseActivity;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/16 17:10.
 */
public class MoreActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack();
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_more);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_more;
    }

    /**
     * 2修改密码
     *
     * @param view
     */
    public void toChangePassWord(View view) {
        openActivity(ChangePassWordActivity.class);
    }

    /**
     * 2关于
     *
     * @param view
     */
    public void toAbout(View view) {

    }
}
