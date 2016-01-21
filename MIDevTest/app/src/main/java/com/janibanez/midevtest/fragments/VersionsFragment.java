package com.janibanez.midevtest.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.janibanez.midevtest.MainActivity;
import com.janibanez.midevtest.R;
import com.janibanez.midevtest.adapters.VersionsListAdapter;
import com.janibanez.server.http.response.DbResponse;
import com.janibanez.server.models.Version;

import java.util.ArrayList;

/**
 * Created by jwgibanez on 21/01/2016.
 */
public class VersionsFragment extends Fragment implements MainActivity.MainUpdateListener {

    MainActivity mActivity;
    ListView mList;
    VersionsListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
        mActivity.addUpdateListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_versions, container, false);

        mAdapter = new VersionsListAdapter(mActivity, new ArrayList<Version>());

        mList = (ListView) rootView.findViewById(R.id.list);
        mList.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.removeUpdateListener(this);
    }

    @Override
    public void onUpdate(DbResponse response) {
        if (response != null) {
            mAdapter.clear();
            mAdapter.addAll(response.android);
            mAdapter.notifyDataSetChanged();
        }
    }
}
