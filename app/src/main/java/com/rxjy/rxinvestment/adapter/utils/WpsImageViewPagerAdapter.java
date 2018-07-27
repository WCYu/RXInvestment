package com.rxjy.rxinvestment.adapter.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.rxjy.rxinvestment.fragment.utils.WpsImageFragment;

import java.util.List;

/**
 * Created by csonezp on 15-12-28.
 */
public class WpsImageViewPagerAdapter extends FragmentStatePagerAdapter {

    List<String> mDatas;

    public WpsImageViewPagerAdapter(android.support.v4.app.FragmentManager fm, List<String> mDatas) {
        super(fm);
        this.mDatas = mDatas;
    }

    @Override
    public Fragment getItem(int position) {
        String url = mDatas.get(position);
        Fragment fragment = WpsImageFragment.newInstance(url);
        return fragment;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }
}
