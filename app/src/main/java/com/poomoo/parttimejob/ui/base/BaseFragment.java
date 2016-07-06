/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.application.MyApplication;
import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * 作者: 李苜菲
 * 日期: 2016/3/18 14:32.
 */
public abstract class BaseFragment extends RxFragment {
    public String TAG = getClass().getSimpleName();
    public MyApplication application;
    //进度对话框
    public ProgressDialog progressDialog = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        application = (MyApplication) getActivity().getApplication();
    }

    /**
     * @param pClass
     */
    protected void openActivity(Class<?> pClass) {
        Intent intent = new Intent(getActivity(), pClass);
        getActivity().startActivity(intent);
    }

    /**
     * @param pClass
     * @param pBundle
     */
    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(getActivity(), pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        this.startActivity(intent);
        getActivityInFromRight();
    }

    /**
     * @param pClass
     * @param requestCode
     */
    protected void openActivityForResult(Class<?> pClass, int requestCode) {
        Intent intent = new Intent(getActivity(), pClass);
        startActivityForResult(intent, requestCode);
        getActivityInFromRight();
    }

    /**
     * activity切换-> 向左进(覆盖)
     */
    protected void getActivityInFromRight() {
        getActivity().overridePendingTransition(R.anim.activity_in_from_right,
                R.anim.activity_center);
    }

    /**
     * activity切换-> 向右出(抽出)
     */
    protected void getActivityOutToRight() {
        getActivity().overridePendingTransition(R.anim.activity_center,
                R.anim.activity_out_to_right);
    }

    /**
     * 显示进度对话框
     *
     * @param msg
     */
    protected void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(msg);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
        progressDialog.setOnKeyListener((dialog, keyCode, event) -> {
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                progressDialog.dismiss();
                progressDialog = null;
                getActivity().finish();
            }
            return false;
        });
    }

    /**
     * 关闭对话框
     */
    protected void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
