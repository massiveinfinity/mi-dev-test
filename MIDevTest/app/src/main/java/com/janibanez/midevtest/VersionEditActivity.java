package com.janibanez.midevtest;

import android.content.DialogInterface;
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

    EditText mName, mVersion, mCodename, mTarget, mDistribution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_version);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mName = (EditText) findViewById(R.id.name);
        mVersion = (EditText) findViewById(R.id.version);
        mCodename = (EditText) findViewById(R.id.codename);
        mTarget = (EditText) findViewById(R.id.target);
        mDistribution = (EditText) findViewById(R.id.distribution);

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
                createVersion();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createVersion() {

        String name = mName.getText().toString();
        String version = mVersion.getText().toString();
        String codename = mCodename.getText().toString();
        String target = mTarget.getText().toString();
        String distribution = mDistribution.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Name is empty.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(version)) {
            Toast.makeText(this, "Version is empty.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(codename)) {
            Toast.makeText(this, "Codename is empty.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(target)) {
            Toast.makeText(this, "Target is empty.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(distribution)) {
            Toast.makeText(this, "Distribution is empty.", Toast.LENGTH_LONG).show();
            return;
        }

        Version data = new Version(name, version, codename, target, distribution);

        MiApi api = new MiApi(this);

        api.call(MiApi.Action.CreateVersion, 0, data, new ICallback() {
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

}
