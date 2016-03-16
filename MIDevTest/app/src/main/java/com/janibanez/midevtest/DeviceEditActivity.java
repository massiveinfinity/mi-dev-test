package com.janibanez.midevtest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.janibanez.midevtest.utilities.DialogUtilities;
import com.janibanez.server.ICallback;
import com.janibanez.server.MiApi;
import com.janibanez.server.models.Device;

import org.w3c.dom.Text;

import java.io.IOException;

/**
 * Created by jwgibanez on 23/01/2016.
 */
public class DeviceEditActivity extends AppCompatActivity {

    Device mData;

    EditText mName, mAndroidId, mSnippet, mCarrier, mImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_device);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mData = (Device) getIntent().getSerializableExtra("device");

        mName = (EditText) findViewById(R.id.name);
        mAndroidId = (EditText) findViewById(R.id.android_id);
        mSnippet = (EditText) findViewById(R.id.snippet);
        mCarrier = (EditText) findViewById(R.id.carrier);
        mImageUrl = (EditText) findViewById(R.id.image_url);

        initFields();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_device, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_create:
                if (mData != null) {
                    if (mData.id > 0) {
                        // put to update device
                        updateDevice();
                        break;
                    }
                }
                // post to create new device
                createDevice();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFields() {
        if (mData != null) {
            if (!TextUtils.isEmpty(mData.name))
                mName.setText(mData.name);

            if (!TextUtils.isEmpty(mData.androidId))
                mAndroidId.setText(mData.androidId);

            if (!TextUtils.isEmpty(mData.snippet))
                mSnippet.setText(mData.snippet);

            if (!TextUtils.isEmpty(mData.carrier))
                mCarrier.setText(mData.carrier);

            if (!TextUtils.isEmpty(mData.imageUrl))
                mImageUrl.setText(mData.imageUrl);
        }
    }

    private boolean isInfoValid() {

        mData.name = mName.getText().toString();
        mData.snippet = mSnippet.getText().toString();
        mData.carrier = mCarrier.getText().toString();
        mData.imageUrl = mImageUrl.getText().toString();
        mData.androidId = mAndroidId.getText().toString();

        if (TextUtils.isEmpty(mData.name)) {
            Toast.makeText(this, "Name is empty.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (TextUtils.isEmpty(mData.androidId)) {
            Toast.makeText(this, "Android ID cannot be empty or 0.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (TextUtils.isEmpty(mData.snippet)) {
            Toast.makeText(this, "Snippet is empty.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (TextUtils.isEmpty(mData.carrier)) {
            Toast.makeText(this, "Carrier is empty.", Toast.LENGTH_LONG).show();
            return false;
        }

        //if (TextUtils.isEmpty(mData.imageUrl)) {
        //    Toast.makeText(this, "Image URL is empty.", Toast.LENGTH_LONG).show();
        //    return false;
        //}

        return true;
    }

    private void createDevice() {

        if (mData == null) {
            mData = new Device();
        }

        if (!isInfoValid())
            // abort
            return;

        MiApi api = new MiApi(this);

        api.call(MiApi.Action.CreateDevice, 0, mData, new ICallback() {
            @Override
            public void onFailure(Throwable throwable) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtilities.showMessageDialog(
                                DeviceEditActivity.this,
                                getString(R.string.dialog_title_alert),
                                getString(R.string.dialog_message_something_went_wrong),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                    }
                });
            }

            @Override
            public void onResponse(Object response) throws IOException {
                setResult(MainActivity.RESULT_REFRESH);
                DeviceEditActivity.this.finish();
            }
        });
    }

    private void updateDevice() {

        if (!isInfoValid())
            // abort
            return;

        MiApi api = new MiApi(this);

        api.call(MiApi.Action.UpdateDevice, mData.id, mData, new ICallback<Device>() {
            @Override
            public void onFailure(Throwable throwable) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtilities.showMessageDialog(
                                DeviceEditActivity.this,
                                getString(R.string.dialog_title_alert),
                                getString(R.string.dialog_message_something_went_wrong),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                    }
                });
            }

            @Override
            public void onResponse(Device response) throws IOException {
                Intent intent = new Intent();
                intent.putExtra("device", response);
                setResult(MainActivity.RESULT_REFRESH, intent);
                DeviceEditActivity.this.finish();
            }
        });
    }

}
