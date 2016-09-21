/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poomoo.api.AbsAPICallback;
import com.poomoo.api.ApiException;
import com.poomoo.api.NetConfig;
import com.poomoo.api.Network;
import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyConfig;
import com.poomoo.commlib.MyUtils;
import com.poomoo.commlib.SPUtils;
import com.poomoo.model.base.BaseRequestBO;
import com.poomoo.model.response.RApplyJobBO;
import com.poomoo.model.response.RAreaBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.database.AreaInfo;
import com.poomoo.parttimejob.database.CityInfo;
import com.poomoo.parttimejob.database.DataBaseHelper;
import com.poomoo.parttimejob.database.ProvinceInfo;
import com.poomoo.parttimejob.event.Events;
import com.poomoo.parttimejob.event.RxBus;
import com.poomoo.parttimejob.ui.activity.AuthActivity;
import com.poomoo.parttimejob.ui.activity.FeedBackActivity;
import com.poomoo.parttimejob.ui.activity.JobIntentionActivity;
import com.poomoo.parttimejob.ui.activity.LoginActivity;
import com.poomoo.parttimejob.ui.activity.MainActivity;
import com.poomoo.parttimejob.ui.activity.MoreActivity;
import com.poomoo.parttimejob.ui.activity.MyApplyActivity;
import com.poomoo.parttimejob.ui.activity.MyCollectionActivity;
import com.poomoo.parttimejob.ui.activity.ResumeActivity;
import com.poomoo.parttimejob.ui.base.BaseFragment;
import com.poomoo.parttimejob.ui.custom.RoundImageView2;
import com.trello.rxlifecycle.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.schedulers.Schedulers;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/12 15:37.
 */
public class PersonalFragment extends BaseFragment {
    @Bind(R.id.img_personalAvatar)
    RoundImageView2 avatarImg;
    @Bind(R.id.txt_personalName)
    TextView nameTxt;
    @Bind(R.id.txt_personalAuth)
    TextView authTxt;

