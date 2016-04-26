package com.poomoo.parttimejob.presentation;

import com.poomoo.api.AbsAPICallback;
import com.poomoo.api.ApiException;
import com.poomoo.api.NetConfig;
import com.poomoo.api.Network;
import com.poomoo.commlib.LogUtils;
import com.poomoo.model.request.QJobIntentionBO;
import com.poomoo.model.request.QUserIdBO;
import com.poomoo.model.response.RIntentionBO;
import com.poomoo.model.response.ResponseBO;
import com.poomoo.parttimejob.view.JobIntentionView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李苜菲 on 2016/4/26.
 */
public class JobIntentionPresenter extends BasePresenter {
    private JobIntentionView jobIntentionView;

    public JobIntentionPresenter(JobIntentionView jobIntentionView) {
        this.jobIntentionView = jobIntentionView;
    }


    public void JobIntentionUp(int userId, String cateId, String workday, String workAreaId, String otherInfo) {
        QJobIntentionBO qJobIntentionBO = new QJobIntentionBO(NetConfig.USERACTION, NetConfig.INTENTIONUP, userId, cateId, workday, workAreaId, otherInfo);
        mSubscriptions.add(Network.getUserApi().jobIntentionUp(qJobIntentionBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<ResponseBO>() {

                    @Override
                    public void onNext(ResponseBO responseBO) {
                        jobIntentionView.UpSucceed(responseBO.msg);
                    }

                    @Override
                    protected void onError(ApiException e) {
                        jobIntentionView.UpFailed(e.getMessage());
                    }
                }));
    }

    public void JobIntentionDown(int userId) {
        QUserIdBO qUserIdBO = new QUserIdBO(NetConfig.USERACTION, NetConfig.INTENTIONDOWN, userId);
        mSubscriptions.add(Network.getUserApi().jobIntentionDown(qUserIdBO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<RIntentionBO>() {
                    @Override
                    protected void onError(ApiException e) {
                        jobIntentionView.DownFailed(e.getMessage());
                    }

                    @Override
                    public void onNext(RIntentionBO rIntentionBO) {
                        jobIntentionView.DownSucceed(rIntentionBO);
                    }
                }));

    }
}
