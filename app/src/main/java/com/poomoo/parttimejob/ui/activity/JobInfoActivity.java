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
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.poomoo.commlib.MyUtils;
import com.poomoo.model.response.RApplicantBO;
import com.poomoo.model.response.RJobInfoBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.adapter.GridAdapter;
import com.poomoo.parttimejob.presentation.JobInfoPresenter;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.view.JobInfoView;

import java.io.Serializable;
import java.util.ArrayList;
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

    private JobInfoPresenter jobInfoPresenter;
    private int[] pics = {R.drawable.ic_1, R.drawable.ic_2, R.drawable.ic_3, R.drawable.ic_4};
    private List<Integer> integers = new ArrayList<>();
    private GridAdapter adapter;
    private List<RApplicantBO> rApplicantBOs = new ArrayList<>();
    private RApplicantBO rApplicantBO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack();
        ButterKnife.bind(this);
        jobInfoPresenter = new JobInfoPresenter(this);
        showProgressDialog(getString(R.string.dialog_msg));
        jobInfoPresenter.queryJobInfo(getIntent().getIntExtra(getString(R.string.intent_value), -1));
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_jobInfo);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_job_info;
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
        for (Integer integer : pics)
            integers.add(integer);
        adapter.addItems(integers);

        for (int i = 0; i < 10; i++) {
            rApplicantBO = new RApplicantBO();
            rApplicantBO.age = i + 20;
            rApplicantBO.sex = i % 2 == 0 ? 1 : 2;
            rApplicantBO.nickName = "安卓" + i;
            rApplicantBO.intention = "求职意向你擦不到" + i;
            rApplicantBO.height = "165cm";
            rApplicantBO.schoolName = "北京姑娘大学";
            rApplicantBO.registDt = "2016-01-25";
            rApplicantBOs.add(rApplicantBO);
        }
    }

    @Override
    public void failed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), msg);
    }

    /**
     * 打电话
     *
     * @param view
     */
    public void dial(View view) {
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
     * 收藏
     *
     * @param view
     */
    public void collect(View view) {

    }

    /**
     * 报名
     *
     * @param view
     */
    public void signIn(View view) {
        openActivity(SignUpActivity.class);
    }

    /**
     * 所有的申请人
     *
     * @param view
     */
    public void allApplicants(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.intent_value), (Serializable) rApplicantBOs);
        openActivity(ApplicantListActivity.class, bundle);
    }


}
