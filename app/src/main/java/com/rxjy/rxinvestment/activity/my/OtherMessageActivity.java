package com.rxjy.rxinvestment.activity.my;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.adapter.my.MessageTwoAdapter;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.base.PathUrl;
import com.rxjy.rxinvestment.entity.InfoMessageBean;
import com.rxjy.rxinvestment.entity.InfoMessageBodyBean;
import com.rxjy.rxinvestment.utils.JSONUtils;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
import com.rxjy.rxinvestment.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OtherMessageActivity extends BaseActivity {

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
    @Bind(R.id.ll_message)
    ListView llMessage;
    private ArrayList<InfoMessageBodyBean> list;
    private MessageTwoAdapter messageTwoAdapter;

    @Override
    public int getLayout() {
        return R.layout.activity_other_message;
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
        showListview();
        tvTitle.setText(getIntent().getStringExtra("title"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInfoMessageTwoList();
    }

    private void getInfoMessageTwoList() {
        String url = null;
        Map map = new HashMap();
        switch (App.is_group) {
            case "0":
            case "1":
                url = PathUrl.REDTASKURL;
                map.put("cardNo", App.cardNo);
                map.put("Group", getIntent().getStringExtra("group_id"));
                break;
            case "2":
                url = PathUrl.WBREDTASKURL;
                map.put("CardNo", App.cardNo);
                map.put("Group", getIntent().getStringExtra("group_id"));
                break;
            default:
                url = PathUrl.REDTASKURL;
                map.put("cardNo", App.cardNo);
                map.put("Group", getIntent().getStringExtra("group_id"));
                break;
        }
        OkhttpUtils.doGet(this, url, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取消息列表", data);
                InfoMessageBean info = JSONUtils.toObject(data, InfoMessageBean.class);
                ArrayList<InfoMessageBodyBean> body = info.getBody();
                int statusCode = info.getStatusCode();
                String statusMsg = info.getStatusMsg();
                if (statusCode == 0) {
                    list.clear();
                    list.addAll(body);
                    messageTwoAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.getInstance().toastCentent(statusMsg);
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取消息列表失败", message);
            }
        });
    }

    private void showListview() {
        list = new ArrayList<>();
        messageTwoAdapter = new MessageTwoAdapter(this, list);
        llMessage.setAdapter(messageTwoAdapter);
        llMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(OtherMessageActivity.this, MessageDetailsActivity.class).putExtra("id", list.get(position).getId() + ""));

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
