package com.dharashah.showcaseandroidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dharashah.showcaseandroidapp.R;
import com.dharashah.showcaseandroidapp.ShowCaseApp;
import com.dharashah.showcaseandroidapp.callback.ILoadListener;
import com.dharashah.showcaseandroidapp.db.DBHelper;
import com.dharashah.showcaseandroidapp.fragment.AndroidHistoryFragment;
import com.dharashah.showcaseandroidapp.fragment.DeviceFragment;
import com.dharashah.showcaseandroidapp.model.AllData;
import com.dharashah.showcaseandroidapp.network.RestClient;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class MainActivity extends BaseActivity implements View.OnClickListener, Callback<AllData>{
    private Button mBtnDevices;
    private Button mBtnHistory;
    private AndroidHistoryFragment mHistoryFragment;
    private DBHelper mDBHelper;
    private DeviceFragment mDeviceFragment;
    private Callback<AllData> mCallback;
    private FloatingActionButton fabAddDevice;
    private FloatingActionButton fabAddVersion;
    private FloatingActionsMenu fabMenu;
    private ILoadListener mLoadListener;
    private Toolbar mToolbar;
    private TextView mTxtTitle;

    public void setLoadListener(ILoadListener loadListener) {
        this.mLoadListener = loadListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initView();
        initFragment();
        initServiceCalls();
    }

    private void initToolbar() {
        mTxtTitle = (TextView)findViewById(R.id.txtTitle);
        mToolbar=(Toolbar)findViewById(R.id.toolbar);
        mTxtTitle.setText("Dashboard");
        setSupportActionBar(mToolbar);
    }

    private void initView() {
        mCallback = this;
        mBtnHistory = (Button)findViewById(R.id.btnAndroidVersions);
        mBtnDevices = (Button)findViewById(R.id.btnAndroidDevices);
        fabAddDevice = (FloatingActionButton)findViewById(R.id.fabAndroidDevice);
        fabAddVersion = (FloatingActionButton)findViewById(R.id.fabAndroidHistory);
        fabMenu =(FloatingActionsMenu)findViewById(R.id.fabMenu);

        mBtnDevices.setOnClickListener(this);
        mBtnHistory.setOnClickListener(this);
        fabAddVersion.setOnClickListener(this);
        fabAddDevice.setOnClickListener(this);

        mDBHelper = DBHelper.getInstance(ShowCaseApp.getAppContext());

    }

    @Override
    public void onClick(View v) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.btnAndroidDevices:
                if(mHistoryFragment != null) {
                    mHistoryFragment.onPause();
                }

                if(mDeviceFragment == null) {
                    mDeviceFragment = DeviceFragment.newInstance();
                }

                ft.replace(R.id.content_frame, mDeviceFragment);
                break;

            case R.id.btnAndroidVersions:
                if(mDeviceFragment != null) {
                    mDeviceFragment.onPause();
                }

                if(mHistoryFragment == null) {
                    mHistoryFragment = AndroidHistoryFragment.newInstance();
                }

                ft.replace(R.id.content_frame, mHistoryFragment);
                break;

            case R.id.fabAndroidDevice:
                fabMenu.collapse();
                Intent intent = new Intent(ShowCaseApp.getAppContext(), AddDeviceActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;

            case R.id.fabAndroidHistory:
                fabMenu.collapse();
                intent = new Intent(ShowCaseApp.getAppContext(), AddVersionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;
        }
        ft.commit();
    }

    private void initFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mDeviceFragment = DeviceFragment.newInstance();
        ft.replace(R.id.content_frame, mDeviceFragment);
        ft.commit();
    }

    private void initServiceCalls() {
        boolean hasData = mDBHelper.hasData();

        // we have data here
        if(hasData) {
            if(mLoadListener != null) {
                // do not do anything
                // load the data into the fragment first selected
                mLoadListener.onLoadData();
            }
        }else {
            if(RestClient.isNetworkAvailable()) {
                Call<AllData> call = new RestClient().getApiService().getAllData();
                call.enqueue(mCallback);
            }else {
                Toast.makeText(ShowCaseApp.getAppContext(),
                        ShowCaseApp.getAppContext().getResources().getString(R.string.no_internet),Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onResponse(Response<AllData> response, Retrofit retrofit) {
        if(response.body() != null) {
            addDataToDatabase(response.body());
        }

        if(mLoadListener != null) {
            mLoadListener.onRefreshData(response.body());
        }
    }

    @Override
    public void onFailure(Throwable t) {

    }

    /**
     * Start threads to add data to the database
     * @param response
     */
    private void addDataToDatabase(final AllData response) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDBHelper.addDevices(response.getDeviceList());
                mLoadListener.onLoadData();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                mDBHelper.addVersions(response.getAndroidHistoryList());
                mLoadListener.onLoadData();
            }
        }).start();
    }
}
