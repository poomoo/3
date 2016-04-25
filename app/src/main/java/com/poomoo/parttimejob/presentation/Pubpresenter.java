/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.presentation;

import com.poomoo.api.AbsAPICallback;
import com.poomoo.api.ApiException;
import com.poomoo.api.NetConfig;
import com.poomoo.api.Network;
import com.poomoo.model.request.QFeedBackBO;
import com.poomoo.model.response.ResponseBO;
import com.poomoo.parttimejob.view.PubView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/16 17:03.
 */
public class PubPresenter extends BasePresenter {
    private PubView pubView;

    public PubPresenter(PubView pubView) {
        this.pubView = pubView;
    }

    /**
     * 反馈
     *
     * @param userId
     * @param contact
     * @param content
     */
    public void feedBack(int userId, String contact, String content) {
        QFeedBackBO qFeedBackBO = new QFeedBackBO(NetConfig.COMMACTION, NetConfig.FEEDBACK, userId, contact, content);
        mSubscriptions.add(Network.getCommApi().feedBack(qFeedBackBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<ResponseBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        pubView.failed(e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBO responseBO) {
                        pubView.succeed(responseBO.msg);
                    }
                }));
    }
}
