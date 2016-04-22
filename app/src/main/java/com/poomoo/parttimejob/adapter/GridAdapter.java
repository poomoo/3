/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.poomoo.parttimejob.R;


/**
 * 抢
 * 作者: 李苜菲
 * 日期: 2015/11/30 14:49.
 */
public class GridAdapter extends MyBaseAdapter<Integer> {
    private String TAG = "GrabAdapter";

    public GridAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        viewHolder = new ViewHolder();
        convertView = inflater.inflate(R.layout.item_grid_test, null);
        viewHolder.image = (ImageView) convertView.findViewById(R.id.img_grid);
        viewHolder.image.setImageResource(itemList.get(position));
        return convertView;
    }


    class ViewHolder {
        private ImageView image;
    }

}
