/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.presentation;

import com.poomoo.api.AbsAPICallback;
import com.poomoo.api.ApiException;
import com.poomoo.api.NetConfig;
import com.poomoo.api.Network;
import com.poomoo.model.base.BaseRequestBO;
import com.poomoo.model.response.RServiceBO;
import com.poomoo.parttimejob.view.ServiceView;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/27 10:04.
 */
public class ServicePresenter extends BasePresenter {
    private ServiceView serviceView;

    public ServicePresenter(ServiceView serviceView) {
        this.serviceView = serviceView;
    }

    /**
     * 客服列表
     */
    public void getServiceList() {
        BaseRequestBO baseRequestBO = new BaseRequestBO(NetConfig.COMMACTION, NetConfig.SERVICELIST);
        mSubscriptions.add(Network.getCommApi().serviceList(baseRequestBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<RServiceBO>>() {
                    @Override
                    protected void onError(ApiException e) {
                        serviceView.failed(e.getMessage());
                    }

                    @Override
                    public void onNext(List<RServiceBO> rServiceBOs) {
                        serviceView.succeed(rServiceBOs);
                    }
                }));
    }
}
