package com.rxjy.rxinvestment.activity.home;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.base.PathUrl;
import com.rxjy.rxinvestment.custom.DownTimerButton;
import com.rxjy.rxinvestment.entity.BaseBean;
import com.rxjy.rxinvestment.entity.BodyBean;
import com.rxjy.rxinvestment.entity.CheckIsBeingBean;
import com.rxjy.rxinvestment.entity.LoginInfoBean;
import com.rxjy.rxinvestment.entity.MemberInfoBean;
import com.rxjy.rxinvestment.entity.VPhoneBean;
import com.rxjy.rxinvestment.utils.JSONUtils;
import com.rxjy.rxinvestment.utils.MySharedPreferences;
import com.rxjy.rxinvestment.utils.NetWorkUtils;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
import com.rxjy.rxinvestment.utils.StringUtils;
import com.rxjy.rxinvestment.utils.ToastUtil;
import com.rxjy.rxinvestment.utils.ZJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.iv_phone_icon)
    ImageView ivPhoneIcon;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.ed_phone)
    EditText edPhone;
    @Bind(R.id.rl_phone)
    RelativeLayout rlPhone;
    @Bind(R.id.iv_verification_code_icon)
    ImageView ivVerificationCodeIcon;
    @Bind(R.id.tv_vitifycode)
    TextView tvVitifycode;
    @Bind(R.id.ed_vitifycode)
    EditText edVitifycode;
    @Bind(R.id.tv_vitifycodeget)
    TextView tvVitifycodeget;
    @Bind(R.id.rl_veritycode)
    RelativeLayout rlVeritycode;
    @Bind(R.id.iv_new_password_icon)
    ImageView ivNewPasswordIcon;
    @Bind(R.id.tv_pwd)
    TextView tvPwd;
    @Bind(R.id.ed_pwd)
    EditText edPwd;
    @Bind(R.id.rl_pwd)
    RelativeLayout rlPwd;
    @Bind(R.id.btn_next)
    Button btnNext;
    @Bind(R.id.tv_forgetpwd)
    TextView tvForgetpwd;
    @Bind(R.id.ll_toforget)
    LinearLayout llToforget;
    @Bind(R.id.tv_prompt)
    TextView tvPrompt;
    private String phonenum;

    private int logintype;//0：验证码登  1：密码登录
    private String phone;
    private String veritycode;
    private String pwdnum;
    private Object userInfo;

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        edPhone.addTextChangedListener(new MyEditListener(edPhone));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void intData() {

    }

    @Override
    public void initAdapter() {

    }

    @OnClick({R.id.tv_vitifycode, R.id.btn_next, R.id.ll_toforget})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_vitifycode://获取验证码
                String phone = edPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
