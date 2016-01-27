package com.zahid.mitask.devicesinfo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.zahid.mitask.devicesinfo.R;
import com.zahid.mitask.devicesinfo.bean.Version;
import com.zahid.mitask.devicesinfo.helper.ProgressDialogHelper;
import com.zahid.mitask.devicesinfo.helper.WebServiceHelper;

public class VersionDetailActivity extends AppCompatActivity{

    private TextView versionName, versionCode, versionCodeName, versionTarget, versionDistribution;
    private Version versionObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_detail);

        // Get selected version object
        versionObj = (Version) getIntent().getSerializableExtra("versionObj");
        setTitle(versionObj.getName());
        initialize();
        populateVersionFields();
    }

    // Initialize views and objects
    private void initialize() {
        versionName = (TextView) findViewById(R.id.textView_version_name);
        versionCode = (TextView) findViewById(R.id.textView_version_code);
        versionCodeName = (TextView) findViewById(R.id.textView_code_name);
        versionTarget = (TextView) findViewById(R.id.textView_target);
        versionDistribution = (TextView) findViewById(R.id.textView_distribution);
    }

    // Populate the version information in respective textviews
    private void populateVersionFields() {
        versionName.setText(versionObj.getName());
        versionCode.setText(versionObj.getVersion());
        versionCodeName.setText(versionObj.getCodename());
        versionDistribution.setText(versionObj.getDistribution());
        versionTarget.setText(versionObj.getTarget());
    }

}
