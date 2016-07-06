/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.presentation;

import com.poomoo.api.AbsAPICallback;
import com.poomoo.api.ApiException;
import com.poomoo.api.NetConfig;
import com.poomoo.api.Network;
import com.poomoo.model.Page;
import com.poomoo.model.request.QCollectCommodityBO;
import com.poomoo.model.request.QCommodityCollectionBO;
import com.poomoo.model.response.RBuyBO;
import com.poomoo.model.response.ResponseBO;
import com.poomoo.parttimejob.view.MyCommodityCollectionView;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者: 李苜菲
 * 日期: 2016/5/24 10:28.
 */
public class MyCommodityCollectionPresenter extends BasePresenter {
    private MyCommodityCollectionView myCommodityCollectionView;

    public MyCommodityCollectionPresenter(MyCommodityCollectionView myCommodityCollectionView) {
        this.myCommodityCollectionView = myCommodityCollectionView;
    }

    /**
     * 我的收藏
     *
     * @param userId
     */
    public void getCollectionList(int userId, int pageNum) {
        mSubscriptions.add(Network.getBuyApi().getCommodityCollectList(new QCommodityCollectionBO(NetConfig.BUYACTION, NetConfig.COLLECTCOMMODITYLIST, userId, pageNum, Page.PAGE_SIZE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<RBuyBO>>() {
                    @Override
                    protected void onError(ApiException e) {
                        myCommodityCollectionView.failed(e.getMessage());
                    }

                    @Override
                    public void onNext(List<RBuyBO> rBuyBOs) {
                        myCommodityCollectionView.succeed(rBuyBOs);
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
                        myCommodityCollectionView.cancelFailed(e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBO responseBO) {
                        myCommodityCollectionView.cancelSucceed(responseBO.msg);
                    }
                }));
    }
}
