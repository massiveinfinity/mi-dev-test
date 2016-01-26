package com.infinity.massive.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.infinity.massive.R;
import com.infinity.massive.controller.AndroidVersionController;
import com.infinity.massive.model.pojo.Android;
import com.infinity.massive.model.pojo.Devices;
import com.infinity.massive.utils.DataBaseClient;
import com.squareup.picasso.Picasso;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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
    private Android androidVersionObj;

    private ImageView deviceImg;
    private TextView deviceNameTxt;
    private TextView deviceIdTxt;
    private TextView deviceDescriptionTxt;
    private TextView androidVersionTxt;


    //ANDROID VERSION
    private TextView androidVersionCodeNameTxt;
    private TextView androidVersionName;
    private TextView androidVersionTarget;
    private TextView androidVersionDistribution;
    private TextView androidVersion;



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

        androidVersionCodeNameTxt = (TextView) findViewById(R.id.android_version_code_name_txt);
        androidVersionName  = (TextView) findViewById(R.id.android_version_name_txt);
        androidVersionTarget = (TextView) findViewById(R.id.android_version_target_txt);
        androidVersionDistribution = (TextView) findViewById(R.id.android_version_distribution_txt);
        androidVersion = (TextView) findViewById(R.id.android_version_version_txt);

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
            getAndroidVersionDetails();
        }else{
            loadingProgress.setVisibility(View.GONE);
            emptyMessageTxt.setVisibility(View.VISIBLE);
        }
    }

    private void getAndroidVersionDetails() {
        AndroidVersionController mAndroidVersionController = new AndroidVersionController();
        Call<Android> response = mAndroidVersionController.getProductDetailsService(device.getAndroidId());
        response.enqueue(new Callback<Android>() {
            @Override
            public void onResponse(Response<Android> response, Retrofit retrofit) {
                Log.d(TAG, "Response Raw :: " + response.raw());
                if (response.body() != null) {
                    //SUCCESSFULLY ANDROID VERSION RECEIVE
                    androidVersionObj = response.body();

                    androidVersionCodeNameTxt.setText(androidVersionObj.getCodename());
                    androidVersionName.setText(androidVersionObj.getName());
                    androidVersionTarget.setText(androidVersionObj.getTarget());
                    androidVersionDistribution.setText(androidVersionObj.getDistribution());
                    androidVersion.setText(androidVersionObj.getVersion());
                }else{
                    findViewById(R.id.android_version_layout).setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Throwable e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                findViewById(R.id.android_version_layout).setVisibility(View.GONE);
            }
        });
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
