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
import com.rxjy.rxinvestment.adapter.my.MessageNewAdapter;
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

/*
* 消息
* */

public class MessageActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.ly_background)
    LinearLayout lyBackground;
    @Bind(R.id.message_list)
    ListView messageList;
    private MessageNewAdapter messageNewAdapter;
    private ArrayList<InfoMessageBodyBean> list;

    @Override
    public int getLayout() {
        return R.layout.activity_message;
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
        tvTitle.setText("消息");
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMessageData();
    }

    private void initListener() {
        messageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = null;
                switch (list.get(i).getGroup_name()) {
                    case "薪酬":
                        intent = new Intent(MessageActivity.this, OtherMessageActivity.class);
                        intent.putExtra("title", "薪酬");
                        switch (App.is_group) {
                            case "0":
                            case "1":
                                intent.putExtra("group_id", list.get(i).getGroup_id());
                                break;
                            case "2":
                                intent.putExtra("group_id", list.get(i).getGroup());
                                break;
                            default:
                                intent.putExtra("group_id", list.get(i).getGroup_id());
                                break;
                        }
                        break;
                    case "事件":
                        intent = new Intent(MessageActivity.this, OtherMessageActivity.class);
                        intent.putExtra("title", "事件");
                        switch (App.is_group) {
                            case "0":
                            case "1":
                                intent.putExtra("group_id", list.get(i).getGroup_id());
                                break;
                            case "2":
                                intent.putExtra("group_id", list.get(i).getGroup());
                                break;
                            default:
                                intent.putExtra("group_id", list.get(i).getGroup_id());
                                break;
                        }
                        break;
                    case "业务":
                        intent = new Intent(MessageActivity.this, OtherMessageActivity.class);
                        intent.putExtra("title", "业务");
                        switch (App.is_group) {
                            case "0":
                            case "1":
                                intent.putExtra("group_id", list.get(i).getGroup_id());
                                break;
                            case "2":
                                intent.putExtra("group_id", list.get(i).getGroup());
                                break;
                            default:
                                intent.putExtra("group_id", list.get(i).getGroup_id());
                                break;
                        }
                        break;
                    case "红包":
                        intent = new Intent(MessageActivity.this, RedEnvelopesActivity.class);
                        switch (App.is_group) {
                            case "0":
                            case "1":
                                intent.putExtra("group_id", list.get(i).getGroup_id());
                                break;
                            case "2":
                                intent.putExtra("group_id", list.get(i).getGroup());
                                break;
                            default:
                                intent.putExtra("group_id", list.get(i).getGroup_id());
                                break;
                        }
                        break;
                    case "其它":
                        intent = new Intent(MessageActivity.this, OtherMessageActivity.class);
                        intent.putExtra("title", "其它");
                        switch (App.is_group) {
                            case "0":
                            case "1":
                                intent.putExtra("group_id", list.get(i).getGroup_id());
                                break;
                            case "2":
                                intent.putExtra("group_id", list.get(i).getGroup());
                                break;
                            default:
                                intent.putExtra("group_id", list.get(i).getGroup_id());
                                break;
                        }
                        break;
                    case "活动":
                        intent = new Intent(MessageActivity.this, OtherMessageActivity.class);
                        intent.putExtra("title", "活动");
                        switch (App.is_group) {
                            case "0":
                            case "1":
                                intent.putExtra("group_id", list.get(i).getGroup_id());
                                break;
                            case "2":
                                intent.putExtra("group_id", list.get(i).getGroup());
                                break;
                            default:
                                intent.putExtra("group_id", list.get(i).getGroup_id());
                                break;
                        }
                        break;
                }

                if (intent != null) {
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void initAdapter() {
        list = new ArrayList();
        messageNewAdapter = new MessageNewAdapter(MessageActivity.this, list);
        messageList.setAdapter(messageNewAdapter);
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }

    public void getMessageData() {
        String url = null;
        Map map = new HashMap();
        switch (App.is_group) {
            case "0":
            case "1":
                url = PathUrl.MESSAGELISTURL;
                map.put("CardNo", App.cardNo);
                break;
            case "2":
                url = PathUrl.WBMESSAGELISTURL;
                map.put("CardNo", App.cardNo);
                map.put("IsApp", 9);
                break;
            default:
                url = PathUrl.MESSAGELISTURL;
                map.put("CardNo", App.cardNo);
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
                    messageNewAdapter.notifyDataSetChanged();
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
}
