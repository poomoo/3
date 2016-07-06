/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.presentation;

import com.poomoo.api.AbsAPICallback;
import com.poomoo.api.ApiException;
import com.poomoo.api.NetConfig;
import com.poomoo.api.Network;
import com.poomoo.model.Page;
import com.poomoo.model.base.BaseJobBO;
import com.poomoo.model.request.QCancelCollectBO;
import com.poomoo.model.request.QMyCollectionBO;
import com.poomoo.model.response.ResponseBO;
import com.poomoo.parttimejob.view.MyJobCollectionView;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者: 李苜菲
 * 日期: 2016/5/24 10:28.
 */
public class MyJobCollectionPresenter extends BasePresenter {
    private MyJobCollectionView myJobCollectionView;

    public MyJobCollectionPresenter(MyJobCollectionView myJobCollectionView) {
        this.myJobCollectionView = myJobCollectionView;
    }

    /**
     * 我的收藏
     *
     * @param userId
     */
    public void getCollectionList(int userId, int pageNum) {
        QMyCollectionBO qMyCollectionBO = new QMyCollectionBO(NetConfig.JOBACTION, NetConfig.COLLECTIONLIST, userId, pageNum, Page.PAGE_SIZE);
        mSubscriptions.add(Network.getJobApi().getCollectionList(qMyCollectionBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<BaseJobBO>>() {
                    @Override
                    protected void onError(ApiException e) {
                        myJobCollectionView.failed(e.getMessage());
                    }

                    @Override
                    public void onNext(List<BaseJobBO> list) {
                        myJobCollectionView.succeed(list);
                    }
                }));
    }

    /**
     * 取消收藏
     *
     * @param jobId
     * @param userId
     */
    public void cancelCollect(int jobId, int userId) {
        QCancelCollectBO qCancelCollectBO = new QCancelCollectBO(NetConfig.JOBACTION, NetConfig.CANCELCOLLECT, jobId, userId);
        mSubscriptions.add(Network.getJobApi().cancelCollect(qCancelCollectBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<ResponseBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        myJobCollectionView.cancelFailed(e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBO responseBO) {
                        myJobCollectionView.cancelSucceed(responseBO.msg);
                    }
                }));
    }
}
