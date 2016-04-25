/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.presentation;

import com.poomoo.api.AbsAPICallback;
import com.poomoo.api.ApiException;
import com.poomoo.api.NetConfig;
import com.poomoo.api.Network;
import com.poomoo.model.base.BaseJobBO;
import com.poomoo.model.request.QApplyBO;
import com.poomoo.model.request.QCollectionBO;
import com.poomoo.parttimejob.view.JobListView;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/15 16:29.
 */
public class ApplyListPresenter extends BasePresenter {
    private JobListView jobListView;

    public ApplyListPresenter(JobListView jobListView) {
        this.jobListView = jobListView;
    }

    /**
     * 我的申请
     *
     * @param userId
     * @param status
     */
    public void getApplyList(int userId, int status) {
        QApplyBO qApplyBO = new QApplyBO(NetConfig.JOBACTION, NetConfig.APPLYLIST, userId, status);
        mSubscriptions.add(Network.getJobApi().getApplyList(qApplyBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<BaseJobBO>>() {
                    @Override
                    protected void onError(ApiException e) {
                        jobListView.failed(e.getMessage());
                    }

                    @Override
                    public void onNext(List<BaseJobBO> list) {
                        jobListView.succeed(list);
                    }
                }));
    }

    /**
     * 我的收藏
     *
     * @param userId
     */
    public void getCollectionList(int userId) {
        QCollectionBO qCollectionBO = new QCollectionBO(NetConfig.JOBACTION, NetConfig.COLLECTIONLIST, userId);
        mSubscriptions.add(Network.getJobApi().getCollectionList(qCollectionBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<BaseJobBO>>() {
                    @Override
                    protected void onError(ApiException e) {
                        jobListView.failed(e.getMessage());
                    }

                    @Override
                    public void onNext(List<BaseJobBO> list) {
                        jobListView.succeed(list);
                    }
                }));
    }
}
