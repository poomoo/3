package com.poomoo.parttimejob.adapter;

import android.content.Context;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyDateFormat;
import com.poomoo.model.response.RMessageBO;
import com.poomoo.parttimejob.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by thanatos on 15/12/22.
 */
public class MessageListAdapter extends BaseListAdapter<RMessageBO> {
    private boolean isShowDate = false;

    public MessageListAdapter(Context context, int mode) {
        super(context, mode);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new MessageViewHolder(mInflater.inflate(R.layout.item_list_message, parent, false));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder h, int position) {
        LogUtils.d("MessageListAdapter", "position:" + position);
        MessageViewHolder holder = (MessageViewHolder) h;
        RMessageBO item = items.get(position);
        if (position > 1)
            if (!MyDateFormat.minus(item.replyDt, items.get(position - 1).replyDt))//五分钟内不需要显示日期时间
                isShowDate = false;
            else
                isShowDate = true;
        if (item.isAdminReply) {
            holder.linearLayoutLeft.setVisibility(View.VISIBLE);
            holder.linearLayoutRight.setVisibility(View.GONE);
            if (TextUtils.isEmpty(item.replyDt))
                holder.dateLeftTxt.setVisibility(View.GONE);
            else {
                holder.dateLeftTxt.setVisibility(View.VISIBLE);
                if (isShowDate)
                    holder.dateLeftTxt.setText(MyDateFormat.formatToMessage(item.replyDt));
                else
                    holder.dateLeftTxt.setVisibility(View.GONE);
            }
            holder.contentLeftTxt.setText(item.content);
            Glide.with(mContext).load(item.headPic).placeholder(R.drawable.ic_defalut_avatar).into(holder.avatarLeftImg);
        } else {
            holder.linearLayoutLeft.setVisibility(View.GONE);
            holder.linearLayoutRight.setVisibility(View.VISIBLE);
            if (isShowDate){
                holder.dateRightTxt.setVisibility(View.VISIBLE);
                holder.dateRightTxt.setText(MyDateFormat.formatToMessage(item.replyDt));
            }
            else
                holder.dateRightTxt.setVisibility(View.GONE);
            holder.contentRightTxt.setText(item.content);
            Glide.with(mContext).load(item.headPic).placeholder(R.drawable.ic_defalut_avatar).into(holder.avatarRightImg);
        }
    }

    public static final class MessageViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.llayout_messageLeft)
        LinearLayout linearLayoutLeft;
        @Bind(R.id.txt_messageDateTimeLeft)
        TextView dateLeftTxt;
        @Bind(R.id.txt_messageContentLeft)
        TextView contentLeftTxt;
        @Bind(R.id.img_messageAvatarLeft)
        ImageView avatarLeftImg;

        @Bind(R.id.llayout_messageRight)
        LinearLayout linearLayoutRight;
        @Bind(R.id.txt_messageDateTimeRight)
        TextView dateRightTxt;
        @Bind(R.id.txt_messageContentRight)
        TextView contentRightTxt;
        @Bind(R.id.img_messageAvatarRight)
        ImageView avatarRightImg;

        public MessageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
