package com.rxjy.rxinvestment.activity.utils;

import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.adapter.utils.WpsImageViewPagerAdapter;
import com.rxjy.rxinvestment.base.App;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.base.PathUrl;
import com.rxjy.rxinvestment.custom.HackyViewPager;
import com.rxjy.rxinvestment.entity.ImagesBean;
import com.rxjy.rxinvestment.utils.Constants;
import com.rxjy.rxinvestment.utils.JSONUtils;
import com.rxjy.rxinvestment.utils.OkhttpUtils;
import com.rxjy.rxinvestment.utils.StringUtils;
import com.rxjy.rxinvestment.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class WpsImageActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

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
    @Bind(R.id.hvp_image)
    HackyViewPager hvpImage;
    @Bind(R.id.tv_image_num)
    TextView tvImageNum;
    private int position;
    private WpsImageViewPagerAdapter adapter;
    private String title;
    private ArrayList<String> imgUrls;
    private List<String> imgQUrl;
    private int size;
    private String baseurl;
    private Object imgsInfo;
    private String type;

    @Override
    public int getLayout() {
        return R.layout.activity_wps_image;
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
        baseurl = getIntent().getStringExtra("BaseUrl");
        title = getIntent().getStringExtra("title");
        imgUrls = getIntent().getStringArrayListExtra(Constants.IMAGE_URL_LIST);
        position = getIntent().getIntExtra("camera_position", -1);
        type = getIntent().getStringExtra("type");

        tvTitle.setText(title);

        imgQUrl = new ArrayList<>();

        if (imgUrls != null) {
            if (imgUrls.size() > 0) {
                size = imgUrls.size();
            } else {
                size = 0;
            }
        }

        if (size == 0) {
            Log.e("查看大图", "一张");
            if (TextUtils.isEmpty(baseurl)) {
                getImgsInfo();
            } else {
                imgQUrl.add(baseurl);
            }
        } else {
            Log.e("查看大图", "多张");
            switch (type) {
                case "量房":
                    for (int i = 0; i < size - 1; i++) {
                        imgQUrl.add(baseurl + imgUrls.get(i));
                        Log.e("查看大图", imgUrls.get(i));
                    }
                    break;
                default:
                    for (int i = 0; i < size; i++) {
                        imgQUrl.add(baseurl + imgUrls.get(i));
                        Log.e("查看大图", imgUrls.get(i));
                    }
                    break;
            }
        }

        adapter = new WpsImageViewPagerAdapter(getSupportFragmentManager(), imgQUrl);
        hvpImage.setAdapter(adapter);
        tvImageNum.setText(position + 1 + " / " + imgQUrl.size());
        hvpImage.setCurrentItem(position);
        hvpImage.setOnPageChangeListener(this);
        imgBack.setVisibility(View.VISIBLE);      //显示控件
        imgBack.setOnClickListener(this);   //设置点击事件
    }

    @Override
    public void initAdapter() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tvImageNum.setText(position + 1 + " / " + imgQUrl.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void getImgsInfo() {

        Map map = new HashMap();
        map.put("card_no", App.cardNo);
        map.put("img_type", type);
        OkhttpUtils.doGet(this, PathUrl.GETIMGSURL, map, new OkhttpUtils.MyCall() {
            @Override
            public void success(String data) {
                Log.e("tag_获取图片信息", data);
                ImagesBean info = JSONUtils.toObject(data, ImagesBean.class);
                ArrayList<ImagesBean.ImagesBody> body = info.getBody();
                int statusCode = info.getStatusCode();
                String statusMsg = info.getStatusMsg();
                if (statusCode == 0) {
                    for (int i = 0; i < body.size(); i++) {
                        if (!StringUtils.isEmpty(body.get(i).getImg_url())) {
                            imgQUrl.add(body.get(i).getImg_url());
                        }
                    }
                    adapter = new WpsImageViewPagerAdapter(getSupportFragmentManager(), imgQUrl);

                    hvpImage.setAdapter(adapter);

                    tvImageNum.setText(position + 1 + " / " + imgQUrl.size());
                    hvpImage.setCurrentItem(position);
                } else {
                    ToastUtil.getInstance().toastCentent(statusMsg);
                }
            }

            @Override
            public void error(String message) {
                Log.e("tag_获取图片失败", message);
            }
        });
    }
}
