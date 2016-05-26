/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/27 09:27.
 */
public class ProtocolActivity extends BaseActivity {
    @Bind(R.id.txt_aboutTitle)
    TextView titleTxt;
    @Bind(R.id.web_about)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack();
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        titleTxt.setVisibility(View.GONE);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.setBackgroundColor(getResources().getColor(R.color.colorBg)); // 设置背景色
        webView.loadUrl("file:///android_asset/protocol.html");
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_protocol);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_about;
    }
}
