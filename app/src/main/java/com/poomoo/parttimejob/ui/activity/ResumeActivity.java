/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.base.BaseActivity;

import butterknife.Bind;

/**
 * 个人简历
 * 作者: 李苜菲
 * 日期: 2016/4/18 10:27.
 */
public class ResumeActivity extends BaseActivity {
    @Bind(R.id.img_userAvatar)
    ImageView userAvatarImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack();
        initView();
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_resume);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_resume;
    }


    private void initView() {
        userAvatarImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }
}
