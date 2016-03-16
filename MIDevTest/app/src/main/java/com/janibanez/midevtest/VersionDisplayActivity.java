package com.janibanez.midevtest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.janibanez.midevtest.utilities.DialogUtilities;
import com.janibanez.server.ICallback;
import com.janibanez.server.MiApi;
import com.janibanez.server.models.Version;

import java.io.IOException;

/**
 * Created by jwgibanez on 22/01/2016.
 */
public class VersionDisplayActivity extends AppCompatActivity {

    private static final int REQUEST_EDIT = 100;

    Version mData;

    TextView mName, mCodename, mVersion, mTarget, mDistribution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_version);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mName = (TextView) findViewById(R.id.name);
        mCodename = (TextView) findViewById(R.id.codename);
        mVersion = (TextView) findViewById(R.id.version);
        mTarget = (TextView) findViewById(R.id.target);
        mDistribution = (TextView) findViewById(R.id.distribution);

        mData = (Version) getIntent().getSerializableExtra("version");

        initFields();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display_version, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_edit:
                Intent intent = new Intent(this, VersionEditActivity.class);
                intent.putExtra("version", mData);
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

                    Version updatedVersion = (Version) data.getSerializableExtra("version");
                    if (updatedVersion != null) {
                        // update view
                        mData = updatedVersion;
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

    private void showConfirmation() {
        DialogUtilities.showConfirmation(
                this,
                getString(R.string.dialog_title_alert),
                getString(R.string.dialog_message_delete_version),
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
                        deleteVersion();
                        dialog.dismiss();
                    }
                });
    }

    private void deleteVersion() {
        MiApi api = new MiApi(this);

        api.call(MiApi.Action.DeleteVersion, mData.id, null, new ICallback() {
            @Override
            public void onFailure(Throwable throwable) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtilities.showMessageDialog(
                                VersionDisplayActivity.this,
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
                VersionDisplayActivity.this.finish();
            }
        });
    }

}
