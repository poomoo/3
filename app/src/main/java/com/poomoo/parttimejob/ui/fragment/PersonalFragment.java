/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poomoo.model.response.RApplyJobBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.activity.AuthActivity;
import com.poomoo.parttimejob.ui.activity.FeedBackActivity;
import com.poomoo.parttimejob.ui.activity.JobIntentionActivity;
import com.poomoo.parttimejob.ui.activity.MainActivity;
import com.poomoo.parttimejob.ui.activity.MoreActivity;
import com.poomoo.parttimejob.ui.activity.MyApplyActivity;
import com.poomoo.parttimejob.ui.activity.MyCollectionActivity;
import com.poomoo.parttimejob.ui.activity.ResumeActivity;
import com.poomoo.parttimejob.ui.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/12 15:37.
 */
public class PersonalFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        ButterKnife.bind(this, view);
        MainActivity.instance.setBackGround1();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden)
            MainActivity.instance.setBackGround1();
    }

    @OnClick({R.id.llayout_signed, R.id.llayout_hired, R.id.llayout_toPost, R.id.llayout_settleMent})
    void toApplyList(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.llayout_signed:
                bundle.putInt(getString(R.string.intent_value), RApplyJobBO.JOB_SIGNED);
                break;
            case R.id.llayout_hired:
                bundle.putInt(getString(R.string.intent_value), RApplyJobBO.JOB_HIRED);
                break;
            case R.id.llayout_toPost:
                bundle.putInt(getString(R.string.intent_value), RApplyJobBO.JOB_TOPOST);
                break;
            case R.id.llayout_settleMent:
                bundle.putInt(getString(R.string.intent_value), RApplyJobBO.JOB_SETTLEMENT);
                break;
        }
        openActivity(MyApplyActivity.class, bundle);
    }

    @OnClick({R.id.llayout_toResume, R.id.rlayout_myCollection, R.id.rlayout_myAuth, R.id.rlayout_setting, R.id.rlayout_feedBack, R.id.rlayout_more})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.llayout_toResume:
                openActivity(ResumeActivity.class);
                break;
            case R.id.rlayout_myCollection:
                openActivity(MyCollectionActivity.class);
                break;
            case R.id.rlayout_myAuth:
                openActivity(AuthActivity.class);
                break;
            case R.id.rlayout_setting:
                openActivity(JobIntentionActivity.class);
                break;
            case R.id.rlayout_feedBack:
                openActivity(FeedBackActivity.class);
                break;
            case R.id.rlayout_more:
                openActivity(MoreActivity.class);
                break;
        }
    }
}
