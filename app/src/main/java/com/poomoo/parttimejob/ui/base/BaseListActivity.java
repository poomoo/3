/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.poomoo.commlib.MyUtils;
import com.poomoo.model.Page;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.adapter.BaseListAdapter;
import com.poomoo.parttimejob.ui.custom.ErrorLayout;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.Iterator;
import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/16 15:34.
 */
public abstract class BaseListActivity<T> extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, ErrorLayout.OnActiveClickListener, BaseListAdapter.OnLoadingListener {

    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected RecyclerView mListView;
    protected ErrorLayout mErrorLayout;

    protected int mCurrentPage = 1;
    protected BaseListAdapter<T> mAdapter;

    public static final int STATE_NONE = 0;
    public static final int STATE_REFRESHING = 1;
    public static final int STATE_PRESSING = 2;// 正在下拉但还没有到刷新的状态
    public static final int STATE_CACHE_LOADING = 3;

    public static final int LOAD_MODE_DEFAULT = 1; // 默认的下拉刷新,小圆圈
    public static final int LOAD_MODE_UP_DRAG = 2; // 上拉到底部时刷新
    public static final int LOAD_MODE_CACHE = 3; // 缓存,ErrorLayout显示情况

    public int mState = STATE_NONE;
    public int action = LOAD_MODE_DEFAULT;

    @Override
    protected int onBindLayout() {
        return R.layout.activity_universal_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mListView = (RecyclerView) findViewById(R.id.recycler_view);
        mErrorLayout = (ErrorLayout) findViewById(R.id.error_frame);


        if (getRefreshable()) {
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorSchemeResources(
                    R.color.swipe_refresh_first, R.color.swipe_refresh_second,
                    R.color.swipe_refresh_third, R.color.swipe_refresh_four
            );
        }

        mErrorLayout.setOnActiveClickListener(this);

        mListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mListView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(setDividerColor())
                .size(setDividerSize())
                .build());

        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter = onSetupAdapter();
            mListView.setAdapter(mAdapter);
            mAdapter.setOnLoadingListener(this);
            mErrorLayout.setState(ErrorLayout.LOADING);
        }
    }


    /**
     * ListView的适配器
     *
     * @return
     */
    protected abstract BaseListAdapter<T> onSetupAdapter();

    /**
     * 分割线的大小
     *
     * @return
     */
    public int setDividerSize() {
        return (int) getResources().getDimension(R.dimen.divider_height5);
    }

    public int setDividerColor() {
        return getResources().getColor(R.color.transparent);
    }

    /**
     * 请求成功，读取缓存成功，加载数据
     *
     * @param result
     */
    public void onLoadResultData(List<T> result) {
        if (result == null) return;

        if (mCurrentPage == 1)
            mAdapter.clear();

        if (mAdapter.getDataSize() + result.size() == 0) {
            mErrorLayout.setState(ErrorLayout.EMPTY_DATA);
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(false);
            mAdapter.setState(BaseListAdapter.STATE_HIDE);
            return;
        } else if (result.size() < Page.PAGE_SIZE) {
            mAdapter.setState(BaseListAdapter.STATE_NO_MORE);
        } else {
            mAdapter.setState(BaseListAdapter.STATE_LOAD_MORE);
        }
        if (mCurrentPage == 1)
            mAdapter.addItems(0, result);
        else
            mAdapter.addItems(result);
        mCurrentPage++;
    }


    /**
     * 触发下拉刷新事件
     */
    @Override
    public void onRefresh() {
        if (mState == STATE_REFRESHING && getRefreshable())
            return;
        if (!MyUtils.hasInternet(this)) {
            onNetworkInvalid(LOAD_MODE_DEFAULT);
            return;
        }
        onLoadingState(LOAD_MODE_DEFAULT);
        action = LOAD_MODE_DEFAULT;
    }

    /**
     * 加载完成!
     */
    public void onLoadFinishState(int mode) {
        switch (mode) {
            case LOAD_MODE_DEFAULT:
                mErrorLayout.setState(ErrorLayout.HIDE);
                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setEnabled(true);
                mState = STATE_NONE;
                break;
            case LOAD_MODE_UP_DRAG:
                break;
            case LOAD_MODE_CACHE:
                mErrorLayout.setState(ErrorLayout.HIDE);
                break;
        }

    }

    /**
     * when load data broken
     */
    public void onLoadErrorState(int mode) {
        switch (mode) {
            case LOAD_MODE_DEFAULT:
                mSwipeRefreshLayout.setEnabled(true);
                if (mAdapter.getDataSize() > 0) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mState = STATE_NONE;
                } else {
                    mErrorLayout.setState(ErrorLayout.LOAD_FAILED);
                }
                break;
            case LOAD_MODE_UP_DRAG:
                mAdapter.setState(BaseListAdapter.STATE_LOAD_ERROR);
                break;
        }

    }

    /**
     * 再错误页面点击重新加载
     */
    @Override
    public void onLoadActiveClick() {
        mErrorLayout.setState(ErrorLayout.LOADING);
        action = LOAD_MODE_DEFAULT;
    }

    /**
     * when there has not valid network
     */
    public void onNetworkInvalid(int mode) {
        switch (mode) {
            case LOAD_MODE_DEFAULT:
                if (mAdapter == null || mAdapter.getDataSize() == 0) {
                    mErrorLayout.setState(ErrorLayout.NOT_NETWORK);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSwipeRefreshLayout.setEnabled(false);
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                break;
            case LOAD_MODE_UP_DRAG:
                mAdapter.setState(BaseListAdapter.STATE_INVALID_NETWORK);
                break;
        }
    }

    /**
     * 顶部加载状态
     */
    public void onLoadingState(int mode) {
        switch (mode) {
            case LOAD_MODE_DEFAULT:
                mState = STATE_REFRESHING;
                mSwipeRefreshLayout.setRefreshing(true);
                mSwipeRefreshLayout.setEnabled(false);
                break;
            case LOAD_MODE_CACHE:
                mState = STATE_CACHE_LOADING;
                mErrorLayout.setState(ErrorLayout.LOADING);
                break;
        }
    }

    /**
     * 设置是否可刷新
     */
    public boolean getRefreshable() {
        return true;
    }

    /**
     * 上拉加载
     */
    @Override
    public void onLoading() {
        if (mState == STATE_REFRESHING) {
            mAdapter.setState(BaseListAdapter.STATE_REFRESHING);
            return;
        }
        mAdapter.setState(BaseListAdapter.STATE_LOADING);
    }

    /**
     * 在消除订阅者的时候解除刷新
     */
    public void dismissRefreshing() {
        if (mState == STATE_REFRESHING) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }
    }
}
