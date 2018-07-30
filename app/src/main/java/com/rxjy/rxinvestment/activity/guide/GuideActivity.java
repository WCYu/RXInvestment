package com.rxjy.rxinvestment.activity.guide;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rxjy.rxinvestment.R;
import com.rxjy.rxinvestment.activity.home.LoginActivity;
import com.rxjy.rxinvestment.base.BaseActivity;
import com.rxjy.rxinvestment.utils.MySharedPreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 引导页
 * Created by hjh on 2017/11/3.
 */
public class GuideActivity extends BaseActivity {

    private ViewPager guide_viewpager;
    private TextView guide_go;
    private View view_guide1;
    private View view_guide2;
    private View view_guide3;
    private View view_guide4;
    private List<View> viewlist;

    @Override
    public void initView() {
        ButterKnife.bind(this);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_guide;
    }

    @Override
    public void intData() {
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void initAdapter() {

    }

    private void init() {
        guide_go = (TextView) findViewById(R.id.guide_go);
        guide_viewpager = (ViewPager) findViewById(R.id.guide_viewpager);

        view_guide1 = LayoutInflater.from(this).inflate(R.layout.activity_guideone, null);
        view_guide2 = LayoutInflater.from(this).inflate(R.layout.activity_guidetwo, null);
        view_guide3 = LayoutInflater.from(this).inflate(R.layout.activity_guidethree, null);
//        view_guide4 = LayoutInflater.from(this).inflate(R.layout.activity_guidefour, null);

        guide_go.setVisibility(View.GONE);

        viewlist = new ArrayList<View>();
        viewlist.add(view_guide1);
        viewlist.add(view_guide2);
        viewlist.add(view_guide3);
//        viewlist.add(view_guide4);

        PagerAdapter pageradapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {

                return arg0 == arg1;
            }

            @Override
            public int getCount() {

                return viewlist.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {

                container.removeView(viewlist.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewlist.get(position));
                return viewlist.get(position);
            }

        };

        guide_viewpager.setAdapter(pageradapter);

        guide_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

                switch (arg0) {
                    case 0:
                        guide_go.setVisibility(View.GONE);
                        break;
                    case 1:
                        guide_go.setVisibility(View.GONE);
                        break;
                    case 2:
//                        guide_go.setVisibility(View.GONE);
                        guide_go.setVisibility(View.VISIBLE);
                        break;
                    case 3:
//                        guide_go.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        guide_go.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
                startActivity(intent);
                MySharedPreferences.getInstance().setLoginState(1);
                finish();
            }
        });

    }

}
