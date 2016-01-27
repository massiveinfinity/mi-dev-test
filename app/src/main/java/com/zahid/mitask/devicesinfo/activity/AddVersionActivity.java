package com.zahid.mitask.devicesinfo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.zahid.mitask.devicesinfo.R;
import com.zahid.mitask.devicesinfo.bean.Device;
import com.zahid.mitask.devicesinfo.bean.Version;
import com.zahid.mitask.devicesinfo.helper.AlertDialogHelper;
import com.zahid.mitask.devicesinfo.helper.ProgressDialogHelper;
import com.zahid.mitask.devicesinfo.helper.WebServiceHelper;
import com.zahid.mitask.devicesinfo.interfaces.DialogClickListener;
import com.zahid.mitask.devicesinfo.interfaces.OnWebServiceListener;
import com.zahid.mitask.devicesinfo.utils.Constants;
import com.zahid.mitask.devicesinfo.utils.Validator;

public class AddVersionActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceListener, DialogClickListener {

    private static final int TAG_ADD_VERSION = 1000;
    private EditText edtName, edtVersionCode, edtCodeName, edtTarget, edtDistribution;
    private Button btnAdd;
    private WebServiceHelper webServiceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private int versionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_version);
        initialize();
        versionId = getIntent().getIntExtra("versionId", 0);
    }

    // Initialize view and objects
    private void initialize() {
        edtName = (EditText) findViewById(R.id.editText_name);
        edtVersionCode = (EditText) findViewById(R.id.editText_code);
        edtTarget = (EditText) findViewById(R.id.editText_target);
        edtCodeName = (EditText) findViewById(R.id.editText_code_name);
        edtDistribution = (EditText) findViewById(R.id.editText_distribution);
        btnAdd = (Button) findViewById(R.id.button);
        btnAdd.setText(getString(R.string.button_add));
        btnAdd.setOnClickListener(this);

        progressDialogHelper = new ProgressDialogHelper(this);
        webServiceHelper = new WebServiceHelper(this);
        webServiceHelper.setOnWebServiceListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == btnAdd) {
            if (validateFields()) {
                progressDialogHelper.showProgressDialog(getString(R.string.loading));
                Version version = new Version();
                if (versionId != 0)
                    version.setId(versionId);
                version.setName(edtName.getText().toString());
                version.setTarget(edtTarget.getText().toString());
                version.setVersion(edtVersionCode.getText().toString());
                version.setCodename(edtCodeName.getText().toString());
                version.setDistribution(edtDistribution.getText().toString());
                Gson gson = new Gson();
                String params = gson.toJson(version);
                // POST service call to add new version
                webServiceHelper.makeJsonObjectRequest(Constants.ANDROID_VERSIONS_URL, Request.Method.POST,
                        params, TAG_ADD_VERSION);
            }
        }
    }

    @Override
    public void onWebServiceResponse(String response, int tag) {
        progressDialogHelper.hideProgressDialog();
        if (tag == TAG_ADD_VERSION) {
            // Upon success response, retrieve the added version object and send it to previous activity
            Gson gson = new Gson();
            Version version = gson.fromJson(response.toString(), Version.class);
            Intent intent = new Intent();
            intent.putExtra("versionObj", version);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onWebServiceError(String error, int tag) {
        progressDialogHelper.hideProgressDialog();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    public boolean validateFields() {
        if (!Validator.checkNullString(edtName.getText().toString())) {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.alert_version_name_empty));
            return false;
        } else if (!Validator.checkNullString(edtVersionCode.getText().toString())) {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.alert_version_code_empty));
            return false;
        } else if (!Validator.checkNullString(edtCodeName.getText().toString())) {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.alert_code_name_empty));
            return false;
        } else if (!Validator.checkNullString(edtTarget.getText().toString())) {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.alert_target_empty));
            return false;
        } else if (!Validator.checkNullString(edtDistribution.getText().toString())) {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.alert_distribution_empty));
            return false;
        }
        return true;
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }
}
