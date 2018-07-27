package com.rxjy.rxinvestment.activity.my;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.adapter.my.LeaveAdapter;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.base.PathUrl;
import com.rxjy.rxinvestment.entity.LeaveBean;
import com.rxjy.rxinvestment.utils.JSONUtils;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
import com.rxjy.rxinvestment.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LeaveActivity extends BaseActivity {

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
    @Bind(R.id.leave_lsit)
    ListView leaveLsit;

    private List<LeaveBean.BodyBean> mlist;
    private LeaveAdapter leaveAdapter;

    @Override
    public int getLayout() {
        return R.layout.activity_leave;
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
        tvTitle.setText("请假");
        mlist = new ArrayList<>();
        leaveAdapter = new LeaveAdapter(this, mlist);
        leaveLsit.setAdapter(leaveAdapter);
        tvRight.setText("添加");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LeaveActivity.this, LeavePageActivirt.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        Map map = new HashMap();
        map.put("CardNo", App.cardNo);
        map.put("year", year);
        map.put("month", month);
        OkhttpUtils.doGet(this, PathUrl.QINGJIALISTURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取请假列表", data);
                LeaveBean info = JSONUtils.toObject(data, LeaveBean.class);
                List<LeaveBean.BodyBean> body = info.getBody();
                int statusCode = info.getStatusCode();
                String statusMsg = info.getStatusMsg();
                if (statusCode == 0) {
                    mlist.clear();
                    mlist.addAll(body);
                    leaveAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.getInstance().toastCentent(statusMsg);
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取请假列表失败", message);
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
