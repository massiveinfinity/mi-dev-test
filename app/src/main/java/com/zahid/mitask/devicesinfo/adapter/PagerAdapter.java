package com.zahid.mitask.devicesinfo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.zahid.mitask.devicesinfo.fragments.DeviceFragment;
import com.zahid.mitask.devicesinfo.fragments.VersionFragment;

/**
 * Created by zahid.r on 1/26/2016.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    private final String[] TITLES = {"DEVICES", "VERSIONS"};

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new DeviceFragment().newInstance();
        else
            return new VersionFragment().newInstance();
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

}
