/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyUtils;
import com.poomoo.model.response.RApplicantBO;
import com.poomoo.model.response.RBuyBO;
import com.poomoo.parttimejob.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/9 13:52.
 */
public class BuyListAdapter extends BaseListAdapter<RBuyBO> {

    public BuyListAdapter(Context context, int mode) {
        super(context, mode);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new ViewHolder(mInflater.inflate(R.layout.item_list_commodity, parent, false));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder h, int position) {
        LogUtils.d("ApplicantListAdapter", "onBindDefaultViewHolder:" + position);
        ViewHolder holder = (ViewHolder) h;
        RBuyBO item = items.get(position);
        holder.nameTxt.setText(item.goodsName);
        holder.priceTxt.setText(item.price + "");
        holder.numTxt.setText(item.exchangedNum + "");
        holder.commodityImg.setLayoutParams(new LinearLayout.LayoutParams(MyUtils.getScreenWidth(mContext) / 2, MyUtils.getScreenWidth(mContext) / 2));
        Glide.with(mContext).load(item.itemPicture).into(holder.commodityImg);

    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.img_commodity)
        ImageView commodityImg;
        @Bind(R.id.txt_commodityName)
        TextView nameTxt;
        @Bind(R.id.txt_commodityPrice)
        TextView priceTxt;
        @Bind(R.id.txt_buyNum)
        TextView numTxt;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
