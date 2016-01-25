package com.sharad.demoapp.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sharad.demoapp.R;
import com.sharad.demoapp.adapter.PhoneDetailAdapter;
import com.sharad.demoapp.component.AlertManager;
import com.sharad.demoapp.entity.PhoneDetailEntity;
import com.sharad.demoapp.sync.SyncListener;
import com.sharad.demoapp.sync.SyncManager;
import com.sharad.demoapp.util.AlertView;
import com.sharad.demoapp.util.UtilityMethod;

/**
 * Created by SharadW on 22-01-2016.
 */
public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SyncListener mSyncListener;
    private SyncManager mSyncManager;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container,false);

        mContext = getActivity();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        getPhoneDetailsFromServer();
        return rootView;
    }

    private void getPhoneDetailsFromServer()
    {



        mSyncListener = null;
        mSyncManager = null;

        UtilityMethod.progressDialogShow(mContext);

        mSyncListener = new SyncListener() {

            boolean isShowingFailDialog = false;

            public void onSyncFailure(final int result, final int taskId, String message) {
                getActivity().runOnUiThread(new Runnable() {

                    public void run() {
                        UtilityMethod.progressDialogDismiss(getActivity());

                        if (result == SyncManager.SYNC_FAIL && !isShowingFailDialog) {
                            if (taskId == SyncManager.LOGIN)

                            {

                            } else {
                                DialogInterface.OnClickListener retryListener = new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        isShowingFailDialog = false;
                                    }
                                };

                                AlertManager.showNonCancelableAlert(mContext, "Error", "Sync Failed", "Retry", retryListener, "Close", null);
                                isShowingFailDialog = true;
                            }
                        } else if (result == SyncManager.NO_INTERNET && !isShowingFailDialog) {

                            DialogInterface.OnClickListener retryListener = new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    isShowingFailDialog = false;
                                }
                            };
                            AlertManager.showNonCancelableAlert(mContext, "Error", "Please make sure your Network Connection is ON", "Retry", retryListener, "Close", null);
                            isShowingFailDialog = true;

                        } else if (result == SyncManager.SERVER_DOWN && !isShowingFailDialog) {// @
                            AlertManager.showAlert(mContext, "Error", "No Response from Server!", "OK", null);
                            isShowingFailDialog = true;
                        }
                    }
                });
            }

            public void onSyncProgressUpdate(int progressPercent, String message) {

            }

            @Override
            public void onSyncSuccess(final int result,int taskId, final Object mObject) {

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        try {

                            UtilityMethod.progressDialogDismiss(getActivity());
                            if (result == SyncManager.SYNC_SUCCESS) {
                                if (mObject != null) {
                                    PhoneDetailEntity mPhoneDetailEntity = (PhoneDetailEntity) mObject;
                                    // specify an adapter
                                    mAdapter = new PhoneDetailAdapter(getActivity(),mPhoneDetailEntity);
                                    mRecyclerView.setAdapter(mAdapter);

                                } else {
                                    AlertView.showLongToast(mContext, (String) mObject.toString());
                                }

                            }
                        } catch (Exception e) {
                            Toast.makeText(mContext,"failed to download ,Please try again!!!",
                                    Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                });

            }
        };

        mSyncManager = new SyncManager(mContext, SyncManager.GET_PHONE_DETAILS, mSyncListener);
        mSyncManager.getPhoneDetaills();
    }
}
