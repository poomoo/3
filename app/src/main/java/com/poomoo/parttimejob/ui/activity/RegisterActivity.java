/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.poomoo.commlib.MyConfig;
import com.poomoo.commlib.MyUtils;
import com.poomoo.commlib.TimeCountDownUtil;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.presentation.RegisterPresenter;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.view.RegisterView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: 李苜菲
 * 日期: 2016/3/22 14:34.
 */
public class RegisterActivity extends BaseActivity implements RegisterView {
    @Bind(R.id.edt_account)
    EditText accountEdt;//账号
    @Bind(R.id.txt_getCode)
    TextView getCodeTxt;//获取验证码
    @Bind(R.id.edt_code)
    EditText codeEdt;//验证码
    @Bind(R.id.edt_passWord)
    EditText passWordEdt;//密码
    @Bind(R.id.edt_inviteCode)
    EditText inviteCodedEdt;//邀请码
    @Bind(R.id.chk_agree)
    CheckBox agreeChk;//是否同意注册协议

    private RegisterPresenter registerPresenter;
    private TimeCountDownUtil countDownTimer;
    private String tel;//电话
    private String code;//验证码
    private String passWord;//密码
    private String inviteCode;//邀请码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initRightMenu();
        registerPresenter = new RegisterPresenter(this);
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_register);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_register;
    }

    private void initRightMenu() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.rightTxt.setVisibility(View.VISIBLE);
        headerViewHolder.rightTxt.setText(R.string.label_login);
    }

    public void toDo(View view) {
        finish();
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
        registerPresenter.getCode(tel);
    }

    public void protocol(View view) {
    }

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
        passWord = passWordEdt.getText().toString().trim();
        if (TextUtils.isEmpty(passWord)) {
            MyUtils.showToast(getApplicationContext(), MyConfig.passWordEmpty);
            return;
        }
        if (!MyUtils.checkPassWord(passWord)) {
            MyUtils.showToast(getApplicationContext(), MyConfig.passWordIllegal);
            return;
        }
        if (!agreeChk.isChecked())
            MyUtils.showToast(getApplicationContext(), "请阅读并同意用户协议");
        inviteCode = inviteCodedEdt.getText().toString().trim();
        registerPresenter.register(tel, code, passWord, inviteCode, MyUtils.getDeviceId(this));
    }

    @Override
    public void code(String msg) {
        MyUtils.showToast(getApplicationContext(), msg);
    }

    @Override
    public void registerSucceed(String msg) {
        MyUtils.showToast(getApplicationContext(), msg);
    }

    @Override
    public void registerFailed(String msg) {
        MyUtils.showToast(getApplicationContext(), msg);
    }
}
