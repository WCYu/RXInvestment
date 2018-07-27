package com.rxjy.rxinvestment.fragment.more;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.activity.my.MessageActivity;
import com.rxjy.rxinvestment.activity.my.SettingActivity;
import com.rxjy.rxinvestment.activity.my.UserInfoActivity;
import com.rxjy.rxinvestment.activity.my.WalletActivity;
import com.rxjy.rxinvestment.activity.my.WorkActivity;
import com.rxjy.rxinvestment.activity.my.investment.InvestmentUserActivity;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.base.PathUrl;
import com.rxjy.rxinvestment.entity.MsgNumBean;
import com.rxjy.rxinvestment.entity.PersonBean;
import com.rxjy.rxinvestment.entity.UserInfoBean;
import com.rxjy.rxinvestment.fragment.utils.BaseFragment;
import com.rxjy.rxinvestment.utils.JSONUtils;
import com.rxjy.rxinvestment.utils.MySharedPreferences;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
import com.rxjy.rxinvestment.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的
 * Created by hjh on 2017/11/6.
 */
public class MainFragment extends BaseFragment {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.img_personicon)
    ImageView imgPersonicon;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_pjob)
    TextView tvPjob;
    @Bind(R.id.tv_paccount)
    TextView tvPaccount;
    @Bind(R.id.imageView5)
    ImageView imageView5;
    @Bind(R.id.img_erweima)
    ImageView img_erweima;
    @Bind(R.id.rl_persondetails)
    RelativeLayout rlPersondetails;
    @Bind(R.id.rl_office)
    RelativeLayout rlOffice;
    @Bind(R.id.rl_wallet)
    RelativeLayout rlWallet;
    @Bind(R.id.rl_jifen)
    RelativeLayout rlJifen;
    @Bind(R.id.rl_chengjiu)
    RelativeLayout rlChengjiu;
    @Bind(R.id.rl_informmessage)
    RelativeLayout rlInformmessage;
    @Bind(R.id.rl_setting)
    RelativeLayout rlSetting;
    @Bind(R.id.tv_messagenum)
    TextView tvMessagenum;

    private UserInfoBean.BodyBean bodyBean;
    private MySharedPreferences instance;

    @Override
    public int getInitId() {
        return R.layout.fragment_main;
    }

    @Override
    public void initView(View view) {
        ButterKnife.bind(this, view);
        imgBack.setVisibility(View.GONE);
        tvTitle.setText("个人中心");
    }

    @Override
    public void initData() {
        instance = MySharedPreferences.getInstance();
        if (!TextUtils.isEmpty(instance.getName())) {
            tvName.setText(instance.getName());
        }
        if (!TextUtils.isEmpty(instance.getCardNo())) {
            tvPaccount.setText("账号：" + instance.getCardNo());
        }
        String image = instance.getImage();
        if (!TextUtils.isEmpty(image)) {
            Glide.with(this).load(image).apply(RequestOptions.circleCropTransform()).into(imgPersonicon);
        }
        if (App.postName.equals("投资招商")) {
            img_erweima.setVisibility(View.VISIBLE);
            rlChengjiu.setVisibility(View.GONE);
            rlWallet.setEnabled(false);
        } else {
            rlOffice.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(App.postName)) {
            tvPjob.setText(App.postName);
        } else {
            tvPjob.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String image = instance.getImage();
        Glide.with(this).load(image).apply(RequestOptions.circleCropTransform()).into(imgPersonicon);
        getMessageNum();
        if (App.postName.equals("投资招商")) {
            getUserInfo();
        }
    }

    @Override
    public void initAdapter() {

    }

    @Override
    public void initLinstener() {

    }

    public void getUserInfo() {
        Map map = new HashMap();
        map.put("cardNo", App.cardNo);
        map.put("token", App.token);

        OkhttpUtils.doPost(PathUrl.ZHAOSHANGUSERURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取用户信息", data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String statusMsg = jsonObject.getString("StatusMsg");
                    int statusCode = jsonObject.getInt("StatusCode");
                    if (statusCode == 0) {
                        Gson gson = new Gson();
                        UserInfoBean info = gson.fromJson(data, UserInfoBean.class);
                        bodyBean = info.getBody().get(0);
                        tvName.setText(bodyBean.getU_name());
                        if (App.postName.equals("投资招商")) {
                            tvPaccount.setText(bodyBean.getPhone());
                        } else {
                            tvPaccount.setText(bodyBean.getCard_no());
                        }
                    } else {
                        ToastUtil.getInstance().toastCentent(statusMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_登陆失败", message);

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.rl_persondetails, R.id.rl_office, R.id.rl_wallet, R.id.rl_jifen, R.id.rl_chengjiu, R.id.rl_informmessage, R.id.rl_setting})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rl_persondetails://用户详情
                if (App.postName.equals("投资招商")) {
                    intent = new Intent(getActivity(), InvestmentUserActivity.class);
                } else {
                    intent = new Intent(getActivity(), UserInfoActivity.class);
                }
                break;
            case R.id.rl_office://办公
                intent = new Intent(getActivity(), WorkActivity.class);
                break;
            case R.id.rl_wallet://钱包
                intent = new Intent(getActivity(), WalletActivity.class);
                break;
            case R.id.rl_jifen://积分

                break;
            case R.id.rl_chengjiu://成就

                break;
            case R.id.rl_informmessage://消息
                intent = new Intent(getActivity(), MessageActivity.class);
                break;
            case R.id.rl_setting://设置
                intent = new Intent(getActivity(), SettingActivity.class);
                startActivityForResult(intent, 10088);
                intent = null;
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }

    //获取消息数量
    public void getMessageNum() {
        String url = null;
        Map map = new HashMap();
        switch (App.is_group) {
            case "0":
            case "1":
                url = PathUrl.MESSAGENUMURL;
                break;
            case "2":
                url = PathUrl.WBMESSAGENUMURL;
                break;
            default:
                url = PathUrl.MESSAGENUMURL;
                break;
        }
        map.put("CardNo", App.cardNo);
        OkhttpUtils.doGet(getActivity(), url, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取消息数量", data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String statusMsg = jsonObject.getString("StatusMsg");
                    int statusCode = jsonObject.getInt("StatusCode");
                    if (statusCode == 0) {
                        MsgNumBean info = JSONUtils.toObject(data, MsgNumBean.class);
                        MsgNumBean.BodyBean body = info.getBody();
                        if (body.getCount() > 0) {
                            tvMessagenum.setVisibility(View.VISIBLE);
                            tvMessagenum.setText(body.getCount() + "");
                        } else {
                            tvMessagenum.setVisibility(View.GONE);
                        }

                    } else {
                        ToastUtil.getInstance().toastCentent(statusMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取消息数量失败", message);
            }
        });
    }
}