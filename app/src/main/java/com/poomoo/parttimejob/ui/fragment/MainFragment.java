/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyUtils;
import com.poomoo.commlib.SPUtils;
import com.poomoo.commlib.StatusBarUtil;
import com.poomoo.model.Page;
import com.poomoo.model.base.BaseJobBO;
import com.poomoo.model.response.RAdBO;
import com.poomoo.model.response.RTypeBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.adapter.BaseListAdapter;
import com.poomoo.parttimejob.adapter.JobsAdapter;
import com.poomoo.parttimejob.adapter.MainGridAdapter;
import com.poomoo.parttimejob.database.DataBaseHelper;
import com.poomoo.parttimejob.database.TypeInfo;
import com.poomoo.parttimejob.event.Events;
import com.poomoo.parttimejob.event.RxBus;
import com.poomoo.parttimejob.presentation.MainPresenter;
import com.poomoo.parttimejob.ui.activity.CityListActivity;
import com.poomoo.parttimejob.ui.activity.CommodityListActivity;
import com.poomoo.parttimejob.ui.activity.JobInfoActivity;
import com.poomoo.parttimejob.ui.activity.JobListByCateActivity;
import com.poomoo.parttimejob.ui.activity.MoreJobsActivity;
import com.poomoo.parttimejob.ui.activity.SearchJobActivity;
import com.poomoo.parttimejob.ui.activity.WebViewActivity;
import com.poomoo.parttimejob.ui.base.BaseFragment;
import com.poomoo.parttimejob.ui.custom.SlideShowView;
import com.poomoo.parttimejob.view.MainView;
import com.trello.rxlifecycle.FragmentEvent;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者: 李苜菲
 * 日期: 2016/3/22 16:23.
 */
