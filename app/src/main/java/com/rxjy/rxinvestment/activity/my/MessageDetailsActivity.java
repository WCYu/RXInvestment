package com.rxjy.rxinvestment.activity.my;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.base.PathUrl;
import com.rxjy.rxinvestment.entity.MessageDetailsBean;
import com.rxjy.rxinvestment.utils.JSONUtils;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
import com.rxjy.rxinvestment.utils.StringUtils;
import com.rxjy.rxinvestment.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageDetailsActivity extends BaseActivity {

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
    @Bind(R.id.tv_titles)
    TextView tvTitles;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.tv_money)
    TextView tvMoney;
    @Bind(R.id.tv_moneynum)
    TextView tvMoneynum;
    @Bind(R.id.tv_person)
    TextView tvPerson;
    @Bind(R.id.tv_time)
    TextView tvTime;

    @Override
    public int getLayout() {
        return R.layout.activity_message_details;
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
        tvTitle.setText("消息详情");
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        getInfoMessageDList(id);
    }

    private void getInfoMessageDList(String id) {
        String url = null;
        Map map = new HashMap();
        switch (App.is_group) {
            case "0":
            case "1":
                url = PathUrl.TONGZHIINFOURL;
                map.put("Id", id);
                break;
            case "2":
                url = PathUrl.WBTONGZHIINFOURL;
                map.put("Id", id);
                break;
            default:
                url = PathUrl.TONGZHIINFOURL;
                map.put("Id", id);
                break;
        }
        OkhttpUtils.doGet(this, url, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取通知详情信息", data);
                MessageDetailsBean info = JSONUtils.toObject(data, MessageDetailsBean.class);
                MessageDetailsBean.BodyBean body = info.getBody();
                int statusCode = info.getStatusCode();
                String statusMsg = info.getStatusMsg();
                if (statusCode == 0) {
                    tvTitles.setText(body.getTitle());
                    tvContent.setText(body.getTxt());
                    tvTime.setText(body.getCreate_date());
                    if (!StringUtils.isEmpty(body.getInitiator_name())) {
                        tvPerson.setText(body.getInitiator_name());
                        tvPerson.setVisibility(View.VISIBLE);
                    } else {
                        tvPerson.setVisibility(View.GONE);
                    }
                    if (!StringUtils.isEmpty(body.getReward_money())) {
                        int money = Integer.parseInt(body.getReward_money());
                        tvMoneynum.setText(body.getReward_money());
                        tvMoneynum.setVisibility(View.VISIBLE);
                        tvMoney.setVisibility(View.VISIBLE);
                        if (money == 0) {
                            tvMoneynum.setVisibility(View.GONE);
                            tvMoney.setVisibility(View.GONE);
                        } else if (money > 0) {
                            tvMoneynum.setTextColor(getResources().getColor(R.color.colorPrimarys));
                        } else {
                            tvMoneynum.setTextColor(getResources().getColor(R.color.textred));
                        }
                    } else {
                        tvMoneynum.setVisibility(View.GONE);
                        tvMoney.setVisibility(View.GONE);
                    }
                } else {
                    ToastUtil.getInstance().toastCentent(statusMsg);
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取通知详情信息失败", message);
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
