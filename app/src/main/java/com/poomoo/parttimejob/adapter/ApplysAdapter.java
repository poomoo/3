package com.poomoo.parttimejob.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.poomoo.commlib.MyUtils;
import com.poomoo.model.response.RJobBO;
import com.poomoo.parttimejob.R;


import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by thanatos on 15/12/22.
 */
public class ApplysAdapter extends BaseListAdapter<RJobBO> {
    private boolean hasType = false;

    public ApplysAdapter(Context context, int mode, boolean hasType) {
        super(context, mode);
        this.hasType = hasType;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new NewsViewHolder(mInflater.inflate(R.layout.item_list_applys, parent, false));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder h, int position) {
        NewsViewHolder holder = (NewsViewHolder) h;
        RJobBO item = items.get(position);
        holder.jobNameTxt.setText(item.jobName);
        holder.payTxt.setText(MyUtils.formatPay(mContext, item.pay));
        holder.areaTxt.setText(item.areaName);
        holder.dateTxt.setText(item.applyDt);
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

    public static final class NewsViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_jobName)
        TextView jobNameTxt;
        @Bind(R.id.txt_pay)
        TextView payTxt;
        @Bind(R.id.txt_area)
        TextView areaTxt;
        @Bind(R.id.txt_date)
        TextView dateTxt;
        @Bind(R.id.img_type)
        ImageView typeImg;

        public NewsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
