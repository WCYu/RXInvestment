package com.rxjy.rxinvestment.activity.more;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.adapter.more.AdTaskAdaper;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.base.PathUrl;
import com.rxjy.rxinvestment.entity.AdRedEnvelopesBean;
import com.rxjy.rxinvestment.utils.JSONUtils;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
import com.rxjy.rxinvestment.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdministrationRedActivity extends BaseActivity {

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
    @Bind(R.id.lv_task)
    ListView lvTask;
    private AdTaskAdaper taskAdaper;
    private ArrayList<AdRedEnvelopesBean.BodyBean> mlist;

    @Override
    public int getLayout() {
        return R.layout.activity_administration_red;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        initListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void intData() {
        tvTitle.setText("红包列表");
        mlist = new ArrayList<>();
        getRedTask(App.cardNo);
    }

    private void getRedTask(String cardNo) {
        Map map = new HashMap();
        map.put("cardNo", cardNo);
        OkhttpUtils.doGet(this, PathUrl.MONEYLISTURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取红包列表", data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String statusMsg = jsonObject.getString("StatusMsg");
                    int statusCode = jsonObject.getInt("StatusCode");
                    if (statusCode == 0) {
                        AdRedEnvelopesBean info = JSONUtils.toObject(data, AdRedEnvelopesBean.class);
                        List<AdRedEnvelopesBean.BodyBean> body = info.getBody();
                        mlist.clear();
                        mlist.addAll(body);
                        taskAdaper.notifyDataSetChanged();
                    } else {
                        ToastUtil.getInstance().toastCentent(statusMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取红包列表失败", message);
            }
        });
    }

    private void initListener() {
        lvTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(AdministrationRedActivity.this, RedTaskActivity.class).putExtra("task", mlist.get(position)));
            }
        });
    }

    @Override
    public void initAdapter() {
        taskAdaper = new AdTaskAdaper(this, mlist);
        lvTask.setAdapter(taskAdaper);
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }
}
