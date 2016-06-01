/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.poomoo.commlib.MyConfig;
import com.poomoo.commlib.MyUtils;
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
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

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
    @Bind(R.id.txt_jobInfoPayNum)
    TextView payNumTxt;
    @Bind(R.id.txt_jobInfoPayUnit)
    TextView payUnitTxt;
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
    @Bind(R.id.llayout_collect)
    LinearLayout collectLlayout;
    @Bind(R.id.scroll_jobInfo)
    ScrollView infoView;

    private JobInfoPresenter jobInfoPresenter;
    private int[] pics = {R.drawable.ic_1, R.drawable.ic_2, R.drawable.ic_3, R.drawable.ic_4};
    private List<Integer> integers = new ArrayList<>();
    private GridAdapter adapter;
    private List<RApplicantBO> rApplicantBOs = new ArrayList<>();
    private RApplicantBO rApplicantBO;
    private RJobInfoBO rJobInfoBO;
    private JobsAdapter jobsAdapter;
    private int jobId;
    private boolean isCollected = false;

    // 首先在您的Activity中添加如下成员变量
    public static final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
    private String content = "边玩边赚钱,就是这么任性!";
    private String website = "http://lzrb.91jiaoyou.cn/lzrb/download/wap.html";
    private String title = "点击下载兼职GO APP";

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
                .size((int) getResources().getDimension(R.dimen.divider_height2))
                .build());
        jobsAdapter = new JobsAdapter(this, BaseListAdapter.NEITHER, false);
        recyclerView.setAdapter(jobsAdapter);

        jobInfoPresenter = new JobInfoPresenter(this);
        showProgressDialog(getString(R.string.dialog_msg));
        jobId = getIntent().getIntExtra(getString(R.string.intent_value), -1);
        jobInfoPresenter.queryJobInfo(jobId, application.getUserId());
        jobInfoPresenter.queryRecommendJobs(application.getUserId(), application.getCurrCityId(), 1);
        jobInfoPresenter.browse(jobId, application.getUserId());
        infoView.setVisibility(View.GONE);
    }

    /**
     * 地图
     *
     * @param view
     */
    public void toMap(View view) {
        Bundle bundle = new Bundle();
        bundle.putDouble(getString(R.string.intent_lat), rJobInfoBO.lat);
        bundle.putDouble(getString(R.string.intent_lng), rJobInfoBO.lng);
        openActivity(MapActivity.class, bundle);
    }

    /**
     * 打电话
     *
     * @param view
     */
    public void dial(View view) {
        if (!application.isLogin()) {
            MyUtils.showToast(getApplicationContext(), MyConfig.pleaseLogin);
            return;
        }
        Dialog dialog = new AlertDialog.Builder(this).setMessage("拨打电话" + telTxt.getText()).setNegativeButton("取消", (dialog1, which) -> {
        }).setPositiveButton("确定", (dialog1, which) -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telTxt.getText().toString()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }).create();
        dialog.show();
    }

    /**
     * 更多
     *
     * @param view
     */
    public void more(View view) {
        if (!application.isLogin()) {
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
        if (!application.isLogin()) {
            MyUtils.showToast(getApplicationContext(), MyConfig.pleaseLogin);
            return;
        }
        showProgressDialog(getString(R.string.dialog_msg));
        if (!isCollected)
            jobInfoPresenter.collect(jobId, application.getUserId());
        else
            jobInfoPresenter.cancelCollect(jobId, application.getUserId());
    }

    /**
     * 分享
     *
     * @param view
     */
    public void share(View view) {
        // 配置需要分享的相关平台
        configPlatforms();
        // 设置分享内容
        shareContent();
        // 是否只有已登录用户才能打开分享选择页
//        mController.getConfig().setPlatforms(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
//                SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT, SHARE_MEDIA.SMS);
        mController.getConfig().setPlatforms(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE);
        mController.openShare(this, false);
    }

    /**
     * 配置分享平台参数</br>
     */
    private void configPlatforms() {
        // 添加新浪SSO授权
        mController.getConfig().setSsoHandler(new SinaSsoHandler());

        // 添加QQ、QZone平台
        addQQZonePlatform();

        // 添加微信、微信朋友圈平台
        addWXPlatform();

        // 添加短信平台
        addSMS();
    }

    public void shareContent() {
        // 本地图片
        UMImage localImage = new UMImage(this, R.mipmap.ic_logo);

        // 配置SSO
        mController.getConfig().setSsoHandler(new SinaSsoHandler());

        // QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this,
        // "100424468", "c7394704798a158208a74ab60104f0ba");

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(content);
        weixinContent.setTitle(title);
        weixinContent.setTargetUrl(website);
        weixinContent.setShareMedia(localImage);
        mController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(content);
        circleMedia.setTitle(title);
        circleMedia.setShareMedia(localImage);
        circleMedia.setTargetUrl(website);
        mController.setShareMedia(circleMedia);

        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(content);
        qzone.setTargetUrl(website);
        qzone.setTitle(title);
        qzone.setShareMedia(localImage);
        mController.setShareMedia(qzone);

        // 设置QQ分享内容
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(content);
        qqShareContent.setTitle(title);
        qqShareContent.setShareMedia(localImage);
        qqShareContent.setTargetUrl(website);
        mController.setShareMedia(qqShareContent);

        // 设置短信分享内容
        SmsShareContent sms = new SmsShareContent();
        sms.setShareContent(content);
        sms.setShareImage(localImage);
        mController.setShareMedia(sms);

        // 设置新浪微博分享内容
        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setShareContent(content);
        sinaContent.setShareImage(localImage);
        mController.setShareMedia(sinaContent);

    }

    /**
     * @return
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     * image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     * 要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     * : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     */
    private void addQQZonePlatform() {
        String appId = "1105430386";
        String appKey = "Nsi92kmIuu6oDpe5";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, appId, appKey);
        qqSsoHandler.setTargetUrl(website);
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, appId, appKey);
        qZoneSsoHandler.addToSocialSDK();
    }

    /**
     * @return
     * @功能描述 : 添加微信平台分享
     */
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
//        String appId = "wx55e834ca0a0327a6";
//        String appSecret = "5bb696d9ccd75a38c8a0bfe0675559b3";

        String appId = "wx49a72bd8d7b71519";
        String appSecret = "584a22fe3611fe843f8486827f8a68ba";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(this, appId, appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(this, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    /**
     * 添加短信平台
     */
    private void addSMS() {
        // 添加短信
        SmsHandler smsHandler = new SmsHandler();
        smsHandler.addToSocialSDK();
    }

    /**
     * 报名
     *
     * @param view
     */
    public void signIn(View view) {
        if (!application.isLogin()) {
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
        if (!application.isLogin()) {
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
        infoView.setVisibility(View.VISIBLE);
        this.rJobInfoBO = rJobInfoBO;
        payNumTxt.setText(MyUtils.formatPay(rJobInfoBO.pay)[0]);
        payUnitTxt.setText(MyUtils.formatPay(rJobInfoBO.pay)[1]);
        browseTxt.setText(rJobInfoBO.browseNum + "");
        jobNameTxt.setText(rJobInfoBO.jobName);
        jobTypeTxt.setText(rJobInfoBO.cateName);
        jobSexTxt.setText(rJobInfoBO.sexReq);
        jobAreaTxt.setText(rJobInfoBO.areaName != null ? rJobInfoBO.areaName : rJobInfoBO.cityName);
        jobPubDateTxt.setText(rJobInfoBO.publishDt);
        jobDescTxt.setText(rJobInfoBO.jobDesc);
        telTxt.setText(rJobInfoBO.contactTel);
        if (rJobInfoBO.isCollect) {
            collectTxt.setText("取消收藏");
            isCollected = true;
        }

        adapter = new GridAdapter(this);
        gridView.setAdapter(adapter);
        rApplicantBOs = rJobInfoBO.applyList;
        adapter.addItems(rApplicantBOs);
    }

    @Override
    public void loadRecommendsSucceed(List<BaseJobBO> rAdBOs) {
        if (rAdBOs == null) return;
        jobsAdapter.addItems(0, rAdBOs);
    }

    @Override
    public void collectSucceed(String msg) {
        closeProgressDialog();
        isCollected = true;
        MyUtils.showToast(getApplicationContext(), "收藏成功");
        collectTxt.setText("取消收藏");
    }

    @Override
    public void collectFailed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), msg);
    }

    @Override
    public void cancelSucceed(String msg) {
        closeProgressDialog();
        isCollected = false;
        MyUtils.showToast(getApplicationContext(), "取消收藏成功");
        collectTxt.setText("收藏");
    }

    @Override
    public void cancelFailed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), msg);
    }

    @Override
    public void failed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), msg);
        finish();
    }
}
