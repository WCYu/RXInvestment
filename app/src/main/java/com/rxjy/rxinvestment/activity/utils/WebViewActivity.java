package com.rxjy.rxinvestment.activity.utils;

import android.content.Intent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebViewActivity extends BaseActivity {

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
    @Bind(R.id.webview)
    WebView webview;

    @Override
    public int getLayout() {
        return R.layout.activity_web_view;
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
        //http://j.rxjy.com/Mobile/News/Index?id=1&cardno=01011166
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String title = intent.getStringExtra("title");
        webview.loadUrl(url);
        tvTitle.setText(title);
        WebSettings wSet = webview.getSettings();
        wSet.setJavaScriptEnabled(true);
        wSet.setUseWideViewPort(true);
        wSet.setLoadWithOverviewMode(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
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
