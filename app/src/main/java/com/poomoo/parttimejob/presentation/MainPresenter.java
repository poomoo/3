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
import com.poomoo.model.base.BaseRequestBO;
import com.poomoo.model.request.QRecommendBO;
import com.poomoo.model.response.RAdBO;
import com.poomoo.parttimejob.view.MainView;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/22 10:06.
 */
public class MainPresenter extends BasePresenter {
    private MainView mainView;

    public MainPresenter(MainView mainView) {
        this.mainView = mainView;
    }

    /**
     * 加载广告
     */
    public void loadAd() {
        BaseRequestBO requestBO = new BaseRequestBO(NetConfig.COMMACTION, NetConfig.AD);
        mSubscriptions.add(Network.getCommApi().getAdvertisements(requestBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<RAdBO>>() {
                    @Override
                    protected void onError(ApiException e) {
                        mainView.failed(e.getMessage());
                    }

                    @Override
                    public void onNext(List<RAdBO> rAdBOs) {
                        mainView.loadAdSucceed(rAdBOs);
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
                        mainView.failed(e.getMessage());
                    }

                    @Override
                    public void onNext(List<BaseJobBO> baseJobBOs) {
                        mainView.loadRecommendsSucceed(baseJobBOs);
                    }
                }));
    }
}
