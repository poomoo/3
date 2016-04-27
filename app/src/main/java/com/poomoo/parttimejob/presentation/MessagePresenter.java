/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.presentation;

import com.poomoo.api.AbsAPICallback;
import com.poomoo.api.ApiException;
import com.poomoo.api.NetConfig;
import com.poomoo.api.Network;
import com.poomoo.model.request.QMessageBO;
import com.poomoo.model.request.QMessageUpBO;
import com.poomoo.model.response.RMessageBO;
import com.poomoo.model.response.ResponseBO;
import com.poomoo.parttimejob.view.MessageView;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/27 11:52.
 */
public class MessagePresenter extends BasePresenter {
    private MessageView messageView;

    public MessagePresenter(MessageView messageView) {
        this.messageView = messageView;
    }

    public void getMessageList(int userId, int msgId) {
        QMessageBO qMessageBO = new QMessageBO(NetConfig.COMMACTION, NetConfig.MESSAGELIST, userId, msgId);
        mSubscriptions.add(Network.getCommApi().messageList(qMessageBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<RMessageBO>>() {
                    @Override
                    protected void onError(ApiException e) {
                        messageView.failed(e.getMessage());
                    }

                    @Override
                    public void onNext(List<RMessageBO> rMessageBOs) {
                        messageView.downSucceed(rMessageBOs);
                    }
                }));
    }

    public void putMessage(int userId, int msgId, String content) {
        QMessageUpBO qMessageUpBO = new QMessageUpBO(NetConfig.COMMACTION, NetConfig.MESSAGEUP, userId, msgId, content);
        mSubscriptions.add(Network.getCommApi().putMessage(qMessageUpBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<ResponseBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        messageView.upFailed(e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBO responseBO) {
                        messageView.upSucceed(responseBO.msg);
                    }
                }));
    }


}
