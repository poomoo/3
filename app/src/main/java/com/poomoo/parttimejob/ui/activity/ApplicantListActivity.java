/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.poomoo.commlib.LogUtils;
import com.poomoo.model.response.RApplicantBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.adapter.ApplicantListAdapter;
import com.poomoo.parttimejob.adapter.BaseListAdapter;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.view.ApplicantListView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 申请人列表
 * 作者: 李苜菲
 * 日期: 2016/4/9 12:51.
 */
public class ApplicantListActivity extends BaseActivity implements ApplicantListView, BaseListAdapter.OnItemClickListener {
    @Bind(R.id.recycler_applicant)
    RecyclerView recyclerView;

    private ApplicantListAdapter adapter;
    private List<RApplicantBO> rApplicantBOs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack();
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_applicantList);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_applicant_list;
    }

    private void init() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(getResources().getColor(R.color.transparent))
                .size((int) getResources().getDimension(R.dimen.divider_height2))
                .build());
        adapter = new ApplicantListAdapter(this, BaseListAdapter.ONLY_FOOTER);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        rApplicantBOs = (ArrayList<RApplicantBO>) getIntent().getSerializableExtra(getString(R.string.intent_value));
        LogUtils.d(TAG, "rApplicantBOs:" + rApplicantBOs.size());
        adapter.addItems(0, rApplicantBOs);
    }

    @Override
    public void onItemClick(int position, long id, View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.intent_value), rApplicantBOs.get(position));
        openActivity(ApplicantInfoActivity.class, bundle);
    }
}