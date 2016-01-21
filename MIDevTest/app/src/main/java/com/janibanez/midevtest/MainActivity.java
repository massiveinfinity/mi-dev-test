package com.janibanez.midevtest;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.janibanez.midevtest.adapters.MainPagerAdapter;
import com.janibanez.midevtest.fragments.DevicesFragment;
import com.janibanez.midevtest.fragments.VersionsFragment;
import com.janibanez.server.ICallback;
import com.janibanez.server.MiApi;
import com.janibanez.server.http.response.DbResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    MainPagerAdapter mPagerAdapter;
    ProgressDialog mProgessDialog;
    TabLayout mTabLayout;
    ViewPager mViewPager;

    DbResponse mData;
    List<MainUpdateListener> mUpdateListeners = new ArrayList<>();

    public interface MainUpdateListener {
        void onUpdate(DbResponse response);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPagerAdapter = new MainPagerAdapter(this, getSupportFragmentManager());

        mPagerAdapter.add(new MainPagerAdapter.FragmentInfo("Versions", VersionsFragment.class.getName()));
        mPagerAdapter.add(new MainPagerAdapter.FragmentInfo("Devices", DevicesFragment.class.getName()));

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        mProgessDialog = new ProgressDialog(this);
        mProgessDialog.setMessage("Loading...");
        mProgessDialog.setIndeterminate(true);
        mProgessDialog.setCancelable(false);

        getData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    private void getData() {

        mProgessDialog.show();

        MiApi api = new MiApi(this);
        api.call(MiApi.Action.GetDb, new ICallback<DbResponse>() {
            @Override
            public void onFailure(final Throwable throwable) {
                // need to run updates on UI thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgessDialog.dismiss();

                        for (int i=0; i < mUpdateListeners.size(); i++) {
                            mUpdateListeners.get(i).onUpdate(null);
                        }
                    }
                });
            }

            @Override
            public void onResponse(final DbResponse response) throws IOException {
                // need to run updates on UI thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgessDialog.dismiss();

                        for (int i=0; i < mUpdateListeners.size(); i++) {
                            mUpdateListeners.get(i).onUpdate(response);
                        }
                    }
                });
            }
        });
    }

    public void addUpdateListener(MainUpdateListener listener) {
        mUpdateListeners.add(listener);
    }

    public void removeUpdateListener(MainUpdateListener listener) {
        mUpdateListeners.remove(listener);
    }
}
