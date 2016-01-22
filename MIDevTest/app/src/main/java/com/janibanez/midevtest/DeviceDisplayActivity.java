package com.janibanez.midevtest;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.janibanez.midevtest.asynctasks.GetBitmapAsyncTask;
import com.janibanez.midevtest.models.ViewHolder;
import com.janibanez.midevtest.utilities.ImageCache;
import com.janibanez.server.models.Device;

/**
 * Created by jwgibanez on 22/01/2016.
 */
public class DeviceDisplayActivity extends AppCompatActivity {

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

        Device data = (Device) getIntent().getSerializableExtra("device");

        if (data != null) {
            if (!TextUtils.isEmpty(data.name))
                name.setText(data.name);

            if (data.androidId > 0)
                androidId.setText(String.valueOf(data.androidId));

            if (!TextUtils.isEmpty(data.snippet))
                snippet.setText(data.snippet);

            if (!TextUtils.isEmpty(data.carrier))
                carrier.setText(data.carrier);

            if (!TextUtils.isEmpty(data.imageUrl))
                loadImage(data.imageUrl);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
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
