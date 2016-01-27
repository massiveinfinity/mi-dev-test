package com.zahid.mitask.devicesinfo.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.zahid.mitask.devicesinfo.R;
import com.zahid.mitask.devicesinfo.adapter.PagerAdapter;
import com.zahid.mitask.devicesinfo.customview.PagerSlidingTabStrip;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    // Initialize view and objects
    private void initialize() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mPagerSlidingTabStrip.setShouldExpand(true);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        mPagerSlidingTabStrip.setViewPager(viewPager);
        mPagerSlidingTabStrip.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
