/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.poomoo.model.base.BaseJobBO;
import com.poomoo.model.response.RApplyJobBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.adapter.BaseListAdapter;
import com.poomoo.parttimejob.adapter.JobsAdapter;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/26 10:22.
 */
public class TestActivity extends BaseActivity {
    @Bind(R.id.list_view)
    RecyclerView listView;

    private JobsAdapter adapter;
    private List<BaseJobBO> rApplyJobBOs = new ArrayList<>();
    private RApplyJobBO rApplyJobBO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected String onSetTitle() {
        return "首页测试";
    }

    @Override
    protected int onBindLayout() {
        return R.layout.fragment_main;
    }

    private void initView() {
        listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(getResources().getColor(R.color.transparent))
                .size((int) getResources().getDimension(R.dimen.divider_height))
                .build());

        adapter = new JobsAdapter(this, BaseListAdapter.ONLY_FOOTER, false);
        listView.setAdapter(adapter);
        for (int i = 0; i < 10; i++) {
            rApplyJobBO = new RApplyJobBO("金阳" + i, i, i, 100 * (i + 1) + "￥/天", "第" + i + "份工作", i + "分钟前");
            rApplyJobBOs.add(rApplyJobBO);
        }
        adapter.addItems(rApplyJobBOs);
    }

}
