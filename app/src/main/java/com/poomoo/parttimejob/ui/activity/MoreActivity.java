/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.poomoo.commlib.SPUtils;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/16 17:10.
 */
public class MoreActivity extends BaseActivity {
    @Bind(R.id.btn_logOut)
    Button logOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack();
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_more);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_more;
    }

    private void initView() {
        if ((boolean) SPUtils.get(getApplicationContext(), getString(R.string.sp_isLogin), false))
            logOutBtn.setVisibility(View.VISIBLE);
        else
            logOutBtn.setVisibility(View.GONE);
    }

    /**
     * 2修改密码
     *
     * @param view
     */
    public void toChangePassWord(View view) {
        openActivity(ChangePassWordActivity.class);
    }

    /**
     * 2关于
     *
     * @param view
     */
    public void toAbout(View view) {

    }

    /**
     * 退出登录
     *
     * @param view
     */
    public void toLogOut(View view) {
        Dialog dialog = new AlertDialog.Builder(this).setMessage("确认退出?").setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.instance.finish();
                openActivity(LoginActivity.class);
                SPUtils.put(getApplicationContext(), getString(R.string.sp_isLogin), false);
                finish();
            }
        }).create();
        dialog.show();
    }
}
