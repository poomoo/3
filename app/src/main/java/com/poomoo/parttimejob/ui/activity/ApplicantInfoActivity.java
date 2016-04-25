/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.poomoo.model.response.RApplicantBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.view.ApplicantInfoView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/9 15:30.
 */
public class ApplicantInfoActivity extends BaseActivity implements ApplicantInfoView {
    @Bind(R.id.img_applicantAvatar)
    ImageView avatarImg;
    @Bind(R.id.img_applicantSex)
    ImageView sexImg;
    @Bind(R.id.txt_applicantSex)
    TextView sexTxt;
    @Bind(R.id.txt_applicantHeight)
    TextView heightTxt;
    @Bind(R.id.txt_applicantNickName)
    TextView nickNameTxt;
    @Bind(R.id.txt_applicantIntention)
    TextView intentionTxt;
    @Bind(R.id.txt_applicantSchool)
    TextView schoolTxt;
    @Bind(R.id.txt_applicantRegisterDate)
    TextView registerDateTxt;

    private RApplicantBO rApplicantBO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rApplicantBO = (RApplicantBO) getIntent().getSerializableExtra(getString(R.string.intent_value));
        setBack();
        ButterKnife.bind(this);

        initView();
    }

    @Override
    protected String onSetTitle() {
        return rApplicantBO.nickName;
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_applicant_info;
    }

    private void initView() {
        sexImg.setImageResource(rApplicantBO.sex == 1 ? R.drawable.ic_man : R.drawable.ic_woman);
        sexTxt.setText(rApplicantBO.sex == 1 ? "男 " + rApplicantBO.age : "女 " + rApplicantBO.age);
        sexTxt.setBackgroundResource(rApplicantBO.sex == 1 ? R.drawable.style_label_male : R.drawable.style_label_female);
        heightTxt.setText(rApplicantBO.height);
        nickNameTxt.setText(rApplicantBO.nickName);
        intentionTxt.setText(rApplicantBO.intention);
        schoolTxt.setText(rApplicantBO.schoolName);
        registerDateTxt.setText(rApplicantBO.registDt);
    }
}
