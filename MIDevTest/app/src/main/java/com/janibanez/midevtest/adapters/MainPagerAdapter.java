package com.janibanez.midevtest.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jwgibanez on 21/01/2016.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {

    Context mContext;
    List<FragmentInfo> mFragmentInfos;

    public MainPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        mFragmentInfos = new ArrayList<>();
    }

    public static class FragmentInfo {
        String fragmentName;
        String tabName;

        public FragmentInfo(String tname, String fname) {
            this.fragmentName = fname;
            this.tabName = tname;
        }
    }

    @Override
    public Fragment getItem(int position) {
        return Fragment.instantiate(mContext, mFragmentInfos.get(position).fragmentName);
    }

    @Override
    public int getCount() {
        return mFragmentInfos.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentInfos.get(position).tabName;
    }

    public void add(FragmentInfo fragmentInfo) {
        mFragmentInfos.add(fragmentInfo);
    }

}
