package com.guo.qlzx.nongji.service.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.guo.qlzx.nongji.service.fragment.BlankFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李 on 2018/5/30.
 */

public class OrderFragmentAdapter extends FragmentPagerAdapter {

    private List<String> list;

    private List<BlankFragment> fragments = new ArrayList<>();

    private int mCurrentView=0;

    public OrderFragmentAdapter(FragmentManager fm, List<String> list, List<BlankFragment> fragments) {
        super(fm);
        this.list = list;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }
}
