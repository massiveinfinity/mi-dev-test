package com.mi.afzaal.showcaseapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.mi.afzaal.adapters.DeviceListAdapter;
import com.mi.afzaal.adddevicedetails.AddNewDevice;
import com.mi.afzaal.models.DeviceData;

import java.util.ArrayList;
import java.util.List;

public class DevicesListActivity extends Activity implements iDeviceListView,OnClickListener {

    ListView devicesList;
    DeviceListPresenterImpl deviceListPresenter;
    RelativeLayout progressview;
    ProgressBar progressbar;
    ImageView AddDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_list);
        devicesList = (ListView)findViewById(R.id.devicesList);
        progressview = (RelativeLayout)findViewById(R.id.progressview);
        progressbar = (ProgressBar)findViewById(R.id.progressbar);
        AddDevice = (ImageView) findViewById(R.id.AddDevice);
        AddDevice.setOnClickListener(this);
        deviceListPresenter = new DeviceListPresenterImpl(this);
        deviceListPresenter.getData();
    }


    @Override
    public void showProgress() {
        progressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressbar.setVisibility(View.GONE);
        progressview.setVisibility(View.GONE);
    }

    @Override
    public void setData(ArrayList<DeviceData> dataList) {
        if(dataList!=null) {
            progressbar.setVisibility(View.GONE);
            progressview.setVisibility(View.GONE);
            deviceListPresenter.saveDataToDB(getApplicationContext(), dataList);
            DeviceListAdapter adapter = new DeviceListAdapter(DevicesListActivity.this, dataList);
            devicesList.setAdapter(adapter);
        }
    }

    @Override
    public void setonError() {
        progressbar.setVisibility(View.GONE);
        progressview.setVisibility(View.GONE);
    }

    @Override
    public void setonSuccess() {
        progressbar.setVisibility(View.GONE);
        progressview.setVisibility(View.GONE);
    }

    @Override
    public void savetoDB(ArrayList<DeviceData> list) {

    }


    @Override
    public void onClick(View v) {
        if(v==AddDevice){
            startActivity(new Intent(DevicesListActivity.this,AddNewDevice.class));
        }
    }
}

