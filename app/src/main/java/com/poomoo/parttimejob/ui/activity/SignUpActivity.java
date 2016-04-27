/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.poomoo.commlib.MyUtils;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.presentation.SignUpPresenter;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.view.SignUpView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/9 12:08.
 */
public class SignUpActivity extends BaseActivity implements SignUpView {
    @Bind(R.id.txt_applicantInfo)
    TextView infoTxt;
    @Bind(R.id.edt_introduce)
    EditText introduceEdt;

    private String info = "";
    private SignUpPresenter signUpPresenter;
    private int jobId;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack();
        ButterKnife.bind(this);
        if (!TextUtils.isEmpty(application.getRealName())) {
            info = application.getRealName();
            infoTxt.setText(info);
        }
        jobId = getIntent().getIntExtra(getString(R.string.intent_value), -1);
        infoTxt.setText(application.getRealName());
        signUpPresenter = new SignUpPresenter(this);
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_signUp);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_signup;
    }

    public void EditResume(View view) {
        openActivity(ResumeActivity.class);
    }

    /**
     * @param view
     */
    public void toSignUp(View view) {
        content = introduceEdt.getText().toString().trim();
        showProgressDialog(getString(R.string.dialog_msg));
        signUpPresenter.signUp(jobId, application.getUserId(), content);
    }

    @Override
    public void succeed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), "报名成功");
        finish();
    }

    @Override
    public void failed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), msg);
    }
}
