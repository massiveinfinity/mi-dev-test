package com.zahid.mitask.devicesinfo.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.zahid.mitask.devicesinfo.R;
import com.zahid.mitask.devicesinfo.bean.Device;
import com.zahid.mitask.devicesinfo.bean.Version;
import com.zahid.mitask.devicesinfo.helper.AlertDialogHelper;
import com.zahid.mitask.devicesinfo.helper.DatabaseHelper;
import com.zahid.mitask.devicesinfo.helper.ProgressDialogHelper;
import com.zahid.mitask.devicesinfo.helper.WebServiceHelper;
import com.zahid.mitask.devicesinfo.interfaces.DialogClickListener;
import com.zahid.mitask.devicesinfo.interfaces.OnWebServiceListener;
import com.zahid.mitask.devicesinfo.tasks.SaveVersionDataInDBTask;
import com.zahid.mitask.devicesinfo.utils.Constants;

import java.util.ArrayList;

public class DeviceDetailActivity extends AppCompatActivity implements OnWebServiceListener, DialogClickListener {

    private static final int TAG_GET_ANDROID_VERSION = 1000;
    private static final int TAG_ADD_VERSION = 101;
    private ImageView mImageView;
    private TextView deviceName, deviceDesc, versionName, versionCode;
    private Device deviceObj;
    private ProgressDialogHelper progressDialogHelper;
    private WebServiceHelper webServiceHelper;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);

        // Get selected device object
        deviceObj = (Device) getIntent().getSerializableExtra("deviceObj");
        setTitle(deviceObj.getName());

        initialize();
        // Fetch Android version of selected device from the Database
        new FetchVersionTask().execute(deviceObj.getAndroidId());

        if (!deviceObj.getImageUrl().isEmpty()) {
            Picasso.with(this).load(deviceObj.getImageUrl())
                    .placeholder(R.drawable.placeholder_small).error(R.drawable.placeholder_small).into(mImageView);
        }
        deviceName.setText(deviceObj.getName());
        deviceDesc.setText(deviceObj.getSnippet());
    }

    // Initialize views and objects
    private void initialize() {
        mImageView = (ImageView) findViewById(R.id.imageView_logo);
        deviceName = (TextView) findViewById(R.id.textView_device_name);
        deviceDesc = (TextView) findViewById(R.id.textView_device_desc);
        versionName = (TextView) findViewById(R.id.textView_version_name);
        versionCode = (TextView) findViewById(R.id.textView_version_code);

        progressDialogHelper = new ProgressDialogHelper(this);
        webServiceHelper = new WebServiceHelper(this);
        webServiceHelper.setOnWebServiceListener(this);
        databaseHelper = new DatabaseHelper(this);
    }

    private void populateVersionFields(Version version) {
        versionName.setText(version.getName());
        versionCode.setText(version.getVersion());
    }

    @Override
    public void onWebServiceResponse(String response, int tag) {
        progressDialogHelper.hideProgressDialog();
        if (tag == TAG_GET_ANDROID_VERSION) {
            Gson gson = new Gson();
            Version version = gson.fromJson(response, Version.class);
            populateVersionFields(version);
        }
    }

    @Override
    public void onWebServiceError(String error, int tag) {
        progressDialogHelper.hideProgressDialog();
        // If version object is not found, prompt the user to add new version
        AlertDialogHelper.showCompoundAlertDialog(this, this, getString(R.string.create_new_version), getString(R.string.alert_button_yes),
                getString(R.string.alert_button_no), TAG_ADD_VERSION);

    }

    @Override
    public void onAlertPositiveClicked(int tag) {
        if (tag == TAG_ADD_VERSION) {
            Intent addIntent = new Intent(this, AddVersionActivity.class);
            addIntent.putExtra("versionId", deviceObj.getAndroidId());
            startActivityForResult(addIntent, TAG_ADD_VERSION);
        }
    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAG_ADD_VERSION && resultCode == RESULT_OK) {
            Version version = (Version) data.getSerializableExtra("versionObj");
            populateVersionFields(version);
            ArrayList<Version> tempList = new ArrayList<>();
            tempList.add(version);
            // Save the version object in the Database
            new SaveVersionDataInDBTask(this).execute(tempList);
        }
    }

    private class FetchVersionTask extends AsyncTask<Integer, Void, Version> {

        @Override
        protected Version doInBackground(Integer... params) {
            return databaseHelper.getVersionInformationFromDB(params[0]);
        }

        @Override
        protected void onPostExecute(Version version) {
            super.onPostExecute(version);
            if (version != null) {
                populateVersionFields(version);
            } else {
                 /*
                 *If version object does not exist in Database, make a service call to get the Android version
                 *information
                 **/
                progressDialogHelper.showProgressDialog(getString(R.string.loading));
                webServiceHelper.makeJsonObjectRequest(Constants.ANDROID_VERSIONS_URL + deviceObj.getAndroidId(),
                        Request.Method.GET, (String) null, TAG_GET_ANDROID_VERSION);
            }
        }
    }

}
