package com.zahid.mitask.devicesinfo.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zahid.mitask.devicesinfo.R;
import com.zahid.mitask.devicesinfo.activity.AddDeviceActivity;
import com.zahid.mitask.devicesinfo.activity.DeviceDetailActivity;
import com.zahid.mitask.devicesinfo.activity.EditDeviceActivity;
import com.zahid.mitask.devicesinfo.adapter.DeviceListAdapter;
import com.zahid.mitask.devicesinfo.bean.Device;
import com.zahid.mitask.devicesinfo.bean.Version;
import com.zahid.mitask.devicesinfo.helper.AlertDialogHelper;
import com.zahid.mitask.devicesinfo.helper.DatabaseHelper;
import com.zahid.mitask.devicesinfo.helper.ProgressDialogHelper;
import com.zahid.mitask.devicesinfo.helper.WebServiceHelper;
import com.zahid.mitask.devicesinfo.interfaces.DialogClickListener;
import com.zahid.mitask.devicesinfo.interfaces.OnWebServiceListener;
import com.zahid.mitask.devicesinfo.interfaces.PopUpMenuClickListener;
import com.zahid.mitask.devicesinfo.tasks.DeleteDeviceFromTableTask;
import com.zahid.mitask.devicesinfo.tasks.SaveDeviceDataInDBTask;
import com.zahid.mitask.devicesinfo.tasks.UpdateDeviceInTableTask;
import com.zahid.mitask.devicesinfo.utils.Constants;
import com.zahid.mitask.devicesinfo.utils.Validator;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class DeviceFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,
        OnWebServiceListener, View.OnClickListener, DialogClickListener, PopUpMenuClickListener {

    private static final int REQUEST_EDIT_DEVICE = 100;
    private static final int REQUEST_ADD_DEVICE = 101;
    private ListView mListView;
    private WebServiceHelper webServiceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private DeviceListAdapter deviceListAdapter;
    private FloatingActionButton floatingActionButton;
    private DatabaseHelper databaseHelper;

    private static final int TAG_GET_DEVICES = 1000, TAG_DELETE_DEVICE = 1001, TAG_DEVICE_SETTINGS = 2000;
    private ArrayList<Device> deviceArrayList;

    private int selectedDevicePosition;
    private String[] popUpMenuItems = {"Edit Device", "Delete Device"};

    public static DeviceFragment newInstance() {
        DeviceFragment frag = new DeviceFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.pager_fragment_layout, container, false);
        initialize(view);

        if (Validator.isNetworkAvailable(getActivity())) {
            progressDialogHelper.showProgressDialog(getString(R.string.loading));
            webServiceHelper.makeJSONArrayRequest(Constants.DEVICES_URL, Request.Method.GET,
                    (String) null, TAG_GET_DEVICES);
        } else {
            new FetchDevicesTask().execute();
        }
        return view;
    }

    // Initialize view and objects
    private void initialize(View view) {
        mListView = (ListView) view.findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);

        webServiceHelper = new WebServiceHelper(getActivity());
        webServiceHelper.setOnWebServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(getActivity());
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);
        databaseHelper = new DatabaseHelper(getActivity());
    }

    // Update List view
    private void updateListView() {
        if (deviceArrayList == null)
            return;
        if (deviceListAdapter == null) {
            deviceListAdapter = new DeviceListAdapter(getActivity(), deviceArrayList);
            mListView.setAdapter(deviceListAdapter);
        } else {
            deviceListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent detailIntent = new Intent(getActivity(), DeviceDetailActivity.class);
        detailIntent.putExtra("deviceObj", deviceListAdapter.getItem(position));
        startActivity(detailIntent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        selectedDevicePosition = position;
        // Shows pop up dialog with "Edit device" and "Delete device" options
        PopUpDialogFragment popupDialogFragment = PopUpDialogFragment.newInstance(
                "", popUpMenuItems, TAG_DEVICE_SETTINGS);
        popupDialogFragment.setOnPopUpMenuListener(this);
        try {
            popupDialogFragment.show(getChildFragmentManager(), "popup_dialog");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    @Override
    public void onWebServiceResponse(String response, int tag) {
        progressDialogHelper.hideProgressDialog();
        if (tag == TAG_GET_DEVICES) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Device>>() {
            }.getType();
            deviceArrayList = gson.fromJson(response, listType);
            // Save the device list to the Database
            new SaveDeviceDataInDBTask(getActivity()).execute(deviceArrayList);
            updateListView();
        } else if (tag == TAG_DELETE_DEVICE) {
            // Delete the device from the Database table
            new DeleteDeviceFromTableTask(getActivity()).execute(deviceArrayList.get(selectedDevicePosition));
            // Remove the selected device from the list and update the listview
            deviceArrayList.remove(selectedDevicePosition);
            updateListView();
        }
    }

    @Override
    public void onWebServiceError(String error, int tag) {
        progressDialogHelper.hideProgressDialog();
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
        if (tag == TAG_GET_DEVICES) {
            // If Internet connectivity fails, fetch the device list from the Database
            new FetchDevicesTask().execute();
        }
    }

    @Override
    public void onPopUpMenuItemClicked(int position, int tag) {
        if (tag == TAG_DEVICE_SETTINGS) {
            if (position == 0) {
                Intent editIntent = new Intent(getActivity(), EditDeviceActivity.class);
                editIntent.putExtra("deviceObj", deviceListAdapter.getItem(selectedDevicePosition));
                startActivityForResult(editIntent, REQUEST_EDIT_DEVICE);
            } else {
                AlertDialogHelper.showCompoundAlertDialog(getActivity(), this, getString(R.string.device_delete_confirmation),
                        getString(R.string.alert_button_yes),
                        getString(R.string.alert_button_no), TAG_DELETE_DEVICE);
            }
        }
    }

    @Override
    public void onAlertPositiveClicked(int tag) {
        if (tag == TAG_DELETE_DEVICE) {
            progressDialogHelper.showProgressDialog(getString(R.string.loading));
            webServiceHelper.makeJsonObjectRequest(Constants.DEVICES_URL + deviceListAdapter.getItem(selectedDevicePosition).getId(),
                    Request.Method.DELETE, (String) null, TAG_DELETE_DEVICE);
        }
    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_DEVICE && resultCode == Activity.RESULT_OK) {
            Device device = (Device) data.getSerializableExtra("deviceObj");
            deviceArrayList.set(selectedDevicePosition, device);
            // Update the device list in the Database
            new UpdateDeviceInTableTask(getActivity()).execute(device);
            updateListView();
        } else if (requestCode == REQUEST_ADD_DEVICE && resultCode == Activity.RESULT_OK) {
            Device device = (Device) data.getSerializableExtra("deviceObj");
            deviceArrayList.add(device);
            ArrayList<Device> tempList = new ArrayList<>();
            tempList.add(device);
            // Save the device object in the Database
            new SaveDeviceDataInDBTask(getActivity()).execute(tempList);
            updateListView();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == floatingActionButton) {
            // Add new device
            Intent addIntent = new Intent(getActivity(), AddDeviceActivity.class);
            startActivityForResult(addIntent, REQUEST_ADD_DEVICE);
        }
    }

    private class FetchDevicesTask extends AsyncTask<Void, Void, ArrayList<Device>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogHelper.showProgressDialog(getString(R.string.loading));
        }

        @Override
        protected ArrayList<Device> doInBackground(Void... params) {
            return databaseHelper.fetchDeviceListFromDB();
        }

        @Override
        protected void onPostExecute(ArrayList<Device> devices) {
            super.onPostExecute(devices);
            progressDialogHelper.hideProgressDialog();
            deviceArrayList = devices;
            updateListView();
        }
    }


}
