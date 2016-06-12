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
import com.poomoo.model.request.QApplyBO;
import com.poomoo.model.request.QCancelCollectBO;
import com.poomoo.model.request.QCateBO;
import com.poomoo.model.request.QRecommendBO;
import com.poomoo.model.request.QUserIdBO;
import com.poomoo.model.response.ResponseBO;
import com.poomoo.parttimejob.view.JobListView;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/15 16:29.
 */
public class AllJobListPresenter extends BasePresenter {
    private JobListView jobListView;

    public AllJobListPresenter(JobListView jobListView) {
        this.jobListView = jobListView;
    }

    /**
     * 我的申请
     *
     * @param userId
     * @param status
     */
    public void getApplyList(int userId, int status, int currPage) {
        QApplyBO qApplyBO = new QApplyBO(NetConfig.JOBACTION, NetConfig.APPLYLIST, userId, status, currPage, Page.PAGE_SIZE);
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
     * 按类型查询
     */
    public void getJobListByCate(int cateId, int currPage) {
        QCateBO qCateBO = new QCateBO(NetConfig.JOBACTION, NetConfig.CATEJOBLIST, cateId, currPage, Page.PAGE_SIZE);
        mSubscriptions.add(Network.getJobApi().getJobByCate(qCateBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<BaseJobBO>>() {
                    @Override
                    protected void onError(ApiException e) {
                        jobListView.failed(e.getMessage());
                    }

                    @Override
                    public void onNext(List<BaseJobBO> baseJobBOs) {
                        jobListView.succeed(baseJobBOs);
                    }
                }));

    }

    /**
     * 推荐职位
     *
     * @param pageNum
     */
    public void queryRecommendJobs(int userId, int cityId, int pageNum) {
        QRecommendBO qRecommendBO = new QRecommendBO(NetConfig.JOBACTION, NetConfig.RECOMMENDLIST, userId == 0 ? "" : userId + "", cityId, pageNum, Page.PAGE_SIZE);
        mSubscriptions.add(Network.getJobApi().getRecommendList(qRecommendBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<BaseJobBO>>() {
                    @Override
                    protected void onError(ApiException e) {
                        jobListView.failed(e.getMessage());
                    }

                    @Override
                    public void onNext(List<BaseJobBO> baseJobBOs) {
                        jobListView.succeed(baseJobBOs);
                    }
                }));
    }
}
