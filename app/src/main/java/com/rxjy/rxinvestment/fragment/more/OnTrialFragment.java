package com.rxjy.rxinvestment.fragment.more;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.acker.simplezxing.activity.CaptureActivity;
import com.bumptech.glide.Glide;
import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.activity.home.QrLoginActivity;
import com.rxjy.rxinvestment.activity.utils.WebViewActivity;
import com.rxjy.rxinvestment.adapter.home.HomeShejiAdapter;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.base.PathUrl;
import com.rxjy.rxinvestment.entity.HomeBean;
import com.rxjy.rxinvestment.entity.QRresultWebBean;
import com.rxjy.rxinvestment.fragment.utils.BaseFragment;
import com.rxjy.rxinvestment.utils.JSONUtils;
import com.rxjy.rxinvestment.utils.MySharedPreferences;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
import com.rxjy.rxinvestment.utils.StringUtils;
import com.rxjy.rxinvestment.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by asus on 2018/5/23.
 */

public class OnTrialFragment extends BaseFragment {

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
    @Bind(R.id.home_view)
    WebView homeView;
    @Bind(R.id.ly_web)
    LinearLayout lyWeb;
    ViewPager vpFindbanner;
    ImageView ivCircleone;
    ImageView ivCircletwo;
    ImageView ivCirclethree;
    RelativeLayout backLayout;
    @Bind(R.id.lv_finddata)
    ListView lvFinddata;
    @Bind(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;

    private HomeShejiAdapter findListAdapter;

    int index = 0;
    int size = 0;
    private PagerAdapter pagerAdapter;

    @Override
    public int getInitId() {
        return R.layout.activity_home_shiji;
    }

    @Override
    public void initView(View view) {
        ButterKnife.bind(this, view);
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.item_home_header, null);
        vpFindbanner = (ViewPager) inflate.findViewById(R.id.vp_findbanner);
        ivCircleone = (ImageView) inflate.findViewById(R.id.iv_circleone);
        ivCircletwo = (ImageView) inflate.findViewById(R.id.iv_circletwo);
        ivCirclethree = (ImageView) inflate.findViewById(R.id.iv_circlethree);
        backLayout = (RelativeLayout) inflate.findViewById(R.id.back_layout);
        lvFinddata.addHeaderView(inflate);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void initData() {
        getFindList();
        tvTitle.setText("首页");
        imgBack.setVisibility(View.GONE);
        publish.setVisibility(View.VISIBLE);
        publish.setBackgroundResource(R.mipmap.scan_pc);
    }

    private void getFindList() {
        Map map = new HashMap();
        map.put("card", App.cardNo);
        OkhttpUtils.doGet(getActivity(), PathUrl.GOMEDATAURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取首页信息", data);
                HomeBean info = JSONUtils.toObject(data, HomeBean.class);
                HomeBean.BodyBean body = info.getBody();
                int statusCode = info.getStatusCode();
                String statusMsg = info.getStatusMsg();
                if (statusCode == 0) {
                    ShowList(body.getList());
                    size = body.getTopList().size();
                    ShowBanner(body.getTopList());
                } else {
                    ToastUtil.getInstance().toastCentent(statusMsg);
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取首页失败", message);
            }
        });

    }

    private void ShowList(final List<HomeBean.BodyBean.ListBean> list) {
        findListAdapter = new HomeShejiAdapter(getActivity(), list);
        lvFinddata.setAdapter(findListAdapter);
        lvFinddata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int id = list.get(i - 1).getId();
                startActivity(new Intent(getContext(), WebViewActivity.class).putExtra("url", "http://j.rxjy.com/Mobile/News/Index?id=" + id + "&cardno=" + App.cardNo).putExtra("title", "详情"));
            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 5) {
                if (index >= size) index = 0;
                if (vpFindbanner != null) {
                    vpFindbanner.setCurrentItem(index++);
                    handler.sendEmptyMessageDelayed(5, 3000);
                } else {
                    handler.removeMessages(5);
                }
            }
        }
    };

    /**
     * 显示顶部数据
     *
     * @param datalist
     */
    private void ShowBanner(final List<HomeBean.BodyBean.TopListBean> datalist) {
        if (datalist.size() == 0) {
            backLayout.setVisibility(View.INVISIBLE);
        }
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
                View view = View.inflate(getActivity(), R.layout.vp_bannerfind, null);
                ImageView iv_bannerimg = (ImageView) view.findViewById(R.id.iv_bannerimg);
                TextView tv_bannerdescribe = (TextView) view.findViewById(R.id.tv_bannerdescribe);
                Glide.with(getActivity()).load(datalist.get(position).getCover()).into(iv_bannerimg);
                tv_bannerdescribe.setText(datalist.get(position).getTitle());
                iv_bannerimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getContext(), WebViewActivity.class).putExtra("url", "http://j.rxjy.com/Mobile/News/Index?id=" + datalist.get(position).getId() + "&cardno=" + App.cardNo).putExtra("title", "详情"));
                    }
                });
                container.addView(view);
                return view;
            }
        };
        vpFindbanner.setAdapter(pagerAdapter);
        vpFindbanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        ivCircleone.setImageResource(R.drawable.dotwhite);
                        ivCircletwo.setImageResource(R.drawable.dotgrey);
                        ivCirclethree.setImageResource(R.drawable.dotgrey);
                        break;
                    case 1:
                        ivCircleone.setImageResource(R.drawable.dotgrey);
                        ivCircletwo.setImageResource(R.drawable.dotwhite);
                        ivCirclethree.setImageResource(R.drawable.dotgrey);
                        break;
                    case 2:
                        ivCircleone.setImageResource(R.drawable.dotgrey);
                        ivCircletwo.setImageResource(R.drawable.dotgrey);
                        ivCirclethree.setImageResource(R.drawable.dotwhite);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        handler.sendEmptyMessageDelayed(5, 3000);
    }

    @Override
    public void initAdapter() {

    }

    @Override
    public void initLinstener() {
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh();
                getFindList();
            }
        });
        smartRefresh.setEnableLoadMore(false);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRCodeScan();
            }
        });
    }

    private static final int REQ_CODE_PERMISSION = 0x1111;

    private void QRCodeScan() {//6.0以上的手机需要处理权限
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQ_CODE_PERMISSION);
        } else {
            startActivityForResult(new Intent(getActivity(), CaptureActivity.class), CaptureActivity.REQ_CODE);
        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
