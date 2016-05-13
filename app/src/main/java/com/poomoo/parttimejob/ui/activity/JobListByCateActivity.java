/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.poomoo.model.base.BaseJobBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.adapter.BaseListAdapter;
import com.poomoo.parttimejob.adapter.JobsAdapter;
import com.poomoo.parttimejob.presentation.AllJobListPresenter;
import com.poomoo.parttimejob.ui.base.BaseListActivity;
import com.poomoo.parttimejob.view.JobListView;

import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/16 15:32.
 */
public class JobListByCateActivity extends BaseListActivity<BaseJobBO> implements BaseListAdapter.OnItemClickListener, JobListView {
    private AllJobListPresenter allJobListPresenter;
    private int cateId;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cateId = getIntent().getIntExtra(getString(R.string.intent_cateId), -1);
        title = getIntent().getStringExtra(getString(R.string.intent_value));
        setBack();

        mListView.setPadding(0, setDividerSize(), 0, 0);
        mAdapter.setOnItemClickListener(this);
        allJobListPresenter = new AllJobListPresenter(this);
        allJobListPresenter.getJobListByCate(cateId,mCurrentPage);
    }

    @Override
    protected BaseListAdapter<BaseJobBO> onSetupAdapter() {
        return new JobsAdapter(this, BaseListAdapter.ONLY_FOOTER, false);
    }

    @Override
    protected String onSetTitle() {
        return title;
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mCurrentPage = 1;
        allJobListPresenter.getJobListByCate(cateId, mCurrentPage);
    }

    @Override
    public void onLoading() {
        super.onLoading();
        allJobListPresenter.getJobListByCate(cateId, mCurrentPage);
    }

    @Override
    public void onLoadActiveClick() {
        super.onLoadActiveClick();
        mCurrentPage = 1;
        allJobListPresenter.getJobListByCate(cateId, mCurrentPage);
    }

    @Override
    public void succeed(List<BaseJobBO> list) {
        onLoadFinishState(action);
        onLoadResultData(list);
    }

    @Override
    public void failed(String msg) {
        if (msg.contains("检查网络"))
            onNetworkInvalid(action);
        else
            onLoadErrorState(action);
    }

    @Override
    public void onItemClick(int position, long id, View view) {
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.intent_value), mAdapter.getItem(position).jobId);
        openActivity(JobInfoActivity.class, bundle);
    }
}
