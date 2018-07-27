package com.rxjy.rxinvestment.adapter.home;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.adapter.utils.SingleBaseAdapter;
import com.rxjy.rxinvestment.adapter.utils.SingleViewHolder;
import com.rxjy.rxinvestment.entity.EnvirBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 阿禹 on 2018/7/3.
 */

public class EnvironmentAdapter extends SingleBaseAdapter<EnvirBean.BodyBean, EnvironmentAdapter.ViewHolder> {

    public EnvironmentAdapter(Context context, List<EnvirBean.BodyBean> datas) {
        super(context, datas);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_envir;
    }

    @Override
    public ViewHolder initViewHolder() {
        return new ViewHolder();
    }

    @Override
    public void onBindView(int position, EnvirBean.BodyBean data, ViewHolder holder) {
        holder.tvTitle.setText(data.getAreaName());
    }

    class ViewHolder implements SingleViewHolder {
        @Bind(R.id.tv_title)
        TextView tvTitle;

        @Override
        public void onInFlate(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
