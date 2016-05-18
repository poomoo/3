package com.poomoo.parttimejob.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poomoo.commlib.MyDateFormat;
import com.poomoo.commlib.MyUtils;
import com.poomoo.model.base.BaseJobBO;
import com.poomoo.parttimejob.R;


import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by thanatos on 15/12/22.
 */
public class JobsAdapter extends BaseListAdapter<BaseJobBO> {
    private boolean hasType = false;

    public JobsAdapter(Context context, int mode, boolean hasType) {
        super(context, mode);
        this.hasType = hasType;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new JobsViewHolder(mInflater.inflate(R.layout.item_list_job, parent, false));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder h, int position) {
        JobsViewHolder holder = (JobsViewHolder) h;
        BaseJobBO item = items.get(position);
        Glide.with(mContext).load(item.icon).into(holder.picImg);
        holder.jobNameTxt.setText(item.jobName);
        holder.payNumTxt.setText(MyUtils.formatPay(item.pay)[0]);
        holder.payUnitTxt.setText(MyUtils.formatPay(item.pay)[1]);
        holder.areaTxt.setText(item.areaName != null ? item.areaName : item.cityName);
        if (!TextUtils.isEmpty(item.applyDt))
            holder.dateTxt.setText(item.applyDt);
        else if (!TextUtils.isEmpty(item.publishDt))
            holder.dateTxt.setText(MyDateFormat.format(item.publishDt));
        else if (!TextUtils.isEmpty(item.collectDt))
            holder.dateTxt.setText(MyDateFormat.format(item.collectDt));
//        holder.dateTxt.setText(TextUtils.isEmpty(item.applyDt) ? MyDateFormat.format(item.publishDt) : item.applyDt);
        //是否显示工作种类
        if (hasType) {
            holder.typeImg.setVisibility(View.VISIBLE);
            switch (item.status) {
                case 1:
                    holder.typeImg.setImageResource(R.drawable.ic_signed);
                    break;
                case 2:
                    holder.typeImg.setImageResource(R.drawable.ic_hired);
                    break;
                case 3:
                    holder.typeImg.setImageResource(R.drawable.ic_to_post);
                    break;
                case 4:
                    holder.typeImg.setImageResource(R.drawable.ic_settlement);
                    break;
            }
        } else
            holder.typeImg.setVisibility(View.GONE);
    }

    public static final class JobsViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_jobInfoName)
        TextView jobNameTxt;
        @Bind(R.id.txt_JobPayNum)
        TextView payNumTxt;
        @Bind(R.id.txt_JobPayUnit)
        TextView payUnitTxt;
        @Bind(R.id.txt_JobArea)
        TextView areaTxt;
        @Bind(R.id.txt_jobDate)
        TextView dateTxt;
        @Bind(R.id.img_jobType)
        ImageView typeImg;
        @Bind(R.id.img_jobPic)
        ImageView picImg;

        public JobsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
