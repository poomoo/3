/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.presentation;

import com.poomoo.api.AbsAPICallback;
import com.poomoo.api.ApiException;
import com.poomoo.api.NetConfig;
import com.poomoo.api.Network;
import com.poomoo.model.request.QCodeBO;
import com.poomoo.model.request.QResetPDBO;
import com.poomoo.model.response.ResponseBO;
import com.poomoo.parttimejob.view.ForgetPassWordView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/8 14:26.
 */
public class ForgetPassWordPresenter extends BasePresenter {
    private ForgetPassWordView forgetPassWordView;

    public ForgetPassWordPresenter(ForgetPassWordView forgetPassWordView) {
        this.forgetPassWordView = forgetPassWordView;
    }

    public void getCode(String tel) {
        QCodeBO qCodeBO = new QCodeBO(NetConfig.USERACTION, NetConfig.CODE, tel);
        mSubscriptions.add(Network.getUserApi().getCode(qCodeBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<ResponseBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        forgetPassWordView.result(e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBO responseBO) {
                        forgetPassWordView.result(responseBO.msg);
                    }
                }));
    }

    /**
     * 重置密码
     *
     * @param tel
     * @param code
     * @param passWord
     */
    public void confirm(String tel, String code, String passWord) {
        QResetPDBO qResetPDBO = new QResetPDBO(NetConfig.USERACTION, NetConfig.RESET, tel, code, passWord);
        mSubscriptions.add(Network.getUserApi().reSetPassWord(qResetPDBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<ResponseBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        forgetPassWordView.result(e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBO responseBO) {
                        forgetPassWordView.succeed(responseBO.msg);
                    }
                }));
    }
}
