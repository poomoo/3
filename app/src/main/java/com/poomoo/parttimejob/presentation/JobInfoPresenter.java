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
import com.poomoo.model.request.QJobInfoBO;
import com.poomoo.model.request.QRecommendBO;
import com.poomoo.model.response.RJobInfoBO;
import com.poomoo.parttimejob.view.JobInfoView;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
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
    public void queryJobInfo(int jobId) {
        QJobInfoBO qJobInfoBO = new QJobInfoBO(NetConfig.JOBACTION, NetConfig.JOBINFO, jobId);
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
}
