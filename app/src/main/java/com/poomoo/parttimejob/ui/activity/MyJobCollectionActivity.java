/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import com.poomoo.commlib.MyUtils;
import com.poomoo.model.base.BaseJobBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.adapter.BaseListAdapter;
import com.poomoo.parttimejob.adapter.JobsAdapter;
import com.poomoo.parttimejob.presentation.MyJobCollectionPresenter;
import com.poomoo.parttimejob.ui.base.BaseListActivity;
import com.poomoo.parttimejob.ui.custom.ErrorLayout;
import com.poomoo.parttimejob.view.MyJobCollectionView;

import java.util.List;

/**
 * 我的收藏
 * 作者: 李苜菲
 * 日期: 2016/4/16 15:32.
 */
public class MyJobCollectionActivity extends BaseListActivity<BaseJobBO> implements BaseListAdapter.OnItemClickListener, BaseListAdapter.OnItemLongClickListener, MyJobCollectionView {
    private JobsAdapter adapter;
    private MyJobCollectionPresenter myJobCollectionPresenter;
    private int cancelPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack();
        mListView.setPadding(0, setDividerSize(), 0, 0);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
        myJobCollectionPresenter = new MyJobCollectionPresenter(this);
        myJobCollectionPresenter.getCollectionList(application.getUserId(), mCurrentPage);
    }

    @Override
    protected BaseListAdapter<BaseJobBO> onSetupAdapter() {
        adapter = new JobsAdapter(this, BaseListAdapter.ONLY_FOOTER, false);
        return adapter;
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_myCollection);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mCurrentPage = 1;
        myJobCollectionPresenter.getCollectionList(application.getUserId(), mCurrentPage);
    }

    @Override
    public void onLoading() {
        super.onLoading();
        myJobCollectionPresenter.getCollectionList(application.getUserId(), mCurrentPage);
    }

    @Override
    public void onLoadActiveClick() {
        super.onLoadActiveClick();
        mCurrentPage = 1;
        myJobCollectionPresenter.getCollectionList(application.getUserId(), mCurrentPage);
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
    public void cancelSucceed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), msg);
        adapter.removeItem(cancelPos);
        if (adapter.getItemCount() == 1) {
            mErrorLayout.setState(ErrorLayout.NO_COLLECTED);
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(false);
            mAdapter.setState(BaseListAdapter.STATE_HIDE);
        }
    }

    @Override
    public void cancelFailed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), msg);
    }

    @Override
    public void onItemClick(int position, long id, View view) {
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.intent_value), adapter.getItem(position).jobId);
        openActivity(JobInfoActivity.class, bundle);
    }

    @Override
    public void onLongClick(int position, long id, View view) {
        Dialog dialog = new AlertDialog.Builder(this).setMessage("确认删除该条职位收藏信息").setNegativeButton("取消", (dialog1, which) -> {
        }).setPositiveButton("确定", (dialog1, which) -> {
            cancelPos = position;
            showProgressDialog(getString(R.string.dialog_msg));
            myJobCollectionPresenter.cancelCollect(adapter.getItem(position).jobId, application.getUserId());
        }).create();
        dialog.show();
    }
}
