/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.poomoo.commlib.MyConfig;
import com.poomoo.commlib.MyUtils;
import com.poomoo.commlib.TimeCountDownUtil;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.presentation.ForgetPassWordPresenter;
import com.poomoo.parttimejob.ui.view.ForgetPassWordView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 忘记密码
 * 作者: 李苜菲
 * 日期: 2016/3/22 11:22.
 */
public class ForgetPassWordActivity extends BaseActivity implements ForgetPassWordView {
    @Bind(R.id.edt_account)
    EditText accountEdt;//账号
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
        ButterKnife.bind(this);
        forgetPassWordPresenter = new ForgetPassWordPresenter(this);

        tel = getIntent().getStringExtra(getString(R.string.intent_value));
        accountEdt.setText(tel);
        initRightMenu();
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_forgetPassWord);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_forget_password;
    }

    private void initRightMenu() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.rightTxt.setVisibility(View.VISIBLE);
        headerViewHolder.rightTxt.setText(R.string.label_login);
    }

    /**
     * 获取验证码
     *
     * @param view
     */
    public void getCode(View view) {
        tel = accountEdt.getText().toString().trim();
        if (TextUtils.isEmpty(tel)) {
            MyUtils.showToast(getApplicationContext(), MyConfig.phoneNumEmpty);
            return;
        }
        if (!MyUtils.checkPhoneNum(tel)) {
            MyUtils.showToast(getApplicationContext(), MyConfig.phoneNumIllegal);
            return;
        }
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
        tel = accountEdt.getText().toString().trim();
        if (TextUtils.isEmpty(tel)) {
            MyUtils.showToast(getApplicationContext(), MyConfig.phoneNumEmpty);
            return;
        }
        if (!MyUtils.checkPhoneNum(tel)) {
            MyUtils.showToast(getApplicationContext(), MyConfig.phoneNumIllegal);
            return;
        }
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

    public void toDo(View view) {
        finish();
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
