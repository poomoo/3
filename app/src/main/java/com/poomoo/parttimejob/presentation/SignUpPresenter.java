/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.presentation;

import com.poomoo.api.AbsAPICallback;
import com.poomoo.api.ApiException;
import com.poomoo.api.NetConfig;
import com.poomoo.api.Network;
import com.poomoo.model.request.QSignUpBO;
import com.poomoo.model.response.ResponseBO;
import com.poomoo.parttimejob.view.SignUpView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/9 12:09.
 */
public class SignUpPresenter extends BasePresenter {
    private SignUpView signUpView;

    public SignUpPresenter(SignUpView signUpView) {
        this.signUpView = signUpView;
    }

    public void signUp(int jobId, int userId, String selfIntro) {
        QSignUpBO qSignUpBO = new QSignUpBO(NetConfig.JOBACTION, NetConfig.SIGNUP, jobId, userId, selfIntro);
        mSubscriptions.add(Network.getJobApi().signUp(qSignUpBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<ResponseBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        signUpView.failed(e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBO responseBO) {
                        signUpView.succeed(responseBO.msg);
                    }
                }));
    }
}
