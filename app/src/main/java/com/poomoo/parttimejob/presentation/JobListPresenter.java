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
import com.poomoo.model.request.QAllJobBO;
import com.poomoo.model.request.QJobTypeBO;
import com.poomoo.model.response.RTypeBO;
import com.poomoo.parttimejob.ui.view.JobView;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/23 15:54.
 */
public class JobListPresenter extends BasePresenter {
    private JobView jobView;

    public JobListPresenter(JobView jobView) {
        this.jobView = jobView;
    }

    /**
     * 工作类型
     */
    public void getType() {
        QJobTypeBO qJobTypeBO = new QJobTypeBO(NetConfig.JOBACTION, NetConfig.JOBTYPE, 2);
        mSubscriptions.add(Network.getJobApi().getType(qJobTypeBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<RTypeBO>>() {
                    @Override
                    protected void onError(ApiException e) {

                    }

                    @Override
                    public void onNext(List<RTypeBO> rTypeBOs) {
                        jobView.type(rTypeBOs);
                    }
                }));
    }

    /**
     * @param lat
     * @param lng
     * @param cityId
     * @param cateId
     * @param areaId
     * @param sexReq
     * @param workSycle
     * @param workday
     * @param startWorkDt
     * @param orderType
     * @param pageNum
     */
    public void getJobList(double lat, double lng, int cityId, String cateId, String areaId, int sexReq, int workSycle, String workday, String startWorkDt, int orderType, int pageNum) {
        QAllJobBO qAllJobBO = new QAllJobBO(NetConfig.JOBACTION, NetConfig.ALLJOBLIST, lat, lng, cityId, cateId, areaId, sexReq, workSycle, workday, startWorkDt, orderType, pageNum, Page.PAGE_SIZE);
        mSubscriptions.add(Network.getJobApi().allJobList(qAllJobBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<BaseJobBO>>() {
                    @Override
                    protected void onError(ApiException e) {
                        jobView.failed(e.getMessage());
                    }

                    @Override
                    public void onNext(List<BaseJobBO> baseJobBOs) {
                        jobView.succeed(baseJobBOs);
                    }
                }));
    }

}
