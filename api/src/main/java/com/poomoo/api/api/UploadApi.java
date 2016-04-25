package com.poomoo.api.api;

import com.poomoo.model.response.RUrl;

import java.io.File;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * 公共接口
 * 作者: 李苜菲
 * 日期: 2016/4/8 15:50.
 */
public interface UploadApi {
    @Multipart
    @POST("lzrb/app/common/uploadPic.ajax")
//    Observable<RUrl> uploadPic( @Part("file\"; filename=\"image.png\"") File file);
    Observable<RUrl> uploadPic(@PartMap Map<String, RequestBody> params);
}
