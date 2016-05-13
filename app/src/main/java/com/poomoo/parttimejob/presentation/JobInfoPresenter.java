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
import com.poomoo.model.request.QCollectBO;
import com.poomoo.model.request.QJobInfoBO;
import com.poomoo.model.request.QRecommendBO;
import com.poomoo.model.response.RJobInfoBO;
import com.poomoo.model.response.ResponseBO;
import com.poomoo.parttimejob.view.JobInfoView;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.android.schedulers.HandlerScheduler;
import rx.schedulers.Schedulers;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/9 11:50.
 */
public class JobInfoPresenter extends BasePresenter {
    private JobInfoView jobInfoView;

    public JobInfoPresenter(JobInfoView jobInfoView) {
        this.jobInfoView = jobInfoView;
    }

    /**
     * 职位详情
     *
     * @param jobId
     */
    public void queryJobInfo(int jobId,int userId) {
        QJobInfoBO qJobInfoBO = new QJobInfoBO(NetConfig.JOBACTION, NetConfig.JOBINFO, jobId,userId);
        mSubscriptions.add(Network.getJobApi().jobInfo(qJobInfoBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<RJobInfoBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        jobInfoView.failed(e.getMessage());
                    }

                    @Override
                    public void onNext(RJobInfoBO rJobInfoBO) {
                        jobInfoView.succeed(rJobInfoBO);
                    }
                }));
    }

    /**
     * 推荐职位
     *
     * @param pageNum
     */
    public void queryRecommendJobs(int pageNum) {
        QRecommendBO qRecommendBO = new QRecommendBO(NetConfig.JOBACTION, NetConfig.RECOMMENDLIST, pageNum, Page.PAGE_SIZE);
        mSubscriptions.add(Network.getJobApi().getRecommendList(qRecommendBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<BaseJobBO>>() {
                    @Override
                    protected void onError(ApiException e) {
                        jobInfoView.failed(e.getMessage());
                    }

                    @Override
                    public void onNext(List<BaseJobBO> baseJobBOs) {
                        jobInfoView.loadRecommendsSucceed(baseJobBOs);
                    }
                }));
    }

    /**
     * 收藏
     *
     * @param jobId
     * @param userId
     */
    public void collet(int jobId, int userId) {
        QCollectBO qCollectBO = new QCollectBO(NetConfig.JOBACTION, NetConfig.COLLECT, jobId, userId, 1);
        mSubscriptions.add(Network.getJobApi().collect(qCollectBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<ResponseBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        jobInfoView.collectFailed(e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBO responseBO) {
                        jobInfoView.collectSucceed(responseBO.msg);
                    }
                }));
    }

    /**
     * 浏览
     *
     * @param jobId
     * @param userId
     */
    public void browse(int jobId, int userId) {
        QCollectBO qCollectBO = new QCollectBO(NetConfig.JOBACTION, NetConfig.COLLECT, jobId, userId, 2);
        mSubscriptions.add(Network.getJobApi().collect(qCollectBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<ResponseBO>() {
                    @Override
                    protected void onError(ApiException e) {
                    }

                    @Override
                    public void onNext(ResponseBO responseBO) {
                    }
                }));
    }
}
