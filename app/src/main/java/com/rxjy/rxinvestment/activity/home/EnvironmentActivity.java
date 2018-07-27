package com.rxjy.rxinvestment.activity.home;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.adapter.home.EnvironmentAdapter;
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

public class EnvironmentActivity extends BaseActivity {

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
    @Bind(R.id.lv_envir)
    ListView lvEnvir;

    ArrayList<String> list;
    ArrayList<String> list_id;
    private EnvironmentAdapter adapter;

    @Override
    public int getLayout() {
        return R.layout.activity_environment;
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
        showLoading();
        tvTitle.setText("环境");
        list = new ArrayList<>();
        list_id = new ArrayList<>();
        getEnvir(App.cardNo, App.region_id, "2");
    }

    private void getEnvir(final String cardNo, final int region_id, String s) {
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
                    adapter = new EnvironmentAdapter(EnvironmentActivity.this, body);
                    lvEnvir.setAdapter(adapter);
                    lvEnvir.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            startActivity(new Intent(EnvironmentActivity.this, EnvironmentDetailsActivity.class)
                                    .putExtra("getid", body.get(position).getId() + "")
                                    .putExtra("getcontentid", body.get(position).getContentId() + "")
                                    .putExtra("getareaname", body.get(position).getAreaName()));
                        }
                    });
                } else {
                    ToastUtil.getInstance().toastCentent(statusMsg);
                }
                dismissLoading();
            }

            @Override
            public void error(String message) {
                dismissLoading();
                ToastUtil.getInstance().toastCentent("获取环境列表失败");
                Log.e("tag_获取环境失败", message);
            }
        });
        setProgressDialog(3000);
    }

    @Override
    public void initAdapter() {

    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }
}
