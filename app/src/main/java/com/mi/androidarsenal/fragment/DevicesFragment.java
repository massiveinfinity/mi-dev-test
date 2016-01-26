package com.mi.androidarsenal.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mi.androidarsenal.R;
import com.mi.androidarsenal.adapter.DevicesAdapter;
import com.mi.androidarsenal.database.DatabaseHandler;
import com.mi.androidarsenal.database.SharedPreferenceUtil;
import com.mi.androidarsenal.network.RequestOperation;
import com.mi.androidarsenal.utility.AndroidResponseObserver;
import com.mi.androidarsenal.utility.AppConstants;
import com.mi.androidarsenal.utility.AppUtils;
import com.mi.androidarsenal.utility.OfflineUtil;

/**
 * This fragment shows the device list
 *
 * @author Samir Sarosh
 */
@SuppressWarnings("ALL")
public class DevicesFragment extends Fragment implements RequestOperation.DeleteItemFromDeviceObserver, AppConstants, OfflineUtil.RefreshDeviceListObserver, AdapterView.OnItemClickListener {

    private DevicesAdapter mDevicesAdapter;
    OnDeviceSelectedListener mOnDeviceSelectedListener;
    private LinearLayout mLoadingViewGroup;
    private LinearLayout mNoListViewGroup;

    public interface OnDeviceSelectedListener {
        public void onDeviceItemSelected(String id, String name, String imageUrl, String snippet, String carrier, String android_id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OfflineUtil.setRefreshDeviceListObserver(this);
        RequestOperation.setDeleteItemFromDeviceObserver(this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mOnDeviceSelectedListener = (OnDeviceSelectedListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_devices, container, false);

        mLoadingViewGroup = (LinearLayout) view
                .findViewById(R.id.loading_layout);
        ProgressBar progressBar = (ProgressBar) view
                .findViewById(R.id.progress_loading_bar);

        int color = AppUtils.getColorFromRes(getActivity(), R.color.colorPrimary);
        progressBar.getIndeterminateDrawable().setColorFilter(color,
                android.graphics.PorterDuff.Mode.SRC_IN);

        mNoListViewGroup = (LinearLayout) view
                .findViewById(R.id.no_list_layout);
        ListView devicesListView = (ListView) view.findViewById(R.id.devices_listview);

        Cursor devicesCursor = DatabaseHandler.getDatabaseHandlerInstance(getActivity()).getDevicesCursor();
        // Setup cursor adapter using cursor from last step
        mDevicesAdapter = new DevicesAdapter(getActivity(), devicesCursor);
        // Attach cursor adapter to the ListView
        devicesListView.setAdapter(mDevicesAdapter);

        devicesListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SharedPreferenceUtil.getSharedPreferenceUtilInstance(getActivity()).isDbPopulatedSuccess()) {
            mLoadingViewGroup.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDeleteItemFromDeviceTable(String id) {
        DatabaseHandler.getDatabaseHandlerInstance(getActivity()).deleteRow(TABLE_DEVICES, KEY_DEVICE_ID, id);

        refreshList();
    }

    @Override
    public void onRefreshDeviceList() {
        refreshList();
    }


    private void refreshList() {
        Cursor devicesCursor = DatabaseHandler.getDatabaseHandlerInstance(getActivity()).getDevicesCursor();
        mDevicesAdapter.changeCursor(devicesCursor);
        mDevicesAdapter.notifyDataSetChanged();
        mLoadingViewGroup.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
        Cursor cursor = (Cursor) mDevicesAdapter.getItem(position);
        String id = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DEVICE_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME));
        String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGE_URL));
        String snippet = cursor.getString(cursor.getColumnIndexOrThrow(KEY_SNIPPET));
        String carrier = cursor.getString(cursor.getColumnIndexOrThrow(KEY_CARRIER));
        String android_id = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ANDROID_ID));

        if (mOnDeviceSelectedListener != null) {
            mOnDeviceSelectedListener.onDeviceItemSelected(id, name, imageUrl, snippet, carrier, android_id);
        }
    }
}
