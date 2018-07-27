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
import com.rxjy.rxinvestment.custom.MoneyDTzView;
import com.rxjy.rxinvestment.custom.MoneyTzView;
import com.rxjy.rxinvestment.entity.MoneyDTzProcessBean;
import com.rxjy.rxinvestment.entity.MoneyDTzResultBean;
import com.rxjy.rxinvestment.entity.PersonBean;
import com.rxjy.rxinvestment.utils.JSONUtils;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
import com.rxjy.rxinvestment.utils.StringUtils;
import com.rxjy.rxinvestment.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoneyDetailsTzActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.publish)
    ImageView publish;
    @Bind(R.id.ly_background)
    LinearLayout lyBackground;
    @Bind(R.id.mvt_one)
    MoneyDTzView mvtOne;
    @Bind(R.id.mvt_two)
    MoneyDTzView mvtTwo;
    @Bind(R.id.mvt_three)
    MoneyDTzView mvtThree;
    @Bind(R.id.mvt_four)
    MoneyDTzView mvtFour;
    @Bind(R.id.mvt_five)
    MoneyDTzView mvtFive;
    @Bind(R.id.mvt_six)
    MoneyDTzView mvtSix;
    @Bind(R.id.mvt_seven)
    MoneyDTzView mvtSeven;

    int year, month;
    String cardno;
    @Bind(R.id.tv_money)
    TextView tvMoney;
    @Bind(R.id.ll_money)
    LinearLayout llMoney;

    @Override
    public int getLayout() {
        return R.layout.activity_money_details_tz;
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

        lyBackground.setBackgroundColor(getResources().getColor(R.color.transparent));
        tvTitle.setText("钱包");
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        String money = intent.getStringExtra("money");
        tvMoney.setText(money);
        cardno = App.cardNo;
        switch (type) {
            case "2"://投资行政经理结果
                tvTitle.setText("结果");
                gettzresultdata(year + "", month + "", cardno);
                break;
            case "3"://投资行政经理过程
                tvTitle.setText("过程");
                gettzprocessdata(year + "", month + "", cardno);
                break;
        }
    }

    //投资行政经理结果
    private void gettzresultdata(String s, String s1, String cardno) {
        Map map = new HashMap();
        map.put("year", s);
        map.put("month", s1);
        map.put("cardNo", cardno);
        OkhttpUtils.doGet(this, PathUrl.TZXZJLJGURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_行政经理结果", data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String statusMsg = jsonObject.getString("StatusMsg");
                    int statusCode = jsonObject.getInt("StatusCode");
                    if (statusCode == 0) {
                        MoneyDTzResultBean info = JSONUtils.toObject(data, MoneyDTzResultBean.class);
                        MoneyDTzResultBean.BodyBean body = info.getBody();

                        mvtOne.setContent("施工签单", "标准 " + body.getSgqdBz1(), "金额 " + StringUtils.getPrettyNumber(body.getSgqdMoney() + ""), null, null, null, null, body.getSgqdGongzi() + "", "合计");
                        int shu = body.getSwlrNum() - body.getSwlrBz();//数
//        BigDecimal bshu=new BigDecimal(Integer.toString(shu));
                        int danjia = body.getSwlrDanjia() * shu;
                        mvtTwo.setContent("商务老人", "标：" + body.getSwlrNum() + "/" + body.getSwlrBz(), "数", shu + "", "单价 " + body.getSwlrDanjia() + "*" + shu + "=" + danjia, "系数 " + danjia + "*0=0", null, body.getSwlrGongzi() + "", "合计");
                        BigDecimal baifenone = new BigDecimal("0.01");
                        String jiajumoney = StringUtils.getPrettyNumber(body.getJiajuGongzi().multiply(baifenone).multiply(body.getJiaju_precent()) + "");
                        String ruodianmoney = StringUtils.getPrettyNumber(body.getRuodianGongzi().multiply(baifenone).multiply(body.getRuodian_precent()) + "");
                        String shejimoney = StringUtils.getPrettyNumber(body.getShejiGongzi().multiply(baifenone).multiply(body.getSheji_precent()) + "");
                        String jiaju = StringUtils.getPrettyNumber(body.getJiajuGongzi() + "");
                        String ruodian = StringUtils.getPrettyNumber(body.getRuodianGongzi() + "");
                        String sheji = StringUtils.getPrettyNumber(body.getShejiGongzi() + "");
                        mvtThree.setContent("家具绩效", "绩效签单 ", jiaju, null, "公式", jiaju + "*" + StringUtils.getPrettyNumber(body.getJiaju_precent() + "") + "%=" + jiajumoney, null, jiaju, "合计");
                        mvtFour.setContent("弱电绩效", "绩效签单 ", "" + ruodian, null, "公式", ruodian + "*" + StringUtils.getPrettyNumber(body.getRuodian_precent() + "") + "%=" + ruodianmoney, null, ruodian, "合计");
                        mvtFive.setContent("设计绩效", "绩效签单 ", "" + sheji, null, "公式", sheji + "*" + StringUtils.getPrettyNumber(body.getSheji_precent() + "") + "%=" + shejimoney, null, sheji, "合计");
                        mvtOne.setTvColor(MoneyDetailsTzActivity.this, 7, 0, StringUtils.getPrettyNumber(body.getSgqdGongzi() + ""), body.getSgqdGongzi());
                        mvtTwo.setTvColor(MoneyDetailsTzActivity.this, 7, 0, StringUtils.getPrettyNumber(body.getSwlrGongzi() + ""), body.getSwlrGongzi());
                        mvtThree.setTvColor(MoneyDetailsTzActivity.this, 7, 0, jiaju, body.getJiajuGongzi());
                        mvtFour.setTvColor(MoneyDetailsTzActivity.this, 7, 0, ruodian, body.getRuodianGongzi());
                        mvtFive.setTvColor(MoneyDetailsTzActivity.this, 7, 0, sheji, body.getShejiGongzi());
                        mvtSix.setVisibility(View.GONE);
                        mvtSeven.setVisibility(View.GONE);

                    } else {
                        ToastUtil.getInstance().toastCentent(statusMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_行政经理结果失败", message);
            }
        });
    }

    private void gettzprocessdata(String s, String s1, String cardno) {
        Map map = new HashMap();
        map.put("year", s);
        map.put("month", s1);
        map.put("cardNo", cardno);
        OkhttpUtils.doGet(this, PathUrl.TZXZJLGCURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取用户信息", data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String statusMsg = jsonObject.getString("StatusMsg");
                    int statusCode = jsonObject.getInt("StatusCode");
                    if (statusCode == 0) {
                        MoneyDTzProcessBean info = JSONUtils.toObject(data, MoneyDTzProcessBean.class);
                        MoneyDTzProcessBean.BodyBean body = info.getBody();
                        int danjia = body.getShangwu_danjia() * body.getShangwu_num();
                        int zadanjia = body.getZhuan_danjia() * body.getZhuan_num();
                        int mddanjia = body.getMaidan_danjia() * body.getMaidan_num();
                        int ztdanjia = body.getZaitanHF_danjia() * body.getZaitanHF_num();
                        int wqdanjia = body.getWeiqian_danjia() * body.getWeiqian_num();
                        int wuxiao = (-body.getShangwuInvalid_koukuan()) * body.getShangwuInvalid_num();
                        int zawuxiao = (-body.getZhuanInvalid_koukuan()) * body.getZhuanInvalid_num();
                        mvtOne.setContent("商务人", "总数 " + body.getShangwu_num(), "无效 " + body.getShangwuInvalid_num(), null, "单价 " + body.getShangwu_danjia() + "*" + body.getShangwu_num() + "=" + danjia, "无效 " + (-body.getShangwuInvalid_koukuan()) + "*" + body.getShangwuInvalid_num() + "=" + wuxiao, "系数 " + danjia + "*0=0", StringUtils.getPrettyNumber(body.getShangwu_gongzi() + ""), "合计");
                        mvtTwo.setContent("主案人", "总数 " + body.getZhuan_num(), "无效 " + body.getZhuanInvalid_num(), null, "单价 " + body.getZhuan_danjia() + "*" + body.getZhuan_num() + "=" + zadanjia, "无效 " + (-body.getZhuanInvalid_koukuan()) + "*" + body.getZhuanInvalid_num() + "=" + zawuxiao, "系数 " + zadanjia + "*0=0", StringUtils.getPrettyNumber(body.getZhuan_gongzi() + ""), "合计");
                        mvtThree.setContent("买单", "次数 " + body.getMaidan_num(), null, null, "单价 " + body.getMaidan_danjia() + "*" + body.getMaidan_num() + "=" + mddanjia, "系数 " + mddanjia + "*0=0", null, StringUtils.getPrettyNumber(body.getMaidan_gongzi() + ""), "合计");
                        mvtFour.setContent("在谈回访", "次数 " + body.getZaitanHF_num(), null, null, "单价 " + body.getZaitanHF_danjia() + "*" + body.getZaitanHF_num() + "=" + ztdanjia, "系数 " + ztdanjia + "*0=0", null, StringUtils.getPrettyNumber(body.getZaitanHF_gongzi() + ""), "合计");
                        mvtFive.setContent("未签", "次数 " + body.getWeiqian_num(), null, null, "单价 " + body.getWeiqian_danjia() + "*" + body.getWeiqian_num() + "=" + wqdanjia, "系数 " + wqdanjia + "*0=0", null, StringUtils.getPrettyNumber(body.getWeiqian_gongzi() + ""), "合计");
                        mvtOne.setTvColor(MoneyDetailsTzActivity.this, 7, 0, StringUtils.getPrettyNumber(body.getShangwu_gongzi() + ""), body.getShangwu_gongzi());
                        mvtTwo.setTvColor(MoneyDetailsTzActivity.this, 7, 0, StringUtils.getPrettyNumber(body.getZhuan_gongzi() + ""), body.getZhuan_gongzi());
                        mvtThree.setTvColor(MoneyDetailsTzActivity.this, 7, 0, StringUtils.getPrettyNumber(body.getMaidan_gongzi() + ""), body.getMaidan_gongzi());
                        mvtFour.setTvColor(MoneyDetailsTzActivity.this, 7, 0, StringUtils.getPrettyNumber(body.getZaitanHF_gongzi() + ""), body.getZaitanHF_gongzi());
                        mvtFive.setTvColor(MoneyDetailsTzActivity.this, 7, 0, StringUtils.getPrettyNumber(body.getWeiqian_gongzi() + ""), body.getWeiqian_gongzi());
                        mvtSix.setVisibility(View.GONE);
                        mvtSeven.setVisibility(View.GONE);
                    } else {
                        ToastUtil.getInstance().toastCentent(statusMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取失败", message);
            }
        });
    }

    @Override
    public void initAdapter() {

    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }
}
