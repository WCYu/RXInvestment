package com.rxjy.rxinvestment.fragment.administration;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.acker.simplezxing.activity.CaptureActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.activity.home.BannerDetailsActivity;
import com.rxjy.rxinvestment.activity.home.EnvironmentActivity;
import com.rxjy.rxinvestment.activity.home.FigureActivity;
import com.rxjy.rxinvestment.activity.home.QrLoginActivity;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.base.PathUrl;
import com.rxjy.rxinvestment.entity.BannerBean;
import com.rxjy.rxinvestment.entity.BannerDataBean;
import com.rxjy.rxinvestment.entity.QRresultWebBean;
import com.rxjy.rxinvestment.fragment.utils.BaseFragment;
import com.rxjy.rxinvestment.utils.GlideRoundTransform;
import com.rxjy.rxinvestment.utils.JSONUtils;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
import com.rxjy.rxinvestment.utils.StringUtils;
import com.rxjy.rxinvestment.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class HomeFragment extends BaseFragment {

    @Bind(R.id.home_view)
    WebView homeView;
    @Bind(R.id.ly_web)
    LinearLayout lyWeb;
    @Bind(R.id.vp_banner)
    ViewPager vpBanner;
    @Bind(R.id.rl_envir)
    RelativeLayout rlEnvir;
    @Bind(R.id.rl_xingxiang)
    RelativeLayout rlXingxiang;
    @Bind(R.id.lv_task)
    ListView lvTask;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_scoresone)
    TextView tvScoresone;
    @Bind(R.id.tv_dayone)
    TextView tvDayone;
    @Bind(R.id.ll_dayone)
    LinearLayout llDayone;
    @Bind(R.id.tv_scorestwo)
    TextView tvScorestwo;
    @Bind(R.id.tv_daytwo)
    TextView tvDaytwo;
    @Bind(R.id.ll_daytwo)
    LinearLayout llDaytwo;
    @Bind(R.id.tv_scoresthree)
    TextView tvScoresthree;
    @Bind(R.id.tv_daythree)
    TextView tvDaythree;
    @Bind(R.id.ll_daythree)
    LinearLayout llDaythree;
    @Bind(R.id.iv_tryimg)
    ImageView ivTryimg;
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
    private PagerAdapter pagerAdapter;

    int index = 0;
    int size = 0;

    @Override
    public int getInitId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void initData() {
        imgBack.setVisibility(View.GONE);
        getBannerList(App.cardNo);
        publish.setVisibility(View.VISIBLE);
        publish.setBackgroundResource(R.mipmap.scan_pc);
        tvTitle.setText("首页");
        lyBackground.setBackgroundColor(getResources().getColor(R.color.transparent));
        rlEnvir.setVisibility(View.VISIBLE);
        rlXingxiang.setVisibility(View.VISIBLE);
        lvTask.setVisibility(View.GONE);
    }

    private void getBannerList(String cardNo) {
        Map map = new HashMap();
        map.put("a_ccount", cardNo);
        OkhttpUtils.doGet(getActivity(), PathUrl.HOMEBANNERURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取首页Banner信息", data);
                BannerBean info = JSONUtils.toObject(data, BannerBean.class);
                ArrayList<BannerDataBean> body = info.getBody();
                int statusCode = info.getStatusCode();
                String statusMsg = info.getStatusMsg();
                size = info.getBody().size();
                if (statusCode == 0) {
                    ShowBanner(body);
                } else {
                    ToastUtil.getInstance().toastCentent(statusMsg);
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取首页Banner失败", message);
            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 4) {
                if (index >= size) index = 0;
                if (vpBanner != null) {
                    vpBanner.setCurrentItem(index++);
                    handler.sendEmptyMessageDelayed(4, 3000);
                } else {
                    handler.removeMessages(4);
                }
            }
        }

    };

    private void ShowBanner(final ArrayList<BannerDataBean> datalist) {

        pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return datalist.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                View view = View.inflate(getActivity(), R.layout.vp_bannerview, null);
                ImageView iv_bannerimg = (ImageView) view.findViewById(R.id.iv_bannerimg);
                TextView tv_bannerdescribe = (TextView) view.findViewById(R.id.tv_bannerdescribe);
                TextView tv_bannertodetails = (TextView) view.findViewById(R.id.tv_bannertodetails);
                RequestOptions options = new RequestOptions();
                options.centerCrop().transform(new GlideRoundTransform(getActivity(), 10));
                Glide.with(getActivity()).load(datalist.get(position).getBanner_img()).apply(options).into(iv_bannerimg);
                tv_bannerdescribe.setText(datalist.get(position).getBanner_title());
                tv_bannertodetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), BannerDetailsActivity.class).putExtra("url", datalist.get(position).getBanner_content()));
                    }
                });
                iv_bannerimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), BannerDetailsActivity.class).putExtra("url", datalist.get(position).getBanner_content()));
                    }
                });
                container.addView(view);
                return view;
            }
        };
        vpBanner.setAdapter(pagerAdapter);
        vpBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        handler.sendEmptyMessageDelayed(4, 3000);
    }

    @Override
    public void initAdapter() {

    }

    @Override
    public void initLinstener() {

    }

    @OnClick({R.id.publish, R.id.rl_envir, R.id.rl_xingxiang})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.publish://扫一扫
                QRCodeScan();
                break;
            case R.id.rl_envir:
                startActivity(new Intent(getActivity(), EnvironmentActivity.class));
                break;
            case R.id.rl_xingxiang:
                startActivity(new Intent(getActivity(), FigureActivity.class));
                break;
        }
    }

    /**
     * 扫描二维码
     */
    private static final int REQ_CODE_PERMISSION = 0x1111;

    private void QRCodeScan() {//6.0以上的手机需要处理权限
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Do not have the permission of camera, request it.
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQ_CODE_PERMISSION);
        } else {
            // Have gotten the permission
            startActivityForResult(new Intent(getActivity(), CaptureActivity.class), CaptureActivity.REQ_CODE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CaptureActivity.REQ_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        Log.e("扫码获取的信息RESULT_OK", data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        if (data != null) {
                            try {
                                String result = data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT);
                                if (!StringUtils.isEmpty(result)) {
                                    if (result.contains("event")) {
                                        Log.e("event", data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                                        QRresultWebBean info = JSONUtils.toObject(result, QRresultWebBean.class);
                                        String biaoshi = info.getParameter().getLogin_id();
                                        if (biaoshi != null || info.getParameter().getApp_id() == 3 || info.getParameter().getApp_id() == 9) {
                                            Log.e("跳转登陆", data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                                            startActivity(new Intent(getActivity(), QrLoginActivity.class).putExtra("appid", biaoshi));
                                        } else {
                                            Log.e("登陆失败", data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                                        }
                                    } else {
                                        ToastUtil.getInstance().toastCentent("本平台暂不支持其他二维码扫描！");
                                    }

                                } else {
                                    ToastUtil.getInstance().toastCentent("本平台暂不支持其他二维码扫描！");
                                }
                            } catch (Exception e) {
                                ToastUtil.getInstance().toastCentent("本平台暂不支持其他二维码扫描！");
                                e.printStackTrace();
                            }

                        }
                        break;
                    case RESULT_CANCELED:
                        if (data != null) {
                            Log.e("扫码获取的信息RESULT_CANCELED", data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        }
                        break;
                }
                break;
        }
    }

}
