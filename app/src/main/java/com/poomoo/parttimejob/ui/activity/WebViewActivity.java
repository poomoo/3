/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.poomoo.commlib.LogUtils;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: 李苜菲
 * 日期: 2016/7/6 11:36.
 */
public class WebViewActivity extends BaseActivity {
    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    private HeaderViewHolder headerViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.app_name);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_webview;
    }

    private void init() {
        webView.setVisibility(View.INVISIBLE);
        String url = getIntent().getStringExtra(getString(R.string.intent_value));
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new webViewClient());

        headerViewHolder = getHeaderView();
        headerViewHolder.rightImg.setImageResource(R.drawable.ic_close);
        headerViewHolder.rightImg.setVisibility(View.VISIBLE);
        headerViewHolder.rightImg.setOnClickListener(v -> {
                    finish();
                    getActivityOutToRight();
                }
        );
        headerViewHolder.backImg.setOnClickListener(v -> {
                    if (webView.canGoBack())
                        webView.goBack();
                    else {
                        finish();
                        getActivityOutToRight();
                    }
                }
        );
    }

    class webViewClient extends WebViewClient {
        //重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtils.d(TAG, "shouldOverrideUrlLoading:" + url);
            if (url.startsWith("tel:") || url.startsWith("mqqwpa:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
            view.loadUrl(url);
            //如果不需要其他对点击链接事件的处理返回true，否则返回false
            return true;
        }

    }

    class MyWebChromeClient extends WebChromeClient {

        public void onProgressChanged(WebView view, int progress) {
            LogUtils.d(TAG, "onProgressChanged:" + progress);
            progressBar.setProgress(progress);
            progressBar.setVisibility(View.VISIBLE);
            if (progress > 50)
                webView.setVisibility(View.VISIBLE);
            if (progress == 100)
                progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            headerViewHolder.titleTxt.setText(title);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
