/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Switch;

import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyUtils;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.activity.FilterActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/9 15:57.
 */
public class JobFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick({R.id.rbtn_type, R.id.rbtn_zone, R.id.rbtn_sort, R.id.txt_filter})
    void select(View view) {
        MyUtils.showToast(getActivity().getApplicationContext(), "点击了:" + view.getId());
        switch (view.getId()) {
            case R.id.rbtn_type:
                break;
            case R.id.rbtn_zone:
                break;
            case R.id.rbtn_sort:
                break;
            case R.id.txt_filter:
                LogUtils.i(TAG, "点击筛选:" + view.getId()+":"+R.id.txt_filter);
                openActivity(FilterActivity.class);
                break;
        }

    }
}
