/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyDateFormat;
import com.poomoo.commlib.MyUtils;
import com.poomoo.model.response.RApplicantBO;
import com.poomoo.parttimejob.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/9 13:52.
 */
public class ApplicantListAdapter extends BaseListAdapter<RApplicantBO> {

    public ApplicantListAdapter(Context context, int mode) {
        super(context, mode);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new ViewHolder(mInflater.inflate(R.layout.item_list_applicant, parent, false));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder h, int position) {
        LogUtils.d("ApplicantListAdapter", "onBindDefaultViewHolder:" + position);
        ViewHolder holder = (ViewHolder) h;
        RApplicantBO item = items.get(position);
        holder.nameTxt.setText(item.nickName);
        if (item.sex == 1) {
            holder.sexTxt.setText("男　" + item.age);
            holder.sexTxt.setBackgroundResource(R.drawable.style_label_male);
        } else {
            holder.sexTxt.setText("女　" + item.age);
            holder.sexTxt.setBackgroundResource(R.drawable.style_label_female);
        }
        holder.intentTxt.setText(item.intention);
        Glide.with(mContext).load(item.headPic).placeholder(R.drawable.ic_login_logo).into(holder.avatarImg);
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.img_applicantAvatar)
        ImageView avatarImg;
        @Bind(R.id.txt_applicantName)
        TextView nameTxt;
        @Bind(R.id.txt_applicantSex)
        TextView sexTxt;
        @Bind(R.id.txt_applicantIntention)
        TextView intentTxt;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
