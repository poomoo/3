/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.poomoo.commlib.MyConfig;
import com.poomoo.model.Page;
import com.poomoo.model.base.BaseJobBO;
import com.poomoo.model.response.RServiceBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.adapter.BaseListAdapter;
import com.poomoo.parttimejob.adapter.JobsAdapter;
import com.poomoo.parttimejob.adapter.ServiceListAdapter;
import com.poomoo.parttimejob.presentation.ServicePresenter;
import com.poomoo.parttimejob.ui.activity.MainActivity;
import com.poomoo.parttimejob.ui.activity.MessageActivity;
import com.poomoo.parttimejob.ui.base.BaseFragment;
import com.poomoo.parttimejob.ui.custom.ErrorLayout;
import com.poomoo.parttimejob.view.ServiceView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/27 09:45.
 */
public class ServiceFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseListAdapter.OnLoadingListener, BaseListAdapter.OnItemClickListener, ErrorLayout.OnActiveClickListener, ServiceView {
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.error_frame)
    ErrorLayout errorLayout;

    public static final int LOAD_MODE_DEFAULT = 1; // 默认的下拉刷新,小圆圈
    public static final int LOAD_MODE_UP_DRAG = 2; // 上拉到底部时刷新
    public int action = LOAD_MODE_DEFAULT;

    private ServiceListAdapter adapter;
    private ServicePresenter servicePresenter;
    private int currPage = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);
        ButterKnife.bind(this, view);
        MainActivity.instance.setBackGround2();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .color(getResources().getColor(R.color.transparent))
                .size((int) getResources().getDimension(R.dimen.divider_height5))
                .build());
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.swipe_refresh_first, R.color.swipe_refresh_second,
                R.color.swipe_refresh_third, R.color.swipe_refresh_four
        );

        adapter = new ServiceListAdapter(getActivity(), BaseListAdapter.ONLY_FOOTER);
        adapter.setOnItemClickListener(this);
        adapter.setOnLoadingListener(this);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        servicePresenter = new ServicePresenter(this);
        servicePresenter.getServiceList();
    }

    /**
     * 触发下拉刷新事件
     */
    @Override
    public void onRefresh() {
        servicePresenter.getServiceList();
    }

    @Override
    public void onLoading() {

    }

    /**
     * 再错误页面点击重新加载
     */
    @Override
    public void onLoadActiveClick() {
        servicePresenter.getServiceList();
    }

    @Override
    public void onItemClick(int position, long id, View view) {
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.intent_msgId), adapter.getItem(position).msgId);
        bundle.putString(getString(R.string.intent_value), adapter.getItem(position).sysNickName);
        openActivity(MessageActivity.class, bundle);
    }

    @Override
    public void succeed(List<RServiceBO> rServiceBOs) {
        swipeRefreshLayout.setRefreshing(false);
        if (rServiceBOs == null) return;

        if (currPage == 1) adapter.clear();

        if (adapter.getDataSize() + rServiceBOs.size() == 0) {
            adapter.setState(BaseListAdapter.STATE_HIDE);
            return;
        } else if (rServiceBOs.size() < Page.PAGE_SIZE) {
            adapter.setState(BaseListAdapter.STATE_NO_MORE);
        } else {
            adapter.setState(BaseListAdapter.STATE_LOAD_MORE);
        }
        Iterator<RServiceBO> iterator = rServiceBOs.iterator();
        final List<RServiceBO> data = adapter.getDataSet();
        while (iterator.hasNext()) {
            RServiceBO obj = iterator.next();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getId().equals(obj.getId())) {
                    data.set(i, obj);
                    iterator.remove();
                    break;
                }
            }
        }
        if (currPage == 1)
            adapter.addItems(0, rServiceBOs);
        else
            adapter.addItems(rServiceBOs);
    }

    @Override
    public void failed(String msg) {
        if (msg.contains("检查网络"))
            onNetworkInvalid(action);
        else
            onLoadErrorState(action);
    }

    public void onNetworkInvalid(int mode) {
        switch (mode) {
            case LOAD_MODE_DEFAULT:
                if (adapter == null || adapter.getDataSize() == 0) {
                    errorLayout.setState(ErrorLayout.NOT_NETWORK);
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setEnabled(false);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
                break;
            case LOAD_MODE_UP_DRAG:
                adapter.setState(BaseListAdapter.STATE_INVALID_NETWORK);
                break;
        }
    }

    public void onLoadErrorState(int mode) {
        switch (mode) {
            case LOAD_MODE_DEFAULT:
                swipeRefreshLayout.setEnabled(true);
                if (adapter.getDataSize() > 0) {
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    errorLayout.setState(ErrorLayout.LOAD_FAILED);
                }
                break;
            case LOAD_MODE_UP_DRAG:
                adapter.setState(BaseListAdapter.STATE_LOAD_ERROR);
                break;
        }

    }

}
