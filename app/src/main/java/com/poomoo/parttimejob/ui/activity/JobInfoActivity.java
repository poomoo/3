/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.poomoo.commlib.MyConfig;
import com.poomoo.commlib.MyUtils;
import com.poomoo.model.Page;
import com.poomoo.model.base.BaseJobBO;
import com.poomoo.model.response.RApplicantBO;
import com.poomoo.model.response.RJobInfoBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.adapter.BaseListAdapter;
import com.poomoo.parttimejob.adapter.GridAdapter;
import com.poomoo.parttimejob.adapter.JobsAdapter;
import com.poomoo.parttimejob.presentation.JobInfoPresenter;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.view.JobInfoView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 职位信息
 * 作者: 李苜菲
 * 日期: 2016/4/9 11:48.
 */
public class JobInfoActivity extends BaseActivity implements JobInfoView {
    @Bind(R.id.txt_jobInfoPay)
    TextView payTxt;
    @Bind(R.id.txt_jobInfoBrowse)
    TextView browseTxt;
    @Bind(R.id.txt_jobInfoName)
    TextView jobNameTxt;
    @Bind(R.id.txt_jobInfoType)
    TextView jobTypeTxt;
    @Bind(R.id.txt_jobInfoSex)
    TextView jobSexTxt;
    @Bind(R.id.txt_jobInfoArea)
    TextView jobAreaTxt;
    @Bind(R.id.txt_jobInfoPubDate)
    TextView jobPubDateTxt;
    @Bind(R.id.txt_jobDesc)
    TextView jobDescTxt;
    @Bind(R.id.txt_tel)
    TextView telTxt;
    @Bind(R.id.grid_applicants)
    GridView gridView;
    @Bind(R.id.recycler_jobInfo)
    RecyclerView recyclerView;
    @Bind(R.id.img_collect)
    ImageView collectImg;
    @Bind(R.id.txt_collet)
    TextView collectTxt;

    private JobInfoPresenter jobInfoPresenter;
    private int[] pics = {R.drawable.ic_1, R.drawable.ic_2, R.drawable.ic_3, R.drawable.ic_4};
    private List<Integer> integers = new ArrayList<>();
    private GridAdapter adapter;
    private List<RApplicantBO> rApplicantBOs = new ArrayList<>();
    private RApplicantBO rApplicantBO;
    private JobsAdapter jobsAdapter;
    private int jobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack();
        ButterKnife.bind(this);

        initView();
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_jobInfo);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_job_info;
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(getResources().getColor(R.color.transparent))
                .size((int) getResources().getDimension(R.dimen.divider_height5))
                .build());
        jobsAdapter = new JobsAdapter(this, BaseListAdapter.NEITHER, false);
        recyclerView.setAdapter(jobsAdapter);

        jobInfoPresenter = new JobInfoPresenter(this);
        showProgressDialog(getString(R.string.dialog_msg));
        jobId = getIntent().getIntExtra(getString(R.string.intent_value), -1);
        jobInfoPresenter.queryJobInfo(jobId);
        jobInfoPresenter.queryRecommendJobs(1);
    }

    /**
     * 打电话
     *
     * @param view
     */
    public void dial(View view) {
        if(!application.isLogin()){
            MyUtils.showToast(getApplicationContext(), MyConfig.pleaseLogin);
            return;
        }
        Dialog dialog = new AlertDialog.Builder(this).setMessage("拨打电话" + telTxt.getText()).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telTxt.getText().toString()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }).create();
        dialog.show();
    }

    /**
     * 更多
     *
     * @param view
     */
    public void more(View view) {
        if(!application.isLogin()){
            MyUtils.showToast(getApplicationContext(), MyConfig.pleaseLogin);
            return;
        }
        openActivity(MoreJobsActivity.class);
    }

    /**
     * 收藏
     *
     * @param view
     */
    public void collect(View view) {
        if(!application.isLogin()){
            MyUtils.showToast(getApplicationContext(), MyConfig.pleaseLogin);
            return;
        }
        jobInfoPresenter.collet(jobId, application.getUserId());
    }

    /**
     * 报名
     *
     * @param view
     */
    public void signIn(View view) {
        if(!application.isLogin()){
            MyUtils.showToast(getApplicationContext(), MyConfig.pleaseLogin);
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.intent_value), jobId);
        openActivity(SignUpActivity.class, bundle);
    }

    /**
     * 所有的申请人
     *
     * @param view
     */
    public void allApplicants(View view) {
        if(!application.isLogin()){
            MyUtils.showToast(getApplicationContext(), MyConfig.pleaseLogin);
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.intent_value), (Serializable) rApplicantBOs);
        openActivity(ApplicantListActivity.class, bundle);
    }

    @Override
    public void succeed(RJobInfoBO rJobInfoBO) {
        closeProgressDialog();
        payTxt.setText(MyUtils.formatPay(rJobInfoBO.pay, false));
        browseTxt.setText(rJobInfoBO.browseNum + "");
        jobNameTxt.setText(rJobInfoBO.jobName);
        jobTypeTxt.setText(rJobInfoBO.cateName);
        jobSexTxt.setText(rJobInfoBO.sexReq);
        jobAreaTxt.setText(rJobInfoBO.areaName);
        jobPubDateTxt.setText(rJobInfoBO.publishDt);
        jobDescTxt.setText(rJobInfoBO.jobDesc);
        telTxt.setText(rJobInfoBO.contactTel);

        adapter = new GridAdapter(this);
        gridView.setAdapter(adapter);
        rApplicantBOs = rJobInfoBO.applyList;
        adapter.addItems(rApplicantBOs);

//        for (Integer integer : pics)

//        for (int i = 0; i < 10; i++) {
//            rApplicantBO = new RApplicantBO();
//            rApplicantBO.age = i + 20;
//            rApplicantBO.sex = i % 2 == 0 ? 1 : 2;
//            rApplicantBO.nickName = "安卓" + i;
//            rApplicantBO.intention = "求职意向你擦不到" + i;
//            rApplicantBO.height = "165cm";
//            rApplicantBO.schoolName = "北京姑娘大学";
//            rApplicantBO.registDt = "2016-01-25";
//            rApplicantBOs.add(rApplicantBO);
//        }
    }

    @Override
    public void loadRecommendsSucceed(List<BaseJobBO> rAdBOs) {
        if (rAdBOs == null) return;
        jobsAdapter.addItems(0, rAdBOs);
    }

    @Override
    public void collectSucceed(String msg) {
        MyUtils.showToast(getApplicationContext(), "收藏成功");
//        collectImg.setImageDrawable(R.drawable.);
        collectTxt.setText("已收藏");
    }

    @Override
    public void collectFailed(String msg) {
        MyUtils.showToast(getApplicationContext(), msg);
    }

    @Override
    public void failed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), msg);
        finish();
    }
}
