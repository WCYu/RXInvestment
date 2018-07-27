package com.rxjy.rxinvestment.activity.home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.base.PathUrl;
import com.rxjy.rxinvestment.entity.HuanYingBean;
import com.rxjy.rxinvestment.entity.IconInfo;
import com.rxjy.rxinvestment.entity.PersonBean;
import com.rxjy.rxinvestment.entity.PersonDataBean;
import com.rxjy.rxinvestment.fragment.administration.HomeFragment;
import com.rxjy.rxinvestment.fragment.more.FindFragment;
import com.rxjy.rxinvestment.fragment.more.JoininMoreFragment;
import com.rxjy.rxinvestment.fragment.more.MainFragment;
import com.rxjy.rxinvestment.fragment.more.OnTrialFragment;
import com.rxjy.rxinvestment.utils.AutoUtils;
import com.rxjy.rxinvestment.utils.CheckPermissionsUitl;
import com.rxjy.rxinvestment.utils.JSONUtils;
import com.rxjy.rxinvestment.utils.MySharedPreferences;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
import com.rxjy.rxinvestment.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

import static com.rxjy.rxinvestment.receiver.MessageReceiver.MSG_NUM;
import static com.rxjy.rxinvestment.receiver.MessageReceiver.msgnum;


/**
 * Created by asus on 2018/5/21.
 */

public class JoininNjjActivity extends BaseActivity {

    @Bind(R.id.fl_main)
    FrameLayout flMain;
    @Bind(R.id.iv_tab_home)
    ImageView ivTabHome;
    @Bind(R.id.tv_tab_home)
    TextView tvTabHome;
    @Bind(R.id.rl_tab_home)
    RelativeLayout rlTabHome;
    @Bind(R.id.iv_tab_wallet)
    ImageView ivTabWallet;
    @Bind(R.id.tv_tab_wallet)
    TextView tvTabWallet;
    @Bind(R.id.rl_tab_wallet)
    RelativeLayout rlTabWallet;
    @Bind(R.id.iv_tab_find)
    ImageView ivTabFind;
    @Bind(R.id.tv_tab_find)
    TextView tvTabFind;
    @Bind(R.id.rl_tab_find)
    RelativeLayout rlTabFind;
    @Bind(R.id.iv_tab_mine)
    ImageView ivTabMine;
    @Bind(R.id.tv_tab_mine)
    TextView tvTabMine;
    @Bind(R.id.ll_view)
    LinearLayout llView;
    @Bind(R.id.img_reddot)
    public ImageView img_reddot;
    @Bind(R.id.rl_tab_mine)
    RelativeLayout rlTabMine;
    @Bind(R.id.tv_msgnum)
    TextView tvMsgnum;

    private static boolean isExit = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    private Fragment currentFragment;
    //指定Fragment的坐标
    private final int HOME_FRAGMENT = 0;
    private final int WALLET_FRAGMENT = 1;
    private final int FIND_FRAGMENT = 2;
    private final int MINE_FRAGMENT = 3;
    //Tab图标的集合
    private List<IconInfo> iconList;
    //碎片的集合
    private List<Fragment> fragmentList;
    private OnTrialFragment onTrialFragment;
    private HomeFragment homeFragment;
    private JoininMoreFragment walletFragment;
    private FindFragment findFragment;
    private MainFragment mineFragment;
    private AlertDialog alertDialog;
    private int[] iconNormal = new int[]{
            R.mipmap.ic_tab_hostpage,
            R.mipmap.ic_tab_more,
            R.mipmap.ic_tab_find,
            R.mipmap.ic_tab_main
    };
    private int[] iconPressed = new int[]{
            R.drawable.ic_tab_hostpagered,
            R.drawable.ic_tab_morered,
            R.drawable.ic_tab_findred,
            R.drawable.ic_tab_mainred
    };

    int type = 0;

