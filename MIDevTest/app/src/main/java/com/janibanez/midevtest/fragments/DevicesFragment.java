package com.janibanez.midevtest.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.janibanez.midevtest.DeviceDisplayActivity;
import com.janibanez.midevtest.MainActivity;
import com.janibanez.midevtest.R;
import com.janibanez.midevtest.adapters.DevicesListAdapter;
import com.janibanez.server.models.Db;
import com.janibanez.server.models.Device;

import java.util.ArrayList;

/**
 * Created by jwgibanez on 21/01/2016.
 */
public class DevicesFragment extends Fragment implements MainActivity.MainUpdateListener, AdapterView.OnItemClickListener {

    MainActivity mActivity;
    ListView mList;
    DevicesListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
        mActivity.addUpdateListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_devices, container, false);

        mAdapter = new DevicesListAdapter(mActivity, new ArrayList<Device>());

        mList = (ListView) rootView.findViewById(R.id.list);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.removeUpdateListener(this);
    }

    @Override
    public void onUpdate(Db response) {
        if (response != null) {
            mAdapter.clear();
            mAdapter.addAll(response.devices);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mActivity, DeviceDisplayActivity.class);
        intent.putExtra("device", mAdapter.getItem(position));
        mActivity.startActivityForResult(intent, MainActivity.REQUEST_DISPLAY_DEVICE);
    }
}
