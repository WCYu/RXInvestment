package com.rxjy.rxinvestment.activity.more;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RenShiActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.ly_background)
    LinearLayout lyBackground;
    @Bind(R.id.img_content)
    ImageView imgContent;

    @Override
    public int getLayout() {
        return R.layout.activity_ren_shi;
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
        String title = getIntent().getStringExtra("title");
        tvTitle.setText(title);
        switch (title) {
            case "财务":
                imgContent.setImageResource(R.mipmap.yuecaiwu);
                break;
            case "资金":
                imgContent.setImageResource(R.mipmap.yuezijin);
                break;
            case "商务":
                imgContent.setImageResource(R.mipmap.yueshangwu);
                break;
            case "主案":
                imgContent.setImageResource(R.mipmap.yuezhuan);
                break;
            case "工程":
                imgContent.setImageResource(R.mipmap.yuegongcheng);
                break;
            case "公司":
                imgContent.setImageResource(R.mipmap.yuegongsi);
                break;
        }
    }

    @Override
    public void initAdapter() {

    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }
}
