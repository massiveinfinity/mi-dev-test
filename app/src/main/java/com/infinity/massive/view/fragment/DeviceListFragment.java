package com.infinity.massive.view.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.infinity.massive.ApplicationMassiveInfinity;
import com.infinity.massive.R;
import com.infinity.massive.controller.service.DeviceDownloadService;
import com.infinity.massive.model.adapter.DeviceListViewAdapter;
import com.infinity.massive.model.pojo.Devices;
import com.infinity.massive.utils.DataBaseClient;

import java.util.List;

/**
 * Created by Ilanthirayan on 25/1/16.
 */
public class DeviceListFragment extends Fragment{

    private static final String TAG = DeviceListFragment.class.getSimpleName();

    private DeviceListViewAdapter adapter;
    private ProgressBar progressBar;
    private TextView emptyListText;
    private Activity mActivity;

    private RecyclerView mRecyclerView;

    private StaggeredGridLayoutManager mStaggeredGridLayoutManager ;
    private List<Devices> devicesList;


    public static DeviceListFragment getInstance(Activity mActivity){
        DeviceListFragment fragment = new DeviceListFragment();
        fragment.mActivity = mActivity;
        return fragment;
    }

    final BroadcastReceiver downloadFinishedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            displayEmptyOrList();
        }
    };

    @Override
    public final void onDestroy(){
        super.onDestroy();
        ApplicationMassiveInfinity.getLbm().unregisterReceiver(downloadFinishedReceiver);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        DeviceDownloadService.start(false, null);
    }

    protected final void displayEmptyOrList() {
        if (mActivity == null)
            return;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final int downloading = DeviceDownloadService.getRemainingBlockRenderProductLayoutDownloads();

                if (downloading > 0) {
                    progressBar.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    emptyListText.setVisibility(View.GONE);
                    Log.d(TAG, "@@displayEmptyOrList showing progress bar");
                    return;
                }

                if (!DataBaseClient.getInstance().hasAnyDevices()) {
                    progressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                    emptyListText.setVisibility(View.VISIBLE);
                    Log.d(TAG, "@@displayEmptyOrList showing empty device list message");
                    return;
                }

                //NORMAL DISPLAY
                progressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                emptyListText.setVisibility(View.GONE);
                initializeAdapter();
                Log.d(TAG, "@@displayEmptyOrList showing the device list");

            }
        });
    }

    private void initializeAdapter(){
        //initialise here otherwise list will be null
        devicesList = DataBaseClient.getInstance().getDevices();
        adapter = new DeviceListViewAdapter(getActivity(), devicesList);
        mRecyclerView.setAdapter(adapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_device, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progressDeviceLayout);
        emptyListText = (TextView) view.findViewById(R.id.device_empty_list_msg);
        ApplicationMassiveInfinity.getLbm().registerReceiver(downloadFinishedReceiver,
                new IntentFilter(DeviceDownloadService.ACTION_BLOCK_RENDER_DEVICE_DOWNLOAD_FINISHED));

        //CARD VIEW
        mRecyclerView = (RecyclerView) view.findViewById(R.id.device_recycle_list);
        mRecyclerView.setHasFixedSize(true);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);

        return view;
    }
}
