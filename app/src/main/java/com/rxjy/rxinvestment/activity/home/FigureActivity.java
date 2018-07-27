package com.rxjy.rxinvestment.activity.home;

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
import com.rxjy.rxinvestment.entity.EnvirBean;
import com.rxjy.rxinvestment.utils.JSONUtils;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
import com.rxjy.rxinvestment.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FigureActivity extends BaseActivity {

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
    @Bind(R.id.imageView26)
    ImageView imageView26;
    @Bind(R.id.ll_tz)
    LinearLayout llTz;
    @Bind(R.id.ll_sw)
    LinearLayout llSw;
    @Bind(R.id.ll_za)
    LinearLayout llZa;
    @Bind(R.id.ll_gc)
    LinearLayout llGc;

    private String id_sw, id_tz, id_gc, id_za;

    @Override
    public int getLayout() {
        return R.layout.activity_figure;
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
        tvTitle.setText("形象");
        getFigureList(App.cardNo, App.region_id, "1");
    }

    private void getFigureList(final String cardNo, final int region_id, String s) {
        Map map = new HashMap();
        map.put("CardNo", cardNo);
        map.put("RegionId", region_id);
        map.put("Type", s);

        OkhttpUtils.doGet(this, PathUrl.GETXINGXIANGURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(final String data) {
                Log.e("tag_获取环境信息" + cardNo + "  " + region_id, data);
                EnvirBean info = JSONUtils.toObject(data, EnvirBean.class);
                final ArrayList<EnvirBean.BodyBean> body = info.getBody();
                int statusCode = info.getStatusCode();
                String statusMsg = info.getStatusMsg();
                if (statusCode == 0) {
                    if (body.size() == 4) {
                        id_tz = body.get(0).getId() + "";
                        id_sw = body.get(1).getId() + "";
                        id_za = body.get(2).getId() + "";
                        id_gc = body.get(3).getId() + "";
                    }
                } else {
                    ToastUtil.getInstance().toastCentent(statusMsg);
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取环境失败", message);
            }
        });
    }

    @Override
    public void initAdapter() {

    }

    @OnClick({R.id.img_back, R.id.ll_tz, R.id.ll_sw, R.id.ll_za, R.id.ll_gc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ll_sw:
                startActivity(new Intent(FigureActivity.this, FigureDetailsActivity.class).putExtra("title", "商务部").putExtra("type", "2").putExtra("id_dnum", id_sw));
                break;
            case R.id.ll_tz:
                startActivity(new Intent(FigureActivity.this, FigureDetailsActivity.class).putExtra("title", "投资部").putExtra("type", "34").putExtra("id_dnum", id_tz));
                break;
            case R.id.ll_gc:
                startActivity(new Intent(FigureActivity.this, FigureDetailsActivity.class).putExtra("title", "工程部").putExtra("type", "4").putExtra("id_dnum", id_gc));
                break;
            case R.id.ll_za:
                startActivity(new Intent(FigureActivity.this, FigureDetailsActivity.class).putExtra("title", "主案部").putExtra("type", "3").putExtra("id_dnum", id_za));
                break;
        }
    }
}
