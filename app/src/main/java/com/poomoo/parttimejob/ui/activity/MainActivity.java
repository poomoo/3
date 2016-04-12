/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioButton;

import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyConfig;
import com.poomoo.commlib.MyUtils;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.fragment.JobFragment;
import com.poomoo.parttimejob.ui.fragment.MainFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者: 李苜菲
 * 日期: 2016/3/22 16:08.
 */
public class MainActivity extends BaseActivity {

    private MainFragment mainFragment;
    private JobFragment jobFragment;
    private Fragment curFragment;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultFragment();
        ButterKnife.bind(this);
    }

    @Override
    protected String onSetTitle() {
        return null;
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_main;
    }

    private void setDefaultFragment() {
        // TODO 自动生成的方法存根
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        mainFragment = new MainFragment();
        curFragment = mainFragment;
        fragmentTransaction.add(R.id.flayout_main, curFragment);
        fragmentTransaction.commit();
    }

    @OnClick({R.id.rbtn_main, R.id.rbtn_job, R.id.rbtn_message, R.id.rbtn_personal})
    void change(RadioButton radioButton) {
        switch (radioButton.getId()) {
            case R.id.rbtn_main:
                break;
            case R.id.rbtn_job:
                if (jobFragment == null)
                    jobFragment = new JobFragment();
                switchFragment(jobFragment);
                curFragment = jobFragment;
                break;
            case R.id.rbtn_message:
                break;
            case R.id.rbtn_personal:
                break;
        }
    }

    private void switchFragment(Fragment to) {
        LogUtils.i(TAG, "switchFragment---" + to);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!to.isAdded()) { // 先判断是否被add过
            fragmentTransaction.hide(curFragment).add(R.id.flayout_main, to); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            fragmentTransaction.hide(curFragment).show(to); // 隐藏当前的fragment，显示下一个
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO 自动生成的方法存根
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitApp();
        }
        return true;
    }

    private void exitApp() {
        // 判断2次点击事件时间
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            MyUtils.showToast(getApplicationContext(), MyConfig.exitApp);
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
