package com.janibanez.midevtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.janibanez.server.models.Version;

/**
 * Created by jwgibanez on 22/01/2016.
 */
public class VersionDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_version);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView name = (TextView) findViewById(R.id.name);
        TextView codename = (TextView) findViewById(R.id.codename);
        TextView version = (TextView) findViewById(R.id.version);
        TextView target = (TextView) findViewById(R.id.target);
        TextView distribution = (TextView) findViewById(R.id.distribution);

        Version data = (Version) getIntent().getSerializableExtra("version");

        if (data != null) {
            if (!TextUtils.isEmpty(data.name))
                name.setText(data.name);

            if (!TextUtils.isEmpty(data.codename))
                codename.setText(data.codename);

            if (!TextUtils.isEmpty(data.version))
                version.setText(data.version);

            if (!TextUtils.isEmpty(data.target))
                target.setText(data.target);

            if (!TextUtils.isEmpty(data.distribution))
                distribution.setText(data.distribution);
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

}
