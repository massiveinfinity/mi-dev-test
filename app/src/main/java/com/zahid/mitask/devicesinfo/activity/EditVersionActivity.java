package com.zahid.mitask.devicesinfo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class EditVersionActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceListener, DialogClickListener {

    private static final int TAG_EDIT_VERSION = 1000;
    private EditText edtName, edtVersionCode, edtCodeName, edtTarget, edtDistribution;
    private Button btnSave;
    private WebServiceHelper webServiceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private Version version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_version);
        version = (Version) getIntent().getSerializableExtra("versionObj");

        initialize();
        populateViews();
    }

    // Initialize views and objects
    private void initialize() {
        edtName = (EditText) findViewById(R.id.editText_name);
        edtVersionCode = (EditText) findViewById(R.id.editText_code);
        edtTarget = (EditText) findViewById(R.id.editText_target);
        edtCodeName = (EditText) findViewById(R.id.editText_code_name);
        edtDistribution = (EditText) findViewById(R.id.editText_distribution);
        btnSave = (Button) findViewById(R.id.button);
        btnSave.setText(getString(R.string.button_save));
        btnSave.setOnClickListener(this);


        progressDialogHelper = new ProgressDialogHelper(this);
        webServiceHelper = new WebServiceHelper(this);
        webServiceHelper.setOnWebServiceListener(this);
    }

    // Populate edittext fields with existing verison information
    private void populateViews() {
        if (version.getName() != null)
            edtName.setText(version.getName());
        if (version.getCodename() != null)
            edtCodeName.setText(version.getCodename());
        if (version.getVersion() != null)
            edtVersionCode.setText(version.getVersion());
        if (version.getTarget() != null)
            edtTarget.setText(version.getTarget());
        if (version.getDistribution() != null)
            edtDistribution.setText(version.getDistribution());
    }


    @Override
    public void onClick(View v) {
        if (v == btnSave) {
            if (validateFields()) {
                progressDialogHelper.showProgressDialog(getString(R.string.loading));
                version.setName(edtName.getText().toString());
                version.setTarget(edtTarget.getText().toString());
                version.setVersion(edtVersionCode.getText().toString());
                version.setCodename(edtCodeName.getText().toString());
                version.setDistribution(edtDistribution.getText().toString());
                Gson gson = new Gson();
                String params = gson.toJson(version);
                // PUT service to update version information
                webServiceHelper.makeJsonObjectRequest(Constants.ANDROID_VERSIONS_URL + version.getId(), Request.Method.PUT,
                        params, TAG_EDIT_VERSION);
            }
        }
    }

    @Override
    public void onWebServiceResponse(String response, int tag) {
        progressDialogHelper.hideProgressDialog();
        if (tag == TAG_EDIT_VERSION) {
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
