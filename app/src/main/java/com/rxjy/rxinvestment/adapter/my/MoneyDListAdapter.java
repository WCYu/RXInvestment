package com.rxjy.rxinvestment.adapter.my;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.adapter.utils.SingleBaseAdapter;
import com.rxjy.rxinvestment.adapter.utils.SingleViewHolder;
import com.rxjy.rxinvestment.entity.MoneyDTzFenhongBean;
import com.rxjy.rxinvestment.utils.StringUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hjh on 2018/3/16.
 */

public class MoneyDListAdapter extends SingleBaseAdapter<MoneyDTzFenhongBean.BodyBean, MoneyDListAdapter.ViewHolder> {


    public MoneyDListAdapter(Context context, List<MoneyDTzFenhongBean.BodyBean> datas) {
        super(context, datas);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_txtlist;
    }

    @Override
    public ViewHolder initViewHolder() {
        return new ViewHolder();
    }

    @Override
    public void onBindView(int position, MoneyDTzFenhongBean.BodyBean data, ViewHolder holder) {
        holder.tvOne.setText(data.getTime());
        holder.tvTwo.setText(data.getMoney());
        holder.tvThree.setText(data.getPrecent()+"%");
        holder.tvFour.setText(StringUtils.getPrettyNumber(data.getFenHong()+""));
        int money=data.getFenHong().intValue();
        if(money>=0){
            holder.tvFour.setTextColor(context.getResources().getColor(R.color.colorPrimarys));
        } else {
            holder.tvFour.setTextColor(context.getResources().getColor(R.color.textred));
        }
        holder.tvFive.setVisibility(View.GONE);
    }


    class ViewHolder implements SingleViewHolder {

        @Bind(R.id.tv_one)
        TextView tvOne;
        @Bind(R.id.tv_two)
        TextView tvTwo;
        @Bind(R.id.tv_three)
        TextView tvThree;
        @Bind(R.id.tv_four)
        TextView tvFour;
        @Bind(R.id.tv_five)
        TextView tvFive;

        @Override
        public void onInFlate(View v) {
            ButterKnife.bind(this, v);
        }
    }
}