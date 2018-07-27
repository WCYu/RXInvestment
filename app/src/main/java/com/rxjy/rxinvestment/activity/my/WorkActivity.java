package com.rxjy.rxinvestment.activity.my;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.activity.utils.WebViewActivity;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.base.PathUrl;
import com.rxjy.rxinvestment.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WorkActivity extends BaseActivity {

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
    @Bind(R.id.qingjia_line)
    LinearLayout qingjiaLine;
    @Bind(R.id.zhuanzheng_line)
    LinearLayout zhuanzhengLine;
    @Bind(R.id.ly_ruzhi)
    LinearLayout lyRuzhi;
    @Bind(R.id.ly_tiaozhi)
    LinearLayout lyTiaozhi;
    @Bind(R.id.ly_tiaoxin)
    LinearLayout lyTiaoxin;
    @Bind(R.id.img_tiao)
    ImageView imgTiao;
    @Bind(R.id.ge_line)
    LinearLayout geLine;
    @Bind(R.id.wenhua_line)
    LinearLayout wenhuaLine;

    @Override
    public int getLayout() {
        return R.layout.activity_work;
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
        tvTitle.setText("办公");
    }

    @Override
    public void initAdapter() {

    }


    @OnClick({R.id.img_back, R.id.qingjia_line, R.id.zhuanzheng_line, R.id.ge_line, R.id.wenhua_line})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.qingjia_line:
                intent = new Intent(WorkActivity.this, LeaveActivity.class);
                break;
            case R.id.zhuanzheng_line:
                if (App.u_start == 2) {
                    startActivity(new Intent(WorkActivity.this, ZhuanZhengActivity.class));
                } else if (App.u_start == 3) {
                    ToastUtil.getInstance().toastCentent("您已经是正式员工");
                } else if (App.u_start == 100010) {
                    ToastUtil.getInstance().toastCentent("您已经提交转正");
                } else {
                    ToastUtil.getInstance().toastCentent("只有试用期人员才可以申请");
                }
                break;
            case R.id.ge_line:
                intent = new Intent(WorkActivity.this, WebViewActivity.class);
                intent.putExtra("url", PathUrl.WEBURL_ENTRYJOBURL + "CardNo=" + App.cardNo + "&Type=" + "5");
                intent.putExtra("title", "瑞祥之歌");

                break;
            case R.id.wenhua_line:
                intent = new Intent(WorkActivity.this, WebViewActivity.class);
                intent.putExtra("url", PathUrl.WEBURL_ENTRYJOBURL + "CardNo=" + App.cardNo + "&Type=" + "4");
                intent.putExtra("title", "瑞祥准则");
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
