package com.janibanez.midevtest;

import android.content.DialogInterface;
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

import java.io.IOException;

/**
 * Created by jwgibanez on 23/01/2016.
 */
public class DeviceEditActivity extends AppCompatActivity {

    EditText mName, mAndroidId, mSnippet, mCarrier, mImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_device);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mName = (EditText) findViewById(R.id.name);
        mAndroidId = (EditText) findViewById(R.id.android_id);
        mSnippet = (EditText) findViewById(R.id.snippet);
        mCarrier = (EditText) findViewById(R.id.carrier);
        mImageUrl = (EditText) findViewById(R.id.image_url);

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
                createDevice();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createDevice() {

        String name = mName.getText().toString();
        String androidId = mAndroidId.getText().toString();
        String snippet = mSnippet.getText().toString();
        String carrier = mCarrier.getText().toString();
        String imageUrl = mImageUrl.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Name is empty.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(androidId)) {
            Toast.makeText(this, "Android ID is empty.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(snippet)) {
            Toast.makeText(this, "Snippet is empty.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(carrier)) {
            Toast.makeText(this, "Carrier is empty.", Toast.LENGTH_LONG).show();
            return;
        }

        //if (TextUtils.isEmpty(imageUrl)) {
        //    Toast.makeText(this, "Image URL is empty.", Toast.LENGTH_LONG).show();
        //    return;
        //}

        Device data = new Device(name, Integer.parseInt(androidId), snippet, carrier, imageUrl);

        MiApi api = new MiApi(this);

        api.call(MiApi.Action.CreateDevice, 0, data, new ICallback() {
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

}
