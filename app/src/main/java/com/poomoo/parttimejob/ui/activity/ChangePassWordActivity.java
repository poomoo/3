/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.poomoo.commlib.MyConfig;
import com.poomoo.commlib.MyUtils;
import com.poomoo.commlib.TimeCountDownUtil;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.presentation.ForgetPassWordPresenter;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.ui.view.ForgetPassWordView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 修改密码
 * 作者: 李苜菲
 * 日期: 2016/3/22 11:22.
 */
public class ChangePassWordActivity extends BaseActivity implements ForgetPassWordView {
    @Bind(R.id.txt_account)
    TextView accountTxt;//账号
    @Bind(R.id.txt_getCode)
    TextView getCodeTxt;//获取验证码
    @Bind(R.id.edt_code)
    EditText codeEdt;//验证码
    @Bind(R.id.edt_newPassWord)
    EditText newPassWordEdt;//新密码

    private TimeCountDownUtil countDownTimer;
    private ForgetPassWordPresenter forgetPassWordPresenter;
    private String tel = "";
    private String code = "";
    private String passWord = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack();
        ButterKnife.bind(this);
        forgetPassWordPresenter = new ForgetPassWordPresenter(this);

        tel = application.getTel();
        accountTxt.setText(tel);
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_changePassWord);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_change_password;
    }

    /**
     * 获取验证码
     *
     * @param view
     */
    public void getCode(View view) {
        countDownTimer = new TimeCountDownUtil(MyConfig.SMSCOUNTDOWNTIME, MyConfig.COUNTDOWNTIBTERVAL, getCodeTxt);
        countDownTimer.start();
        forgetPassWordPresenter.getCode(tel);
    }

    /**
     * 确认
     *
     * @param view
     */
    public void toConfirm(View view) {
        code = codeEdt.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            MyUtils.showToast(getApplicationContext(), MyConfig.codeEmpty);
            return;
        }
        passWord = newPassWordEdt.getText().toString().trim();
        if (TextUtils.isEmpty(passWord)) {
            MyUtils.showToast(getApplicationContext(), MyConfig.passWordEmpty);
            return;
        }
        if (!MyUtils.checkPassWord(passWord)) {
            MyUtils.showToast(getApplicationContext(), MyConfig.passWordIllegal);
            return;
        }
        showProgressDialog(getString(R.string.dialog_msg));
        forgetPassWordPresenter.confirm(tel, code, passWord);
    }

    @Override
    public void result(String msg) {
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
