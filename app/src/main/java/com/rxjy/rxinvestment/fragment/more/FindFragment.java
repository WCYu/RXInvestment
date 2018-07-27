package com.rxjy.rxinvestment.fragment.more;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.activity.find.FindListActivity;
import com.rxjy.rxinvestment.activity.find.PublishActivity;
import com.rxjy.rxinvestment.activity.home.JoininNjjActivity;
import com.rxjy.rxinvestment.adapter.find.FindListAdapter;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.base.PathUrl;
import com.rxjy.rxinvestment.entity.FindBean;
import com.rxjy.rxinvestment.entity.FindBodyBean;
import com.rxjy.rxinvestment.entity.FindDataBean;
import com.rxjy.rxinvestment.entity.ManagementBean;
import com.rxjy.rxinvestment.fragment.utils.BaseFragment;
import com.rxjy.rxinvestment.utils.JSONUtils;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
import com.rxjy.rxinvestment.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hjh on 2017/11/6.
 */
public class FindFragment extends BaseFragment {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.publish)
    ImageView publish;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.ly_background)
    LinearLayout lyBackground;
    ViewPager vpFindbanner;
    ImageView ivCircleone;
    ImageView ivCircletwo;
    ImageView ivCirclethree;
    RelativeLayout backLayout;
    @Bind(R.id.lv_finddata)
    ListView lvFinddata;
    @Bind(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;
    private PagerAdapter pagerAdapter;
    private ScrollView scrollView;
    int pageIndex = 1;//页数
    int pageSize = 10;//每页条数
    int ispulldown = 0;//是否刷新  1：是
    int isload = 0;//1:不可加载
    int index = 0;
    int size = 0;

    @Override
    public int getInitId() {
        return R.layout.fragment_find;
    }

    @Override
    public void initView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void initData() {
        showLoading();
        if (App.postName.equals("投资招商")) {
            JoininNjjActivity activity = (JoininNjjActivity) getActivity();
            activity.img_reddot.setVisibility(View.GONE);
        }
        publish.setBackgroundResource(R.mipmap.write);
        tvTitle.setText("发现");
        imgBack.setVisibility(View.GONE);
        getUserType();
        requestDate(App.cardNo);
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.item_banner, null);
        vpFindbanner = (ViewPager) inflate.findViewById(R.id.vp_findbanner);
        ivCircleone = (ImageView) inflate.findViewById(R.id.iv_circleone);
        ivCircletwo = (ImageView) inflate.findViewById(R.id.iv_circletwo);
        ivCirclethree = (ImageView) inflate.findViewById(R.id.iv_circlethree);
        backLayout = (RelativeLayout) inflate.findViewById(R.id.back_layout);
        lvFinddata.addHeaderView(inflate);
    }

    private void requestDate(final String cardno) {
        getFindList(cardno, pageIndex, pageSize);
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh();
                pageIndex = 1;
                pageSize = 10;
                ispulldown = 1;
                getFindList(cardno, pageIndex, pageSize);
            }
        });
        smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore();
                if (isload == 1) {
                    Toast.makeText(getActivity(), "没有更多数据啦！", Toast.LENGTH_SHORT).show();
                }
                pageIndex = pageIndex + 1;
                getFindListLoadmore(cardno, pageIndex, pageSize);
            }
        });
    }

    private void getFindListLoadmore(String cardno, int pageIndex, int pageSize) {
        Map map = new HashMap();
        map.put("cardNo", cardno);
        map.put("page", pageIndex);
        map.put("rows", pageSize);
        Log.e("参数", cardno + " " + pageIndex + " " + pageIndex + " ");
        OkhttpUtils.doGet(getActivity(), PathUrl.FINDLISTURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取发现信息", data);
                FindBean info = JSONUtils.toObject(data, FindBean.class);
                FindBodyBean body = info.getBody();
                int statusCode = info.getStatusCode();
                String statusMsg = info.getStatusMsg();
                if (statusCode == 0) {
                    if (body.getList() != null && body.getList().size() < 10) {
                        isload = 1;
                    }
                    showlist.addAll(body.getList());
                    findListAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.getInstance().toastCentent(statusMsg);
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取发现失败", message);
            }
        });
    }

    private void getFindList(String cardno, int pageIndex, int pageSize) {
        Map map = new HashMap();
        map.put("cardNo", cardno);
        map.put("appId", App.app_id);
        map.put("page", pageIndex);
        map.put("rows", pageSize);
        Log.e("参数", cardno + " " + pageIndex + " " + pageIndex + " ");
        OkhttpUtils.doGet(getActivity(), PathUrl.FINDLISTURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取发现信息", data);
                FindBean info = JSONUtils.toObject(data, FindBean.class);
                FindBodyBean body = info.getBody();
                int statusCode = info.getStatusCode();
                String statusMsg = info.getStatusMsg();
                if (statusCode == 0) {
                    if (body.getTopList() != null) {
                        size = body.getTopList().size();
                    }
                    if (ispulldown == 1) {
                        ispulldown = 0;
                    }
                    ShowBanner(body.getTopList());
                    showlist = new ArrayList<>();
                    showlist.addAll(body.getList());
                    ShowList(showlist);
                } else {
                    ToastUtil.getInstance().toastCentent(statusMsg);
                }
                dismissLoading();
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取发现失败", message);
                dismissLoading();
                ToastUtil.getInstance().toastCentent("获取信息失败");
            }
        });
        setProgressDialog(3000);
    }

    /**
     * 显示列表数据
     */
    FindListAdapter findListAdapter;
    ArrayList<FindDataBean> showlist;

    private void ShowList(final ArrayList<FindDataBean> list) {
        findListAdapter = new FindListAdapter(getActivity(), list);
        lvFinddata.setAdapter(findListAdapter);
        lvFinddata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String title = "";
                switch (list.get(i).getType()) {
                    case 0:
                        title = list.get(i).getName();
                        break;
                    case 1:
                        title = "岗前答题";
                        break;
                    case 2:
                        title = "日常培训";
                        break;
                    case 3:
                        title = list.get(i).getName();
                        break;
                }
                startActivity(new Intent(getActivity(), FindListActivity.class).putExtra("id", list.get(i - 1).getId() + "").putExtra("title", title).putExtra("type", list.get(i - 1).getType() + "").putExtra("courseId", list.get(i).getCourseId() + ""));
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
    private void ShowBanner(final ArrayList<FindDataBean> datalist) {

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
                tv_bannerdescribe.setText(datalist.get(position).getName());
                iv_bannerimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), FindListActivity.class).putExtra("id", datalist.get(position).getId() + "").putExtra("title", datalist.get(position).getName()).putExtra("type", "0"));
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

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.publish)
    public void onViewClicked() {
        startActivity(new Intent(getContext(), PublishActivity.class));
    }

    //获取是否是管理层
    public void getUserType() {
        Map map = new HashMap();
        map.put("CardNo", App.cardNo);
        OkhttpUtils.doGet(getActivity(), PathUrl.USERTYPEURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取用户类型", data);
                ManagementBean info = JSONUtils.toObject(data, ManagementBean.class);
                ManagementBean.BodyBean body = info.getBody();
                int statusCode = info.getStatusCode();
                String statusMsg = info.getStatusMsg();
                if (statusCode == 0) {
                    int isManage = body.getIsManage();
                    if (isManage == 1) {
                        publish.setVisibility(View.VISIBLE);
                    } else {
                        publish.setVisibility(View.GONE);
                    }
                } else {
//                    ToastUtil.getInstance().toastCentent(statusMsg);
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取失败", message);
            }
        });

    }
}