//                    ToastUtil.getInstance().toastCentent("请输入手机号");
                    showTiShi("请输入手机号");
                    return;
                }
                if (!StringUtils.isMobileNO(phone)) {
//                    ToastUtil.getInstance().toastCentent("请输入正确的手机号");
                    showTiShi("请输入正确的手机号");
                    return;
                }
                DownTimerButton.setIsclick(1);
                getVCode(phonenum);
                break;
            case R.id.btn_next://登陆/激活
                veritycode = edVitifycode.getText().toString();
                pwdnum = edPwd.getText().toString();
                if (pwdnum.length() < 6) {
//                    tvPrompt.setText("密码不少于6位");
                    showTiShi("密码不少于6位");
                    return;
                }
                showLoading();
                switch (logintype) {
                    case 0:
                        if (!phonenum.isEmpty() && !veritycode.isEmpty() && !pwdnum.isEmpty()) {
                            //请求登录
                            login(phonenum, veritycode, pwdnum);
                        } else if (phonenum.isEmpty()) {
//                            tvPrompt.setText("请输入手机号！");
                            showTiShi("请输入手机号");
                        } else if (veritycode.isEmpty()) {
//                            tvPrompt.setText("请输入验证码！");
                            showTiShi("请输入验证码");
                        } else if (pwdnum.isEmpty()) {
//                            tvPrompt.setText("请输入密码！");
                            showTiShi("请输入密码");
                        }
                        break;
                    case 1:
                        if (!phonenum.isEmpty() && !pwdnum.isEmpty()) {
                            //请求登录
                            login(phonenum, veritycode, pwdnum);
                        } else if (phonenum.isEmpty()) {
//                            tvPrompt.setText("请输入手机号！");
                            showTiShi("请输入手机号");
                        } else if (pwdnum.isEmpty()) {
//                            tvPrompt.setText("请输入密码！");
                            showTiShi("请输入密码");
                        }
                        break;
                }
                break;
            case R.id.ll_toforget://忘记密码
                startActivity(new Intent(this, ForgetPwdActivity.class).putExtra("phone", phonenum));
                break;
        }
    }

    private void getVCode(String phone) {
        Map map = new HashMap();
        map.put("phone", phone);
        map.put("AppId", "9");
        OkhttpUtils.doPost(PathUrl.RENZHENGCODEURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取验证码", data);
                BaseBean info = JSONUtils.toObject(data, BaseBean.class);
                int statusCode = info.getStatusCode();
                String statusMsg = info.getStatusMsg();
                if (statusCode == 0) {

                } else {
//                    ToastUtil.getInstance().toastCentent(statusMsg);
                    showTiShi(statusMsg);
                }

            }

            @Override
            public void error(String message) {
                Log.e("tag_验证码失败", message);
            }
        });
    }

    private void login(final String phone, String code, final String psw) {
        Map map = new HashMap();
        map.put("cardNo", phone);
        map.put("password", psw);
        map.put("vCode", code);
        map.put("appId", "9");
        OkhttpUtils.doPost(PathUrl.LOGINURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_登陆成功", data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String statusMsg = jsonObject.getString("StatusMsg");
                    int statusCode = jsonObject.getInt("StatusCode");
                    if (statusCode == 0) {
                        LoginInfoBean info = JSONUtils.toObject(data, LoginInfoBean.class);
                        LoginInfoBean.BodyBean body = info.getBody();
                        MySharedPreferences.getInstance().setToken(body.getToken());
                        MySharedPreferences.getInstance().setCardNo(body.getCardNo());
                        MySharedPreferences.getInstance().setAccount(body.getAccount());
                        MySharedPreferences.getInstance().setApp_id(body.getAppId());
                        MySharedPreferences.getInstance().setIs_group(body.getIs_group() + "");
                        switch (body.getIs_group()) {
                            case "0":
                            case "1":
                                getUserInfo(body.getCardNo(), body.getToken());
                                break;
                            case "2":
                                getUserInfoMember(body.getCardNo(), body.getToken());
                                break;
                            default:
                                break;
                        }
                        startLogin(MySharedPreferences.getInstance().getCardNo());
                    } else {
//                        ToastUtil.getInstance().toastCentent(statusMsg);
//                        tvPrompt.setText(statusMsg);
                        dismissLoading();
                        showTiShi(statusMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_登陆失败", message);
//                tvPrompt.setText("登陆失败");
                showTiShi("登陆失败");
                dismissLoading();
            }
        });

        setProgressDialog(5000);
    }

    private void startLogin(String cardNo) {
        Map map = new HashMap();
        map.put("app_id", "");
        map.put("card_no", cardNo);
        map.put("landing_date", "");
        map.put("offline_date", "");
        map.put("locate_province_now", "");
        map.put("locate_city_now", "");
        map.put("a_equipment", android.os.Build.MODEL);//使用设备
        switch (NetWorkUtils.getAPNType(this)) {
            case 0:
                map.put("network_status", "");//网络状态
                break;
            case 1:
                map.put("network_status", "WIFI");//网络状态
                break;
            case 2:
                map.put("network_status", "2G");//网络状态
                break;
            case 3:
                map.put("network_status", "3G");//网络状态
                break;
            case 4:
                map.put("network_status", "4G");//网络状态
                break;
            default:
                break;
        }
        map.put("a_ip ", "");//IP地址
        map.put("id", "");
        map.put("flag", "");
        map.put("name", "");
        map.put("create_date", "");
        map.put("update_date", "");
        map.put("app_version_number", App.getVersionName());
        map.put("system_version_number", android.os.Build.VERSION.SDK + ","
                + android.os.Build.VERSION.RELEASE);//系统版本
        String androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String id = androidID + Build.SERIAL;
        map.put("mac_address", id);
        String toJSONMap = ZJson.toJSONMap(map);
        OkHttpClient client = new OkHttpClient();
        MediaType MEDIA_TYPE_TEXT = MediaType.parse("application/json");
        Request request = new Request.Builder()
                .url("https://api.dcwzg.com:9191/actionapi/AppHome/AddlandingMessage")
                .post(RequestBody.create(MEDIA_TYPE_TEXT, toJSONMap))
                .build();
        Log.e("lrj", "136236");
        Log.e("tag_数据统计", toJSONMap);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("tag_上线失败", e.getMessage().toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                Log.e("tag_上线", string);
            }
        });

    }

    private void getUserInfoMember(String cardNo, String token) {
        Map map = new HashMap();
        map.put("CardNo", cardNo);
        map.put("Token", token);
        OkhttpUtils.doPost(this, PathUrl.MEMBERINFOURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取会员信息", data);
                MySharedPreferences instance = MySharedPreferences.getInstance();
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String statusMsg = jsonObject.getString("StatusMsg");
                    int statusCode = jsonObject.getInt("StatusCode");
                    if (statusCode == 0) {
                        MemberInfoBean info = JSONUtils.toObject(data, MemberInfoBean.class);
                        List<MemberInfoBean.BodyBean> bodyBean = info.getBody();
                        MemberInfoBean.BodyBean body = bodyBean.get(0);
                        instance.setImage(body.getImage());
                        instance.setPostid(body.getPost_id());
                        instance.setDepart(body.getDepart_id());
                        instance.setRegion_id(body.getRegion_id());
                        instance.setRegion_name(body.getRegion_name());
                        instance.setSex(body.getSex());
                        instance.setApp_id(body.getApp_id());
                        instance.setPostName(body.getPost_name());
                        instance.setUserPhone(MySharedPreferences.getInstance().getAccount());
                        instance.setUserPsw(pwdnum);
                        instance.setName(body.getU_name());
                        App.is_exist = body.getIs_exist();
                        App.setAlias(body.getPhone());
                        Log.e("别名", body.getPhone());
                        App.token = MySharedPreferences.getInstance().getToken();         //用户
                        App.cardNo = MySharedPreferences.getInstance().getCardNo();        //卡号
                        App.app_id = MySharedPreferences.getInstance().getApp_id();
                        App.account = MySharedPreferences.getInstance().getAccount();
                        App.is_group = MySharedPreferences.getInstance().getIs_group();      //分组
                        App.depart = MySharedPreferences.getInstance().getDepart();        //部门
                        App.postid = MySharedPreferences.getInstance().getPostid();           //部门id
                        if (!TextUtils.isEmpty(MySharedPreferences.getInstance().getPostName())) {
                            App.postName = MySharedPreferences.getInstance().getPostName();      //部门名称
                        }
                        App.region_id = MySharedPreferences.getInstance().getRegion_id();     //集团id
                        App.sex = MySharedPreferences.getInstance().getSex();           //性别
                        App.name = MySharedPreferences.getInstance().getName();          //姓名
                        if (App.u_start != 2 && App.u_start != 3 && App.u_start != 4) {
                            isShowDaTi(JoininNjjActivity.class);
                        } else {
                            getIsConsent(App.cardNo, "2");//请求是否需要同意协议
                        }
                    } else {
//                        ToastUtil.getInstance().toastCentent(statusMsg);
                        showTiShi(statusMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取会员失败", message);
            }
        });
    }

    public void getUserInfo(String cardNo, String token) {
        Map map = new HashMap();
        map.put("CardNo", cardNo);
        map.put("Token", token);
        OkhttpUtils.doGet(this, PathUrl.GETUSERINFOURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取用户信息", data);
                MySharedPreferences instance = MySharedPreferences.getInstance();
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String statusMsg = jsonObject.getString("StatusMsg");
                    int statusCode = jsonObject.getInt("StatusCode");
                    if (statusCode == 0) {
                        CheckIsBeingBean info = JSONUtils.toObject(data, CheckIsBeingBean.class);
                        BodyBean body = info.getBody();
                        instance.setApp_stage(body.getApp_stage());
                        instance.setDepart(body.getDepart());
                        instance.setImage(body.getImage());
                        instance.setIs_exist(body.getIs_exist());
                        instance.setName(body.getName());
                        instance.setPostid(body.getPost_id());
                        instance.setPostName(body.getPostName());
                        instance.setRegion_id(body.getRegion_id());
                        instance.setRegion_name(body.getRegion_name());
                        instance.setU_start(body.getU_start());
                        instance.setSex(body.getSex());
                        instance.setPassport_photo(body.getPassport_photo());
                        instance.setDepartName(body.getDepartName());
                        instance.setIsResult(body.getIsResult());
                        instance.setUserPhone(body.getAccount());
                        instance.setUserPsw(pwdnum);
                        App.is_exist = body.getIs_exist();
                        App.setAlias(body.getAccount());
                        Log.e("别名", body.getAccount());
                        App.token = MySharedPreferences.getInstance().getToken();         //用户
                        App.is_group = MySharedPreferences.getInstance().getIs_group();      //分组
                        App.cardNo = MySharedPreferences.getInstance().getCardNo();        //卡号
                        App.depart = MySharedPreferences.getInstance().getDepart();        //部门
                        App.postid = MySharedPreferences.getInstance().getPostid();           //部门id
                        if (!TextUtils.isEmpty(MySharedPreferences.getInstance().getPostName())) {
                            App.postName = MySharedPreferences.getInstance().getPostName();      //部门名称
                        }
                        App.name = MySharedPreferences.getInstance().getName();          //姓名
                        App.u_start = MySharedPreferences.getInstance().getU_start();         //
                        App.app_stage = MySharedPreferences.getInstance().getApp_stage();        //
                        App.image = MySharedPreferences.getInstance().getImage();         //头像
                        App.region_id = MySharedPreferences.getInstance().getRegion_id();     //集团id
                        App.region_name = MySharedPreferences.getInstance().getRegion_name();   //集团名称
                        App.passport_photo = MySharedPreferences.getInstance().getPassport_photo();//证件照
                        App.sex = MySharedPreferences.getInstance().getSex();           //性别
                        App.account = MySharedPreferences.getInstance().getAccount();
                        App.app_id = MySharedPreferences.getInstance().getApp_id();
                        if (App.u_start != 2 && App.u_start != 3 && App.u_start != 4) {
                            isShowDaTi(JoininNjjActivity.class);
                        } else {
                            getIsConsent(App.cardNo, "2");//请求是否需要同意协议
                        }
                    } else {
//                        ToastUtil.getInstance().toastCentent(statusMsg);
                        showTiShi(statusMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取用户信息失败", message);
            }
        });
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
                        startActivity(new Intent(LoginActivity.this, EntryJobProtocolActivity.class).putExtra("from", "1"));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
//                    ToastUtil.getInstance().toastCentent("登陆异常");
                    showTiShi("登陆异常");
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
                        Intent intent = new Intent(LoginActivity.this, cls);
                        intent.putExtra("isShow", 1);
                        startActivity(intent);
                        dismissLoading();
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
                            Intent intent = new Intent(LoginActivity.this, cls);
                            if (statusCode == 0) {
                                intent.putExtra("isShow", 0);
                            } else {
                                intent.putExtra("isShow", 1);
                            }
                            startActivity(intent);
                            dismissLoading();
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            ToastUtil.getInstance().toastCentent("登陆异常");
                            showTiShi("登陆异常");
                        }
                    }
                });
            }
        });
    }

    //自定义EditText监听
    private class MyEditListener implements TextWatcher {

        private EditText edittext;

        public MyEditListener(EditText edittext) {
            super();
            this.edittext = edittext;
        }

        @Override
        public void afterTextChanged(Editable arg0) {
            int lengths = arg0.length();
            switch (edittext.getId()) {
                case R.id.ed_phone:
                    phonenum = edPhone.getText().toString();
                    if (lengths == 11) {//11手机号位请求判断
                        getNumberInfo();
                    } else if (lengths == 8) {
                        String strthree = phonenum.substring(0, 3);
                        if (!strthree.equals("WTS")) {
                            int cardnum = 0;
                            try {
                                cardnum = Integer.parseInt(phonenum);
                                if (cardnum < 10000000) {
                                    getNumberInfo();
                                }
                            } catch (Exception e) {
//                                tvPrompt.setText("请输入正确的账号！");
                                showTiShi("请输入正确的账号");
                            }
                        }

                    } else {
//                        tvPrompt.setText("");
                        rlPwd.setVisibility(View.GONE);
                        btnNext.setVisibility(View.GONE);
                        rlVeritycode.setVisibility(View.GONE);
                        llToforget.setVisibility(View.GONE);
                    }
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }
    }

    public void getNumberInfo() {
        Map map = new HashMap();
        map.put("Phone", edPhone.getText().toString());
        map.put("AppId", "9");
        OkhttpUtils.doGet(this, PathUrl.NUMBERINFOURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取账号信息", data);
                VPhoneBean info = JSONUtils.toObject(data, VPhoneBean.class);
                Log.e("tag_获取账号信息", info.getStatusCode() + "");
                switch (info.getStatusCode()) {
                    case 0://用户存在
//                        phone = info.getBody().getPhone();
                        btnNext.setText("登录");
                        logintype = 1;
                        rlPwd.setVisibility(View.VISIBLE);
                        btnNext.setVisibility(View.VISIBLE);
                        llToforget.setVisibility(View.VISIBLE);
                        break;
                    case 1://用户不存在,获取验证码注册
                        logintype = 0;
                        rlPwd.setVisibility(View.VISIBLE);
                        btnNext.setVisibility(View.VISIBLE);
                        rlVeritycode.setVisibility(View.VISIBLE);
                        break;
                    default:
//                        tvPrompt.setText(info.getStatusMsg());
                        showTiShi(info.getStatusMsg());
                        break;
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取账号信息失败", message);
//                tvPrompt.setText("获取账号信息失败");
                showTiShi("获取账号信息失败");
            }
        });
    }

}
