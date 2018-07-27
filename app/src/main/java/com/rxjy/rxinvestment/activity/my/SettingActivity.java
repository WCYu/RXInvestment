package com.rxjy.rxinvestment.activity.my;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.activity.home.JoininNjjActivity;
import com.rxjy.rxinvestment.activity.home.LoginActivity;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.utils.Constants;
import com.rxjy.rxinvestment.utils.MySharedPreferences;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
import com.rxjy.rxinvestment.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class SettingActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.ly_background)
    LinearLayout lyBackground;
    @Bind(R.id.rl_pwdfix)
    RelativeLayout rlPwdfix;
    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.tv_version)
    TextView tvVersion;
    @Bind(R.id.btn_exitlogin)
    Button btnExitlogin;

    @Override
    public int getLayout() {
        return R.layout.activity_setting;
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
        tvTitle.setText("设置");
        String verson = null;
        try {
            verson = StringUtils.getVersionName(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvVersion.setText(verson);
    }

    @Override
    public void initAdapter() {

    }

    @OnClick({R.id.img_back, R.id.rl_pwdfix, R.id.btn_exitlogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.rl_pwdfix:

                break;
            case R.id.btn_exitlogin:
                new AlertDialog.Builder(this).setTitle("系统提示").setMessage("是否退出登录").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        appDownLine(MySharedPreferences.getInstance().getCardNo());
                        MySharedPreferences.getInstance().empty();
                        SharedPreferences sharedPreferences = getSharedPreferences(Constants.IS_SETALIAS, MODE_PRIVATE);
                        sharedPreferences.edit().putString(App.jpushname, null).commit();
//                        JPushInterface.setAlias(SettingActivity.this, 0, null);
                        Log.e("设置别名", "清空别名");
                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                        startActivity(intent);
                        if (JoininNjjActivity.activity != null) {
                            JoininNjjActivity.activity.finish();
                        }
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                break;
        }
    }

    private void appDownLine(String cardNo) {
        if (!TextUtils.isEmpty(cardNo)) {
            Map map = new HashMap();
            map.put("cardNo", App.cardNo);
            OkhttpUtils.doPost("https://api.dcwzg.com:9191/actionapi/AppHome/OfflineApp", map, new OkhttpUtils.MyCall() {
                @Override
                public void success(String data) {
                    Log.e("tag_下线", data);
                }

                @Override
                public void error(String message) {
                    Log.e("tag_下线失败", message);
                }
            });
        }
    }

}
