package com.poomoo.api.api;

import com.poomoo.model.request.BaseRequestBO;
import com.poomoo.model.response.RAreaBO;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 公共接口
 * 作者: 李苜菲
 * 日期: 2016/4/8 15:50.
 */
public interface CommApi {

    @POST("lzrb/app/call.htm")
    Observable<List<RAreaBO>> getCitys(@Body BaseRequestBO data);
}
