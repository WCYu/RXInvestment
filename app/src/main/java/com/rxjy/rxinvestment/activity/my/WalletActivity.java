package com.rxjy.rxinvestment.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.base.PathUrl;
import com.rxjy.rxinvestment.custom.MoneyTzView;
import com.rxjy.rxinvestment.custom.RiseNumberTextView;
import com.rxjy.rxinvestment.entity.MoneyTzBean;
import com.rxjy.rxinvestment.utils.JSONUtils;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
import com.rxjy.rxinvestment.utils.StringUtils;
import com.rxjy.rxinvestment.utils.ToastUtil;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
* 钱包
* */

public class WalletActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.ly_background)
    LinearLayout lyBackground;
    @Bind(R.id.publish)
    ImageView publish;
    @Bind(R.id.tv_money)
    RiseNumberTextView tvMoney;
    @Bind(R.id.mv_one)
    MoneyTzView mvOne;
    @Bind(R.id.mv_two)
    MoneyTzView mvTwo;
    @Bind(R.id.mv_three)
    MoneyTzView mvThree;
    @Bind(R.id.mv_four)
    MoneyTzView mvFour;
    @Bind(R.id.mv_five)
    MoneyTzView mvFive;
    @Bind(R.id.mv_six)
    MoneyTzView mvSix;
    private int year;
    private int month;

    String tzxzresultmoney, tzxzgcmoney, tzxzjfmoney, tzxzfhmoney;
    Float moneyall;

    @Override
    public int getLayout() {
        return R.layout.activity_wallet;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void intData() {
        tvTitle.setText("钱包");
        mvOne.setTitleAndType("合计", "保底", "绩效", "", "最终");
        mvTwo.setTitleAndType("结果", "结果", "", "", "合计");
        mvThree.setTitleAndType("过程", "过程", "", "", "合计");
        mvFour.setTitleAndType("奖罚", "奖", "罚", "现金", "合计");
        mvFive.setTitleAndType("社保", "社保", "公积金", "", "合计");
        mvSix.setTitleAndType("分红", "笔数", "", "", "合计");
        getMoneryData();
    }

    @Override
    public void initAdapter() {

    }

    public void getMoneryData() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        Map map = new HashMap();
        map.put("year", year);
        map.put("month", month);
        map.put("cardNo", App.cardNo);
        OkhttpUtils.doGet(this, PathUrl.MONEYURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取行政经理钱包", data);
                MoneyTzBean info = JSONUtils.toObject(data, MoneyTzBean.class);
                MoneyTzBean.BodyBean body = info.getBody();
                int statusCode = info.getStatusCode();
                String statusMsg = info.getStatusMsg();
                if (statusCode == 0) {
//        tvMoney.setText(datas.getZuiZhongGongZi());
                    moneyall = Float.valueOf(body.getZuiZhongGongZi());
                    tvMoney.withNumber(moneyall, false).start();
                    mvOne.setLasttwoContent(body.getBaoDiGongZi(), body.getJiXiaoGongZi(), "", body.getZuiZhongGongZi(), body.getZuiZhongGongZiHou());
                    mvTwo.setContent("" + body.getJieGuoGongZi(), "", "", "" + body.getJieGuoGongZi());
                    tzxzresultmoney = body.getJieGuoGongZi();
                    mvThree.setContent(body.getGuoChengGongZi(), "", "", body.getGuoChengGongZi());
                    tzxzgcmoney = body.getGuoChengGongZi();
                    mvFour.setLasttwoContent(body.getrMoney(), body.getPmoney(), body.getXianJin(), StringUtils.getPrettyNumber("" + body.getJiangFaGongZi()), StringUtils.getPrettyNumber("" + body.getJiangFaGongZiHou()));
                    tzxzjfmoney = StringUtils.getPrettyNumber("" + body.getJiangFaGongZiHou());
                    mvFive.setContent(body.getSocialPrivate(), body.getHousePrivate(), "", body.getSheBao());
                    mvSix.setContent(body.getFenHongNum(), "", "", body.getFenHongSumMoney());
                    tzxzfhmoney = body.getFenHongSumMoney();
                    int jfhjmoney = body.getJiangFaGongZi().intValue();
                    int jfhjmoneyhou = body.getJiangFaGongZiHou().intValue();
                    if (jfhjmoney >= 0) {
                        mvFour.setTextColor(1, 4);
                    } else {
                        mvFour.setTextColor(2, 4);
                    }
                    if (jfhjmoneyhou >= 0) {
                        mvFour.setTextColor(1, 5);
                    } else {
                        mvFour.setTextColor(2, 5);
                    }
                } else {
                    ToastUtil.getInstance().toastCentent(statusMsg);
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取政经理钱包失败", message);
            }
        });

    }

    /**
     * 查看详情
     */
    private void DataDetails(int type) {//种类
        switch (type) {
            case 2:
                startActivity(new Intent(this, MoneyDetailsTzActivity.class).putExtra("type", type + "").putExtra("money", tzxzresultmoney));
                break;
            case 3:
                startActivity(new Intent(this, MoneyDetailsTzActivity.class).putExtra("type", type + "").putExtra("money", tzxzgcmoney));
                break;
            case 4:
                startActivity(new Intent(this, MoneyDetailsTzTwoActivity.class).putExtra("type", "jiangfa").putExtra("money", tzxzjfmoney));
                break;
            case 6:
                startActivity(new Intent(this, MoneyDetailsTzTwoActivity.class).putExtra("type", "fenhong").putExtra("money", tzxzfhmoney));
                break;
        }

    }

    @OnClick({R.id.img_back, R.id.mv_one, R.id.mv_two, R.id.mv_three, R.id.mv_four, R.id.mv_five, R.id.mv_six})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.mv_one:
                DataDetails(1);
                break;
            case R.id.mv_two:
                DataDetails(2);
                break;
            case R.id.mv_three:
                DataDetails(3);
                break;
            case R.id.mv_four:
                DataDetails(4);
                break;
            case R.id.mv_five:
                DataDetails(5);
                break;
            case R.id.mv_six:
                DataDetails(6);
                break;
        }
    }
}
