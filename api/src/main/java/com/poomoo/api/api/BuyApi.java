/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.api.api;

import com.poomoo.model.request.QBuyBO;
import com.poomoo.model.request.QCollectCommodityBO;
import com.poomoo.model.request.QCommodityCollectionBO;
import com.poomoo.model.request.QCommodityInfoBO;
import com.poomoo.model.response.RBuyBO;
import com.poomoo.model.response.RCommodityInfoBO;
import com.poomoo.model.response.ResponseBO;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 作者: 李苜菲
 * 日期: 2016/7/5 15:50.
 */
public interface BuyApi {
    @POST("lzrb/app/call.htm")
    Observable<List<RBuyBO>> getBuyList(@Body QBuyBO data);

    @POST("lzrb/app/call.htm")
    Observable<RCommodityInfoBO> getCommodityInfo(@Body QCommodityInfoBO data);

    @POST("lzrb/app/call.htm")
    Observable<ResponseBO> collect(@Body QCollectCommodityBO data);

    @POST("lzrb/app/call.htm")
    Observable<ResponseBO> cancelCollect(@Body QCollectCommodityBO data);

    @POST("lzrb/app/call.htm")
    Observable<List<RBuyBO>> getCommodityCollectList(@Body QCommodityCollectionBO data);
}
