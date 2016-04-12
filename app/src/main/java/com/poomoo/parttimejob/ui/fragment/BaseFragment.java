/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.fragment;

import android.app.Fragment;
import android.content.Intent;

/**
 * 作者: 李苜菲
 * 日期: 2016/3/18 14:32.
 */
public class BaseFragment extends Fragment {
    public String TAG = getClass().getSimpleName();

    /**
     * @param pClass
     */
    protected void openActivity(Class<?> pClass) {
        Intent intent = new Intent(getActivity(), pClass);
        getActivity().startActivity(intent);
    }
}
