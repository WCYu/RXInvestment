package com.rxjy.rxinvestment.adapter.my;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.adapter.utils.SingleBaseAdapter;
import com.rxjy.rxinvestment.adapter.utils.SingleViewHolder;
import com.rxjy.rxinvestment.entity.InfoMessageBodyBean;
import com.rxjy.rxinvestment.utils.StringUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hjh on 2018/3/8.
 */
public class MessageNewAdapter extends SingleBaseAdapter<InfoMessageBodyBean, MessageNewAdapter.ViewHolder> {


    public MessageNewAdapter(Context context, ArrayList<InfoMessageBodyBean> datas) {
        super(context, datas);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_messagenew;
    }

    @Override
    public ViewHolder initViewHolder() {
        return new ViewHolder();
    }

    @Override
    public void onBindView(int position, InfoMessageBodyBean data, ViewHolder holder) {
        holder.tvTitle.setText(data.getGroup_name());
        String time = data.getCreate_date();
        if (!StringUtils.isEmpty(time) && time.length() >= 10) {
            time = time.substring(0, 10);
            holder.tvTime.setText(time);
        }
        holder.tvContent.setText(data.getTxt());
        if (!StringUtils.isEmpty(data.getNotice_count()) && !data.getNotice_count().equals("0")) {
            holder.tvMsgnum.setText(data.getNotice_count());
            holder.tvMsgnum.setVisibility(View.VISIBLE);
        } else {
            holder.tvMsgnum.setVisibility(View.GONE);
        }
        Glide.with(context).load(data.getImage()).into(holder.ivImg);
    }

    class ViewHolder implements SingleViewHolder {

        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.iv_img)
        ImageView ivImg;
        @Bind(R.id.tv_msgnum)
        TextView tvMsgnum;

        @Override
        public void onInFlate(View v) {
            ButterKnife.bind(this, v);
        }
    }
}