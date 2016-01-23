package com.dharashah.showcaseandroidapp.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dharashah.showcaseandroidapp.R;
import com.dharashah.showcaseandroidapp.ShowCaseApp;
import com.dharashah.showcaseandroidapp.activity.MainActivity;
import com.dharashah.showcaseandroidapp.adapter.DeviceAdapter;
import com.dharashah.showcaseandroidapp.callback.IDataFunctionListener;
import com.dharashah.showcaseandroidapp.callback.ILoadListener;
import com.dharashah.showcaseandroidapp.db.DBHelper;
import com.dharashah.showcaseandroidapp.model.AllData;
import com.dharashah.showcaseandroidapp.model.Devices;
import com.dharashah.showcaseandroidapp.network.RestClient;

import java.util.LinkedList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Dhara shah on 22/01/2016.
 */
public class DeviceFragment extends Fragment implements ILoadListener, SwipeRefreshLayout.OnRefreshListener, Callback<List<Devices>>, IDataFunctionListener {
    private static DeviceFragment mFragment;
    private View mView;
    private ListView mListView;
    private DBHelper mDbHelper;
    private ILoadListener mListener;
    private LinkedList<Devices> deviceList;
    private SwipeRefreshLayout mRefreshLayout;
    private Callback<List<Devices>> mCallback;
    private DeviceAdapter mAdapter;
    private IDataFunctionListener mDeleteListener;

    public static DeviceFragment newInstance() {
        mFragment = new DeviceFragment();
        return mFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_devices, container, false);
        initView();
        return mView;
    }

    private void initView() {
        mListener = this;
        mDeleteListener = this;
        mDbHelper = DBHelper.getInstance(ShowCaseApp.getAppContext());
        mListView = (ListView)mView.findViewById(android.R.id.list);
        mRefreshLayout = (SwipeRefreshLayout)mView.findViewById(R.id.swipeRefreshLayout);
        mRefreshLayout.setOnRefreshListener(this);
        deviceList = new LinkedList<>();
        ((MainActivity)getActivity()).setLoadListener(mListener);
        mAdapter = new DeviceAdapter(ShowCaseApp.getAppContext(), deviceList, mDeleteListener);
        mListView.setAdapter(mAdapter);
        onLoadData();
    }

    @Override
    public void onLoadData() {
        // get data from the database and load here
        LinkedList<Devices> devices = mDbHelper.getDevices();
        deviceList.clear();
        deviceList.addAll(devices);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onRefreshData(AllData data) {
        List<Devices> items = data.getDeviceList();
        deviceList.clear();
        deviceList.addAll(items);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        mCallback = this;
        int startCounter = (deviceList == null) ? 0 : deviceList.size();
        int endCounter = startCounter + 10;

        if(RestClient.isNetworkAvailable()) {
            Call<List<Devices>> call =
                    new RestClient().getApiService().getDevices(startCounter, endCounter);
            call.enqueue(mCallback);
        }else {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onResponse(final Response<List<Devices>> response, Retrofit retrofit) {
        if(response.body() != null) {
            LinkedList<Devices> deviceListNew = new LinkedList<>();
            deviceListNew.addAll(response.body());
            deviceListNew.addAll(deviceList);
            deviceList.clear();
            deviceList.addAll(deviceListNew);

            if(mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    mDbHelper.addDevices(response.body());
                }
            }).start();
        }

        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailure(Throwable t) {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDeleteSelected(int id) {
        mDbHelper.deleteDevice(id);

        // make a call to delete the record
        Call deleteCall = new RestClient().getApiService().deleteDevice(id);
        deleteCall.enqueue(new Callback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void onDisplaySelected(Object object) {
        if(object instanceof Devices) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.pop_up_device_details);
            TextView txtAndroidVersionName = (TextView)dialog.findViewById(R.id.txtAndroidVersion);
            TextView txtDeviceName = (TextView)dialog.findViewById(R.id.txtDeviceName);
            TextView txtDeviceDesc = (TextView)dialog.findViewById(R.id.txtDeviceDesc);
            ImageView imgClose = (ImageView)dialog.findViewById(R.id.imgClose);

            Devices device = (Devices)object;
            String androidVersionName = mDbHelper.getVersionName(device.getAndroidId());

            txtAndroidVersionName.setText(androidVersionName);
            txtDeviceDesc.setText(device.getDeviceDesc());
            txtDeviceName.setText(device.getDeviceName());

            imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.setCancelable(true);
            dialog.show();
        }
    }
}
