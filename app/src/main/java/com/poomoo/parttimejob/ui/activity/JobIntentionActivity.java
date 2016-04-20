/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyConfig;
import com.poomoo.commlib.MyUtils;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.ui.custom.FreeTimeView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 求职意向设置
 * 作者: 李苜菲
 * 日期: 2016/4/18 11:53.
 */
public class JobIntentionActivity extends BaseActivity {
    @Bind(R.id.freeTimeView)
    FreeTimeView freeTimeView;
    @Bind(R.id.llayout_zone)
    LinearLayout zoneLlayout;
    @Bind(R.id.flayout_type)
    TagFlowLayout typeFlayout;
    @Bind(R.id.flayout_zone)
    TagFlowLayout zoneFlayout;

    private String[] zone = {"不限", "南明", "修文县", "息烽县", "开阳县", "小河", "白云", "乌当", "花溪", "云岩", "清镇市"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        initTitleBar();
        final LayoutInflater mInflater = LayoutInflater.from(this);
        initJobType(mInflater);//兼职类型
        initJobZone(mInflater);//兼职地点
    }

    private void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.rightTxt.setVisibility(View.VISIBLE);
        headerViewHolder.rightTxt.setText(R.string.label_reSet);
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_jobIntention);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_job_intention;
    }

    private void initJobType(final LayoutInflater mInflater) {
        typeFlayout.setAdapter(new TagAdapter<String>(MyConfig.jobType) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, typeFlayout, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public boolean setSelected(int position, String s) {
                return s.equals(MyConfig.jobType[0]);
            }
        });

        typeFlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                MyUtils.showToast(getApplicationContext(), "兼职类型" + MyConfig.jobType[position]);
                return true;
            }
        });
    }

    private void initJobZone(final LayoutInflater mInflater) {
        zoneFlayout.setAdapter(new TagAdapter<String>(zone) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, zoneFlayout, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public boolean setSelected(int position, String s) {
                return s.equals(zone[0]);
            }
        });
        zoneFlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                MyUtils.showToast(getApplicationContext(), "兼职地点" + zone[position]);
                return true;
            }
        });

    }

    public void toConfirm(View view) {
        LogUtils.d(TAG, "" + zoneFlayout.getSelectedList().iterator().next());
    }

    /**
     * 重置
     *
     * @param view
     */
    public void toDo(View view) {
        freeTimeView.clearAll();
        typeFlayout.reSet();
        zoneFlayout.reSet();
    }
}
