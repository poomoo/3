package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.poomoo.commlib.MyConfig;
import com.poomoo.commlib.MyUtils;
import com.poomoo.commlib.SPUtils;
import com.poomoo.model.response.RUserBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.presentation.LoginPresenter;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.ui.view.LoginView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity implements LoginView {
    @Bind(R.id.img_remember)
    ImageView rememberImg;
    @Bind(R.id.edt_name)
    EditText nameEdt;
    @Bind(R.id.edt_passWord)
    EditText passWordEdt;

    private LoginPresenter loginPresenter;
    private String tel = "";
    private String passWord = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        loginPresenter = new LoginPresenter(this);
        if ((boolean) SPUtils.get(getApplicationContext(), getString(R.string.sp_isRemember), false)) {
            nameEdt.setText((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_phoneNum), ""));
            passWordEdt.setText((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_passWord), ""));
            rememberImg.setImageResource(R.drawable.ic_remember_password_yes);
        }

    }

    @Override
    protected String onSetTitle() {
        return null;
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_login;
    }

    /**
     * 记住密码
     *
     * @param view
     */
    public void toRemember(View view) {
        loginPresenter.remember(getApplicationContext());
    }

    /**
     * 登录
     *
     * @param view
     */
    public void toLogin(View view) {
        tel = nameEdt.getText().toString().trim();
        if (TextUtils.isEmpty(tel)) {
            MyUtils.showToast(getApplicationContext(), MyConfig.phoneNumEmpty);
            return;
        }
        if (!MyUtils.checkPhoneNum(tel)) {
            MyUtils.showToast(getApplicationContext(), MyConfig.phoneNumIllegal);
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
        showProgressDialog(getString(R.string.dialog_msg));
        loginPresenter.login(tel, passWord, MyUtils.getDeviceId(getApplicationContext()));
    }

    /**
     * 忘记密码
     *
     * @param view
     */
    public void toFindPassWord(View view) {
        tel = nameEdt.getText().toString().trim();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.intent_value), tel);
        openActivity(ForgetPassWordActivity.class, bundle);
    }

    /**
     * 注册
     *
     * @param view
     */
    public void toRegister(View view) {
        openActivity(RegisterActivity.class);
    }

    /**
     * 随便看看
     *
     * @param view
     */
    public void toVisit(View view) {
        openActivity(MainActivity.class);
        finish();
    }

    @Override
    public void loginSucceed(RUserBO rUserBO) {
        closeProgressDialog();
        SPUtils.put(getApplicationContext(), getString(R.string.sp_isLogin), true);
        if ((boolean) SPUtils.get(getApplicationContext(), getString(R.string.sp_isRemember), false)) {
            SPUtils.put(getApplicationContext(), getString(R.string.sp_phoneNum), tel);
            SPUtils.put(getApplicationContext(), getString(R.string.sp_passWord), passWord);
        }
        this.application.setUserId(rUserBO.userId);
        this.application.setNickName(rUserBO.nickName);
        this.application.setHeadPic(rUserBO.headPic);
        this.application.setSchoolName(rUserBO.schoolName);
        this.application.setIntoSchoolDt(rUserBO.intoSchoolDt);
        this.application.setTel(rUserBO.tel);
        this.application.setIdCardNum(rUserBO.idCardNum);
        this.application.setIdPicture(rUserBO.idPicture);
        this.application.setPassword(rUserBO.password);
        this.application.setStatus(rUserBO.status);
        this.application.setInviteCode(rUserBO.inviteCode);
        this.application.setDeviceNum(rUserBO.deviceNum);
        this.application.setInsertDt(rUserBO.insertDt);
        this.application.setUpdateDtv(rUserBO.updateDtv);
        openActivity(MainActivity.class);
    }

    @Override
    public void loginFailed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), msg);
    }

    @Override
    public void remember() {
        rememberImg.setImageResource(R.drawable.ic_remember_password_yes);
    }

    @Override
    public void unRemember() {
        rememberImg.setImageResource(R.drawable.ic_remember_password_no);
    }

}
