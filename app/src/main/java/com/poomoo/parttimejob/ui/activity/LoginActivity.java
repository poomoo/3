package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyConfig;
import com.poomoo.commlib.MyUtils;
import com.poomoo.commlib.SPUtils;
import com.poomoo.model.response.RUserBO;
import com.poomoo.model.response.RWxInfoBO;
import com.poomoo.model.response.RWxTokenBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.application.MyApplication;
import com.poomoo.parttimejob.presentation.LoginPresenter;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.ui.custom.RoundImageView2;
import com.poomoo.parttimejob.view.LoginView;
import com.tencent.mm.sdk.modelmsg.SendAuth;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity implements LoginView {
    @Bind(R.id.img_loginAvatar)
    RoundImageView2 avatarImg;
    @Bind(R.id.img_remember)
    ImageView rememberImg;
    @Bind(R.id.edt_name)
    EditText nameEdt;
    @Bind(R.id.edt_passWord)
    EditText passWordEdt;

    public static LoginPresenter loginPresenter;
    private String tel = "";
    private String passWord = "";
    public static LoginActivity instance = null;
    private RWxInfoBO rWxInfoBO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        instance = this;

        loginPresenter = new LoginPresenter(this);
        if ((boolean) SPUtils.get(getApplicationContext(), getString(R.string.sp_isRemember), false)) {
            passWordEdt.setText((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_passWord), ""));
            rememberImg.setImageResource(R.drawable.ic_remember_password_yes);
        }
        nameEdt.setText((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_phoneNum), ""));
        LogUtils.d(TAG, "头像:" + SPUtils.get(this, getString(R.string.sp_headPic), ""));
        if (!TextUtils.isEmpty((String) SPUtils.get(this, getString(R.string.sp_headPic), ""))) {
            Glide.with(this).load(SPUtils.get(this, getString(R.string.sp_headPic), "")).placeholder(R.drawable.ic_defalut_avatar).into(avatarImg);
        }
        application.setCurrCityId((Integer) SPUtils.get(getApplicationContext(), getString(R.string.sp_currCityId), 1));
        application.setCurrCity((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_currCity), "贵阳"));
    }

    @Override
    protected String onSetTitle() {
        return null;
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_login;
    }

//    private void initSubscribers() {
//        RxBus.with(this)
//                .setEvent(Events.EventEnum.DELIVER_AVATAR)
//                .setEndEvent(FragmentEvent.DESTROY)
//                .onNext((events) -> {
//                    Glide.with(this).load(application.getHeadPic()).into(avatarImg);
//                }).create();
//    }

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
        loginPresenter.login(tel, passWord, MyUtils.getChannelId(getApplicationContext()));
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
        this.application.setLogin(false);
        openActivity(MainActivity.class);
        finish();
    }

    @Override
    public void loginSucceed(RUserBO rUserBO) {
        closeProgressDialog();
        SPUtils.put(getApplicationContext(), getString(R.string.sp_isLogin), true);
        if ((boolean) SPUtils.get(getApplicationContext(), getString(R.string.sp_isRemember), false))
            SPUtils.put(getApplicationContext(), getString(R.string.sp_passWord), passWord);

        SPUtils.put(getApplicationContext(), getString(R.string.sp_phoneNum), tel);
        this.application.setUserId(rUserBO.userId);
        this.application.setNickName(rUserBO.nickName);
        this.application.setRealName(rUserBO.realName);
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
        this.application.setLogin(true);

        SPUtils.put(getApplicationContext(), getString(R.string.sp_userId), application.getUserId());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_nickName), application.getNickName());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_realName), application.getRealName());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_headPic), application.getHeadPic());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_schoolName), application.getSchoolName());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_intoSchoolDt), application.getIntoSchoolDt());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_idPicture), application.getIdPicture());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_idCardNum), application.getIdCardNum());

        openActivity(MainActivity.class);
        finish();
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

    @Override
    public void getToken(RWxTokenBO rWxTokenBO) {
        loginPresenter.getInfo(rWxTokenBO.access_token, rWxTokenBO.openid);
    }

    @Override
    public void getInfo(RWxInfoBO rWxInfoBO) {
//        closeProgressDialog();
//        LogUtils.d(TAG, "微信个人信息:" + rWxInfoBO.toString());
        loginPresenter.isBond(rWxInfoBO.openid);
        this.rWxInfoBO = rWxInfoBO;
    }

    @Override
    public void isBond(String msg) {
        closeProgressDialog();
        showProgressDialog("正在登录...");
        loginPresenter.loginByWX(MyUtils.getChannelId(getApplicationContext()), rWxInfoBO.openid);
    }

    @Override
    public void notBond(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), msg);
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.intent_wxNum), rWxInfoBO.openid);
        bundle.putString(getString(R.string.intent_wxNickName), rWxInfoBO.nickname);
        bundle.putString(getString(R.string.intent_wxHeadPic), rWxInfoBO.headimgurl);
        openActivity(BondActivity.class, bundle);
    }

    /**
     * 微信登录
     *
     * @param view
     */
    public void wxLogin(View view) {
        SendAuth.Req req = new SendAuth.Req();

        //授权读取用户信息
        req.scope = "snsapi_userinfo";

        //自定义信息
        req.state = "兼职GO测试";

        //向微信发送请求
        MyApplication.api.sendReq(req);
        showProgressDialog(getString(R.string.dialog_msg));
        LogUtils.d(TAG, "微信登录调用");
//        // 构造一个Resp
//        GetMessageFromWX.Resp resp = new GetMessageFromWX.Resp();
//        LogUtils.d(TAG, "GetMessageFromWX:" + resp.getType() + ":" + "");
    }

    @Override
    public void failed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), msg);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO 自动生成的方法存根
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MyConfig.isRun = false;
            finish();
            System.exit(0);//正常退出App
        }
        return true;
    }
}
