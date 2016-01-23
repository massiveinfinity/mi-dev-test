package com.dharashah.showcaseandroidapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dharashah.showcaseandroidapp.R;
import com.dharashah.showcaseandroidapp.ShowCaseApp;
import com.dharashah.showcaseandroidapp.db.DBHelper;
import com.dharashah.showcaseandroidapp.model.AndroidHistory;
import com.dharashah.showcaseandroidapp.network.RestClient;
import com.dharashah.showcaseandroidapp.utility.CustomInputValidator;
import com.rengwuxian.materialedittext.MaterialEditText;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by user on 22/01/2016.
 */
public class AddVersionActivity extends BaseActivity implements View.OnFocusChangeListener {
    private MaterialEditText mEtAndroidName;
    private MaterialEditText mEtAndroidCodeName;
    private MaterialEditText mEtAndroidVersion;
    private MaterialEditText mEtDistribution;
    private MaterialEditText mEtTarget;
    private LinearLayout mLinAndroidName;
    private LinearLayout mLinAndroidCodeName;
    private LinearLayout mLinAndroidVersion;
    private LinearLayout mLinTarget;
    private LinearLayout mLinDistribution;
    private DBHelper mDbHelper;
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
        setContentView(R.layout.activity_add_history);
        initToolbar();
        initView();
    }

    private void initToolbar() {
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mTxtTitle = (TextView)findViewById(R.id.txtTitle);
        mTxtTitle.setText("Add Android Version");
        mToolbar.setNavigationIcon(R.drawable.abc_ic_clear_mtrl_alpha);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        mEtAndroidName = (MaterialEditText)findViewById(R.id.etAndroidName);
        mEtAndroidCodeName = (MaterialEditText)findViewById(R.id.etAndroidCodeName);
        mEtAndroidVersion = (MaterialEditText)findViewById(R.id.etAndroidVersion);
        mEtDistribution = (MaterialEditText)findViewById(R.id.etDistribution);
        mEtTarget = (MaterialEditText)findViewById(R.id.etAndroidTarget);

        mLinAndroidName = (LinearLayout)findViewById(R.id.linAndroidName);
        mLinAndroidCodeName = (LinearLayout)findViewById(R.id.linAndroidCodeName);
        mLinAndroidVersion = (LinearLayout)findViewById(R.id.linAndroidVersion);
        mLinTarget = (LinearLayout)findViewById(R.id.linAndroidTarget);
        mLinDistribution = (LinearLayout)findViewById(R.id.linDistribution);

        mEtAndroidName.addTextChangedListener(new CustomInputValidator(mEtAndroidName));
        mEtAndroidName.setOnFocusChangeListener(AddVersionActivity.this);

        mEtAndroidCodeName.addTextChangedListener(new CustomInputValidator(mEtAndroidCodeName));
        mEtAndroidCodeName.setOnFocusChangeListener(AddVersionActivity.this);

        mEtAndroidVersion.addTextChangedListener(new CustomInputValidator(mEtAndroidVersion));
        mEtAndroidVersion.setOnFocusChangeListener(AddVersionActivity.this);

        mEtTarget.addTextChangedListener(new CustomInputValidator(mEtTarget));
        mEtTarget.setOnFocusChangeListener(AddVersionActivity.this);

        mEtDistribution.addTextChangedListener(new CustomInputValidator(mEtDistribution));
        mEtDistribution.setOnFocusChangeListener(AddVersionActivity.this);

        mDbHelper = DBHelper.getInstance(ShowCaseApp.getAppContext());
    }

    private void hideToolTip() {
        mLinAndroidVersion.setVisibility(View.GONE);
        mLinAndroidCodeName.setVisibility(View.GONE);
        mLinAndroidName.setVisibility(View.GONE);
        mLinTarget.setVisibility(View.GONE);
        mLinDistribution.setVisibility(View.GONE);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.etAndroidName:
                if(hasFocus) {
                    mLinAndroidName.setVisibility(View.VISIBLE);
                }else {
                    mLinAndroidName.setVisibility(View.GONE);
                }
                break;

            case R.id.etAndroidCodeName:
                if(hasFocus) {
                    mLinAndroidCodeName.setVisibility(View.VISIBLE);
                }else {
                    mLinAndroidCodeName.setVisibility(View.GONE);
                }
                break;

            case R.id.etAndroidVersion:
                if(hasFocus) {
                    mLinAndroidVersion.setVisibility(View.VISIBLE);
                }else {
                    mLinAndroidVersion.setVisibility(View.GONE);
                }
                break;

            case R.id.etAndroidTarget:
                if(hasFocus) {
                    mLinTarget.setVisibility(View.VISIBLE);
                }else {
                    mLinTarget.setVisibility(View.GONE);
                }
                break;

            case R.id.etDistribution:
                if(hasFocus) {
                    mLinDistribution.setVisibility(View.VISIBLE);
                }else {
                    mLinDistribution.setVisibility(View.GONE);
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

        if(CustomInputValidator.checkError(mEtAndroidName))
            return;
        if(CustomInputValidator.checkError(mEtAndroidCodeName))
            return;
        if(CustomInputValidator.checkError(mEtAndroidVersion))
            return;
        if(CustomInputValidator.checkError(mEtTarget))
            return;
        if(CustomInputValidator.checkError(mEtDistribution))
            return;

        AndroidHistory history = new AndroidHistory();
        history.setCodeName(mEtAndroidCodeName.getText().toString().trim());
        history.setTarget(mEtTarget.getText().toString().trim());
        history.setDistribution(mEtDistribution.getText().toString().trim() + "%");
        history.setVersion(mEtAndroidVersion.getText().toString().trim());

        if(RestClient.isNetworkAvailable()) {
            Call call = new RestClient().getApiService().addAndroidVersion(history);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Response response, Retrofit retrofit) {
                    if(response.errorBody()==null){
                        Toast.makeText(ShowCaseApp.getAppContext(),
                                "Android version added successfully!",Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                    
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });

            mDbHelper.insertAndroidVersion(history);
        }else {
            Toast.makeText(ShowCaseApp.getAppContext(),
                    ShowCaseApp.getAppContext().getResources().getString(R.string.no_internet),
                    Toast.LENGTH_LONG).show();
        }
    }
}
