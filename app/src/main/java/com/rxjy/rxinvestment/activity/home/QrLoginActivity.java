package com.rxjy.rxinvestment.activity.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.base.PathUrl;
import com.rxjy.rxinvestment.entity.CheckInfo;
import com.rxjy.rxinvestment.utils.GlideCircleTransform;
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

public class QrLoginActivity extends BaseActivity {

    @Bind(R.id.qr_photo)
    ImageView qrPhoto;
    @Bind(R.id.qrRwdid)
    TextView qrRwdid;
    @Bind(R.id.qrRwdid_two)
    TextView qrRwdidTwo;
    @Bind(R.id.qr_login)
    TextView qrLogin;
    @Bind(R.id.cancel_text)
    TextView cancelText;

    @Override
    public int getLayout() {
        return R.layout.activity_qr_login;
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
        qrRwdid.setText(App.name);
        RequestOptions options = new RequestOptions();
        options.centerCrop().transform(new GlideCircleTransform(this));
        options.error(R.mipmap.userimage);
        options.placeholder(R.mipmap.userimage);
        options.centerCrop().transform(new GlideCircleTransform(this));
        Glide.with(this).load(App.image).apply(options).into(qrPhoto);
    }

    @Override
    public void initAdapter() {

    }

    @OnClick({R.id.qr_login, R.id.cancel_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.qr_login:
                String appid = getIntent().getStringExtra("appid");
                String userPsw = MySharedPreferences.getInstance().getUserPsw();
                getRrLogin(App.cardNo, userPsw, appid);
                break;
            case R.id.cancel_text:
                finish();
                break;
        }
    }

    private void getRrLogin(String cardNo, String rxdy_pwd, String appid) {
        Map map = new HashMap();
        map.put("cardNo", cardNo);
        map.put("password", rxdy_pwd);
        map.put("loginId", appid);
        OkhttpUtils.doPost(PathUrl.SMLOGINURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取扫码登陆信息", data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String statusMsg = jsonObject.getString("StatusMsg");
                    int statusCode = jsonObject.getInt("StatusCode");
                    if (statusCode == 0) {
                        CheckInfo info = JSONUtils.toObject(data, CheckInfo.class);
                        if (info.getStatusCode() == 0) {
                            ToastUtil.getInstance().toastCentent("登陆成功");
                            finish();
                        } else {
                            ToastUtil.getInstance().toastCentent(info.getStatusMsg());
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
                Log.e("tag_扫码登陆失败", message);

            }
        });
    }
}
