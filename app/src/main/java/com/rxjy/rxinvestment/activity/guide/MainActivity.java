package com.rxjy.rxinvestment.activity.guide;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.activity.home.EntryJobProtocolActivity;
import com.rxjy.rxinvestment.activity.home.JoininNjjActivity;
import com.rxjy.rxinvestment.activity.home.LoginActivity;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.base.PathUrl;
import com.rxjy.rxinvestment.entity.VersionInfo;
import com.rxjy.rxinvestment.entity.ZiDonBean;
import com.rxjy.rxinvestment.utils.DownLoadApk;
import com.rxjy.rxinvestment.utils.JSONUtils;
import com.rxjy.rxinvestment.utils.MySharedPreferences;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
import com.rxjy.rxinvestment.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends BaseActivity {


    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {

    }

    @Override
    public void intData() {
        getVersionInfo(Integer.parseInt(App.getVersionCode()));
    }

    @Override
    public void initAdapter() {

    }

    private void init() {
        Log.e("开始", "开始");
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //读取存储，记录应用启动次数
                MySharedPreferences instance = MySharedPreferences.getInstance(MainActivity.this);
                int loginState = instance.getLoginState();
                String userPhone = instance.getUserPhone();
                String userPsw = instance.getUserPsw();
                String token = instance.getToken();
                if (loginState == 0) {
                    startActivity(new Intent(MainActivity.this, GuideActivity.class));
                    finish();
                } else {
                    if (!TextUtils.isEmpty(userPhone) && !TextUtils.isEmpty(userPsw)) {
                        login(userPhone, token);
                    } else {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            }
        };
        timer.schedule(timerTask, 2000);

        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
    }

    private void login(final String phone, final String psw) {
        Map map = new HashMap();
        map.put("CardNo", phone);
        map.put("Token", psw);
        OkhttpUtils.doGet(this, PathUrl.TPKENURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_自动登陆成功", data);
                MySharedPreferences instance = MySharedPreferences.getInstance();
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONObject body1 = jsonObject.getJSONObject("Body");
                    int isResult = body1.getInt("isResult");
                    if (isResult == 0) {
                        ToastUtil.getInstance().toastCentent("您的账号已被他人登陆，请重新登陆", MainActivity.this);
                        appDownLine(MySharedPreferences.getInstance().getCardNo());
                        MySharedPreferences.getInstance().empty();
                        Intent intentl = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intentl);
                        finish();
                    } else {
                        ZiDonBean info = JSONUtils.toObject(data, ZiDonBean.class);
                        ZiDonBean.BodyBean body = info.getBody();
                        String statusMsg = info.getStatusMsg();
                        if (info.getStatusCode() == 0) {
                            instance.setApp_stage(body.getApp_stage());
                            instance.setCardNo(body.getCardNo());
                            instance.setDepart(body.getDepart());
                            instance.setImage(body.getImage());
                            instance.setIs_exist(body.getIs_exist());
                            instance.setIs_group(body.getIs_group() + "");
                            instance.setName(body.getName());
                            instance.setPostid(body.getPost_id());
                            if (!TextUtils.isEmpty(MySharedPreferences.getInstance().getPostName())) {
                                App.postName = MySharedPreferences.getInstance().getPostName();      //部门名称
                            }
                            instance.setRegion_id(body.getRegion_id());
                            instance.setRegion_name(body.getRegion_name());
                            instance.setU_start(body.getU_start());
                            instance.setSex(body.getSex());
                            instance.setPassport_photo(body.getPassport_photo());
                            instance.setIsResult(body.getIsResult());
                            instance.setApp_id(body.getApp_id());
                            instance.setDepartName(body.getDepartName());
                            instance.setAccount(body.getAccount());
                            Log.e("自动登陆", "----");
                            App.is_exist = body.getIs_exist();
                            if (App.u_start != 2 && App.u_start != 3 && App.u_start != 4) {
                                isShowDaTi(JoininNjjActivity.class);
                            } else {
                                getIsConsent(App.cardNo, "2");//请求是否需要同意协议
                            }
                        } else {
                            ToastUtil.getInstance().toastCentent(statusMsg, MainActivity.this);
                            Intent intentl = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intentl);
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_自动登陆失败", message);
                ToastUtil.getInstance().toastCentent("登陆异常", MainActivity.this);
                Intent intentl = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentl);
                finish();
            }
        });
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

    private void getIsConsent(String cardNo, String type) {

        Map map = new HashMap();
        map.put("CardNo", cardNo);
        map.put("Type", type);
        OkhttpUtils.doGet(this, PathUrl.ISAGREEURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_是否同意协议", data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String statusMsg = jsonObject.getString("StatusMsg");
                    int statusCode = jsonObject.getInt("StatusCode");
                    if (statusCode == 0) {
                        isShowDaTi(JoininNjjActivity.class);
                    } else {
                        startActivity(new Intent(MainActivity.this, EntryJobProtocolActivity.class).putExtra("from", "1"));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.getInstance().toastCentent("登陆异常", MainActivity.this);
                    Intent intentl = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intentl);
                    finish();
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取是否同意协议失败", message);
            }
        });
    }

    public void isShowDaTi(final Class cls) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request build = new Request.Builder().url("http://edu.rxjy.com/a/api/" + App.app_id + "/isViewQues").build();
        okHttpClient.newCall(build).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("tag_是否显示答题失败", e.getMessage().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, cls);
                        intent.putExtra("isShow", 1);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                Log.e("tag_是否显示答题", string);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(string);
                            int statusCode = jsonObject.getInt("StatusCode");
                            Intent intent = new Intent(MainActivity.this, cls);
                            if (statusCode == 0) {
                                intent.putExtra("isShow", 0);
                            } else {
                                intent.putExtra("isShow", 1);
                            }
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Intent intent = new Intent(MainActivity.this, cls);
                            intent.putExtra("isShow", 1);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }

    private void getVersionInfo(int version) {
        Map map = new HashMap();
        map.put("Version", version);
        map.put("AppId", "9");
        OkhttpUtils.doGet(this, PathUrl.ISGENGXINURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取版本信息", data);
                VersionInfo versionInfo = JSONUtils.toObject(data, VersionInfo.class);
                if (versionInfo.getStatusCode() == 1) {
                    VersionInfo.Version body = versionInfo.getBody();
                    if (data != null) {
                        responseVersionData(body);
                    }
                } else {
                    init();
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取版本信息失败", message);
                init();
            }
        });
        Log.e("tag_版本号", version + "");
    }

    //Apk下载
    private void responseVersionData(final VersionInfo.Version data) {
        if (data.getVersionNo() > Integer.parseInt(App.getVersionCode())) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_shengji, null);
            TextView shengji = view.findViewById(R.id.tv_shengji);
            TextView quxiao = view.findViewById(R.id.tv_quxiao);
            TextView contentTv = view.findViewById(R.id.tv_content);
            if (data != null) {
                String content = data.getContent();
                if (!TextUtils.isEmpty(content)) {
                    contentTv.setText(content);
                }
                int force = data.getForce();
                if (force == 1) {
                    quxiao.setVisibility(View.GONE);
                }
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(view);
            builder.setCancelable(false);// 设置点击屏幕Dialog不消失
            final AlertDialog dialog = builder.create();
            dialog.show();
            shengji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    DownLoadApk downLoadApk = new DownLoadApk(MainActivity.this);
                    downLoadApk.downLoadApk(data);
                }
            });

            quxiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null) {
                        dialog.dismiss();
                        init();
                    }
                }
            });
        } else {
            init();
        }
    }

}
