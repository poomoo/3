/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyUtils;
import com.poomoo.model.response.RIntentionBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.database.DataBaseHelper;
import com.poomoo.parttimejob.presentation.JobIntentionPresenter;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.ui.custom.FreeTimeView;
import com.poomoo.parttimejob.view.JobIntentionView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 求职意向设置
 * 作者: 李苜菲
 * 日期: 2016/4/18 11:53.
 */
public class JobIntentionActivity extends BaseActivity implements JobIntentionView {
    @Bind(R.id.freeTimeView)
    FreeTimeView freeTimeView;
    @Bind(R.id.flayout_type)
    TagFlowLayout typeFlayout;
    @Bind(R.id.flayout_zone)
    TagFlowLayout zoneFlayout;
    @Bind(R.id.edt_other)
    EditText otherEdt;

    //    private String[] zone = {"不限", "南明", "修文县", "息烽县", "开阳县", "小河", "白云", "乌当", "花溪", "云岩", "清镇市"};
    private List<String> typeInfos;
    private List<String> areaInfos;
    private String cateId = "";
    private String areaId = "";
    private String workday = "";
    private String otherInfo = "";
    private JobIntentionPresenter jobIntentionPresenter;
    private LayoutInflater mInflater;
    private List<Integer> type = new ArrayList<>();
    private List<Integer> area = new ArrayList<>();
    private List<Integer> work = new ArrayList<>();
    private TagAdapter<String> adapterType;
    private TagAdapter<String> adapterArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        initTitleBar();
        mInflater = LayoutInflater.from(this);
        jobIntentionPresenter = new JobIntentionPresenter(this);
        initJobType(mInflater);//兼职类型
        initJobZone(mInflater);//兼职地点

        showProgressDialog(getString(R.string.dialog_msg));
        jobIntentionPresenter.JobIntentionDown(application.getUserId());
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
        typeInfos = DataBaseHelper.getType();
        LogUtils.d(TAG, "typeInfos:" + typeInfos);
        adapterType = new TagAdapter<String>(typeInfos) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, typeFlayout, false);
                tv.setText(s.split("#")[0]);
                return tv;
            }

            @Override
            public boolean setSelected(int position, String s) {
                return s.equals(typeInfos.get(position));
            }
        };
        typeFlayout.setAdapter(adapterType);
    }

    private void initJobZone(final LayoutInflater mInflater) {
        areaInfos = DataBaseHelper.getArea1(application.getCurrCityId());
        LogUtils.d(TAG, "areaInfos:" + areaInfos);
        adapterArea = new TagAdapter<String>(areaInfos) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, zoneFlayout, false);
                tv.setText(s.split("#")[0]);
                return tv;
            }

            @Override
            public boolean setSelected(int position, String s) {
                return s.equals(areaInfos.get(position));
            }

        };
        zoneFlayout.setAdapter(adapterArea);
    }

    public void toConfirm(View view) {
        Iterator<Integer> itType = typeFlayout.getSelectedList().iterator();
        while (itType.hasNext()) {
            String temp[] = typeInfos.get(itType.next()).split("#");
            cateId += temp.length == 2 ? temp[1] + "," : "";
        }

        Iterator<Integer> itArea = zoneFlayout.getSelectedList().iterator();
        while (itArea.hasNext()) {
            String temp[] = areaInfos.get(itArea.next()).split("#");
            areaId += temp.length == 2 ? temp[1] + "," : "";
        }
        cateId = cateId.length() > 0 ? cateId.substring(0, cateId.length() - 1) : "";
        areaId = areaId.length() > 0 ? areaId.substring(0, areaId.length() - 1) : "";
        int len = freeTimeView.getSparseArray().size();
        for (int i = 0; i < len; i++) {
            if (freeTimeView.getSparseArray().get(i)) {
                workday += i / 8 + "" + i % 8 + ",";
            }
        }
        otherInfo = otherEdt.getText().toString().trim();
        LogUtils.d(TAG, "cateId:" + cateId + "areaId:" + areaId + "workday:" + workday);
        showProgressDialog(getString(R.string.dialog_msg));
        jobIntentionPresenter.JobIntentionUp(application.getUserId(), cateId, workday, areaId, otherInfo);
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
        cateId = "";
        areaId = "";
        workday = "";
        otherInfo = "";
        otherEdt.setText(otherInfo);
    }

    @Override
    public void UpSucceed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), msg);
        finish();
    }

    @Override
    public void UpFailed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), msg);
    }

    @Override
    public void DownSucceed(RIntentionBO rIntentionBO) {
        closeProgressDialog();
        int len = rIntentionBO.cateList.size();
        for (int i = 0; i < len; i++) {
            if (rIntentionBO.cateList.get(i).selected)
                type.add(typeInfos.indexOf(rIntentionBO.cateList.get(i).cateName + "#" + rIntentionBO.cateList.get(i).cateId));
        }
        adapterType.setSelectedList(type);
        LogUtils.d(TAG, "TYPE" + type.size() + "type:" + type.toString() + "len:" + len);

        if (!TextUtils.isEmpty(rIntentionBO.workAreaId)) {
            String[] areaStr = rIntentionBO.workAreaId.split(",");
            len = areaStr.length;
            for (int i = 0; i < len; i++)
                area.add(Integer.parseInt(areaStr[i]));
            adapterArea.setSelectedList(area);
            LogUtils.d(TAG, "AREA" + area.size() + "len:" + len + "area.get(0)" + area.get(0));
        }

        if (!TextUtils.isEmpty(rIntentionBO.workDay)) {
            String[] workStr = rIntentionBO.workDay.split(",");
            len = workStr.length;
            for (int i = 0; i < len; i++)
                work.add(getPos(workStr[i]));
            LogUtils.d(TAG, "WORK" + work.size() + "len:" + len);
            freeTimeView.setSelected(work);
        }

        otherEdt.setText(rIntentionBO.otherInfo);
    }

    private int getPos(String workday) {
        int x = Integer.parseInt(workday.substring(0, 1));
        int y = Integer.parseInt(workday.substring(1, 2));
        return (x - 1) * 8 + (y + 8);
    }

    @Override
    public void DownFailed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), msg);
        finish();
    }
}
