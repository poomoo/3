/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.adapter.ApplicantListAdapter;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.ui.view.ApplicantListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 申请人列表F
 * 作者: 李苜菲
 * 日期: 2016/4/9 12:51.
 */
public class ApplicantListActivity extends BaseActivity implements ApplicantListView {
    @Bind(R.id.list_applicant)
    ListView listView;

    private ApplicantListAdapter adapter;
    private List<String> strings = new ArrayList<>();

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
        adapter = new ApplicantListAdapter(this);
        listView.setAdapter(adapter);
        for (int i = 0; i < 10; i++)
            strings.add(i + "");
        adapter.setItems(strings);
    }

}
