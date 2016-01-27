package com.mi.showcase;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mi.showcase.exec.ShowcaseMobileTask;
import com.mi.showcase.util.DialogTypes;
import com.mi.showcase.util.MessageHandler;
import com.mi.showcase.util.ShowcaseUtils;
import com.mi.showcase.view.dto.MobileDataListModel;

public class InsertDataActivity extends AppCompatActivity {

    private TextView[] textViews = new TextView[9];

    private ProgressBar savingProgressBar;
    private ScrollView scrollView;
    private ShowcaseUtils.ShowcaseUtilDialogs connectionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        connectionDialog = new ShowcaseUtils.ShowcaseUtilDialogs(this, DialogTypes.CONNECTION_DIALOG, null, null);

        textViews[0] = (TextView) findViewById(R.id.deviceName);
        textViews[1] = (TextView) findViewById(R.id.androidName);
        textViews[2] = (TextView) findViewById(R.id.carrier);
        textViews[3] = (TextView) findViewById(R.id.version);
        textViews[4] = (TextView) findViewById(R.id.target);
        textViews[5] = (TextView) findViewById(R.id.codeName);
        textViews[6] = (TextView) findViewById(R.id.distribution);
        textViews[7] = (TextView) findViewById(R.id.remark);
        textViews[8] = (TextView) findViewById(R.id.imageUrl);

        savingProgressBar = (ProgressBar) findViewById(R.id.savingProgress);
        scrollView = (ScrollView) findViewById(R.id.savingScroller);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_insert_data, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                boolean isConnected = ShowcaseUtils.isConnected(getApplicationContext());
                if (!isConnected) {
                    connectionDialog.showDialog(null);
                } else {
                    boolean isRequiredEmpty = false;
                    for (TextView textView : textViews) {
                        if (!textView.equals(textViews[2])) {
                            if (!textView.equals(textViews[8])) {
                                String text = textView.getText().toString();
                                if (text.equals(getString(R.string.empty_string))) {
                                    isRequiredEmpty = true;
                                }
                            }
                        }
                    }
                    if (!isRequiredEmpty) {
                        try {
                            View view = this.getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                            MessageHandler.executeHandler(saveData);
                        } catch (InterruptedException ex) {
                            Log.e(ex.getMessage(), ex.toString());
                        }
                    } else {
                        Snackbar.make(scrollView, getString(R.string.required_fields_empty), Snackbar.LENGTH_LONG).show();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private final Runnable saveData = new Runnable() {
        @Override
        public void run() {
            MobileDataListModel mobileDataListModel = new MobileDataListModel();
            mobileDataListModel.setDeviceName(textViews[0].getText().toString());
            mobileDataListModel.setAndroidName(textViews[1].getText().toString());
            mobileDataListModel.setCarrier(textViews[2].getText().toString());
            mobileDataListModel.setVersion(textViews[3].getText().toString());
            mobileDataListModel.setTarget(textViews[4].getText().toString());
            mobileDataListModel.setCodeName(textViews[5].getText().toString());
            mobileDataListModel.setDistribution(textViews[6].getText().toString());
            mobileDataListModel.setSnippet(textViews[7].getText().toString());
            mobileDataListModel.setImageUrl(textViews[8].getText().toString());

            ShowcaseMobileTask.AddDeviceTask addDeviceTask
                    = new ShowcaseMobileTask.AddDeviceTask(InsertDataActivity.this, savingProgressBar, scrollView, textViews);
            addDeviceTask.execute(mobileDataListModel);
        }
    };
}
