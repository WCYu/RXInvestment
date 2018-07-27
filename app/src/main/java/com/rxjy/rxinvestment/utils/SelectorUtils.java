package com.rxjy.rxinvestment.utils;

import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.rxjy.rxinvestment.base.App;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 阿禹 on 2018/6/27.
 * 选择器
 */

public class SelectorUtils {

    //时间选择器 pattern格式："yyyy年MM月dd日" "yyyy/MM/dd"
    public static void timeSelector(final TextView view , final String pattern) {
        final String[] birth = {null};
        TimePickerView timePickerView = new TimePickerView.Builder(App.context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String dateToString = getDateToString(date, pattern);
                view.setText(dateToString);
            }
        }).setType(new boolean[]{true, true, true, false, false, false})
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentSize(16)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleText("请选择时间")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .setRange(1900, Calendar.YEAR)
                .isCyclic(true)//是否循环滚动
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                        .isDialog(true)//是否显示为对话框样式
                .build();
        timePickerView.show();
    }

    private static String getDateToString(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    public static void dataSelector(final TextView view, final List list) {
        OptionsPickerView pickerView = new OptionsPickerView.Builder(App.context, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                view.setText(list.get(options1).toString());
            }
        }).build();
        pickerView.setPicker(list);
        pickerView.show();
    }
}