public class MainFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseListAdapter.OnLoadingListener, BaseListAdapter.OnItemClickListener, MainView, AdapterView.OnItemClickListener {
    //    @Bind(R.id.bar_main)
//    RelativeLayout bar;
//    @Bind(R.id.toolbar)
//    Toolbar toolbar;
//    @Bind(R.id.rlayout_interesting)
//    RelativeLayout interestingRlayout;
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
    @Bind(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    private JobsAdapter adapter;
    private MainGridAdapter gridAdapter;
    private String[] urls;
    private RAdBO rAdBO;
    private int currPage = 1;

    private MainPresenter mainPresenter;
    private boolean isLoadAd = false;
    private boolean isLoadType = false;
    private int alpha = 0;
    private int height = 0;
    private Bundle bundle;
    private String url;

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
        StatusBarUtil.setTransparent(getActivity());
        height = MyUtils.getScreenWidth(getActivity()) / 2;//设置广告栏的宽高比为3:1
        slideShowView.setLayoutParams(new CollapsingToolbarLayout.LayoutParams(CollapsingToolbarLayout.LayoutParams.MATCH_PARENT, height));
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
                    if (verticalOffset >= 0)
                        swipeRefreshLayout.setEnabled(true);
                    else
                        swipeRefreshLayout.setEnabled(false);
                    alpha = 255 * Math.abs(verticalOffset) / appBarLayout.getTotalScrollRange();
                    collapsingToolbarLayout.setContentScrimColor(Color.argb(alpha, 13, 176, 155));
                }
        );

        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));

        CollapsingToolbarLayout.LayoutParams layoutParams1 = new CollapsingToolbarLayout.LayoutParams(CollapsingToolbarLayout.LayoutParams.MATCH_PARENT, CollapsingToolbarLayout.LayoutParams.WRAP_CONTENT);
        layoutParams1.setMargins(0, height, 0, 10);//(int) getActivity().getResources().getDimension(R.dimen.dp_31)
        gridView.setLayoutParams(layoutParams1);

        gridAdapter = new MainGridAdapter(getActivity());
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(this);

        mainPresenter = new MainPresenter(this);
        mainPresenter.loadAd();
        mainPresenter.loadCate();
        application.setCurrCityId((Integer) SPUtils.get(getActivity().getApplicationContext(), getString(R.string.sp_currCityId), 1));
        application.setCurrCity((String) SPUtils.get(getActivity().getApplicationContext(), getString(R.string.sp_currCity), "贵阳市"));
        mainPresenter.queryRecommendJobs(application.getUserId(), application.getCurrCityId(), currPage);
        mainPresenter.getType();

        initSubscribers();
    }

    private void initSubscribers() {
        cityTxt.setText(application.getCurrCity());
        RxBus.with(this)
                .setEvent(Events.EventEnum.DELIVER_CITY)
                .setEndEvent(FragmentEvent.DESTROY)
                .onNext((events) -> {
                    cityTxt.setText(application.getCurrCity());
                    SPUtils.put(getActivity().getApplicationContext(), getString(R.string.sp_currCity), application.getCurrCity());
                    SPUtils.put(getActivity().getApplicationContext(), getString(R.string.sp_currCityId), application.getCurrCityId());
                    currPage = 1;
                    mainPresenter.queryRecommendJobs(application.getUserId(), application.getCurrCityId(), currPage);
                }).create();

        RxBus.with(this)
                .setEvent(Events.EventEnum.SET_INTENTION)
                .setEndEvent(FragmentEvent.DESTROY)
                .onNext((events) -> {
                    currPage = 1;
                    mainPresenter.queryRecommendJobs(application.getUserId(), application.getCurrCityId(), currPage);
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
        mainPresenter.queryRecommendJobs(application.getUserId(), application.getCurrCityId(), currPage);
    }

    @Override
    public void onRefresh() {
        if (!isLoadAd)
            mainPresenter.loadAd();
        if (!isLoadType)
            mainPresenter.loadCate();
        currPage = 1;
        mainPresenter.queryRecommendJobs(application.getUserId(), application.getCurrCityId(), currPage);
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
        slideShowView.setPics(urls, position -> {
            url = rAdBOs.get(position).url;
            if (!TextUtils.isEmpty(url)) {
                LogUtils.d(TAG, "url:" + url);
                if (url.startsWith("http://")) {
                    bundle = new Bundle();
                    bundle.putString(getString(R.string.intent_value), url);
                    openActivity(WebViewActivity.class, bundle);
                } else if (url.startsWith("jobId")) {
                    String[] temp = url.split("=");
                    bundle = new Bundle();
                    bundle.putInt(getString(R.string.intent_value), Integer.parseInt(temp[1]));
                    openActivity(JobInfoActivity.class, bundle);
                }
            }
        });
    }

    @Override
    public void loadTypeSucceed(List<RTypeBO> rTypeBOs) {
        isLoadType = true;
        RTypeBO rTypeBO = new RTypeBO();
        rTypeBO.name = "兼职换购";
        rTypeBOs.add(rTypeBO);
        gridAdapter.setItems(rTypeBOs);
    }

    @Override
    public void loadRecommendsSucceed(List<BaseJobBO> rAdBOs) {
        adapter.clearSelectedList();
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
    public void type(List<RTypeBO> rTypeBOs) {
        List<TypeInfo> typeInfos = new ArrayList<>();
        for (RTypeBO rTypeBO : rTypeBOs) {
            TypeInfo typeInfo = new TypeInfo();
            typeInfo.setCateId(rTypeBO.cateId);
            typeInfo.setName(rTypeBO.name);
            typeInfos.add(typeInfo);
        }
        DataBaseHelper.saveType(typeInfos);
    }

    @Override
    public void onItemClick(int position, long id, View view) {
        adapter.addSelectedList(position);
        adapter.notifyItemChanged(position);

        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.intent_value), adapter.getItem(position).jobId);
        openActivity(JobInfoActivity.class, bundle);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == gridAdapter.getCount() - 1) {
            openActivity(CommodityListActivity.class);
        } else {
            RTypeBO rTypeBO = (RTypeBO) gridAdapter.getItem(position);
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.intent_value), rTypeBO.name);
            bundle.putInt(getString(R.string.intent_cateId), rTypeBO.cateId);
            openActivity(JobListByCateActivity.class, bundle);
        }
    }
}
