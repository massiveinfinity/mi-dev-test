package com.dharashah.showcaseandroidapp.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dharashah.showcaseandroidapp.R;
import com.dharashah.showcaseandroidapp.ShowCaseApp;
import com.dharashah.showcaseandroidapp.adapter.AndroidVersionsAdapter;
import com.dharashah.showcaseandroidapp.db.DBHelper;
import com.dharashah.showcaseandroidapp.model.AndroidHistory;
import com.dharashah.showcaseandroidapp.model.Devices;
import com.dharashah.showcaseandroidapp.network.RestClient;
import com.dharashah.showcaseandroidapp.utility.CustomInputValidator;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Dhara Shah on 22/01/2016.
 */
public class AddDeviceActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private MaterialEditText mEtDeviceName;
    private MaterialEditText mEtDeviceDesc;
    private MaterialEditText mEtAndroidVersion;
    private LinearLayout mLinDeviceName;
    private LinearLayout mLinDeviceDesc;
    private LinearLayout mLinAndroidVersion;
    private DBHelper mDbHelper;
    private int mAndroidVersionId;
    private Toolbar mToolbar;
    private TextView mTxtTitle;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        initToolbar();
        initView();
    }

    private void initToolbar() {
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mTxtTitle = (TextView)findViewById(R.id.txtTitle);
        mToolbar.setNavigationIcon(R.drawable.abc_ic_clear_mtrl_alpha);
        mTxtTitle.setText("Add Device");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        mEtDeviceName = (MaterialEditText)findViewById(R.id.etDeviceName);
        mEtDeviceDesc = (MaterialEditText)findViewById(R.id.etDeviceDescription);
        mEtAndroidVersion = (MaterialEditText)findViewById(R.id.etAndroidVersion);
        mLinDeviceName = (LinearLayout)findViewById(R.id.linDeviceName);
        mLinDeviceDesc = (LinearLayout)findViewById(R.id.linDeviceDescription);
        mLinAndroidVersion = (LinearLayout)findViewById(R.id.linAndroidVersion);

        mEtAndroidVersion.setOnClickListener(this);

        mEtDeviceName.addTextChangedListener(new CustomInputValidator(mEtDeviceName));
        mEtDeviceName.setOnFocusChangeListener(AddDeviceActivity.this);

        mEtDeviceDesc.setFilters(new InputFilter[]{new InputFilter.LengthFilter(256)});
        mEtDeviceDesc.addTextChangedListener(new CustomInputValidator(mEtDeviceDesc));
        mEtDeviceDesc.setOnFocusChangeListener(AddDeviceActivity.this);

        mDbHelper = DBHelper.getInstance(ShowCaseApp.getAppContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etAndroidVersion:
                bindAndroidVersions();
                break;
        }
    }

    /**
     * Binds the android versions to the dropdrown pop up view
     */
    private void bindAndroidVersions() {
        hideToolTip();
        final Dialog dialog = new Dialog(AddDeviceActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_android_versions);
        final ListView list = (ListView) dialog.findViewById(R.id.list);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        final List<AndroidHistory> androidVersionList = mDbHelper.getAndroidVersions(" ASC ");
        AndroidVersionsAdapter adapter =
                new AndroidVersionsAdapter(ShowCaseApp.getAppContext(),
                        R.layout.individual_row_android_versions, androidVersionList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list.setPressed(true);
                dialog.dismiss();
                mAndroidVersionId = androidVersionList.get(position).getAndroidId();
                mEtAndroidVersion.setText(androidVersionList.get(position).getName());
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    private void hideToolTip() {
        mLinAndroidVersion.setVisibility(View.GONE);
        mLinDeviceDesc.setVisibility(View.GONE);
        mLinDeviceName.setVisibility(View.GONE);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.etDeviceName:
                if(hasFocus) {
                    mLinDeviceName.setVisibility(View.VISIBLE);
                }else {
                    mLinDeviceName.setVisibility(View.GONE);
                }
                break;

            case R.id.etDeviceDescription:
                if(hasFocus) {
                    mLinDeviceDesc.setVisibility(View.VISIBLE);
                }else {
                    mLinDeviceDesc.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_device, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            // save to the db and also save to the server
            validateSubmit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void validateSubmit() {
        hideToolTip();

        if(CustomInputValidator.checkError(mEtDeviceName))
            return;
        if(CustomInputValidator.checkError(mEtDeviceDesc))
            return;
        if(CustomInputValidator.checkError(mEtAndroidVersion))
            return;

        Devices device = new Devices();
        device.setAndroidId(mAndroidVersionId);
        device.setDeviceDesc(mEtDeviceDesc.getText().toString().trim());
        device.setDeviceName(mEtDeviceName.getText().toString().trim());
        device.setImageURL("");

        if(RestClient.isNetworkAvailable()) {
            mDbHelper.insertNewDevice(device);

            Call call = new RestClient().getApiService().addDevice(device);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Response response, Retrofit retrofit) {
                    if(response.errorBody()==null){
                        Toast.makeText(ShowCaseApp.getAppContext(),
                                "Device added successfully!",Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }

                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }else {
            Toast.makeText(ShowCaseApp.getAppContext(),
                    ShowCaseApp.getAppContext().getResources().getString(R.string.no_internet),
                    Toast.LENGTH_LONG).show();

        }
    }
}
