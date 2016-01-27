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
import com.zahid.mitask.devicesinfo.helper.AlertDialogHelper;
import com.zahid.mitask.devicesinfo.helper.ProgressDialogHelper;
import com.zahid.mitask.devicesinfo.helper.WebServiceHelper;
import com.zahid.mitask.devicesinfo.interfaces.DialogClickListener;
import com.zahid.mitask.devicesinfo.interfaces.OnWebServiceListener;
import com.zahid.mitask.devicesinfo.utils.Constants;
import com.zahid.mitask.devicesinfo.utils.Validator;

public class AddDeviceActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceListener, DialogClickListener {

    private static final int TAG_ADD_DEVICE = 1000;
    private EditText edtName, edtImageUrl, edtDescription, edtAndroidId;
    private Button btnAdd;
    private WebServiceHelper webServiceHelper;
    private ProgressDialogHelper progressDialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_device);
        initialize();
    }


    // Initialize views and objects
    private void initialize() {
        edtName = (EditText) findViewById(R.id.editText_name);
        edtImageUrl = (EditText) findViewById(R.id.editText_imageUrl);
        edtAndroidId = (EditText) findViewById(R.id.editText_androidId);
        edtDescription = (EditText) findViewById(R.id.editText_desc);
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
                Device device = new Device();
                device.setName(edtName.getText().toString());
                device.setAndroidId(Integer.parseInt(edtAndroidId.getText().toString()));
                device.setImageUrl(edtImageUrl.getText().toString());
                device.setSnippet(edtDescription.getText().toString());
                Gson gson = new Gson();
                String params = gson.toJson(device);
                // POST service call to add device
                webServiceHelper.makeJsonObjectRequest(Constants.DEVICES_URL, Request.Method.POST,
                        params, TAG_ADD_DEVICE);
            }
        }
    }

    @Override
    public void onWebServiceResponse(String response, int tag) {
        progressDialogHelper.hideProgressDialog();
        if (tag == TAG_ADD_DEVICE) {
            // Upon Success response, retrieve tha added device object and send it to previous activity
            Gson gson = new Gson();
            Device device = gson.fromJson(response.toString(), Device.class);
            Intent intent = new Intent();
            intent.putExtra("deviceObj", device);
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
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.alert_name_empty));
            return false;
        } else if (!Validator.checkNullString(edtAndroidId.getText().toString())) {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.alert_android_id_empty));
            return false;
        } else if (TextUtils.isDigitsOnly(edtDescription.getText().toString())) {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.alert_android_id_number));
            return false;
        } else if (!Validator.checkNullString(edtDescription.getText().toString())) {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.alert_description_empty));
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
