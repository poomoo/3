/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyUtils;
import com.poomoo.model.Page;
import com.poomoo.model.base.BaseJobBO;
import com.poomoo.model.response.RTypeBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.adapter.BaseListAdapter;
import com.poomoo.parttimejob.adapter.JobsAdapter;
import com.poomoo.parttimejob.database.DataBaseHelper;
import com.poomoo.parttimejob.database.TypeInfo;
import com.poomoo.parttimejob.event.Events;
import com.poomoo.parttimejob.event.RxBus;
import com.poomoo.parttimejob.presentation.JobListPresenter;
import com.poomoo.parttimejob.ui.activity.CityListActivity;
import com.poomoo.parttimejob.ui.activity.FilterActivity;
import com.poomoo.parttimejob.ui.activity.JobInfoActivity;
import com.poomoo.parttimejob.ui.activity.MainActivity;
import com.poomoo.parttimejob.ui.base.BaseFragment;
import com.poomoo.parttimejob.ui.custom.ErrorLayout;
import com.poomoo.parttimejob.ui.popup.SortPopUpWindow;
import com.poomoo.parttimejob.ui.popup.TypePopUpWindow;
import com.poomoo.parttimejob.ui.popup.ZonePopUpWindow;
import com.poomoo.parttimejob.view.JobView;
import com.trello.rxlifecycle.FragmentEvent;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/9 15:57.
 */
public class JobFragment extends BaseFragment implements JobView, BaseListAdapter.OnLoadingListener, BaseListAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.error_frame)
    ErrorLayout errorLayout;
    @Bind(R.id.txt_position)
    TextView cityTxt;

    private TypePopUpWindow typePopUpWindow = null;
    private ZonePopUpWindow zonePopUpWindow = null;
    private SortPopUpWindow sortPopUpWindow = null;

    private JobListPresenter jobListPresenter;
    private int currPage = 1;
    private JobsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job, container, false);
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
        adapter.setOnLoadingListener(this);
        adapter.setOnItemClickListener(this);

        jobListPresenter = new JobListPresenter(this);
        jobListPresenter.getType();
        getJobList(true);

        initSubscribers();
    }

    private void initSubscribers() {
        cityTxt.setText(application.getCurrCity());
        RxBus.with(this)
                .setEvent(Events.EventEnum.DELIVER_CITY)
                .setEndEvent(FragmentEvent.DESTROY)
                .onNext((events) -> {
                    LogUtils.d(TAG, "initSubscribers onNext");
                    cityTxt.setText(application.getCurrCity());
                    currPage = 1;
                    zonePopUpWindow = null;
                    typePopUpWindow = null;
                    sortPopUpWindow = null;
                    application.setAreaId("");
                    application.setCateId("");
                    getJobList(true);
                }).create();
    }


    @OnClick({R.id.rbtn_type, R.id.rbtn_zone, R.id.rbtn_sort, R.id.txt_toFilter})
    void select(View view) {
        switch (view.getId()) {
            case R.id.rbtn_type:
                if (typePopUpWindow == null)
                    typePopUpWindow = new TypePopUpWindow(getActivity(), DataBaseHelper.getType(), typeCategory);
                typePopUpWindow.showAsDropDown(view);
                break;
            case R.id.rbtn_zone:
                if (zonePopUpWindow == null)
                    zonePopUpWindow = new ZonePopUpWindow(getActivity(), DataBaseHelper.getCityAndArea(application.getCurrCity(), application.getCurrCityId()), zoneCategory);
                zonePopUpWindow.showAsDropDown(view);
                break;
            case R.id.rbtn_sort:
                if (sortPopUpWindow == null)
                    sortPopUpWindow = new SortPopUpWindow(getActivity(), sortCategory);
                sortPopUpWindow.showAsDropDown(view);
                break;
            case R.id.txt_toFilter:
                openActivityForResult(FilterActivity.class, 1);
                break;
        }

    }

    TypePopUpWindow.SelectCategory typeCategory = type -> {
        int len = type.size();
        String temp = "";
        for (int i = 0; i < len; i++)
            temp += type.get(i) + ",";
        LogUtils.d(TAG, "selectCategory:" + temp);
        temp = temp.substring(0, temp.length() - 1);
        LogUtils.d(TAG, "selectCategory2:" + temp);
        application.setCateId(temp);
        currPage = 1;
        getJobList(true);
    };

    ZonePopUpWindow.SelectCategory zoneCategory = type -> {
        int len = type.size();
        String temp = "";
        for (int i = 0; i < len; i++)
            temp += type.get(i) + ",";
        temp = temp.substring(0, temp.length() - 1);
        application.setAreaId(temp);
        currPage = 1;
        getJobList(true);
    };

    SortPopUpWindow.SelectCategory sortCategory = type -> {
        application.setOrderType(type);
        currPage = 1;
        getJobList(true);
    };


    @OnClick(R.id.llayout_city2)
    void click() {
        openActivity(CityListActivity.class);
    }

    private void getJobList(boolean refresh) {
        if (refresh)
            swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
        jobListPresenter.getJobList(application.getLat(), application.getLng(), application.getCurrCityId(), application.getCateId(), application.getAreaId(), application.getSexReq(), application.getWorkSycle(), application.getWorkday(), application.getStartWorkDt(), application.getOrderType(), currPage);
    }

    @Override
    public void onLoading() {
        getJobList(false);
    }

    @Override
    public void onRefresh() {
        currPage = 1;
        getJobList(true);
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
        LogUtils.d(TAG, "type:" + typeInfos.size());
        DataBaseHelper.saveType(typeInfos);
    }

    @Override
    public void succeed(List<BaseJobBO> baseJobBOs) {
        swipeRefreshLayout.setRefreshing(false);
        if (baseJobBOs == null) return;

        if (currPage == 1) {
            LogUtils.d(TAG, "清除数据");
            adapter.clear();
        }

        if (adapter.getDataSize() + baseJobBOs.size() == 0) {
            adapter.setState(BaseListAdapter.STATE_HIDE);
            errorLayout.setState(ErrorLayout.EMPTY_DATA);
            return;
        } else if (baseJobBOs.size() < Page.PAGE_SIZE) {
            adapter.setState(BaseListAdapter.STATE_NO_MORE);
            errorLayout.setState(ErrorLayout.HIDE);
        } else {
            adapter.setState(BaseListAdapter.STATE_LOAD_MORE);
            errorLayout.setState(ErrorLayout.HIDE);
        }
        Iterator<BaseJobBO> iterator = baseJobBOs.iterator();
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
        LogUtils.d(TAG, "succeed:" + baseJobBOs.size());
        if (currPage == 1)
            adapter.addItems(0, baseJobBOs);
        else
            adapter.addItems(baseJobBOs);
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden)
            MainActivity.instance.setBackGround2();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1)
            getJobList(true);
    }

}
