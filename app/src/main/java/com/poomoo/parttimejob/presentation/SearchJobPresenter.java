/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.presentation;

import com.poomoo.api.AbsAPICallback;
import com.poomoo.api.ApiException;
import com.poomoo.api.NetConfig;
import com.poomoo.api.Network;
import com.poomoo.model.Page;
import com.poomoo.model.base.BaseJobBO;
import com.poomoo.model.request.QSearchBO;
import com.poomoo.parttimejob.ui.view.SearchJobView;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/9 10:56.
 */
public class SearchJobPresenter extends BasePresenter {

    private SearchJobView searchJobView;

    public SearchJobPresenter(SearchJobView searchJobView) {
        this.searchJobView = searchJobView;
    }

    public void searchJob(String searchContent, int pageNum) {
        QSearchBO qSearchBO = new QSearchBO(NetConfig.JOBACTION, NetConfig.SEARCHLIST, searchContent, pageNum, Page.PAGE_SIZE);
        mSubscriptions.add(Network.getJobApi().searchJobList(qSearchBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<BaseJobBO>>() {
                    @Override
                    protected void onError(ApiException e) {
                        searchJobView.failed(e.getMessage());
                    }

                    @Override
                    public void onNext(List<BaseJobBO> baseJobBOs) {
                        searchJobView.succeed(baseJobBOs);
                    }
                }));
    }
}
