package com.rxjy.rxinvestment.activity.my.investment;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetEmiActivity extends BaseActivity {

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
    @Bind(R.id.tv_name)
    EditText tvName;
    @Bind(R.id.rl_name)
    LinearLayout rlName;
    @Bind(R.id.btn_commit)
    Button btnCommit;

    @Override
    public int getLayout() {
        return R.layout.activity_set_emi;
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
        tvTitle.setText("修改邮箱");
    }

    @Override
    public void initAdapter() {

    }

    @OnClick({R.id.img_back, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_commit:
                if(!TextUtils.isEmpty(tvName.getText().toString())){
                    setResult(1002,getIntent().putExtra("emi",tvName.getText().toString()));
                    finish();
                }else {
                    ToastUtil.getInstance().toastCentent("请输入邮箱",this);
                }

                break;
        }
    }
}
