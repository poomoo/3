/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.poomoo.model.base.BaseJobBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.adapter.JobsAdapter;
import com.poomoo.parttimejob.adapter.BaseListAdapter;
import com.poomoo.parttimejob.presentation.ApplyListPresenter;
import com.poomoo.parttimejob.ui.base.BaseListActivity;
import com.poomoo.parttimejob.view.JobListView;

import java.util.List;

/**
 * 我的收藏
 * 作者: 李苜菲
 * 日期: 2016/4/16 15:32.
 */
public class MyCollectionActivity extends BaseListActivity<BaseJobBO> implements BaseListAdapter.OnItemClickListener, JobListView {
    private ApplyListPresenter applyListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack();
        mListView.setPadding(0, setDividerSize(), 0, 0);
        mAdapter.setOnItemClickListener(this);
        applyListPresenter = new ApplyListPresenter(this);
        applyListPresenter.getCollectionList(1);
    }

    @Override
    protected BaseListAdapter<BaseJobBO> onSetupAdapter() {
        return new JobsAdapter(this, BaseListAdapter.ONLY_FOOTER, false);
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_myCollection);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        applyListPresenter.getCollectionList(1);
    }

    @Override
    public void onLoadActiveClick() {
        super.onLoadActiveClick();
        applyListPresenter.getCollectionList(1);
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

    }
}
