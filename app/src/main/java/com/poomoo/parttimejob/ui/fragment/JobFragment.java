/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyConfig;
import com.poomoo.commlib.MyUtils;
import com.poomoo.commlib.SPUtils;
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
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/9 15:57.
 */
public class JobFragment extends BaseFragment implements JobView, BaseListAdapter.OnLoadingListener, BaseListAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.rlayout_job)
    RelativeLayout titleRlayout;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.error_frame)
    ErrorLayout errorLayout;
    @Bind(R.id.txt_position)
    TextView cityTxt;
    @Bind(R.id.txt_type)
    CheckedTextView typeTxt;
    @Bind(R.id.txt_zone)
    CheckedTextView zoneTxt;
    @Bind(R.id.txt_sort)
    CheckedTextView sortTxt;
    @Bind(R.id.txt_divider)
    TextView dividerTxt;

    private TypePopUpWindow typePopUpWindow = null;
    private ZonePopUpWindow zonePopUpWindow = null;
    private SortPopUpWindow sortPopUpWindow = null;

    private JobListPresenter jobListPresenter;
    private int currPage = 1;
    private JobsAdapter adapter;
    public static JobFragment instance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job, container, false);
        ButterKnife.bind(this, view);
//        MainActivity.instance.setBackGround2();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        instance = this;
        if (!(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)) {
            titleRlayout.setPadding( (int) getActivity().getResources().getDimension(R.dimen.dp_8), (int) getActivity().getResources().getDimension(R.dimen.toolbar_marginTop), (int) getActivity().getResources().getDimension(R.dimen.dp_8), (int) getActivity().getResources().getDimension(R.dimen.dp_8));
            titleRlayout.setGravity(Gravity.BOTTOM);
        }

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

        jobListPresenter = new JobListPresenter(this);
//        jobListPresenter.getType();
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
                    SPUtils.put(getActivity().getApplicationContext(), getString(R.string.sp_currCity), application.getCurrCity());
                    SPUtils.put(getActivity().getApplicationContext(), getString(R.string.sp_currCityId), application.getCurrCityId());
                }).create();
    }


    @OnClick({R.id.llayout_type, R.id.llayout_zone, R.id.llayout_sort, R.id.txt_toFilter})
    void select(View view) {
        switch (view.getId()) {
            case R.id.llayout_type:
                if (typePopUpWindow == null)
                    typePopUpWindow = new TypePopUpWindow(getActivity(), DataBaseHelper.getType(), typeCategory);
                closeOtherPop(typePopUpWindow);
                if (typeTxt.isChecked())
                    typePopUpWindow.dismiss();
                else
                    typePopUpWindow.showAsDropDown(dividerTxt);
                typeTxt.toggle();
                break;
            case R.id.llayout_zone:
                if (zonePopUpWindow == null)
                    zonePopUpWindow = new ZonePopUpWindow(getActivity(), DataBaseHelper.getCityAndArea(application.getCurrCity(), application.getCurrCityId()), zoneCategory);
                closeOtherPop(zonePopUpWindow);
                if (zoneTxt.isChecked())
                    zonePopUpWindow.dismiss();
                else
                    zonePopUpWindow.showAsDropDown(dividerTxt);
                zoneTxt.toggle();
                break;
            case R.id.llayout_sort:
                if (sortPopUpWindow == null)
                    sortPopUpWindow = new SortPopUpWindow(getActivity(), sortCategory);
                closeOtherPop(sortPopUpWindow);
                if (sortTxt.isChecked())
                    sortPopUpWindow.dismiss();
                else
                    sortPopUpWindow.showAsDropDown(dividerTxt);
                sortTxt.toggle();
                break;
            case R.id.txt_toFilter:
                closeOtherPop(null);
                openActivityForResult(FilterActivity.class, 1);
                break;
        }

    }

    public void closeOtherPop(PopupWindow popupWindow) {
        if (typePopUpWindow != null && typePopUpWindow.isShowing() && typePopUpWindow != popupWindow) {
            typePopUpWindow.dismiss();
            typeTxt.toggle();
        }

        if (zonePopUpWindow != null && zonePopUpWindow.isShowing() && zonePopUpWindow != popupWindow) {
            zonePopUpWindow.dismiss();
            zoneTxt.toggle();
        }

        if (sortPopUpWindow != null && sortPopUpWindow.isShowing() && sortPopUpWindow != popupWindow) {
            sortPopUpWindow.dismiss();
            sortTxt.toggle();
        }
    }

    private boolean isShowing() {
        if (typePopUpWindow != null && typePopUpWindow.isShowing())
            return true;
        if (zonePopUpWindow != null && zonePopUpWindow.isShowing())
            return true;
        if (sortPopUpWindow != null && sortPopUpWindow.isShowing())
            return true;
        return false;
    }

    TypePopUpWindow.SelectCategory typeCategory = type -> {
        typeTxt.toggle();
        int len = type.size();
        String name = "";
        String id = "";
        for (int i = 0; i < len; i++) {
            String temp[] = type.get(i).split("#");
            id += temp.length == 2 ? temp[1] + "," : "";
            name += temp.length == 2 ? temp[0] + "/" : "类型";
        }
        LogUtils.d(TAG, "TYPE name" + name + "id" + id);
        if (id.length() > 0)
            id = id.substring(0, id.length() - 1);
        if (name.length() > 0 && !name.equals("类型"))
            name = name.substring(0, name.length() - 1);
        application.setCateId(id);
        currPage = 1;
        getJobList(true);
        typeTxt.setText(name);
    };

    ZonePopUpWindow.SelectCategory zoneCategory = type -> {
        zoneTxt.toggle();
        int len = type.size();
        String id = "";
        String name = "";
        for (int i = 0; i < len; i++) {
            String temp[] = type.get(i).split("#");
            id += temp.length == 2 ? temp[1] + "," : "";
            name += temp.length == 2 ? temp[0] + "/" : "区域";
        }
        if (id.length() > 0)
            id = id.substring(0, id.length() - 1);
        if (name.length() > 0 && !name.equals("区域"))
            name = name.substring(0, name.length() - 1);
        application.setAreaId(id);
        currPage = 1;
        getJobList(true);
        zoneTxt.setText(name);
    };

    SortPopUpWindow.SelectCategory sortCategory = type -> {
        sortTxt.toggle();
        application.setOrderType(type);
        currPage = 1;
        getJobList(true);
        sortTxt.setText(MyConfig.sortType[type - 1]);
    };


    @OnClick(R.id.llayout_city2)
    void click() {
        closeOtherPop(null);
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

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden)
//        MainActivity.instance.setBackGround2();
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1)
            getJobList(true);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            if (isShowing()) {
                closeOtherPop(null);
                return true;
            } else
                return false;
        }
        return true;
    }

}
