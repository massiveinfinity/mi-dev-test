package com.janibanez.midevtest;

import android.content.DialogInterface;
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

    Device mData;
    ImageCache mImageCache;

    LinearLayout mImageLayout;
    ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_device);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MainApplication application = (MainApplication) getApplication();
        mImageCache = application.getImageCache();

        TextView name = (TextView) findViewById(R.id.name);
        TextView androidId = (TextView) findViewById(R.id.android_id);
        TextView snippet = (TextView) findViewById(R.id.snippet);
        TextView carrier = (TextView) findViewById(R.id.carrier);
        mImageLayout = (LinearLayout) findViewById(R.id.li_image);
        mImage = (ImageView) findViewById(R.id.image);

        mData = (Device) getIntent().getSerializableExtra("device");

        if (mData != null) {
            if (!TextUtils.isEmpty(mData.name))
                name.setText(mData.name);

            if (mData.androidId > 0)
                androidId.setText(String.valueOf(mData.androidId));

            if (!TextUtils.isEmpty(mData.snippet))
                snippet.setText(mData.snippet);

            if (!TextUtils.isEmpty(mData.carrier))
                carrier.setText(mData.carrier);

            if (!TextUtils.isEmpty(mData.imageUrl))
                loadImage(mData.imageUrl);
        }
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
            case R.id.action_delete:
                showConfirmation();
                break;
        }
        return super.onOptionsItemSelected(item);
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
