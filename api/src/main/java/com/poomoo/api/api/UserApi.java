// (c)2016 Flipboard Inc, All Rights Reserved.

package com.poomoo.api.api;


import com.poomoo.model.request.QAuthBO;
import com.poomoo.model.request.QCodeBO;
import com.poomoo.model.request.QJobIntentionBO;
import com.poomoo.model.request.QLoginBO;
import com.poomoo.model.request.QRegisterBO;
import com.poomoo.model.request.QResetPDBO;
import com.poomoo.model.request.QResumeBO;
import com.poomoo.model.request.QUserIdBO;
import com.poomoo.model.response.RIntentionBO;
import com.poomoo.model.response.RResumeBO;
import com.poomoo.model.response.RUserBO;
import com.poomoo.model.response.ResponseBO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface UserApi {
    @POST("lzrb/app/call.htm")
    Observable<ResponseBO> getCode(@Body QCodeBO data);

    @POST("lzrb/app/call.htm")
    Observable<ResponseBO> register(@Body QRegisterBO data);

    @POST("lzrb/app/call.htm")
    Observable<RUserBO> login(@Body QLoginBO data);

    @POST("lzrb/app/call.htm")
    Observable<ResponseBO> reSetPassWord(@Body QResetPDBO data);

    @POST("lzrb/app/call.htm")
    Observable<RResumeBO> resumeDown(@Body QUserIdBO data);

    @POST("lzrb/app/call.htm")
    Observable<ResponseBO> resumeUp(@Body QResumeBO data);

    @POST("lzrb/app/call.htm")
    Observable<ResponseBO> auth(@Body QAuthBO data);

    @POST("lzrb/app/call.htm")
    Observable<ResponseBO> jobIntentionUp(@Body QJobIntentionBO data);

    @POST("lzrb/app/call.htm")
    Observable<RIntentionBO> jobIntentionDown(@Body QUserIdBO data);
}
