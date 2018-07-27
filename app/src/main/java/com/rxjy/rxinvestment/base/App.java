package com.rxjy.rxinvestment.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.rxjy.rxinvestment.utils.Constants;
import com.rxjy.rxinvestment.utils.MySharedPreferences;
import com.rxjy.rxinvestment.utils.NetWorkUtils;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
import com.rxjy.rxinvestment.utils.StringUtils;
import com.rxjy.rxinvestment.utils.ZJson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 阿禹 on 2018/6/25.
 */

public class App extends Application {
    private static final String TAG = "JPushapp";
    public static Context context;
    public static BaseActivity baseActivity;
    public static App app;
    public static SharedPreferences sp;

    public static String app_id;         //用户
    public static String token;         //用户
    public static String is_group;      //分组
    public static String cardNo;        //卡号
    public static int depart;        //部门
    public static int postid;           //部门id
    public static String postName = "未知";      //职务名称
    public static String name;          //姓名
    public static int u_start;          //
    public static int app_stage;        //
    public static String image;         //头像
    public static int region_id;     //集团id
    public static String region_name;   //集团名称
    public static String passport_photo;//证件照
    public static String sex;           //性别
    public static int is_exist = 1;         //已同意协议（温特斯会员）
    public static String account;       //手机号/账号

    public int count = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        JPushInterface.setDebugMode(false);//测试
        JPushInterface.init(this);
        sp = getSharedPreferences(Constants.IS_SETALIAS, MODE_PRIVATE);
        app_id = MySharedPreferences.getInstance(this).getApp_id();
        token = MySharedPreferences.getInstance(this).getToken();         //用户
        is_group = MySharedPreferences.getInstance(this).getIs_group();      //分组
        cardNo = MySharedPreferences.getInstance(this).getCardNo();        //卡号
        depart = MySharedPreferences.getInstance(this).getDepart();        //部门
        postid = MySharedPreferences.getInstance(this).getPostid();           //部门id
        postName = MySharedPreferences.getInstance(this).getPostName();      //部门名称
        name = MySharedPreferences.getInstance(this).getName();          //姓名
        u_start = MySharedPreferences.getInstance(this).getU_start();         //
        app_stage = MySharedPreferences.getInstance(this).getApp_stage();        //
        image = MySharedPreferences.getInstance(this).getImage();         //头像
        region_id = MySharedPreferences.getInstance(this).getRegion_id();     //集团id
        region_name = MySharedPreferences.getInstance(this).getRegion_name();   //集团名称
        passport_photo = MySharedPreferences.getInstance(this).getPassport_photo();//证件照
        sex = MySharedPreferences.getInstance(this).getSex();           //性别
        account = MySharedPreferences.getInstance(this).getAccount();

        appGoOnLine();
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

    private void appGoOnLine() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityStopped(Activity activity) {
                Log.e("viclee", activity + "onActivityStopped");
                count--;
                String cardNo = MySharedPreferences.getInstance().getCardNo();
                if (count == 0) {
                    appDownLine(cardNo);
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.e("viclee", activity + "onActivityStarted");
                String cardNo = MySharedPreferences.getInstance().getCardNo();
                if (count == 0) {
                    if (TextUtils.isEmpty(cardNo)) {

                    } else {
                        Log.e("lrj", cardNo + "1314");
                        startLogin(cardNo);
                    }
                }
                count++;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.e("viclee", activity + "onActivitySaveInstanceState");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.e("viclee", activity + "onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.e("viclee", activity + "onActivityPaused");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.e("viclee", activity + "onActivityDestroyed");
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.e("viclee", activity + "onActivityCreated");
            }
        });
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

    public static App getApp() {
        return app;
    }

    public static Context getContext() {
        return getApp().getApplicationContext();
    }

    //获取版本号
    public static String getVersionCode() {
        try {
            PackageManager manager = getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getContext().getPackageName(), 0);
            return String.valueOf(info.versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //获取版本名称
    public static String getVersionName() {
        try {
            PackageManager manager = getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getContext().getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String jpushname = "IsSetAlias" + App.cardNo;

    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    public static void setAlias(String alias) {
        Log.e("设置别名，，，", "11111111");
        String isset = sp.getString(jpushname, null);
        if (!StringUtils.isEmpty(isset) && isset.equals("1")) {
        } else {
            // 调用 Handler 来异步设置别名
            if (!StringUtils.isEmpty(alias)) {
                mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
            } else {
                Log.e("别名为空", "dsasda");
            }
        }
    }

    public static TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    sp.edit().putString(jpushname, "1").commit();
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
        }
    };

    private static final int MSG_SET_ALIAS = 1001;
    public static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(context,
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };
}
