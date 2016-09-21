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

import com.bumptech.glide.util.LogTime;
import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyUtils;
import com.poomoo.model.response.RIntentionBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.database.DataBaseHelper;
import com.poomoo.parttimejob.event.Events;
import com.poomoo.parttimejob.event.RxBus;
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

    private List<String> typeInfos;
    private List<String> areaInfos;
    private List<Integer> areaId_list=new ArrayList<>();
    private String cateId = "";
    private String areaId = "";
    private String workday = "";
    private String otherInfo = "";
    private JobIntentionPresenter jobIntentionPresenter;
    private LayoutInflater mInflater;
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
        jobIntentionPresenter.JobIntentionDown(application.getUserId(), typeInfos, areaId_list);
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
        for (String temp : areaInfos)
            areaId_list.add(temp.split("#").length==2?Integer.parseInt(temp.split("#")[1]):0);

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
            int pos = itArea.next();
            String temp[] = areaInfos.get(pos).split("#");
            areaId += temp.length == 2 ? temp[1] + "," : "";
        }
        cateId = cateId.length() > 0 ? cateId.substring(0, cateId.length() - 1) : "0";
        areaId = areaId.length() > 0 ? areaId.substring(0, areaId.length() - 1) : "0";
        int len = freeTimeView.getSparseArray().size();
        for (int i = 0; i < len; i++) {
            if (freeTimeView.getSparseArray().get(i)) {
                workday += i / 8 + "" + i % 8 + ",";
            }
        }
        otherInfo = otherEdt.getText().toString().trim();
        String type = "1";
        if (TextUtils.isEmpty(cateId) && TextUtils.isEmpty(areaId) && TextUtils.isEmpty(workday) && TextUtils.isEmpty(otherInfo))
            type = "2";
        showProgressDialog(getString(R.string.dialog_msg));
        jobIntentionPresenter.JobIntentionUp(application.getUserId(), type, cateId, workday, areaId, otherInfo);
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
        RxBus.getInstance().send(Events.EventEnum.SET_INTENTION, null);
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
        LogUtils.d(TAG, "RIntentionBO: workAreaId" + rIntentionBO.workAreaId + "\n" + "area:" + rIntentionBO.area);
        LogUtils.d(TAG, "RIntentionBO: type" + rIntentionBO.type);
        closeProgressDialog();
        adapterType.setSelectedList(rIntentionBO.type);

        if (!TextUtils.isEmpty(rIntentionBO.workAreaId))
            adapterArea.setSelectedList(rIntentionBO.area);

        if (!TextUtils.isEmpty(rIntentionBO.workDay))
            freeTimeView.setSelected(rIntentionBO.work);

        otherEdt.setText(rIntentionBO.otherInfo);
    }

//    private int getPos(String workday) {
//        int x = Integer.parseInt(workday.substring(0, 1));
//        int y = Integer.parseInt(workday.substring(1, 2));
//        return (x - 1) * 8 + (y + 8);
//    }

    @Override
    public void DownFailed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), msg);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        jobIntentionPresenter.onDestroy();
    }
}
