/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyConfig;
import com.poomoo.commlib.MyUtils;
import com.poomoo.commlib.StatusBarUtil;
import com.poomoo.model.response.RCommodityInfoBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.listener.ScrollViewListener;
import com.poomoo.parttimejob.presentation.CommodityPresenter;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.ui.custom.MyScrollView;
import com.poomoo.parttimejob.ui.custom.SlideShowView;
import com.poomoo.parttimejob.view.CommodityInfoView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/27 09:27.
 */
public class CommodityInfoActivity extends BaseActivity implements CommodityInfoView, ScrollViewListener {
    @Bind(R.id.myScrollView)
    MyScrollView myScrollView;
    @Bind(R.id.slide_commodity)
    SlideShowView slideShowView;
    @Bind(R.id.txt_commodityName)
    TextView commodityNameTxt;
    @Bind(R.id.txt_commodityPrice)
    TextView commodityPriceTxt;
    @Bind(R.id.txt_buyNum)
    TextView buyNumTxt;
    @Bind(R.id.txt_condition)
    TextView conditionTxt;
    @Bind(R.id.webView_commodity)
    WebView commodityWeb;
    @Bind(R.id.img_collect)
    ImageView collectImg;
    @Bind(R.id.txt_collect)
    TextView collectTxt;
    @Bind(R.id.titleBar)
    RelativeLayout titleBar;
    @Bind(R.id.txt_titleBar_name)
    TextView titleTxt;
    @Bind(R.id.img_titleBar_back)
    ImageView back;
    @Bind(R.id.llayout_commodityInfo)
    LinearLayout linearLayout;

    private CommodityPresenter commodityPresenter;
    private boolean isCollected = false;
    private int goodsId;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        titleTxt.setText(getString(R.string.title_buy));
        back.setOnClickListener(v -> {
            finish();
        });
        commodityPresenter = new CommodityPresenter(this);
        init();
    }


    @Override
    protected String onSetTitle() {
        return getString(R.string.title_buy);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_commodify_info;
    }

    private void init() {
        StatusBarUtil.setTransparent(this);
        titleBar.getBackground().setAlpha(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            titleBar.setGravity(Gravity.BOTTOM);
            titleBar.setPadding(0, (int) getResources().getDimension(R.dimen.toolbar_marginTop), 0, (int) getResources().getDimension(R.dimen.dp_8));
        }
        myScrollView.setScrollViewListener(this);

        goodsId = getIntent().getIntExtra(getString(R.string.intent_value), -1);
        showProgressDialog(getString(R.string.dialog_msg));
        commodityPresenter.getCommodityInfo(goodsId, application.getUserId());
        linearLayout.setVisibility(View.INVISIBLE);

        slideShowView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MyUtils.getScreenWidth(this)));//设置广告栏的宽高比为3:1
    }

    public void collect(View view) {
        if (!application.isLogin()) {
            MyUtils.showToast(getApplicationContext(), MyConfig.pleaseLogin);
            return;
        }
        showProgressDialog(getString(R.string.dialog_msg));
        if (isCollected)
            commodityPresenter.cancelCollect(goodsId, application.getUserId());
        else
            commodityPresenter.collect(goodsId, application.getUserId());
    }

    public void exchange(View view) {
        if (!application.isLogin()) {
            MyUtils.showToast(getApplicationContext(), MyConfig.pleaseLogin);
            return;
        }
        LogUtils.d(TAG, "URL:" + url);
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.intent_value), url);
        openActivity(WebViewActivity.class, bundle);
    }


    @Override
    public void failed(String msg) {
        MyUtils.showToast(getApplicationContext(), msg);
        finish();
    }

    @Override
    public void succeed(RCommodityInfoBO rCommodityInfoBO) {
        String[] urls = rCommodityInfoBO.picList.toArray(new String[rCommodityInfoBO.picList.size()]);
//        String[] urls = new String[]{"http://img11.360buyimg.com/cms/jfs/t568/258/1055840219/709214/704836ad/54aaa17aN6a93104f.jpg", "http://www.gg888.com.cn/pictures/banner/banner-1.jpg", "http://easyread.ph.126.net/BxgFDAoYxb1R67r8N-Ujeg==/7916515605825262015.jpg"};
        LogUtils.d(TAG, "urls:" + urls.length + ":" + urls[0] + ":" + urls[1] + ";" + urls[2]);
        slideShowView.setPics(urls, position -> {

        });

        commodityNameTxt.setText(rCommodityInfoBO.goodsName);

        commodityPriceTxt.setText(rCommodityInfoBO.price + "");
        buyNumTxt.setText(rCommodityInfoBO.exchangedNum + "");
        conditionTxt.setText(rCommodityInfoBO.exchangeCondition);
        commodityWeb.loadData(rCommodityInfoBO.goodsDetails, "text/html; charset=UTF-8", null);// 这种写法可以正确解码

        if (rCommodityInfoBO.isCollect) {
            isCollected = true;
            collectTxt.setText(getString(R.string.btn_unCollect));
        } else {
            isCollected = false;
            collectTxt.setText(getString(R.string.btn_collect));
        }
        goodsId = rCommodityInfoBO.goodsId;
        url = rCommodityInfoBO.jumpUrl;
        closeProgressDialog();
        linearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void collectSucceed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), "收藏成功");
        collectTxt.setText(getString(R.string.btn_unCollect));
        isCollected = true;
    }

    @Override
    public void collectFailed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), "收藏失败");
    }

    @Override
    public void cancelCollectSucceed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), "取消收藏成功");
        collectTxt.setText(getString(R.string.btn_collect));
        isCollected = false;
    }

    @Override
    public void cancelCollectFailed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), "取消收藏失败");
    }

    @Override
    public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (scrollView.getScrollY() < MyUtils.getScreenWidth(this))
            titleBar.getBackground().setAlpha(255 * scrollView.getScrollY() / MyUtils.getScreenWidth(this));
        else
            titleBar.getBackground().setAlpha(255);
    }
}
