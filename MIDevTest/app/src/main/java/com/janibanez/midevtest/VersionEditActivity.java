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
import com.janibanez.server.models.Version;

import java.io.IOException;

/**
 * Created by jwgibanez on 23/01/2016.
 */
public class VersionEditActivity extends AppCompatActivity {

    Version mData;

    EditText mName, mVersion, mCodename, mTarget, mDistribution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_version);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mData = (Version) getIntent().getSerializableExtra("version");

        mName = (EditText) findViewById(R.id.name);
        mVersion = (EditText) findViewById(R.id.version);
        mCodename = (EditText) findViewById(R.id.codename);
        mTarget = (EditText) findViewById(R.id.target);
        mDistribution = (EditText) findViewById(R.id.distribution);

        initFields();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_version, menu);
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
                        // put to update version
                        updateVersion();
                        break;
                    }
                }
                // post to create new version
                createVersion();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFields() {
        if (mData != null) {
            if (!TextUtils.isEmpty(mData.name))
                mName.setText(mData.name);

            if (!TextUtils.isEmpty(mData.codename))
                mCodename.setText(mData.codename);

            if (!TextUtils.isEmpty(mData.version))
                mVersion.setText(mData.version);

            if (!TextUtils.isEmpty(mData.target))
                mTarget.setText(mData.target);

            if (!TextUtils.isEmpty(mData.distribution))
                mDistribution.setText(mData.distribution);
        }
    }

    private boolean isInfoValid() {

        mData.name = mName.getText().toString();
        mData.version = mVersion.getText().toString();
        mData.codename = mCodename.getText().toString();
        mData.target = mTarget.getText().toString();
        mData.distribution = mDistribution.getText().toString();

        if (TextUtils.isEmpty(mData.name)) {
            Toast.makeText(this, "Name is empty.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (TextUtils.isEmpty(mData.version)) {
            Toast.makeText(this, "Version is empty.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (TextUtils.isEmpty(mData.codename)) {
            Toast.makeText(this, "Codename is empty.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (TextUtils.isEmpty(mData.target)) {
            Toast.makeText(this, "Target is empty.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (TextUtils.isEmpty(mData.distribution)) {
            Toast.makeText(this, "Distribution is empty.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void createVersion() {

        if (mData == null) {
            mData = new Version();
        }

        if (!isInfoValid())
            // abort
            return;

        MiApi api = new MiApi(this);

        api.call(MiApi.Action.CreateVersion, 0, mData, new ICallback() {
            @Override
            public void onFailure(Throwable throwable) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtilities.showMessageDialog(
                                VersionEditActivity.this,
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
                VersionEditActivity.this.finish();
            }
        });
    }

    private void updateVersion() {

        if (!isInfoValid())
            // abort
            return;

        MiApi api = new MiApi(this);

        api.call(MiApi.Action.UpdateVersion, mData.id, mData, new ICallback<Version>() {
            @Override
            public void onFailure(Throwable throwable) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtilities.showMessageDialog(
                                VersionEditActivity.this,
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
            public void onResponse(Version response) throws IOException {
                Intent intent = new Intent();
                intent.putExtra("version", response);
                setResult(MainActivity.RESULT_REFRESH, intent);
                VersionEditActivity.this.finish();
            }
        });
    }

}
