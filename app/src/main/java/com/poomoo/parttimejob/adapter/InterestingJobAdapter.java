/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poomoo.parttimejob.R;

/**
 * 感兴趣的工作列表适配
 * 作者: 李苜菲
 * 日期: 2016/3/23 10:56.
 */
public class InterestingJobAdapter extends MyBaseAdapter<String> {
    public InterestingJobAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
//        LogUtils.i("getCount:" + itemList.size());
        return itemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        LogUtils.i("getView:" + position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_main,
                    null);
            viewHolder = new ViewHolder();
//            viewHolder.payTxt = (TextView) convertView.findViewById(R.id.txt_pay);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        viewHolder.payTxt.setText(itemList.get(position));
        return convertView;
    }

    class ViewHolder {
        private TextView payTxt;
    }
}
