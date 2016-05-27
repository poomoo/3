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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyUtils;
import com.poomoo.commlib.SPUtils;
import com.poomoo.model.Page;
import com.poomoo.model.base.BaseJobBO;
import com.poomoo.model.response.RAdBO;
import com.poomoo.model.response.RTypeBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.adapter.BaseListAdapter;
import com.poomoo.parttimejob.adapter.JobsAdapter;
import com.poomoo.parttimejob.adapter.MainGridAdapter;
import com.poomoo.parttimejob.event.Events;
import com.poomoo.parttimejob.event.RxBus;
import com.poomoo.parttimejob.presentation.MainPresenter;
import com.poomoo.parttimejob.ui.activity.CityListActivity;
import com.poomoo.parttimejob.ui.activity.JobInfoActivity;
import com.poomoo.parttimejob.ui.activity.JobListByCateActivity;
import com.poomoo.parttimejob.ui.activity.MainActivity;
import com.poomoo.parttimejob.ui.activity.MoreJobsActivity;
import com.poomoo.parttimejob.ui.activity.SearchJobActivity;
import com.poomoo.parttimejob.ui.base.BaseFragment;
import com.poomoo.parttimejob.ui.custom.SlideShowView;
import com.poomoo.parttimejob.view.MainView;
import com.trello.rxlifecycle.FragmentEvent;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者: 李苜菲
 * 日期: 2016/3/22 16:23.
 */
public class MainFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseListAdapter.OnLoadingListener, BaseListAdapter.OnItemClickListener, MainView, AdapterView.OnItemClickListener {
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_main)
    RecyclerView recyclerView;
    @Bind(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @Bind(R.id.flipper_ad)
    SlideShowView slideShowView;
    @Bind(R.id.txt_position)
    TextView cityTxt;
    @Bind(R.id.grid_main)
    GridView gridView;

    private JobsAdapter adapter;
    private MainGridAdapter gridAdapter;
    private String[] urls;
    private RAdBO rAdBO;
    private int currPage = 1;

    private MainPresenter mainPresenter;
    private boolean isLoadAd = false;
    private boolean isLoadType = false;

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
        slideShowView.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, MyUtils.getScreenWidth(getActivity()) / 3));//设置广告栏的宽高比为3:1
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .color(getResources().getColor(R.color.transparent))
                .size((int) getResources().getDimension(R.dimen.divider_height2))
                .build());

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.swipe_refresh_first, R.color.swipe_refresh_second,
                R.color.swipe_refresh_third, R.color.swipe_refresh_four
        );
        adapter = new JobsAdapter(getActivity(), BaseListAdapter.ONLY_FOOTER, false);
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadingListener(this);
        adapter.setOnItemClickListener(this);
        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            if (verticalOffset >= 0) {
                swipeRefreshLayout.setEnabled(true);
            } else {
                swipeRefreshLayout.setEnabled(false);
            }
        });
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
        gridAdapter = new MainGridAdapter(getActivity());
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(this);

        mainPresenter = new MainPresenter(this);
        mainPresenter.loadAd();
        mainPresenter.loadCate();
        mainPresenter.queryRecommendJobs(currPage);

        initSubscribers();
    }

    private void initSubscribers() {
        LogUtils.d(TAG, "currCity" + application.getCurrCity());
        cityTxt.setText(application.getCurrCity());
        RxBus.with(this)
                .setEvent(Events.EventEnum.DELIVER_CITY)
                .setEndEvent(FragmentEvent.DESTROY)
                .onNext((events) -> {
                    LogUtils.d(TAG, "initSubscribers onNext");
                    cityTxt.setText(application.getCurrCity());
                    SPUtils.put(getActivity().getApplicationContext(), getString(R.string.sp_currCity), application.getCurrCity());
                    SPUtils.put(getActivity().getApplicationContext(), getString(R.string.sp_currCityId), application.getCurrCityId());
                }).create();
    }

    @OnClick({R.id.llayout_citys, R.id.img_search, R.id.txt_more})
    void click(View view) {
        switch (view.getId()) {
            case R.id.llayout_citys:
                openActivity(CityListActivity.class);
                break;
            case R.id.img_search:
                openActivity(SearchJobActivity.class);
                break;
            case R.id.txt_more:
                openActivity(MoreJobsActivity.class);
                break;
        }
    }

    @Override
    public void onLoading() {
        mainPresenter.queryRecommendJobs(currPage);
    }

    @Override
    public void onRefresh() {
        if (!isLoadAd)
            mainPresenter.loadAd();
        if (!isLoadType)
            mainPresenter.loadCate();
        currPage = 1;
        mainPresenter.queryRecommendJobs(currPage);
    }

    @Override
    public void loadAdSucceed(List<RAdBO> rAdBOs) {
        isLoadAd = true;
        int len = rAdBOs.size();
        urls = new String[len];
        for (int i = 0; i < len; i++) {
            rAdBO = new RAdBO();
            rAdBO = rAdBOs.get(i);
            urls[i] = rAdBO.picture;
        }
//        urls[len] = "http://hunchaowang.com/hckj/images/slide1.jpg";
//        urls[len+1] = "http://img5.imgtn.bdimg.com/it/u=1831523257,4273085642&fm=21&gp=0.jpg";
//        urls[len+2] = "http://img0.imgtn.bdimg.com/it/u=2724261082,1059352100&fm=21&gp=0.jpg";
        slideShowView.setPics(urls, position -> {

        });
    }

    @Override
    public void loadTypeSucceed(List<RTypeBO> rTypeBOs) {
        isLoadType = true;
        gridAdapter.setItems(rTypeBOs);
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
        if (currPage == 1)
            adapter.addItems(0, rAdBOs);
        else
            adapter.addItems(rAdBOs);
        currPage++;
    }

    @Override
    public void failed(String msg) {
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
        MyUtils.showToast(getActivity().getApplicationContext(), msg);
    }

    @Override
    public void onItemClick(int position, long id, View view) {
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.intent_value), adapter.getItem(position).jobId);
        openActivity(JobInfoActivity.class, bundle);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RTypeBO rTypeBO = (RTypeBO) gridAdapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.intent_value), rTypeBO.name);
        bundle.putInt(getString(R.string.intent_cateId), rTypeBO.cateId);
        openActivity(JobListByCateActivity.class, bundle);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden)
            MainActivity.instance.setBackGround2();
    }
}
