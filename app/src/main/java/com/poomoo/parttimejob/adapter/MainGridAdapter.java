/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poomoo.commlib.LogUtils;
import com.poomoo.model.response.RApplicantBO;
import com.poomoo.model.response.RTypeBO;
import com.poomoo.parttimejob.R;


/**
 * 类型
 * 作者: 李苜菲
 * 日期: 2015/11/30 14:49.
 */
public class MainGridAdapter extends MyBaseAdapter<RTypeBO> {
    private String TAG = "MainGridAdapter";
    private RTypeBO item;

    public MainGridAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        viewHolder = new ViewHolder();
        item = itemList.get(position);
        convertView = inflater.inflate(R.layout.item_grid_main, null);
        viewHolder.image = (ImageView) convertView.findViewById(R.id.img_main);
        viewHolder.txt = (TextView) convertView.findViewById(R.id.txt_main);
        Glide.with(context).load(item.icon).placeholder(R.drawable.ic_defalut_avatar).into(viewHolder.image);
        viewHolder.txt.setText(item.name);
        return convertView;
    }

    class ViewHolder {
        private ImageView image;
        private TextView txt;
    }

}
