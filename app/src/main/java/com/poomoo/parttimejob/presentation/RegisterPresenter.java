/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.presentation;

import com.poomoo.api.AbsAPICallback;
import com.poomoo.api.ApiException;
import com.poomoo.api.NetConfig;
import com.poomoo.api.Network;
import com.poomoo.commlib.LogUtils;
import com.poomoo.model.request.QCodeBO;
import com.poomoo.model.request.QRegisterBO;
import com.poomoo.model.response.ResponseBO;
import com.poomoo.parttimejob.ui.view.RegisterView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 注册
 * 作者: 李苜菲
 * 日期: 2016/4/5 15:55.
 */
public class RegisterPresenter extends BasePresenter {
    private RegisterView registerView;

    public RegisterPresenter(RegisterView registerView) {
        this.registerView = registerView;
    }

    public void getCode(String tel) {
        QCodeBO qCodeBO = new QCodeBO(NetConfig.USERACTION,NetConfig.CODE,tel);
        mSubscriptions.add(Network.getUserApi().getCode(qCodeBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<ResponseBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        LogUtils.d("onError" + e.getMessage());
                        registerView.code(e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBO responseBO) {
//                        registerView.code("验证码发送成功");
                        registerView.code(responseBO.msg);
                    }
                }));
    }


    public void register(String tel, String code, String password, String inviteCode, String deviceNum) {
        QRegisterBO qRegisterBO = new QRegisterBO(NetConfig.USERACTION,NetConfig.REGISTER,tel,password,code,inviteCode,deviceNum,1);
        mSubscriptions.add(Network.getUserApi().register(qRegisterBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<ResponseBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        registerView.registerFailed(e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBO responseBO) {
                        registerView.registerFailed(responseBO.msg);
                    }
                }));
    }
}
