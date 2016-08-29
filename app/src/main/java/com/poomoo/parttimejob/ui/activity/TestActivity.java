/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.poomoo.commlib.StatusBarUtil;
import com.poomoo.parttimejob.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: 李苜菲
 * 日期: 2016/6/30 14:29.
 */
public class TestActivity extends AppCompatActivity {
    @Bind(R.id.web_test)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        webView.loadUrl("http://m.xintongdai.com/loan/register/toRegister");
//        webView.loadUrl("http://baidu.com");
        webView.setWebViewClient(new webViewClient());

        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
//        webView.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
//        webView.getSettings().setSupportZoom(true);//是否可以缩放，默认true
//        webView.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
//        webView.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
//        webView.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
//        webView.getSettings().setAppCacheEnabled(true);//是否使用缓存
//        webView.getSettings().setDomStorageEnabled(true);//DOM Storage
    }

    class webViewClient extends WebViewClient {
        //重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            //如果不需要其他对点击链接事件的处理返回true，否则返回false
            return true;
        }
    }
}
