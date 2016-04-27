/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.presentation;

import com.poomoo.api.AbsAPICallback;
import com.poomoo.api.ApiException;
import com.poomoo.api.NetConfig;
import com.poomoo.api.Network;
import com.poomoo.model.base.BaseRequestBO;
import com.poomoo.model.response.RAboutBO;
import com.poomoo.parttimejob.view.AboutView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/27 09:32.
 */
public class AboutPresenter extends BasePresenter {
    private AboutView aboutView;

    public AboutPresenter(AboutView aboutView) {
        this.aboutView = aboutView;
    }

    public void about() {
        BaseRequestBO baseRequestBO = new BaseRequestBO(NetConfig.COMMACTION, NetConfig.ABOUT);
        mSubscriptions.add(Network.getCommApi().about(baseRequestBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<RAboutBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        aboutView.failed(e.getMessage());
                    }

                    @Override
                    public void onNext(RAboutBO rAboutBO) {
                        aboutView.succeed(rAboutBO);
                    }
                }));
    }
}
