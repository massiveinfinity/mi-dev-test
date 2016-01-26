package com.mi.androidarsenal.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mi.androidarsenal.R;
import com.mi.androidarsenal.adapter.VersionsAdapter;
import com.mi.androidarsenal.database.DatabaseHandler;
import com.mi.androidarsenal.database.SharedPreferenceUtil;
import com.mi.androidarsenal.network.RequestOperation;
import com.mi.androidarsenal.utility.AppConstants;
import com.mi.androidarsenal.utility.AppUtils;
import com.mi.androidarsenal.utility.OfflineUtil;

/**
 * This fragment shows the versions list
 *
 * @author Samir Sarosh
 */
@SuppressWarnings("ALL")
public class AndroidVersionsFragment extends Fragment implements RequestOperation.DeleteItemFromVersionObserver, AppConstants, OfflineUtil.RefreshVersionListObserver, AdapterView.OnItemClickListener{
    private VersionsAdapter mVersionsAdapter;
    private LinearLayout mLoadingViewGroup;
    private OnVersionSelectedListener mOnVersionSelectedListener;

    public interface OnVersionSelectedListener {
        public void onVersionItemSelected(String id, String name, String version, String codename, String target, String distribution);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OfflineUtil.setRefreshVersionListObserver(this);
        RequestOperation.setDeleteItemFromVersionObserver(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mOnVersionSelectedListener = (OnVersionSelectedListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_android_versions, container, false);

        mLoadingViewGroup = (LinearLayout) view
                .findViewById(R.id.loading_layout);
        ProgressBar progressBar = (ProgressBar) view
                .findViewById(R.id.progress_loading_bar);

        int color = AppUtils.getColorFromRes(getActivity(), R.color.colorPrimary);
        progressBar.getIndeterminateDrawable().setColorFilter(color,
                android.graphics.PorterDuff.Mode.SRC_IN);

        ListView versionListView = (ListView) view.findViewById(R.id.version_listview);

        Cursor versionCursor = DatabaseHandler.getDatabaseHandlerInstance(getActivity()).getVersionsCursor();
        // Setup cursor adapter using cursor from last step
        mVersionsAdapter = new VersionsAdapter(getActivity(), versionCursor);
        // Attach cursor adapter to the ListView
        versionListView.setAdapter(mVersionsAdapter);

        versionListView.setOnItemClickListener(this);
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
    public void onDeleteItemFromVersionTable(String id) {
        DatabaseHandler.getDatabaseHandlerInstance(getActivity()).deleteRow(TABLE_VERSIONS, KEY_VERSION_ID, id);
        refreshList();
    }



    @Override
    public void onRefreshVersionList() {
        refreshList();
    }

    /**
     *
     */
    private void refreshList(){
        Cursor versionCursor = DatabaseHandler.getDatabaseHandlerInstance(getActivity()).getVersionsCursor();
        mVersionsAdapter.changeCursor(versionCursor);
        mVersionsAdapter.notifyDataSetChanged();
        mLoadingViewGroup.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
        Cursor cursor = (Cursor) mVersionsAdapter.getItem(position);
        String id = cursor.getString(cursor.getColumnIndexOrThrow(KEY_VERSION_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_VERSION_NAME));
        String version = cursor.getString(cursor.getColumnIndexOrThrow(KEY_VERSION));
        String codename = cursor.getString(cursor.getColumnIndexOrThrow(KEY_CODENAME));
        String target = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TARGET));
        String distribution = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESTRIBUTION));

        if (mOnVersionSelectedListener != null) {
            mOnVersionSelectedListener.onVersionItemSelected(id, name, version, codename, target, distribution);
        }
    }


}
