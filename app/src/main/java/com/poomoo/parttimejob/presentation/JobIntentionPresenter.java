package com.poomoo.parttimejob.presentation;

import android.text.TextUtils;

import com.poomoo.api.AbsAPICallback;
import com.poomoo.api.ApiException;
import com.poomoo.api.NetConfig;
import com.poomoo.api.Network;
import com.poomoo.model.request.QJobIntentionBO;
import com.poomoo.model.request.QUserIdBO;
import com.poomoo.model.response.RIntentionBO;
import com.poomoo.model.response.ResponseBO;
import com.poomoo.parttimejob.view.JobIntentionView;

import java.util.List;

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


    public void JobIntentionUp(int userId, String type, String cateId, String workday, String workAreaId, String otherInfo) {
        QJobIntentionBO qJobIntentionBO = new QJobIntentionBO(NetConfig.USERACTION, NetConfig.INTENTIONUP, type, userId, cateId, workday, workAreaId, otherInfo);
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

    public void JobIntentionDown(int userId, List<String> typeInfos, List<Integer> areaIds) {
        QUserIdBO qUserIdBO = new QUserIdBO(NetConfig.USERACTION, NetConfig.INTENTIONDOWN, userId);
        mSubscriptions.add(Network.getUserApi().jobIntentionDown(qUserIdBO)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(rIntentionBO -> {
                    int len = rIntentionBO.cateList.size();
                    for (int i = 0; i < len; i++) {
                        if (rIntentionBO.cateList.get(i).selected)
                            rIntentionBO.type.add(typeInfos.indexOf(rIntentionBO.cateList.get(i).cateName + "#" + rIntentionBO.cateList.get(i).cateId));
                    }
                    if (!TextUtils.isEmpty(rIntentionBO.workAreaId)) {
                        String[] areaStr = rIntentionBO.workAreaId.split(",");
                        len = areaStr.length;
                        for (int i = 0; i < len; i++)
                            rIntentionBO.area.add(areaIds.indexOf(Integer.parseInt(areaStr[i])));
                        if (rIntentionBO.area.contains(-1)) {
                            rIntentionBO.area.clear();
                            rIntentionBO.area.add(0);
                        }
                    }
                    if (!TextUtils.isEmpty(rIntentionBO.workDay)) {
                        String[] workStr = rIntentionBO.workDay.split(",");
                        len = workStr.length;
                        for (int i = 0; i < len; i++)
                            rIntentionBO.work.add(getPos(workStr[i]));
                    }
                    return rIntentionBO;
                })
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

    private int getPos(String workday) {
        int x = Integer.parseInt(workday.substring(0, 1));
        int y = Integer.parseInt(workday.substring(1, 2));
        return (x - 1) * 8 + (y + 8);
    }
}
