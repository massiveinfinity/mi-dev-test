package com.janibanez.midevtest;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.janibanez.database.helpers.DeviceDaoHelper;
import com.janibanez.database.helpers.VersionDaoHelper;
import com.janibanez.database.models.DbDevice;
import com.janibanez.database.models.DbVersion;
import com.janibanez.midevtest.adapters.MainPagerAdapter;
import com.janibanez.midevtest.fragments.DevicesFragment;
import com.janibanez.midevtest.fragments.VersionsFragment;
import com.janibanez.server.ICallback;
import com.janibanez.server.MiApi;
import com.janibanez.server.models.Db;
import com.janibanez.server.models.Device;
import com.janibanez.server.models.Version;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_DISPLAY_DEVICE = 100;
    public static final int REQUEST_DISPLAY_VERSION = 101;

    public static final int REQUEST_EDIT_DEVICE = 200;
    public static final int REQUEST_EDIT_VERSION = 201;

    public static final int RESULT_REFRESH = 300;

    MainPagerAdapter mPagerAdapter;
    ProgressDialog mProgessDialog;
    TabLayout mTabLayout;
    ViewPager mViewPager;

    Db mData;
    List<MainUpdateListener> mUpdateListeners = new ArrayList<>();

    public interface MainUpdateListener {
        void onUpdate(Db response);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPagerAdapter = new MainPagerAdapter(this, getSupportFragmentManager());

        mPagerAdapter.add(new MainPagerAdapter.FragmentInfo("Devices", DevicesFragment.class.getName()));
        mPagerAdapter.add(new MainPagerAdapter.FragmentInfo("Versions", VersionsFragment.class.getName()));

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
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_add_device:
                intent = new Intent(this, DeviceEditActivity.class);
                startActivityForResult(intent, REQUEST_EDIT_DEVICE);
                break;
            case R.id.action_add_version:
                intent = new Intent(this, VersionEditActivity.class);
                startActivityForResult(intent, REQUEST_EDIT_VERSION);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_DISPLAY_DEVICE:
            case REQUEST_DISPLAY_VERSION:
            case REQUEST_EDIT_DEVICE:
            case REQUEST_EDIT_VERSION:
                if (resultCode == RESULT_REFRESH) {
                    getData();
                }
                break;
        }

    }

    private void getData() {

        mProgessDialog.show();

        MiApi api = new MiApi(this);
        api.call(MiApi.Action.GetDb, 0, null, new ICallback<Db>() {
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
            public void onResponse(final Db response) throws IOException {
                // need to run updates on UI thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgessDialog.dismiss();

                        // clear previous db data
                        DeviceDaoHelper.deleteDevices(MainActivity.this);
                        VersionDaoHelper.deleteVersions(MainActivity.this);

                        // add all latest devices
                        if (response.devices != null) {
                            for (Device device : response.devices) {
                                ContentValues values = new ContentValues();
                                values.put(DbDevice.COLUMN_ID, device.id);
                                values.put(DbDevice.COLUMN_ANDROID_ID, device.androidId);
                                values.put(DbDevice.COLUMN_NAME, device.name);
                                values.put(DbDevice.COLUMN_SNIPPET, device.snippet);
                                values.put(DbDevice.COLUMN_CARRIER, device.carrier);
                                values.put(DbDevice.COLUMN_IMAGE_URL, device.imageUrl);
                                DeviceDaoHelper.insertDevice(MainActivity.this, values);
                            }
                        }

                        // add all latest versions
                        if (response.android != null) {
                            for (Version version : response.android) {
                                ContentValues values = new ContentValues();
                                values.put(DbVersion.COLUMN_ID, version.id);
                                values.put(DbVersion.COLUMN_NAME, version.name);
                                values.put(DbVersion.COLUMN_VERSION, version.version);
                                values.put(DbVersion.COLUMN_CODENAME, version.codename);
                                values.put(DbVersion.COLUMN_TARGET, version.target);
                                values.put(DbVersion.COLUMN_DISTRIBUTION, version.distribution);
                                VersionDaoHelper.insertVersion(MainActivity.this, values);
                            }
                        }

                        for (int i=0; i < mUpdateListeners.size(); i++) {
                            mUpdateListeners.get(i).onUpdate(response);
                        }

                        /* TESTS
                        List<Version> versions = VersionDaoHelper.getVersions(MainActivity.this);

                        VersionDaoHelper.deleteVersions(MainActivity.this);

                        versions = VersionDaoHelper.getVersions(MainActivity.this);

                        List<Device> devices = DeviceDaoHelper.getDevices(MainActivity.this);

                        DeviceDaoHelper.deleteDevices(MainActivity.this);

                        devices = DeviceDaoHelper.getDevices(MainActivity.this);
                        */
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
