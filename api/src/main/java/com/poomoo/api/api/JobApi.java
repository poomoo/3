/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.api.api;

import com.poomoo.model.base.BaseJobBO;
import com.poomoo.model.request.QApplyBO;
import com.poomoo.model.request.QCollectionBO;
import com.poomoo.model.request.QRecommendBO;
import com.poomoo.model.response.RApplyJobBO;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/15 16:33.
 */
public interface JobApi {
    @POST("lzrb/app/call.htm")
    Observable<List<BaseJobBO>> getRecommendList(@Body QRecommendBO data);

    @POST("lzrb/app/call.htm")
    Observable<List<BaseJobBO>> getApplyList(@Body QApplyBO data);

    @POST("lzrb/app/call.htm")
    Observable<List<BaseJobBO>> getCollectionList(@Body QCollectionBO data);
}
