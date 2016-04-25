/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.poomoo.commlib.MyUtils;
import com.poomoo.model.Page;
import com.poomoo.model.base.BaseJobBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.adapter.BaseListAdapter;
import com.poomoo.parttimejob.adapter.JobsAdapter;
import com.poomoo.parttimejob.presentation.SearchJobPresenter;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.ui.custom.ErrorLayout;
import com.poomoo.parttimejob.view.SearchJobView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 搜索职位
 * 作者: 李苜菲
 * 日期: 2016/4/9 10:54.
 */
public class SearchJobActivity extends BaseActivity implements SearchJobView, BaseListAdapter.OnLoadingListener, BaseListAdapter.OnItemClickListener {
    @Bind(R.id.recycler_search)
    RecyclerView recyclerView;
    @Bind(R.id.edt_search_job)
    EditText editText;
    @Bind(R.id.error_frame)
    ErrorLayout errorLayout;

    private SearchJobPresenter searchJobPresenter;

    private JobsAdapter adapter;
    private int currPage = 1;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack();
        ButterKnife.bind(this);
        initView();
        searchJobPresenter = new SearchJobPresenter(this);
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_searchJob);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_search_job;
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(getResources().getColor(R.color.transparent))
                .size((int) getResources().getDimension(R.dimen.divider_height5))
                .build());
        adapter = new JobsAdapter(this, BaseListAdapter.ONLY_FOOTER, false);
        adapter.setOnItemClickListener(this);
        adapter.setOnLoadingListener(this);
        recyclerView.setAdapter(adapter);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 在这里编写自己想要实现的功能
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    content = editText.getText().toString().trim();
                    if (content.length() > 0)
                        search();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClick(int position, long id, View view) {
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.intent_value), adapter.getItem(position).jobId);
        openActivity(JobInfoActivity.class, bundle);
    }

    @Override
    public void onLoading() {
        searchJobPresenter.searchJob(content, currPage);
    }

    private void search() {
        currPage = 1;
        errorLayout.setState(ErrorLayout.LOADING);
        searchJobPresenter.searchJob(content, currPage);
    }

    @Override
    public void succeed(List<BaseJobBO> baseJobBOs) {
        if (baseJobBOs == null) return;

        if (currPage == 1)
            adapter.clear();

        if (adapter.getDataSize() + baseJobBOs.size() == 0) {
            errorLayout.setState(ErrorLayout.EMPTY_DATA);
            adapter.setState(BaseListAdapter.STATE_HIDE);
            return;
        } else if (baseJobBOs.size() < Page.PAGE_SIZE) {
            adapter.setState(BaseListAdapter.STATE_NO_MORE);
        } else {
            adapter.setState(BaseListAdapter.STATE_LOAD_MORE);
        }
        errorLayout.setState(ErrorLayout.HIDE);
        Iterator<BaseJobBO> iterator = baseJobBOs.iterator();
        final List<BaseJobBO> data = adapter.getDataSet();
        while (iterator.hasNext()) {
            BaseJobBO obj = iterator.next();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getId().equals(obj.getId())) {
                    data.set(i, obj);
                    iterator.remove();
                    break;
                }
            }
        }
        if (currPage == 1)
            adapter.addItems(0, baseJobBOs);
        else
            adapter.addItems(baseJobBOs);

        currPage++;
    }

    @Override
    public void failed(String msg) {
        errorLayout.setState(ErrorLayout.LOAD_FAILED);
        MyUtils.showToast(getApplicationContext(), msg);
    }
}
