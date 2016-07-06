/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.presentation;

import com.poomoo.api.AbsAPICallback;
import com.poomoo.api.ApiException;
import com.poomoo.api.NetConfig;
import com.poomoo.api.Network;
import com.poomoo.model.request.QCollectCommodityBO;
import com.poomoo.model.request.QCommodityInfoBO;
import com.poomoo.model.response.RCommodityInfoBO;
import com.poomoo.model.response.ResponseBO;
import com.poomoo.parttimejob.view.CommodityInfoView;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者: 李苜菲
 * 日期: 2016/7/5 17:55.
 */
public class CommodityPresenter extends BasePresenter {
    private CommodityInfoView commodityInfoView;

    public CommodityPresenter(CommodityInfoView commodityInfoView) {
        this.commodityInfoView = commodityInfoView;
    }

    public void getCommodityInfo(int goodsId, int userId) {
        mSubscriptions.add(Network.getBuyApi().getCommodityInfo(new QCommodityInfoBO(NetConfig.BUYACTION, NetConfig.COMMODITYINFO, goodsId, userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<RCommodityInfoBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        commodityInfoView.failed(e.getMessage());
                    }

                    @Override
                    public void onNext(RCommodityInfoBO rCommodityInfoBO) {
                        commodityInfoView.succeed(rCommodityInfoBO);
                    }
                }));
    }

    /**
     * 收藏
     *
     * @param goodsId
     * @param userId
     */
    public void collect(int goodsId, int userId) {
        mSubscriptions.add(Network.getBuyApi().collect(new QCollectCommodityBO(NetConfig.BUYACTION, NetConfig.COLLECTCOMMODITY, goodsId, userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<ResponseBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        commodityInfoView.collectFailed(e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBO responseBO) {
                        commodityInfoView.collectSucceed(responseBO.msg);
                    }
                }));
    }

    /**
     * 取消收藏
     *
     * @param goodsId
     * @param userId
     */
    public void cancelCollect(int goodsId, int userId) {
        mSubscriptions.add(Network.getBuyApi().cancelCollect(new QCollectCommodityBO(NetConfig.BUYACTION, NetConfig.CANCELCOLLECTCOMMODITY, goodsId, userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<ResponseBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        commodityInfoView.cancelCollectFailed(e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBO responseBO) {
                        commodityInfoView.cancelCollectSucceed(responseBO.msg);
                    }
                }));
    }
}
