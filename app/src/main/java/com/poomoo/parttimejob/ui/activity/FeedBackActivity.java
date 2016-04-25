/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.poomoo.commlib.MyConfig;
import com.poomoo.commlib.MyUtils;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.presentation.PubPresenter;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.view.PubView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 意见反馈
 * 作者: 李苜菲
 * 日期: 2016/4/16 17:00.
 */
public class FeedBackActivity extends BaseActivity implements PubView {
    @Bind(R.id.edt_mail)
    EditText mailEdt;
    @Bind(R.id.edt_content)
    EditText contentEdt;

    private String mail;
    private String content;
    private PubPresenter pubPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack();
        ButterKnife.bind(this);
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

    public void toSubmit(View view) {
        mail = mailEdt.getText().toString().trim();
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(mail);
        if (!matcher.matches()) {
            MyUtils.showToast(getApplicationContext(), MyConfig.emailIllegal);
            return;
        }

        content = contentEdt.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            MyUtils.showToast(getApplicationContext(), MyConfig.contentEmpty);
            return;
        }
        showProgressDialog(getString(R.string.dialog_msg));
        pubPresenter.feedBack(application.getUserId(), mail, content);
    }

    @Override
    public void failed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), msg);
    }

    @Override
    public void succeed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), msg);
        finish();
    }
}
