/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.base.BaseTabFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/15 10:21.
 */
public abstract class TabFragment extends BaseTabFragment {
    @Bind(R.id.tab_nav)
    TabLayout mTabLayout;
    private int currItem;

    public TabFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_universal_tab, container, false);
    }

    @SuppressWarnings("all")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ViewCompat.setElevation(mTabLayout, 7);
        if (mAdapter == null) {
            mTabLayout.setupWithViewPager(mViewPager);
        }
        mViewPager.setCurrentItem(currItem);
    }

    public void setPage(int currItem) {
        this.currItem = currItem;
    }

    @Override
    public TextView setupTabItemView(String tag) {
        RelativeLayout layout = (RelativeLayout) View.inflate(mContext, R.layout.view_tab_item, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        layout.setLayoutParams(params);
        TextView tabItemView = (TextView) layout.findViewById(R.id.pager_nav_item);
        tabItemView.setText(tag);
        return tabItemView;
    }

    /**
     * 设置Fragment
     */
    public abstract void onSetupTabs();
}
