package com.rxjy.rxinvestment.activity.home;

import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.utils.AndroidBug5497Workaround;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BannerDetailsActivity extends BaseActivity {

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
    @Bind(R.id.wb_rxsongs)
    WebView wbRxsongs;

    @Override
    public int getLayout() {
        return R.layout.activity_banner_details;
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
        AndroidBug5497Workaround.assistActivity(this);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        wbRxsongs = (WebView) findViewById(R.id.wb_rxsongs);
        tvTitle.setText("详情");
        wbRxsongs = (WebView) findViewById(R.id.wb_rxsongs);
        wbRxsongs.getSettings().setJavaScriptEnabled(true);
        wbRxsongs.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String request) {
                return false;
            }
        });
        wbRxsongs.loadUrl(url);
    }

    @Override
    public void initAdapter() {

    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }
}
