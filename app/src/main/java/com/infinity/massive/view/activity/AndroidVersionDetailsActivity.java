package com.infinity.massive.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.infinity.massive.R;
import com.infinity.massive.model.pojo.Devices;
import com.infinity.massive.utils.DataBaseClient;
import com.squareup.picasso.Picasso;

/**
 * Created by Ilanthirayan on 26/1/16.
 */
public class AndroidVersionDetailsActivity extends AppCompatActivity {

    private static final String TAG = AndroidVersionDetailsActivity.class.getSimpleName();

    public static final String EXTRAS_MASSIVE_INFINITY_DEVICE_ID = "extra_massive_infinity_device_id";

    private RelativeLayout emptyOrLoadingLayout;
    private ProgressBar loadingProgress;
    private TextView emptyMessageTxt;
    private Toolbar toolbar;

    private int device_id;
    private Devices device;

    private ImageView deviceImg;
    private TextView deviceNameTxt;
    private TextView deviceIdTxt;
    private TextView deviceDescriptionTxt;
    private TextView androidVersionTxt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_version_details);

        //ACTION TOOL BAR
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Android Version");

        emptyOrLoadingLayout = (RelativeLayout) findViewById(R.id.loading_empty_msg_layout);
        emptyMessageTxt = (TextView)  findViewById(R.id.empty_message_txt);
        loadingProgress = (ProgressBar) findViewById(R.id.progressAndroidVersionDetailsLayout);
        deviceNameTxt = (TextView) findViewById(R.id.device_name_txt);
        deviceIdTxt = (TextView) findViewById(R.id.device_description_txt);
        deviceDescriptionTxt = (TextView) findViewById(R.id.device_id_txt);
        androidVersionTxt = (TextView) findViewById(R.id.android_version_txt);
        deviceImg = (ImageView)findViewById(R.id.device_image);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            device_id = extras.getInt(EXTRAS_MASSIVE_INFINITY_DEVICE_ID);
            /*
             * Instead of getting over the internet can from SQLite
             * which has been save already.
             */
            getDeviceDetailsFromDataBase();
        }else{
            loadingProgress.setVisibility(View.GONE);
            emptyMessageTxt.setVisibility(View.VISIBLE);
        }


    }



    private void getDeviceDetailsFromDataBase(){
        device = DataBaseClient.getInstance().getDeviceDetails(device_id);
        if(device != null){
            emptyOrLoadingLayout.setVisibility(View.GONE);
            deviceNameTxt.setText(device.getName());
            deviceDescriptionTxt.setText(device.getSnippet());
            deviceIdTxt.setText("Device ID :: "+String.valueOf(device.getId()));
            androidVersionTxt.setText("Android ID :: " +device.getAndroidId());
            //Get the product image
            if(device.getImageUrl() != null) {
                Picasso.with(this).load(device.getImageUrl()).into(deviceImg);
            }
        }else{
            loadingProgress.setVisibility(View.GONE);
            emptyMessageTxt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