    public static JoininNjjActivity activity;
    //检测权限列表
    private String[] requestPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE
    };
    private TextView text;
    private TextView msgtwo;
    private MySharedPreferences instance;

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_njj;
    }

    @Override
    public void intData() {
        activity = this;
        instance = MySharedPreferences.getInstance();
        isShow();
        initIcon();
        initFragment();
        //加载默认显示碎片
        showFragment(fragmentList.get(HOME_FRAGMENT), HOME_FRAGMENT);
        SharedPreferences preferences = getSharedPreferences(MSG_NUM, MODE_PRIVATE);
        int num = preferences.getInt(msgnum, 0);
        if (num > 0) {
            ShowMsgNum(num);
        }
    }

    public void ShowMsgNum(int num) {
        if (num > 0) {
            tvMsgnum.setText(num + "");
            tvMsgnum.setVisibility(View.VISIBLE);
        } else {
            tvMsgnum.setVisibility(View.GONE);
        }
    }

    @Override
    public void initAdapter() {

    }

    private void initIcon() {
        //初始化iconList数据集合
        iconList = new ArrayList<>();
        //将图标添加到集合中
        iconList.add(new IconInfo(ivTabHome, tvTabHome));
        iconList.add(new IconInfo(ivTabWallet, tvTabWallet));
        iconList.add(new IconInfo(ivTabFind, tvTabFind));
        iconList.add(new IconInfo(ivTabMine, tvTabMine));

    }

    @Override
    protected void onResume() {
        super.onResume();
        //权限检测
        CheckPermissionsUitl.checkPermissions(this, requestPermissions);
        Log.e("App.is", App.is_exist + "");

        type = 0;
        if (!TextUtils.isEmpty(App.is_group)) {
            if (App.is_group.equals("2")) {
                if (App.is_exist == 0) {
                    getHuanYing(App.account);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.ActionSheetDialogStyle);
                    View inflate = getLayoutInflater().inflate(R.layout.home_dialog, null);
                    AutoUtils.setSize(this, false, 720, 1280);
                    AutoUtils.auto(inflate);
                    ImageView image = (ImageView) inflate.findViewById(R.id.delete_image);
                    text = (TextView) inflate.findViewById(R.id.dialog_title);
                    msgtwo = (TextView) inflate.findViewById(R.id.dialog_title_two);
                    ImageView view = (ImageView) inflate.findViewById(R.id.home_image);
                    dialog.setView(inflate);

                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            type = 1;
                            startActivity(new Intent(JoininNjjActivity.this, AnswerActivity.class));
                            alertDialog.dismiss();

                        }
                    });
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            type = 1;
                            alertDialog.dismiss();
                            startActivity(new Intent(JoininNjjActivity.this, AnswerActivity.class));

                        }
                    });
                    alertDialog = dialog.create();

                    alertDialog.show();
                    alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (type == 0) {
                                startActivity(new Intent(JoininNjjActivity.this, AnswerActivity.class));
                            } else {

                            }
                        }
                    });
                }
            }
        }
    }

    private void getHuanYing(String account) {
        Map map = new HashMap();
        map.put("card", account);
        OkhttpUtils.doGet(this, PathUrl.HUANYINGDATAURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取欢迎信息", data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String statusMsg = jsonObject.getString("StatusMsg");
                    int statusCode = jsonObject.getInt("StatusCode");
                    if (statusCode == 0) {
                        HuanYingBean info = JSONUtils.toObject(data, HuanYingBean.class);
                        HuanYingBean.BodyBean body = info.getBody();
                        if (body != null) {
                            Log.e("tag", body.getWelcomes() + "");
                            String[] split = body.getWelcomes().split("，");
                            String substring = split[0].substring(0, 2);
                            String substring1 = split[0].substring(2, split[0].length());
                            String s1 = "<font color='#696969'>" + substring + "</font>" + "<font color='#FF0000'>" + substring1 + "</font>";
                            text.setText(Html.fromHtml(s1));
                            msgtwo.setText(split[1]);
                        }
                    } else {
                        ToastUtil.getInstance().toastCentent(statusMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取欢迎失败", message);
            }
        });
    }

    private void initFragment() {
        //初始化碎片
        if (onTrialFragment == null) {
            onTrialFragment = new OnTrialFragment();
        }
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        if (walletFragment == null) {
            walletFragment = new JoininMoreFragment();
        }
        if (findFragment == null) {
            findFragment = new FindFragment();
        }
        if (mineFragment == null) {
            mineFragment = new MainFragment();
        }

        fragmentList = new ArrayList<>();
        //将碎片添加到集合中
        Log.e("日志", App.postName);
        switch (App.postName) {
            case "行政经理":
                fragmentList.add(homeFragment);
                break;
            case "投资招商":
                fragmentList.add(onTrialFragment);
                break;
            default:
                fragmentList.add(homeFragment);
                break;
        }
        fragmentList.add(walletFragment);
        fragmentList.add(findFragment);
        fragmentList.add(mineFragment);
    }

    //显示指定Fragment界面的方法
    private void showFragment(Fragment fragment, int position) {
        JumpFragment(fragment);
        setIcon(position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        type = 0;
    }

    //加载指定Fragment的方法
    private void JumpFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction action = manager.beginTransaction();
        hideFragment(action);
        if (!fragment.isAdded()) {
            action.add(R.id.fl_main, fragment);
        }
//        if (currentFragment != null) {
//            action.hide(currentFragment);
//        }
        action.show(fragment);
        action.commit();
//        currentFragment = fragment;
    }

    private void hideFragment(FragmentTransaction ft) {
        // 如果不为空，就先隐藏起来
        if (onTrialFragment != null) {
            ft.hide(onTrialFragment);
        }
        if (homeFragment != null) {
            ft.hide(homeFragment);
        }
        if (walletFragment != null) {
            ft.hide(walletFragment);
        }

        if (findFragment != null) {
            ft.hide(findFragment);
        }

        if (mineFragment != null) {
            ft.hide(mineFragment);
        }
    }

    //设置图标点击效果
    private void setIcon(int position) {

        for (int i = 0; i < iconList.size(); i++) {
            iconList.get(i).getImageView().setImageResource(iconNormal[i]);
            iconList.get(i).getTextView().setTextColor(this.getResources().getColor(R.color.colorGrayDark));
        }

        iconList.get(position).getImageView().setImageResource(iconPressed[position]);
        iconList.get(position).getTextView().setTextColor(this.getResources().getColor(R.color.colorRedLight));
    }

    @OnClick({R.id.rl_tab_home, R.id.rl_tab_wallet, R.id.rl_tab_find, R.id.rl_tab_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_tab_home:
                showFragment(fragmentList.get(HOME_FRAGMENT), HOME_FRAGMENT);
                break;
            case R.id.rl_tab_wallet:
                showFragment(fragmentList.get(WALLET_FRAGMENT), WALLET_FRAGMENT);
                break;
            case R.id.rl_tab_find:
                showFragment(fragmentList.get(FIND_FRAGMENT), FIND_FRAGMENT);
                break;
            case R.id.rl_tab_mine:
                SharedPreferences preferences = getSharedPreferences(MSG_NUM, MODE_PRIVATE);
                preferences.edit().putInt(msgnum, 0).commit();
                ShowMsgNum(0);
                showFragment(fragmentList.get(MINE_FRAGMENT), MINE_FRAGMENT);
                break;
        }
    }

    public void isShow() {

        Map map = new HashMap();
        map.put("cardno", App.cardNo);
        map.put("appId", App.app_id);

        OkhttpUtils.doGet(this, PathUrl.ISSHOWREDDOTURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取是否有新文章", data);
                PersonBean info = JSONUtils.toObject(data, PersonBean.class);
                PersonDataBean body = info.getBody();
                int statusCode = info.getStatusCode();
                String statusMsg = info.getStatusMsg();
                if (statusCode == 0) {
                    img_reddot.setVisibility(View.VISIBLE);
                } else {
                    img_reddot.setVisibility(View.GONE);
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取是否有新文章失败", message);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            ToastUtil.getInstance().toastCentent("再按一次退出程序", JoininNjjActivity.this);
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
//            System.exit(0);
        }
    }

}
