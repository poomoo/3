/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.poomoo.commlib.MyUtils;
import com.poomoo.model.response.RAboutBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.presentation.AboutPresenter;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.view.AboutView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/27 09:27.
 */
public class AboutActivity extends BaseActivity implements AboutView {
    @Bind(R.id.txt_aboutTitle)
    TextView titleTxt;
    @Bind(R.id.web_about)
    WebView webView;

    private AboutPresenter aboutPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack();
        ButterKnife.bind(this);
        aboutPresenter = new AboutPresenter(this);
        showProgressDialog(getString(R.string.dialog_msg));
        aboutPresenter.about();
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_about);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_about;
    }

    @Override
    public void succeed(RAboutBO rAboutBO) {
        closeProgressDialog();
        titleTxt.setText(rAboutBO.title);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.setBackgroundColor(getResources().getColor(R.color.colorBg)); // 设置背景色
        webView.loadData(rAboutBO.content, "text/html; charset=UTF-8", null);// 这种写法可以正确解码
    }

    @Override
    public void failed(String msg) {
        closeProgressDialog();
        MyUtils.showToast(getApplicationContext(), msg);
        finish();

    }
}
