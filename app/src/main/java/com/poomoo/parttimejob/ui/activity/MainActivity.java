/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyConfig;
import com.poomoo.commlib.MyUtils;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.ui.fragment.JobFragment;
import com.poomoo.parttimejob.ui.fragment.MainFragment;
import com.poomoo.parttimejob.ui.fragment.PersonalFragment;
import com.poomoo.parttimejob.ui.fragment.ServiceFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者: 李苜菲
 * 日期: 2016/3/22 16:08.
 */
public class MainActivity extends BaseActivity {
    @Bind(R.id.llayout_main)
    LinearLayout mainLlayout;

    private Fragment mainFragment;
    private Fragment jobFragment;
    private Fragment serviceFragment;
    private Fragment personalFragment;
    private Fragment curFragment;
    private long exitTime = 0;
    public static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultFragment();
        ButterKnife.bind(this);
        instance = this;

        String bundle = getIntent().getStringExtra(getString(R.string.intent_bundle));
        if (!TextUtils.isEmpty(bundle))
            toJob();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.d(TAG, "onNewIntent");
        toJob();
    }

    private void toJob() {
        if (jobFragment == null)
            jobFragment = Fragment.instantiate(this, JobFragment.class.getName());
        switchFragment(jobFragment);
        curFragment = jobFragment;
        ((RadioButton) findViewById(R.id.rbtn_job)).setChecked(true);
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
        mainFragment = Fragment.instantiate(this, MainFragment.class.getName());
        curFragment = mainFragment;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.flayout_main, curFragment)
                .commit();
    }

//    @Override
//    protected void onResume() {
//        LogUtils.i(TAG, "onResume");
//        super.onResume();
//    }

    @OnClick({R.id.rbtn_main, R.id.rbtn_job, R.id.rbtn_message, R.id.rbtn_personal})
    void change(RadioButton radioButton) {
        switch (radioButton.getId()) {
            case R.id.rbtn_main:
                switchFragment(mainFragment);
                curFragment = mainFragment;
                break;
            case R.id.rbtn_job:
                if (jobFragment == null)
                    jobFragment = Fragment.instantiate(this, JobFragment.class.getName());
                switchFragment(jobFragment);
                curFragment = jobFragment;
                break;
            case R.id.rbtn_message:
                if (serviceFragment == null)
                    serviceFragment = Fragment.instantiate(this, ServiceFragment.class.getName());
                switchFragment(serviceFragment);
                curFragment = serviceFragment;
                break;
            case R.id.rbtn_personal:
                if (personalFragment == null)
                    personalFragment = Fragment.instantiate(this, PersonalFragment.class.getName());
                switchFragment(personalFragment);
                curFragment = personalFragment;
                break;
        }
    }

    private void switchFragment(Fragment to) {
        LogUtils.d(TAG, "to:" + to + " isAdd():" + to.isAdded());
        if (!to.isAdded()) { // 先判断是否被add过
            getSupportFragmentManager().beginTransaction()
                    .hide(curFragment)
                    .add(R.id.flayout_main, to)
                    .commit();
            // 隐藏当前的fragment，add下一个到Activity中
        } else {
            getSupportFragmentManager().beginTransaction()
                    .hide(curFragment)
                    .show(to)
                    .commit();
            // 隐藏当前的fragment，显示下一个
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO 自动生成的方法存根
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (curFragment == jobFragment) {
                if (JobFragment.instance.onKeyDown(keyCode, event))
                    return true;
            }
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
            MyConfig.isRun = false;
            finish();
            System.exit(0);//正常退出App
        }
    }

//    public void setBackGround1() {
//        mainLlayout.setBackgroundResource(R.drawable.bg_personal);
//    }
//
//    public void setBackGround2() {
//        mainLlayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//    }
//
//    public void setBackGround3() {
//        mainLlayout.setBackgroundColor(getResources().getColor(R.color.transparent));
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*然后在碎片中调用重写的onActivityResult方法*/
        curFragment.onActivityResult(requestCode, resultCode, data);
    }
}
