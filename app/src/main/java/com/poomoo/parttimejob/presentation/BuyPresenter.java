/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.presentation;

import com.poomoo.api.AbsAPICallback;
import com.poomoo.api.ApiException;
import com.poomoo.api.NetConfig;
import com.poomoo.api.Network;
import com.poomoo.model.Page;
import com.poomoo.model.base.BaseRequestBO;
import com.poomoo.model.request.QBuyBO;
import com.poomoo.model.response.RAboutBO;
import com.poomoo.model.response.RBuyBO;
import com.poomoo.parttimejob.view.AboutView;
import com.poomoo.parttimejob.view.BuyView;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/27 09:32.
 */
public class BuyPresenter extends BasePresenter {
    private BuyView buyView;

    public BuyPresenter(BuyView buyView) {
        this.buyView = buyView;
    }

    public void getBuyList(int pageNum) {
        mSubscriptions.add(Network.getBuyApi().getBuyList(new QBuyBO(NetConfig.BUYACTION, NetConfig.BUYLIST, pageNum, Page.PAGE_SIZE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<RBuyBO>>() {
                    @Override
                    protected void onError(ApiException e) {
                        buyView.failed(e.getMessage());
                    }

                    @Override
                    public void onNext(List<RBuyBO> rBuyBOs) {
                        buyView.succeed(rBuyBOs);
                    }
                }));
    }
}
