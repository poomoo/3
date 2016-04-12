/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.poomoo.parttimejob.R;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/9 13:52.
 */
public class ApplicantListAdapter extends MyBaseAdapter<String> {
    public ApplicantListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_applicant,
                    null);
        }
        return convertView;
    }
}
