/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyUtils;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.activity.CityListActivity;
import com.poomoo.parttimejob.ui.custom.NoScrollListView;
import com.poomoo.parttimejob.ui.custom.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import com.poomoo.parttimejob.adapter.InterestingJobAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者: 李苜菲
 * 日期: 2016/3/22 16:23.
 */
public class MainFragment extends BaseFragment implements RefreshLayout.OnLoadListener {
    @Bind(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @Bind(R.id.list_main)
    NoScrollListView listView;

    private InterestingJobAdapter adapter;
    private List<String> strings = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
//        refreshLayout = (RefreshLayout) getActivity().findViewById(R.id.refreshLayout);
        refreshLayout.setOnLoadListener(this);
//        listView = (NoScrollListView) getActivity().findViewById(R.id.list_main);

        adapter = new InterestingJobAdapter(getActivity());
        listView.setAdapter(adapter);
        for (int i = 0; i < 10; i++)
            strings.add("213");
        LogUtils.i("长度:" + strings.size());
        adapter.setItems(strings);
    }

    @Override
    public void onLoad() {
        LogUtils.i("开始加载了");
    }

    @OnClick(R.id.llayout_citys)
    void city() {
        MyUtils.showToast(getActivity().getApplicationContext(), "城市");
        openActivity(CityListActivity.class);
    }
}
