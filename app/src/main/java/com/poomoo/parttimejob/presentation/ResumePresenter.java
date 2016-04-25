/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.presentation;

import com.poomoo.api.AbsAPICallback;
import com.poomoo.api.ApiException;
import com.poomoo.api.NetConfig;
import com.poomoo.api.Network;
import com.poomoo.commlib.LogUtils;
import com.poomoo.model.request.QResumeBO;
import com.poomoo.model.request.QUserIdBO;
import com.poomoo.model.response.RResumeBO;
import com.poomoo.model.response.RUrl;
import com.poomoo.model.response.ResponseBO;
import com.poomoo.parttimejob.view.ResumeView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/25 14:10.
 */
public class ResumePresenter extends BasePresenter {
    private ResumeView resumeView;

    public ResumePresenter(ResumeView resumeView) {
        this.resumeView = resumeView;
    }

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
                        resumeView.failed(e.getMessage());
                    }

                    @Override
                    public void onNext(RUrl rUrl) {
                        resumeView.upLoadSucceed(rUrl);
                    }
                }));

    }

    /**
     * 修改简历
     *
     * @param userId
     * @param headPic
     * @param realName
     * @param sex
     * @param height
     * @param birthday
     * @param provinceId
     * @param cityId
     * @param areaId
     * @param schoolName
     * @param email
     * @param qqNum
     * @param contactTel
     * @param workResume
     * @param workExp
     */
    public void changeResume(int userId, String headPic, String realName, int sex, String height, String birthday, int provinceId, int cityId, int areaId, String schoolName, String email, String qqNum, String contactTel, String workResume, String workExp) {
        QResumeBO qResumeBO = new QResumeBO(NetConfig.USERACTION, NetConfig.RESUMEUP, userId, headPic, realName, sex, height, birthday, provinceId, cityId, areaId, schoolName, email, qqNum, contactTel, workResume, workExp);
        mSubscriptions.add(Network.getUserApi().resumeUp(qResumeBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<ResponseBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        resumeView.failed(e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBO responseBO) {
                        resumeView.submitSucceed(responseBO.msg);
                    }
                }));
    }

    /**
     * 下载简历
     *
     * @param userId
     */
    public void downResume(int userId) {
        QUserIdBO qUserIdBO = new QUserIdBO(NetConfig.USERACTION, NetConfig.RESUMEDOWN, userId);
        mSubscriptions.add(Network.getUserApi().resumeDown(qUserIdBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<RResumeBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        resumeView.failed(e.getMessage());
                    }

                    @Override
                    public void onNext(RResumeBO rResumeBO) {
                        resumeView.downSucceed(rResumeBO);
                    }
                }));
    }
}
