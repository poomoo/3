/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.poomoo.parttimejob.R;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/23 11:12.
 */
public class JobTypeAdapter extends MyBaseAdapter<String> {
    private int len;
    private SparseArray<Boolean> sparseArray = new SparseArray<>();

    public JobTypeAdapter(Context context, int len) {
        super(context);
        this.len = len;
        initSparseArray();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        viewHolder = new ViewHolder();
        convertView = inflater.inflate(R.layout.item_list_filter, null);
        viewHolder.typeTxt = (TextView) convertView.findViewById(R.id.txt_filter);
        viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.chk_filter);
        String item = itemList.get(position);

        viewHolder.typeTxt.setText(item.split("#")[0]);
        if (sparseArray.get(position))
            viewHolder.checkBox.setChecked(true);
        else
            viewHolder.checkBox.setChecked(false);
        return convertView;
    }

    public void initSparseArray() {
        for (int i = 0; i < len; i++) {
            if (i == 0)
                sparseArray.put(i, true);
            else
                sparseArray.put(i, false);
        }
    }

    public void clearSparseArray() {
        for (int i = 0; i < len; i++)
            sparseArray.put(i, false);
    }


    class ViewHolder {
        TextView typeTxt;
        CheckBox checkBox;
    }

    public boolean hasChecked() {
        for (int i = 1; i < len; i++) {
            if (sparseArray.get(i))
                return true;
        }
        return false;
    }

    public boolean isAllChecked() {
        for (int i = 1; i < len; i++) {
            if (!sparseArray.get(i))
                return false;
        }
        return true;
    }

    public SparseArray<Boolean> getSparseArray() {
        return sparseArray;
    }

}