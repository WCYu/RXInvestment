package com.rxjy.rxinvestment.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.base.PathUrl;
import com.rxjy.rxinvestment.utils.Constants;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
import com.rxjy.rxinvestment.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EntryJobProtocolActivity extends BaseActivity {
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
    @Bind(R.id.wb_entryjobview)
    WebView wbEntryjobview;
    @Bind(R.id.cb_entrybox)
    CheckBox cbEntrybox;
    @Bind(R.id.tv_sure)
    TextView tvSure;

    String isfrom, cardno, phonenum;

    @Override
    public int getLayout() {
        return R.layout.activity_entry_job_protocol;
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
        tvTitle.setText("入职须知");
        Intent intent = getIntent();
        isfrom = intent.getStringExtra("from");
        tvSure.setBackgroundColor(getResources().getColor(R.color.textgreytwo));
        tvSure.setEnabled(false);//设置不可点击
        SharedPreferences sp = getSharedPreferences("rxdy_userdatas", Activity.MODE_PRIVATE);
        phonenum = sp.getString("rxdy_phonenum", null);
        cardno = App.cardNo;
        initListener();
        showWebView();
    }

    private void initListener() {
        cbEntrybox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {//选中
                    tvSure.setBackgroundColor(getResources().getColor(R.color.colorred));
                    tvSure.setEnabled(true);//设置可点击
                } else {
                    tvSure.setBackgroundColor(getResources().getColor(R.color.textgreytwo));
                    tvSure.setEnabled(false);//设置不可点击
                }
            }
        });
    }

    private void showWebView() {
        wbEntryjobview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String request) {
                return false;
            }
        });
        wbEntryjobview.loadUrl(Constants.WEBURL_ENTRYJOB + "CardNo=" + cardno + "&Type=" + "2");
    }

    @Override
    public void initAdapter() {

    }

    @OnClick({R.id.img_back, R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_sure:
                getConsent(cardno, "2");
                break;
        }
    }

    private void getConsent(String cardno, String type) {
        Map map = new HashMap();
        map.put("CardNo", cardno);
        map.put("Type", type);
        OkhttpUtils.doGet(this, PathUrl.AGREEURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_是否同意协议", data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String statusMsg = jsonObject.getString("StatusMsg");
                    int statusCode = jsonObject.getInt("StatusCode");
                    if (statusCode == 0) {
                        Intent intent = new Intent(EntryJobProtocolActivity.this, JoininNjjActivity.class);
                        startActivity(intent);
                    } else {
                        ToastUtil.getInstance().toastCentent(statusMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取是否同意协议失败", message);
            }
        });
    }
}
