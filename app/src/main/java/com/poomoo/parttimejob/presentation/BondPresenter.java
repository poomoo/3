/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.presentation;

import com.poomoo.api.AbsAPICallback;
import com.poomoo.api.ApiException;
import com.poomoo.api.NetConfig;
import com.poomoo.api.Network;
import com.poomoo.commlib.LogUtils;
import com.poomoo.model.request.QBondBO;
import com.poomoo.model.request.QCheckBO;
import com.poomoo.model.request.QCodeBO;
import com.poomoo.model.response.RUserBO;
import com.poomoo.model.response.ResponseBO;
import com.poomoo.parttimejob.view.BondView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 会员模块
 * 作者: 李苜菲
 * 日期: 2016/4/5 09:55.
 */
public class BondPresenter extends BasePresenter {
    private BondView bondView;

    public BondPresenter(BondView bondView) {
        this.bondView = bondView;
    }

    public void checkPhone(String tel) {
        QCheckBO qCheckBO = new QCheckBO(NetConfig.USERACTION, NetConfig.CHECK, tel);
        mSubscriptions.add(Network.getUserApi().checkPhone(qCheckBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<ResponseBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        bondView.checkFailed(e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBO responseBO) {
                        bondView.checkSucceed("");
                    }
                }));
    }


    public void getCode(String tel) {
        QCodeBO qCodeBO = new QCodeBO(NetConfig.USERACTION, NetConfig.CODE, tel);
        mSubscriptions.add(Network.getUserApi().getCode(qCodeBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<ResponseBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        LogUtils.d("onError" + e.getMessage());
                        bondView.code(e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBO responseBO) {
//                        bondView.code("验证码发送成功");
                        bondView.code(responseBO.msg);
                    }
                }));
    }

    /**
     * 微信号和手机绑定
     *
     * @param wxNum
     * @param nickName
     * @param HeadPic
     * @param tel
     * @param password
     * @param code
     */
    public void bond(String wxNum, String nickName, String HeadPic, String tel, String password, String code) {
        mSubscriptions.add(Network.getUserApi().bond(new QBondBO(NetConfig.USERACTION, NetConfig.BOND, wxNum, nickName, HeadPic, tel, password, code, 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<RUserBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        bondView.loginFailed(e.getMessage());
                    }

                    @Override
                    public void onNext(RUserBO rUserBO) {
                        bondView.loginSucceed(rUserBO);
                    }
                }));
    }

}
