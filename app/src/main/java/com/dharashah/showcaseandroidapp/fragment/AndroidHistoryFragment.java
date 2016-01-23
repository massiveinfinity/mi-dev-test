package com.dharashah.showcaseandroidapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dharashah.showcaseandroidapp.R;
import com.dharashah.showcaseandroidapp.ShowCaseApp;
import com.dharashah.showcaseandroidapp.activity.MainActivity;
import com.dharashah.showcaseandroidapp.adapter.VersionsAdapter;
import com.dharashah.showcaseandroidapp.callback.IDataFunctionListener;
import com.dharashah.showcaseandroidapp.callback.ILoadListener;
import com.dharashah.showcaseandroidapp.db.DBHelper;
import com.dharashah.showcaseandroidapp.model.AllData;
import com.dharashah.showcaseandroidapp.model.AndroidHistory;
import com.dharashah.showcaseandroidapp.network.RestClient;

import java.util.LinkedList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Dhara Shah on 22/01/2016.
 */
public class AndroidHistoryFragment extends Fragment implements  ILoadListener,
        SwipeRefreshLayout.OnRefreshListener, Callback<List<AndroidHistory>>, IDataFunctionListener {
    private static AndroidHistoryFragment mFragment;
    private View mView;
    private ListView mListView;
    private ILoadListener mListener;
    private DBHelper mDbHelper;
    private LinkedList<AndroidHistory> mAndroidHistoryList;
    private SwipeRefreshLayout mRefreshLayout;
    private Callback<List<AndroidHistory>> mCallback;
    private VersionsAdapter mAdapter;
    private IDataFunctionListener mDeleteListener;

    public static AndroidHistoryFragment newInstance() {
        mFragment = new AndroidHistoryFragment();
        return mFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_history, container, false);
        initView();
        return mView;
    }

    private void initView() {
        mDeleteListener = this;
        mCallback = this;
        mListener = this;
        mDbHelper = DBHelper.getInstance(ShowCaseApp.getAppContext());
        mListView = (ListView)mView.findViewById(android.R.id.list);
        mRefreshLayout = (SwipeRefreshLayout)mView.findViewById(R.id.swipeRefreshLayout);
        mRefreshLayout.setOnRefreshListener(this);
        mAndroidHistoryList = new LinkedList<>();
        ((MainActivity)getActivity()).setLoadListener(mListener);
        mAdapter = new VersionsAdapter(ShowCaseApp.getAppContext(), mAndroidHistoryList, mDeleteListener);
        mListView.setAdapter(mAdapter);
        onLoadData();
    }

    @Override
    public void onLoadData() {
        // get data from the database and load here
        LinkedList<AndroidHistory> histories = mDbHelper.getAndroidVersions(" DESC ");
        mAndroidHistoryList.clear();
        mAndroidHistoryList.addAll(histories);

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
        List<AndroidHistory> historyList = data.getAndroidHistoryList();
        mAndroidHistoryList.clear();
        mAndroidHistoryList.addAll(historyList);

        if(mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        int startCounter = (mAndroidHistoryList == null) ? 0 : mAndroidHistoryList.size();
        int endCounter = startCounter +  10;

        if(RestClient.isNetworkAvailable()) {
            Call<List<AndroidHistory>> call =
                    new RestClient().getApiService().getAndroidVersions(startCounter, endCounter);
            call.enqueue(mCallback);
        }else {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onResponse(final Response<List<AndroidHistory>> response, Retrofit retrofit) {
        if(response.body() != null) {
            LinkedList<AndroidHistory> historyList = new LinkedList<>();
            historyList.addAll(response.body());
            historyList.addAll(mAndroidHistoryList);
            mAndroidHistoryList.clear();
            mAndroidHistoryList.addAll(historyList);
            if(mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    mDbHelper.addVersions(response.body());
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
        mDbHelper.deleteAndroidVersion(id);

        Call deleteCall = new RestClient().getApiService().deleteVersion(id);
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

    }
}
