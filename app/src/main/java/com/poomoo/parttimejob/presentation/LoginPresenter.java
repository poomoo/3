/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.presentation;

import android.content.Context;

import com.poomoo.api.AbsAPICallback;
import com.poomoo.api.ApiException;
import com.poomoo.api.NetConfig;
import com.poomoo.api.Network;
import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyConfig;
import com.poomoo.commlib.SPUtils;
import com.poomoo.model.request.QBondBO;
import com.poomoo.model.request.QIsBondBO;
import com.poomoo.model.request.QLoginBO;
import com.poomoo.model.request.QLoginByWXBO;
import com.poomoo.model.response.RUserBO;
import com.poomoo.model.response.RWxInfoBO;
import com.poomoo.model.response.RWxTokenBO;
import com.poomoo.model.response.ResponseBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.view.LoginView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 会员模块
 * 作者: 李苜菲
 * 日期: 2016/4/5 09:55.
 */
public class LoginPresenter extends BasePresenter {
    private LoginView loginView;

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
    }

    /**
     * 登录
     *
     * @param tel
     * @param password
     * @param deviceNum
     */
    public void login(String tel, String password, String deviceNum) {
        QLoginBO qLoginBO = new QLoginBO(NetConfig.USERACTION, NetConfig.LOGIN, tel, password, deviceNum, 1);
        mSubscriptions.add(Network.getUserApi().login(qLoginBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<RUserBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        loginView.loginFailed(e.getMessage());
                    }

                    @Override
                    public void onNext(RUserBO rUserBO) {
                        loginView.loginSucceed(rUserBO);
                    }
                }));
    }

    /**
     * @param context
     */
    public void remember(Context context) {
        if ((boolean) SPUtils.get(context, context.getString(R.string.sp_isRemember), false)) {
            SPUtils.put(context, context.getString(R.string.sp_isRemember), false);
            loginView.unRemember();
        } else {
            SPUtils.put(context, context.getString(R.string.sp_isRemember), true);
            loginView.remember();
        }
    }

    public void getToken(String code) {
        mSubscriptions.add(Network.getWxApi().getToken(MyConfig.weixinAppId, MyConfig.weixinAppSecret, code, "authorization_code")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<RWxTokenBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        loginView.failed("获取授权失败");
                    }

                    @Override
                    public void onNext(RWxTokenBO rWxTokenBO) {
                        loginView.getToken(rWxTokenBO);
                    }
                }));
    }

    public void getInfo(String access_token, String openid) {
        mSubscriptions.add(Network.getWxApi().getInfo(access_token, openid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<RWxInfoBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        loginView.failed("获取个人信息失败");
                    }

                    @Override
                    public void onNext(RWxInfoBO rWxInfoBO) {
                        loginView.getInfo(rWxInfoBO);
                    }
                }));
    }

    /**
     * 微信号是否已经绑定了手机
     *
     * @param wxNum
     */
    public void isBond(String wxNum) {
        mSubscriptions.add(Network.getUserApi().isBond(new QIsBondBO(NetConfig.USERACTION, NetConfig.ISBOND, wxNum))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<ResponseBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        loginView.notBond(e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBO responseBO) {
                        loginView.isBond(responseBO.msg);
                    }
                }));
    }

    /**
     * 通过微信号登录
     *
     * @param deviceNum
     * @param wxNum
     */
    public void loginByWX(String deviceNum, String wxNum) {
        mSubscriptions.add(Network.getUserApi().loginByWx(new QLoginByWXBO(NetConfig.USERACTION, NetConfig.LOGINBYWX, wxNum, deviceNum, 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<RUserBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        loginView.loginFailed(e.getMessage());
                    }

                    @Override
                    public void onNext(RUserBO rUserBO) {
                        loginView.loginSucceed(rUserBO);
                    }
                }));
    }
}
