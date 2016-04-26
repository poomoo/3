/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.presentation;

import com.poomoo.api.AbsAPICallback;
import com.poomoo.api.ApiException;
import com.poomoo.api.NetConfig;
import com.poomoo.api.Network;
import com.poomoo.model.request.QAuthBO;
import com.poomoo.model.response.RUrl;
import com.poomoo.model.response.ResponseBO;
import com.poomoo.parttimejob.view.AuthView;
import com.poomoo.parttimejob.view.PubView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/26 09:31.
 */
public class AuthPresenter extends BasePresenter {
    private AuthView authView;

    public AuthPresenter(AuthView authView) {
        this.authView = authView;
    }

    /**
     * 上传图片
     *
     * @param file
     */
    public void uploadPic(File file) {
        Map<String, RequestBody> map = new HashMap<>();
        if (file != null) {
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
            map.put("file\"; filename=\"" + file.getName() + "", fileBody);
        }

        mSubscriptions.add(Network.getUploadApi().uploadPic(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<RUrl>() {
                    @Override
                    protected void onError(ApiException e) {
                        authView.failed(e.getMessage());
                    }

                    @Override
                    public void onNext(RUrl rUrl) {
                        authView.upLoadSucceed(rUrl);
                    }
                }));

    }

    /**
     * 实名认证
     *
     * @param userId
     * @param realName
     * @param schoolName
     * @param intoSchoolDt
     * @param idCardNum
     * @param idPicture
     */
    public void auth(int userId, String realName, String schoolName, String intoSchoolDt, String idCardNum, String idPicture) {
        QAuthBO qAuthBO = new QAuthBO(NetConfig.USERACTION, NetConfig.AUTH, userId, realName, schoolName, intoSchoolDt, idCardNum, idPicture);
        mSubscriptions.add(Network.getUserApi().auth(qAuthBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<ResponseBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        authView.failed(e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBO responseBO) {
                        authView.submitSucceed(responseBO.msg);
                    }
                }));
    }
}
