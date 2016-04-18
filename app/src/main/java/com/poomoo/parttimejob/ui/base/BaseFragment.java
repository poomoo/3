/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.poomoo.parttimejob.R;

/**
 * 作者: 李苜菲
 * 日期: 2016/3/18 14:32.
 */
public abstract class BaseFragment extends Fragment {
    public String TAG = getClass().getSimpleName();

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
}
