/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyConfig;
import com.poomoo.commlib.MyUtils;
import com.poomoo.model.base.BaseJobBO;
import com.poomoo.model.response.RAboutBO;
import com.poomoo.model.response.RApplicantBO;
import com.poomoo.model.response.RBuyBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.adapter.ApplicantListAdapter;
import com.poomoo.parttimejob.adapter.BaseListAdapter;
import com.poomoo.parttimejob.adapter.BuyListAdapter;
import com.poomoo.parttimejob.adapter.JobsAdapter;
import com.poomoo.parttimejob.presentation.AboutPresenter;
import com.poomoo.parttimejob.presentation.BuyPresenter;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.ui.base.BaseListActivity;
import com.poomoo.parttimejob.view.AboutView;
import com.poomoo.parttimejob.view.BuyView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/27 09:27.
 */
public class CommodityListActivity extends BaseListActivity<RBuyBO> implements BuyView, BaseListAdapter.OnItemClickListener {

    private BuyPresenter buyPresenter;
    private int currPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack();
        ButterKnife.bind(this);
        buyPresenter = new BuyPresenter(this);
        init();
    }

    @Override
    protected BaseListAdapter<RBuyBO> onSetupAdapter() {
        return new BuyListAdapter(this, BaseListAdapter.ONLY_FOOTER);
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_buy);
    }


    private void init() {
        mListView.setLayoutManager(new GridLayoutManager(this, 2));
        mListView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(getResources().getColor(R.color.transparent))
                .size((int) getResources().getDimension(R.dimen.divider_height2))
                .build());
        mListView.addItemDecoration(new VerticalDividerItemDecoration.Builder(this)
                .color(getResources().getColor(R.color.transparent))
                .size((int) getResources().getDimension(R.dimen.divider_height2))
                .build());
        mAdapter.setOnItemClickListener(this);

        buyPresenter.getBuyList(currPage);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mCurrentPage = 1;
        buyPresenter.getBuyList(currPage);
    }

    @Override
    public void onLoading() {
        super.onLoading();
        buyPresenter.getBuyList(currPage);
    }

    @Override
    public void succeed(List<RBuyBO> rBuyBOs) {
        onLoadFinishState(action);
        onLoadResultData(rBuyBOs);
    }

    @Override
    public void failed(String msg) {
        if (msg.contains("检查网络"))
            onNetworkInvalid(action);
        else
            onLoadErrorState(action);
    }

    @Override
    public void onItemClick(int position, long id, View view) {
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.intent_value), mAdapter.getItem(position).goodsId);
        openActivity(CommodityInfoActivity.class, bundle);
    }
}
