package com.rxjy.rxinvestment.adapter.home;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.adapter.utils.SingleBaseAdapter;
import com.rxjy.rxinvestment.adapter.utils.SingleViewHolder;
import com.rxjy.rxinvestment.entity.Person;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by asus on 2018/5/24.
 */

public class AnswerAdapter extends SingleBaseAdapter<Person, AnswerAdapter.ViewHoider> {


    public AnswerAdapter(Context context, List<Person> datas) {
        super(context, datas);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.ltem_checkbox;
    }

    @Override
    public ViewHoider initViewHolder() {
        return new ViewHoider();
    }

    @Override
    public void onBindView(int position, Person data, ViewHoider holder) {
//判断CheckBox的状态
        if (data.isChecked()) {
            holder.cbCheckbox.setChecked(true);//选中
        } else {
            holder.cbCheckbox.setChecked(false);//未选中
        }
        holder.tvTitle.setText(data.getTitle());

    }

    public class ViewHoider implements SingleViewHolder {
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.cb_checkbox)
        CheckBox cbCheckbox;

        @Override
        public void onInFlate(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
