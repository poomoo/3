/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.api.api;

import com.poomoo.model.response.RWxInfoBO;
import com.poomoo.model.response.RWxTokenBO;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/15 16:33.
 */
public interface WxApi {
    @GET("/sns/oauth2/access_token")
    Observable<RWxTokenBO> getToken(@Query("appid") String appId, @Query("secret") String secret, @Query("code") String code, @Query("grant_type") String grant_type);

    @GET("/sns/userinfo")
    Observable<RWxInfoBO> getInfo(@Query("access_token") String access_token, @Query("openid") String openid);
}
