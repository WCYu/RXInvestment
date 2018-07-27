package com.rxjy.rxinvestment.fragment.more;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.activity.more.AdministrationRedActivity;
import com.rxjy.rxinvestment.activity.more.RenShiActivity;
import com.rxjy.rxinvestment.activity.utils.WebViewActivity;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.fragment.utils.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by asus on 2018/5/21.
 * 更多
 */

public class JoininMoreFragment extends BaseFragment {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.iv_morebanner)
    ImageView ivMorebanner;
    @Bind(R.id.ly_hongbao)
    LinearLayout lyHongbao;
    @Bind(R.id.ly_huitouke)
    LinearLayout lyHuitouke;
    @Bind(R.id.ly_jiameng)
    LinearLayout lyJiameng;
    @Bind(R.id.ly_renshi)
    LinearLayout lyRenshi;
    @Bind(R.id.ly_caiwu)
    LinearLayout lyCaiwu;
    @Bind(R.id.ly_zijin)
    LinearLayout lyZijin;
    @Bind(R.id.ly_shangwu)
    LinearLayout lyShangwu;
    @Bind(R.id.ly_zhuan)
    LinearLayout lyZhuan;
    @Bind(R.id.ly_gongcheng)
    LinearLayout lyGongcheng;
    @Bind(R.id.ly_gongsi)
    LinearLayout lyGongsi;
    @Bind(R.id.ly_cailiaoshang)
    LinearLayout lyCailiaoshang;
    @Bind(R.id.ly_jingli)
    LinearLayout lyJingli;
    @Bind(R.id.ly_touzi)
    LinearLayout lyTouzi;
    @Bind(R.id.img_fuwu1)
    ImageView imgFuwu1;
    @Bind(R.id.tv_fuwu1)
    TextView tvFuwu1;
    @Bind(R.id.img_fuwu2)
    ImageView imgFuwu2;
    @Bind(R.id.tv_fuwu2)
    TextView tvFuwu2;
    @Bind(R.id.img_fuwu3)
    ImageView imgFuwu3;
    @Bind(R.id.tv_fuwu3)
    TextView tvFuwu3;

    @Override
    public int getInitId() {
        return R.layout.joinin_more;
    }

    @Override
    public void initView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void initData() {
        imgBack.setVisibility(View.GONE);
        tvTitle.setText("");

        switch (App.postName) {
            case "投资招商":
                lyTouzi.setVisibility(View.VISIBLE);
                tvFuwu1.setText("加盟介绍");
                Glide.with(this).load(R.mipmap.jiamengjieshao).into(imgFuwu1);
                tvFuwu2.setText("企业官网");
                Glide.with(this).load(R.mipmap.qiyeguanwang).into(imgFuwu2);
                tvFuwu3.setText("关于我们");
                Glide.with(this).load(R.mipmap.guanyuwomen).into(imgFuwu3);
                break;
            case "行政经理":
                lyTouzi.setVisibility(View.GONE);
                break;
            default:
                lyTouzi.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void initAdapter() {

    }

    @Override
    public void initLinstener() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.ly_hongbao, R.id.ly_huitouke, R.id.ly_jiameng, R.id.ly_renshi, R.id.ly_caiwu, R.id.ly_zijin, R.id.ly_shangwu, R.id.ly_zhuan, R.id.ly_gongcheng, R.id.ly_gongsi, R.id.ly_cailiaoshang, R.id.ly_jingli})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ly_hongbao://红包
                if (App.postName.equals("投资招商")) {
                    tvFuwu1.setText("加盟介绍");
                    intent = new Intent(getContext(), WebViewActivity.class);
                    intent.putExtra("title", "加盟介绍");
                    intent.putExtra("url", "http://jm.rxjy.com");
                    intent.putExtra("type", "投资招商");
                } else {
                    intent = new Intent(getActivity(), AdministrationRedActivity.class);
                }
                break;
            case R.id.ly_huitouke://回头客
                if (App.postName.equals("投资招商")) {
                    tvFuwu2.setText("企业官网");
                    intent = new Intent(getContext(), WebViewActivity.class);
                    intent.putExtra("title", "企业官网");
                    intent.putExtra("url", "http://j.rxjy.com/Mobile/More/index.html");
                    intent.putExtra("type", "投资招商");
                }
                break;
            case R.id.ly_jiameng://加盟
                if (App.postName.equals("投资招商")) {
                    intent = new Intent(getContext(), WebViewActivity.class);
                    intent.putExtra("title", "关于我们");
                    intent.putExtra("url", "http://j.rxjy.com/Mobile/video.html");
                    intent.putExtra("type", "投资招商");
                } else {
                    intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("url", "https://api.niujingji.cn:8183/static/develop/recommendApp.html?CardNo=" + App.cardNo);
                    intent.putExtra("title", "加盟推荐");
                    intent.putExtra("type", "顾问");
                }
                break;
            case R.id.ly_renshi://人事
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", "人事");
                intent.putExtra("url", "http://j.rxjy.com/Mobile/More/Personnel?app_id=" + App.app_id);
                break;
            case R.id.ly_caiwu://财务
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", "财务");
                intent.putExtra("url", "http://j.rxjy.com/Mobile/More/Finance?app_id=" + App.app_id);
                break;
            case R.id.ly_zijin://资金
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", "资金");
                intent.putExtra("url", "http://j.rxjy.com/Mobile/More/Capital?app_id=" + App.app_id);
                break;
            case R.id.ly_shangwu://商务
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", "商务");
                intent.putExtra("url", "http://j.rxjy.com/Mobile/More/Business?app_id=" + App.app_id);
                break;
            case R.id.ly_zhuan://主案
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", "主案");
                intent.putExtra("url", "http://j.rxjy.com/Mobile/More/Design?app_id=" + App.app_id);
                break;
            case R.id.ly_gongcheng://工程
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", "工程");
                intent.putExtra("url", "http://j.rxjy.com/Mobile/More/Engineering?app_id=" + App.app_id);
                break;
            case R.id.ly_gongsi://公司
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", "公司");
                intent.putExtra("url", "http://j.rxjy.com/Mobile/More/Company?app_id=" + App.app_id);
                break;
            case R.id.ly_cailiaoshang://材料商
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", "材料商");
                intent.putExtra("url", "http://j.rxjy.com/Mobile/More/material?app_id=" + App.app_id);
                break;
            case R.id.ly_jingli://项目经理
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", "项目经理");
                intent.putExtra("url", "http://j.rxjy.com/Mobile/More/ProjectsMgr?app_id=" + App.app_id);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