    private List<ProvinceInfo> province_list;
    private List<CityInfo> city_list;
    private List<AreaInfo> area_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (application.isLogin()) {
            initSubscribers();
            initView();
        } else {
            nameTxt.setText(MyConfig.pleaseLogin);
            authTxt.setVisibility(View.GONE);
        }

    }

    private void initSubscribers() {
        RxBus.with(this)
                .setEvent(Events.EventEnum.DELIVER_AVATAR)
                .setEndEvent(FragmentEvent.DESTROY)
                .onNext((events) -> {
                    LogUtils.d(TAG, "onNext");
                    initView();
                }).create();
    }

    private void initView() {
        LogUtils.d(TAG, "头像:" + application.getHeadPic());
        if (!TextUtils.isEmpty(application.getHeadPic())) {
            LogUtils.d(TAG, "有头像");
            Glide.with(this).load(application.getHeadPic()).placeholder(R.drawable.ic_defalut_avatar).into(avatarImg);
        }

        if (!TextUtils.isEmpty(application.getRealName()))
            nameTxt.setText(application.getRealName());
        if ((boolean) SPUtils.get(getActivity().getApplicationContext(), getString(R.string.sp_isAuth), false))
            authTxt.setText("实名认证中");
    }

    @OnClick({R.id.llayout_signed, R.id.llayout_hired, R.id.llayout_toPost, R.id.llayout_settleMent})
    void toApplyList(View view) {
        if (!application.isLogin()) {
            Dialog dialog = new AlertDialog.Builder(getActivity()).setMessage("请先登录").setPositiveButton("确定", (dialog1, which) -> {
                MainActivity.instance.finish();
                openActivity(LoginActivity.class);
                getActivityOutToRight();
            }).setNegativeButton("取消", (dialog1, which) -> {

            }).create();
            dialog.show();
            return;
        }
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.llayout_signed:
                bundle.putInt(getString(R.string.intent_value), RApplyJobBO.JOB_SIGNED);
                break;
            case R.id.llayout_hired:
                bundle.putInt(getString(R.string.intent_value), RApplyJobBO.JOB_HIRED);
                break;
            case R.id.llayout_toPost:
                bundle.putInt(getString(R.string.intent_value), RApplyJobBO.JOB_TOPOST);
                break;
            case R.id.llayout_settleMent:
                bundle.putInt(getString(R.string.intent_value), RApplyJobBO.JOB_SETTLEMENT);
                break;
        }
        openActivity(MyApplyActivity.class, bundle);
    }

    @OnClick({R.id.llayout_toResume, R.id.rlayout_myCollection, R.id.rlayout_myAuth, R.id.rlayout_setting, R.id.rlayout_feedBack, R.id.rlayout_more})
    void OnClick(View view) {
        if (!application.isLogin()) {
            Dialog dialog = new AlertDialog.Builder(getActivity()).setMessage("请先登录").setPositiveButton("确定", (dialog1, which) -> {
                MainActivity.instance.finish();
                openActivity(LoginActivity.class);
                getActivityOutToRight();
            }).setNegativeButton("取消", (dialog1, which) -> {

            }).create();
            dialog.show();
            return;
        }
        switch (view.getId()) {
            case R.id.llayout_toResume:
                getCity();
                break;
            case R.id.rlayout_myCollection:
                openActivity(MyCollectionActivity.class);
                break;
            case R.id.rlayout_myAuth:
                openActivity(AuthActivity.class);
                break;
            case R.id.rlayout_setting:
                openActivity(JobIntentionActivity.class);
                LogUtils.d(TAG, "openActivity");
                break;
            case R.id.rlayout_feedBack:
                openActivity(FeedBackActivity.class);
                break;
            case R.id.rlayout_more:
                openActivity(MoreActivity.class);
                break;
        }
    }

    public void getCity() {
        if (DataBaseHelper.getProvinceList().size() == 1) {
            showProgressDialog("同步城市中,请稍后...");
            BaseRequestBO baseRequestBO = new BaseRequestBO(NetConfig.COMMACTION, NetConfig.CITY);
            Network.getCommApi().getCitys(baseRequestBO)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(new AbsAPICallback<List<RAreaBO>>() {
                        @Override
                        protected void onError(ApiException e) {
                            getActivity().runOnUiThread(() -> {
                                closeProgressDialog();
                                MyUtils.showToast(getActivity().getApplicationContext(), e.getMessage());
                            });
                        }

                        @Override
                        public void onNext(List<RAreaBO> rAreaBOs) {
                            city_list = new ArrayList<>();
                            province_list = new ArrayList<>();
                            city_list = new ArrayList<>();
                            area_list = new ArrayList<>();
                            for (RAreaBO rAreaBO : rAreaBOs) {
                                ProvinceInfo provinceInfo = new ProvinceInfo(rAreaBO.provinceId, rAreaBO.provinceName);
                                city_list = new ArrayList<>();
                                for (RAreaBO.city city : rAreaBO.cityList) {
                                    CityInfo cityInfo = new CityInfo(city.cityId, city.cityName, city.isHot, provinceInfo.getProvinceId());
                                    city_list.add(cityInfo);
                                    area_list = new ArrayList<>();
                                    for (RAreaBO.area area : city.areaList)
                                        area_list.add(new AreaInfo(area.areaId, area.areaName, cityInfo.getCityId()));
                                    DataBaseHelper.saveArea(area_list);
                                    city_list.add(cityInfo);
                                }
                                DataBaseHelper.saveCity(city_list);
                                province_list.add(provinceInfo);
                            }
                            DataBaseHelper.saveProvince(province_list);
                            getActivity().runOnUiThread(() -> {
                                closeProgressDialog();
                                openActivity(ResumeActivity.class);
                            });
                        }
                    });
        } else
            openActivity(ResumeActivity.class);
    }

}
