/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.poomoo.parttimejob.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/23 11:12.
 */
public class JobTypeAdapter extends MyBaseAdapter<String> {
    private int len;
    //    private isCheckedMap<Boolean> isCheckedMap = new isCheckedMap<>();
    private Map<Integer, Boolean> isCheckedMap = new HashMap<>();

    public JobTypeAdapter(Context context, int len) {
        super(context);
        this.len = len;
        initIsCheckedMap();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        viewHolder = new ViewHolder();
        convertView = inflater.inflate(R.layout.item_list_filter, null);
        viewHolder.typeTxt = (TextView) convertView.findViewById(R.id.txt_filter);
        viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.chk_filter);
        convertView.setTag(viewHolder);
        String item = itemList.get(position);

        viewHolder.typeTxt.setText(item.split("#")[0]);
        viewHolder.checkBox.setChecked(isCheckedMap.get(position));
        return convertView;
    }

    public void initIsCheckedMap() {
        for (int i = 0; i < len; i++) {
            if (i == 0)
                isCheckedMap.put(i, true);
            else
                isCheckedMap.put(i, false);
        }
    }

    public void clearIsCheckedMap() {
        for (int i = 0; i < len; i++)
            isCheckedMap.put(i, false);
    }


    public static class ViewHolder {
        TextView typeTxt;
        public CheckBox checkBox;
    }

    public boolean hasChecked() {
        for (int i = 1; i < len; i++) {
            if (isCheckedMap.get(i))
                return true;
        }
        return false;
    }

    public boolean isAllChecked() {
        for (int i = 1; i < len; i++) {
            if (!isCheckedMap.get(i))
                return false;
        }
        return true;
    }

    public Map<Integer, Boolean> getIsCheckedMap() {
        return isCheckedMap;
    }
}