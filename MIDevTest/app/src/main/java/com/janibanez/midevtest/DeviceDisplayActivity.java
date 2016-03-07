package com.janibanez.midevtest;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.janibanez.midevtest.asynctasks.GetBitmapAsyncTask;
import com.janibanez.midevtest.models.ViewHolder;
import com.janibanez.midevtest.utilities.DialogUtilities;
import com.janibanez.midevtest.utilities.ImageCache;
import com.janibanez.server.ICallback;
import com.janibanez.server.MiApi;
import com.janibanez.server.models.Device;

import java.io.IOException;

/**
 * Created by jwgibanez on 22/01/2016.
 */
public class DeviceDisplayActivity extends AppCompatActivity {

    private static final int REQUEST_EDIT = 100;

    Device mData;
    ImageCache mImageCache;

    LinearLayout mImageLayout;
    TextView mName, mAndroidId, mSnippet, mCarrier;
    ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_device);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MainApplication application = (MainApplication) getApplication();
        mImageCache = application.getImageCache();

        mName = (TextView) findViewById(R.id.name);
        mAndroidId = (TextView) findViewById(R.id.android_id);
        mSnippet = (TextView) findViewById(R.id.snippet);
        mCarrier = (TextView) findViewById(R.id.carrier);
        mImageLayout = (LinearLayout) findViewById(R.id.li_image);
        mImage = (ImageView) findViewById(R.id.image);

        mData = (Device) getIntent().getSerializableExtra("device");

        initFields();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display_device, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_edit:
                Intent intent = new Intent(this, DeviceEditActivity.class);
                intent.putExtra("device", mData);
                startActivityForResult(intent, REQUEST_EDIT);
                break;
            case R.id.action_delete:
                showConfirmation();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_EDIT:
                if (resultCode == MainActivity.RESULT_REFRESH) {
                    // set result to update main activity
                    setResult(MainActivity.RESULT_REFRESH);

                    Device updatedDevice = (Device) data.getSerializableExtra("device");
                    if (updatedDevice != null) {
                        // update view
                        mData = updatedDevice;
                        initFields();
                    }
                }
                break;
        }
    }

    private void initFields() {
        if (mData != null) {
            if (!TextUtils.isEmpty(mData.name))
                mName.setText(mData.name);

            if (!TextUtils.isEmpty(mData.androidId))
                mAndroidId.setText(String.valueOf(mData.androidId));

            if (!TextUtils.isEmpty(mData.snippet))
                mSnippet.setText(mData.snippet);

            if (!TextUtils.isEmpty(mData.carrier))
                mCarrier.setText(mData.carrier);

            if (!TextUtils.isEmpty(mData.imageUrl))
                loadImage(mData.imageUrl);
        }
    }

    private void showConfirmation() {
        DialogUtilities.showConfirmation(
                this,
                getString(R.string.dialog_title_alert),
                getString(R.string.dialog_message_delete_device),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // neutral click
                        dialog.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive click
                        deleteDevice();
                        dialog.dismiss();
                    }
                });
    }

    private void deleteDevice() {
        MiApi api = new MiApi(this);

        api.call(MiApi.Action.DeleteDevice, mData.id, null, new ICallback() {
            @Override
            public void onFailure(Throwable throwable) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtilities.showMessageDialog(
                                DeviceDisplayActivity.this,
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
                DeviceDisplayActivity.this.finish();
            }
        });
    }

    private void loadImage(String url) {

        Bitmap bitmap = mImageCache.getBitmapFromMemCache(url);

        if (bitmap != null) {
            mImageLayout.setVisibility(View.VISIBLE);
            mImage.setImageBitmap(bitmap);
        } else {
            GetBitmapAsyncTask task = new GetBitmapAsyncTask(url, null, new GetBitmapAsyncTask.GetBitmapCallback() {
                @Override
                public void onSuccess(String url, ViewHolder holder, Bitmap bitmap) {
                    if (bitmap != null) {
                        mImageLayout.setVisibility(View.VISIBLE);
                        mImage.setImageBitmap(bitmap);
                    }
                }
            });
            task.execute();
        }
    }
}
