package com.poomoo.parttimejob.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poomoo.model.response.RServiceBO;
import com.poomoo.parttimejob.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by thanatos on 15/12/22.
 */
public class ServiceListAdapter extends BaseListAdapter<RServiceBO> {

    public ServiceListAdapter(Context context, int mode) {
        super(context, mode);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new ServiceViewHolder(mInflater.inflate(R.layout.item_list_service, parent, false));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder h, int position) {
        ServiceViewHolder holder = (ServiceViewHolder) h;
        RServiceBO item = items.get(position);
        holder.nameTxt.setText(item.sysNickName);
        holder.dateTxt.setText(item.pushDt);
        holder.contentTxt.setText(item.content);
        Glide.with(mContext).load(item.headPic).into(holder.avatarImg);
    }

    public static final class ServiceViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_serviceName)
        TextView nameTxt;
        @Bind(R.id.txt_serviceDate)
        TextView dateTxt;
        @Bind(R.id.txt_serviceContent)
        TextView contentTxt;
        @Bind(R.id.img_servicePic)
        ImageView avatarImg;

        public ServiceViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
