package com.rxjy.rxinvestment.activity.my;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.adapter.my.MoneyDLisTwoAdapter;
import com.rxjy.rxinvestment.adapter.my.MoneyDListAdapter;
import com.rxjy.rxinvestment.adapter.my.MoneyDTitleAdapter;
import com.rxjy.rxinvestment.adapter.my.MoneyDZaRewardAdapter;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.base.PathUrl;
import com.rxjy.rxinvestment.custom.RiseNumberTextView;
import com.rxjy.rxinvestment.entity.MoneyDTzFenhongBean;
import com.rxjy.rxinvestment.entity.MoneyDTzRewardBean;
import com.rxjy.rxinvestment.entity.MoneyDZaRewardBean;
import com.rxjy.rxinvestment.utils.JSONUtils;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
import com.rxjy.rxinvestment.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoneyDetailsTzTwoActivity extends BaseActivity {

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
    @Bind(R.id.tv_money)
    RiseNumberTextView tvMoney;
    @Bind(R.id.gv_one)
    GridView gvOne;
    @Bind(R.id.lv_one)
    ListView lvOne;

    MoneyDTitleAdapter moneyDTitleAdapter;

    int year, month;
    String cardno;
    private MoneyDListAdapter moneyDListAdapter;
    private MoneyDLisTwoAdapter moneyDLisTwoAdapter;

    @Override
    public int getLayout() {
        return R.layout.activity_money_details_tz_two;
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

    /**
     * 显示种类标题
     */
    ArrayList<String> titlelist;

    private void ShowTitle() {
        gvOne.setNumColumns(titlelist.size());
        moneyDTitleAdapter = new MoneyDTitleAdapter(this, titlelist);
        gvOne.setAdapter(moneyDTitleAdapter);
    }

    ArrayList<MoneyDTzFenhongBean.BodyBean> fenhongdata;
    ArrayList<MoneyDTzRewardBean.BodyBean> rewarddata;

    @Override
    public void intData() {
        cardno = App.cardNo;
        lyBackground.setBackgroundColor(getResources().getColor(R.color.transparent));
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        String money = intent.getStringExtra("money");
        tvMoney.setText(money);
        titlelist = new ArrayList<>();
        fenhongdata = new ArrayList<>();
        rewarddata = new ArrayList<>();
        switch (type) {
            case "fenhong":
                tvTitle.setText("分红");
                titlelist.add("日期");
                titlelist.add("金额");
                titlelist.add("比例");
                titlelist.add("分红");
                ShowTitle();
                gettzfenhongdata(year + "", month + "", cardno);
                break;
            case "jiangfa":
                tvTitle.setText("奖罚");
                titlelist.add("日期");
                titlelist.add("来源");
                titlelist.add("内容");
                titlelist.add("金额");
                titlelist.add("状态");
                ShowTitle();
                gettzrewarddata(year + "", month + "", cardno);
                break;
        }
    }

    //行政经理分红
    private void gettzfenhongdata(String s, String s1, String cardno) {
        Map map = new HashMap();
        map.put("year", s);
        map.put("month", s1);
        map.put("cardNo", cardno);
        OkhttpUtils.doGet(this, PathUrl.TZXZJLFHURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取分红信息", data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String statusMsg = jsonObject.getString("StatusMsg");
                    int statusCode = jsonObject.getInt("StatusCode");
                    if (statusCode == 0) {
                        MoneyDTzFenhongBean info = JSONUtils.toObject(data, MoneyDTzFenhongBean.class);
                        ArrayList<MoneyDTzFenhongBean.BodyBean> body = info.getBody();
                        fenhongdata.clear();
                        fenhongdata = body;
                        moneyDListAdapter = new MoneyDListAdapter(MoneyDetailsTzTwoActivity.this, fenhongdata);
                        lvOne.setAdapter(moneyDListAdapter);
                    } else {
                        ToastUtil.getInstance().toastCentent(statusMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取分红失败", message);
            }
        });
    }

    //行政经理奖罚
    private void gettzrewarddata(String s, String s1, String cardno) {
        Map map = new HashMap();
        map.put("year", s);
        map.put("month", s1);
        map.put("cardNo", cardno);
        OkhttpUtils.doGet(this, PathUrl.TZXZJLJFURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取奖罚信息", data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String statusMsg = jsonObject.getString("StatusMsg");
                    int statusCode = jsonObject.getInt("StatusCode");
                    if (statusCode == 0) {
                        MoneyDTzRewardBean info = JSONUtils.toObject(data, MoneyDTzRewardBean.class);
                        ArrayList<MoneyDTzRewardBean.BodyBean> body = info.getBody();
                        rewarddata.clear();
                        rewarddata = body;
                        moneyDLisTwoAdapter = new MoneyDLisTwoAdapter(MoneyDetailsTzTwoActivity.this, rewarddata);
                        lvOne.setAdapter(moneyDLisTwoAdapter);
                        lvOne.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                startActivity(new Intent(MoneyDetailsTzTwoActivity.this, TextDetailsActivity.class).putExtra("txt", rewarddata.get(position).getContent()));
                            }
                        });
                    } else {
                        ToastUtil.getInstance().toastCentent(statusMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取奖罚失败", message);
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
