package com.zahid.mitask.devicesinfo.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.zahid.mitask.devicesinfo.activity.AddVersionActivity;
import com.zahid.mitask.devicesinfo.activity.EditVersionActivity;
import com.zahid.mitask.devicesinfo.activity.VersionDetailActivity;
import com.zahid.mitask.devicesinfo.adapter.VersionListAdapter;
import com.zahid.mitask.devicesinfo.bean.Version;
import com.zahid.mitask.devicesinfo.helper.AlertDialogHelper;
import com.zahid.mitask.devicesinfo.helper.DatabaseHelper;
import com.zahid.mitask.devicesinfo.helper.ProgressDialogHelper;
import com.zahid.mitask.devicesinfo.helper.WebServiceHelper;
import com.zahid.mitask.devicesinfo.interfaces.DialogClickListener;
import com.zahid.mitask.devicesinfo.interfaces.OnWebServiceListener;
import com.zahid.mitask.devicesinfo.interfaces.PopUpMenuClickListener;
import com.zahid.mitask.devicesinfo.tasks.DeleteVersionFromTableTask;
import com.zahid.mitask.devicesinfo.tasks.SaveVersionDataInDBTask;
import com.zahid.mitask.devicesinfo.tasks.UpdateVersionInTableTask;
import com.zahid.mitask.devicesinfo.utils.Constants;
import com.zahid.mitask.devicesinfo.utils.Validator;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class VersionFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,
        OnWebServiceListener, View.OnClickListener, DialogClickListener, PopUpMenuClickListener {

    private static final int REQUEST_EDIT_VERSION = 100;
    private static final int REQUEST_ADD_VERSION = 101;
    private ListView mListView;
    private WebServiceHelper webServiceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private VersionListAdapter versionListAdapter;
    private FloatingActionButton floatingActionButton;
    private DatabaseHelper databaseHelper;

    private static final int TAG_GET_VERSION = 1000, TAG_DELETE_VERSION = 1001, TAG_VERSION_SETTINGS = 2000;
    private ArrayList<Version> versionArrayList;

    private int selectedVersionPosition;
    private String[] popUpMenuItems = {"Edit Version", "Delete Version"};


    public static VersionFragment newInstance() {
        VersionFragment frag = new VersionFragment();
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
            webServiceHelper.makeJSONArrayRequest(Constants.ANDROID_VERSIONS_URL, Request.Method.GET,
                    (String) null, TAG_GET_VERSION);
        } else {
            new FetchVersionsTask().execute();
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
        if (versionArrayList == null)
            return;
        if (versionListAdapter == null) {
            versionListAdapter = new VersionListAdapter(getActivity(), versionArrayList);
            mListView.setAdapter(versionListAdapter);
        } else {
            versionListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent detailIntent = new Intent(getActivity(), VersionDetailActivity.class);
        detailIntent.putExtra("versionObj", versionListAdapter.getItem(position));
        startActivity(detailIntent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        selectedVersionPosition = position;
        // Shows pop up dialog with "Edit version" and "Delete version" options
        PopUpDialogFragment popupDialogFragment = PopUpDialogFragment.newInstance(
                "", popUpMenuItems, TAG_VERSION_SETTINGS);
        popupDialogFragment.setOnPopUpMenuListener(this);

        try {
            popupDialogFragment.show(getActivity().getSupportFragmentManager(), "popup_dialog");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    @Override
    public void onWebServiceResponse(String response, int tag) {
        progressDialogHelper.hideProgressDialog();
        if (tag == TAG_GET_VERSION) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Version>>() {
            }.getType();
            versionArrayList = gson.fromJson(response, listType);
            // Save the versions list to the Database
            new SaveVersionDataInDBTask(getActivity()).execute(versionArrayList);
            updateListView();
        } else if (tag == TAG_DELETE_VERSION) {
            // Delete the version from the Database table
            new DeleteVersionFromTableTask(getActivity()).execute(versionArrayList.get(selectedVersionPosition));
            // Remove the selected version from the list and update the listview
            versionArrayList.remove(selectedVersionPosition);
            updateListView();
        }
    }

    @Override
    public void onWebServiceError(String error, int tag) {
        progressDialogHelper.hideProgressDialog();
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
        if (tag == TAG_GET_VERSION) {
            // If Internet connectivity fails, fetch the version list from the Database
            new FetchVersionsTask().execute();
        }
    }

    @Override
    public void onPopUpMenuItemClicked(int position, int tag) {
        if (tag == TAG_VERSION_SETTINGS) {
            if (position == 0) {
                Intent editIntent = new Intent(getActivity(), EditVersionActivity.class);
                editIntent.putExtra("versionObj", versionListAdapter.getItem(selectedVersionPosition));
                startActivityForResult(editIntent, REQUEST_EDIT_VERSION);
            } else {
                AlertDialogHelper.showCompoundAlertDialog(getActivity(), this, getString(R.string.version_delete_confirmation),
                        getString(R.string.alert_button_yes),
                        getString(R.string.alert_button_no), TAG_DELETE_VERSION);
            }
        }
    }

    @Override
    public void onAlertPositiveClicked(int tag) {
        if (tag == TAG_DELETE_VERSION) {
            progressDialogHelper.showProgressDialog(getString(R.string.loading));
            webServiceHelper.makeJsonObjectRequest(Constants.ANDROID_VERSIONS_URL + versionListAdapter.getItem(selectedVersionPosition).getId(),
                    Request.Method.DELETE, (String) null, TAG_DELETE_VERSION);
        }
    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_VERSION && resultCode == Activity.RESULT_OK) {
            Version version = (Version) data.getSerializableExtra("versionObj");
            versionArrayList.set(selectedVersionPosition, version);
            // Update the version list in the Database
            new UpdateVersionInTableTask(getActivity()).execute(version);
            updateListView();
        } else if (requestCode == REQUEST_ADD_VERSION && resultCode == Activity.RESULT_OK) {
            Version version = (Version) data.getSerializableExtra("versionObj");
            versionArrayList.add(version);
            ArrayList<Version> tempList = new ArrayList<>();
            tempList.add(version);
            // Save the version object in the Database
            new SaveVersionDataInDBTask(getActivity()).execute(tempList);
            updateListView();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == floatingActionButton) {
            // Add new version
            Intent addIntent = new Intent(getActivity(), AddVersionActivity.class);
            startActivityForResult(addIntent, REQUEST_ADD_VERSION);
        }
    }

    private class FetchVersionsTask extends AsyncTask<Void, Void, ArrayList<Version>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogHelper.showProgressDialog(getString(R.string.loading));
        }

        @Override
        protected ArrayList<Version> doInBackground(Void... params) {
            return databaseHelper.fetchVersionListFromDB();
        }

        @Override
        protected void onPostExecute(ArrayList<Version> versions) {
            super.onPostExecute(versions);
            progressDialogHelper.hideProgressDialog();
            versionArrayList = versions;
            updateListView();
        }
    }


}
