/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;

import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.view.SearchJobView;

/**
 * 搜索职位
 * 作者: 李苜菲
 * 日期: 2016/4/9 10:54.
 */
public class SearchJobActivity extends BaseActivity implements SearchJobView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_searchJob);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_search_job;
    }
}
