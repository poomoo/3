/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyUtils;
import com.poomoo.model.Page;
import com.poomoo.model.base.BaseJobBO;
import com.poomoo.model.response.RAdBO;
import com.poomoo.model.response.RApplyJobBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.adapter.BaseListAdapter;
import com.poomoo.parttimejob.adapter.JobsAdapter;
import com.poomoo.parttimejob.listener.AdvertisementListener;
import com.poomoo.parttimejob.presentation.MainPresenter;
import com.poomoo.parttimejob.ui.activity.CityListActivity;
import com.poomoo.parttimejob.ui.activity.JobInfoActivity;
import com.poomoo.parttimejob.ui.activity.MainActivity;
import com.poomoo.parttimejob.ui.activity.SearchJobActivity;
import com.poomoo.parttimejob.ui.base.BaseFragment;
import com.poomoo.parttimejob.ui.custom.SlideShowView;
import com.poomoo.parttimejob.ui.view.MainView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者: 李苜菲
 * 日期: 2016/3/22 16:23.
 */
public class MainFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseListAdapter.OnLoadingListener, BaseListAdapter.OnItemClickListener, MainView {
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_main)
    RecyclerView recyclerView;
    @Bind(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @Bind(R.id.flipper_ad)
    SlideShowView slideShowView;

    private JobsAdapter adapter;
    private List<RApplyJobBO> rApplyJobBOs = new ArrayList<>();
    private RApplyJobBO rApplyJobBO;
    private String[] urls;
    private RAdBO rAdBO;
    private int currPage = 1;

    private MainPresenter mainPresenter;

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
        adapter = new JobsAdapter(getActivity(), BaseListAdapter.ONLY_FOOTER, false);
        recyclerView.setAdapter(adapter);
//        for (int i = 0; i < 10; i++) {
//            rApplyJobBO = new RApplyJobBO("金阳" + i, i + "", i, 100 * (i + 1) + "￥/天", "第" + i + "份工作", i + "分钟前");
//            rApplyJobBOs.add(rApplyJobBO);
//        }
//        adapter.addItems(rApplyJobBOs);
        adapter.setOnLoadingListener(this);
        adapter.setOnItemClickListener(this);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset >= 0) {
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        mainPresenter = new MainPresenter(this);
        mainPresenter.loadAd();
        mainPresenter.queryRecommendJobs(currPage);
    }

    @OnClick({R.id.llayout_citys, R.id.img_search})
    void click(View view) {
        switch (view.getId()) {
            case R.id.llayout_citys:
                openActivity(CityListActivity.class);
                break;
            case R.id.img_search:
                openActivity(SearchJobActivity.class);
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden)
            MainActivity.instance.setBackGround2();

    }

    @Override
    public void onLoading() {
        mainPresenter.queryRecommendJobs(currPage);
    }

    @Override
    public void onRefresh() {
//        mainPresenter.loadAd();
        currPage = 1;
        mainPresenter.queryRecommendJobs(currPage);
    }

    @Override
    public void loadAdSucceed(List<RAdBO> rAdBOs) {
        int len = rAdBOs.size();
        urls = new String[len];
        for (int i = 0; i < len; i++) {
            rAdBO = new RAdBO();
            rAdBO = rAdBOs.get(i);
            urls[i] = rAdBO.picture;
        }
        slideShowView.setPics(urls, new AdvertisementListener() {
            @Override
            public void onResult(int position) {

            }
        });
    }

    @Override
    public void loadRecommendsSucceed(List<BaseJobBO> rAdBOs) {
        LogUtils.d(TAG, "loadRecommendsSucceed:" + rAdBOs);
        swipeRefreshLayout.setRefreshing(false);
        if (rAdBOs == null) return;

        if (currPage == 1) adapter.clear();

        if (adapter.getDataSize() + rAdBOs.size() == 0) {
            adapter.setState(BaseListAdapter.STATE_HIDE);
            return;
        } else if (rAdBOs.size() < Page.PAGE_SIZE) {
            adapter.setState(BaseListAdapter.STATE_NO_MORE);
        } else {
            adapter.setState(BaseListAdapter.STATE_LOAD_MORE);
        }
        Iterator<BaseJobBO> iterator = rAdBOs.iterator();
        final List<BaseJobBO> data = adapter.getDataSet();
        while (iterator.hasNext()) {
            BaseJobBO obj = iterator.next();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getId().equals(obj.getId())) {
                    data.set(i, obj);
                    iterator.remove();
                    break;
                }
            }
        }
        if (currPage == 1)
            adapter.addItems(0, rAdBOs);
        else
            adapter.addItems(rAdBOs);
        currPage++;
    }

    @Override
    public void failed(String msg) {
        swipeRefreshLayout.setRefreshing(false);
        MyUtils.showToast(getActivity().getApplicationContext(), msg);
    }

    @Override
    public void onItemClick(int position, long id, View view) {
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.intent_value), adapter.getItem(position).jobId);
        openActivity(JobInfoActivity.class, bundle);
    }
}
