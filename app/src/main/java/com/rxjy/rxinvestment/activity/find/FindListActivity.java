package com.rxjy.rxinvestment.activity.find;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.utils.AndroidBug5497Workaround;
import com.rxjy.rxinvestment.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindListActivity extends BaseActivity {

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
        return R.layout.activity_find_list;
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        init();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void init(){
        Intent intent=getIntent();
        String url_id=intent.getStringExtra("id");
        String title=intent.getStringExtra("title");
        String type=intent.getStringExtra("type");
        String courseId = intent.getStringExtra("courseId");
        tvTitle.setText("详情");
        wbRxsongs.getSettings().setJavaScriptEnabled(true);
        wbRxsongs.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wbRxsongs.getSettings().setDomStorageEnabled(true);
        wbRxsongs.getSettings().setMediaPlaybackRequiresUserGesture(false);
        wbRxsongs.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String request) {
                return false;
            }
        });
        switch (type){
            case "0":
//                wb_rxsongs.loadUrl("http://news.rx/front/app_details.html?id="+url_id+"&cardNo="+ App.cardNo);
                wbRxsongs.loadUrl("http://wpsnew.rxjy.com//front/app_details.html?id="+url_id+"&cardNo="+ App.cardNo+"&appId="+App.app_id+"&isAndroid=0");
//                Log.e("h5url","http://news.rx/front/app_details.html?id="+url_id+"&cardNo"+App.cardNo);
                break;
            case "1"://岗前答题
                wbRxsongs.loadUrl("http://edu.rxjy.com/a/rs/curaInfo/"+App.cardNo+"/"+courseId+"/GetTryPostQues");
//                wb_rxsongs.loadUrl("http://news.rx//front/app_video.html");
                break;
            case "2"://日常培训
//                wb_rxsongs.loadUrl("http://news.rx//front/app_video.html");
                wbRxsongs.loadUrl("http://edu.rxjy.com/a/rs/cour/"+App.cardNo+"/GetTrainCurr");
                break;
            case "3":
                wbRxsongs.loadUrl("http://wpsnew.rxjy.com//front/app_details.html?id="+url_id+"&cardNo="+ App.cardNo+"&appId="+App.app_id+"&isAndroid=0");
        }
//        wb_rxsongs.loadUrl("http://news.rx/front/app_details.html?id="+url_id+"&cardNo"+ App.cardNo);
//        Log.e("h5url","http://news.rx/front/app_details.html?id="+url_id+"&cardNo"+App.cardNo);
////        wb_rxsongs.loadUrl("http://news.rx/front/app_details.html?id="+url_id);
    }

    @Override
    public void initAdapter() {

    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }
}
