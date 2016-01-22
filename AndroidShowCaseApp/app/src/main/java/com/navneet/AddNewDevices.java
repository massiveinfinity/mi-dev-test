package com.navneet;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.network.NetworkClassHandler;
import com.network.NetworkInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by root on 1/8/16.
 */
public class AddNewDevices extends Activity implements View.OnClickListener, NetworkInterface{
    private ProgressBar progressBar;
    private SQLiteDatabase database;
    private Button submit;
    private EditText name, version, codename, target, distribution;
    private String err = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_layout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        submit = (Button) findViewById(R.id.submit);
        name = (EditText) findViewById(R.id.name);
        version = (EditText) findViewById(R.id.version);
        codename = (EditText) findViewById(R.id.codename);
        target = (EditText) findViewById(R.id.target);
        distribution = (EditText) findViewById(R.id.distribution);
        submit.setOnClickListener(this);
    }
    private boolean Validation(){
        if(TextUtils.isEmpty(name.getText().toString().trim())) {
            err = "Enter the Name";
            return false;
        } else if(TextUtils.isEmpty(version.getText().toString().trim())) {
            err = "Enter the Version";
            return false;
        } else if(TextUtils.isEmpty(codename.getText().toString().trim())) {
            err = "Enter the Code name";
            return false;
        } else if(TextUtils.isEmpty(target.getText().toString().trim())) {
            err = "Enter the Target";
            return false;
        } else if(TextUtils.isEmpty(distribution.getText().toString().trim())) {
            err = "Enter the Distribution";
            return false;
        }
        return true;
    }
    @Override
    public void onClick(View v) {
        if(Validation()){
            progressBar.setVisibility(View.VISIBLE);
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", name.getText().toString().trim());
            map.put("version", version.getText().toString().trim());
            map.put("codename", codename.getText().toString().trim());
            map.put("target", target.getText().toString().trim());
            map.put("distribution", distribution.getText().toString().trim());
            new NetworkClassHandler().OnPostRequestWithParams(this, 5, map,"http://mobilesandboxdev.azurewebsites.net/android");
        } else {
            Toast.makeText(this, err,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(String response, int requestCode) {
        try {
            JSONObject json = new JSONObject(response);
            MyDatabaseHelper mMyDatabaseHelper = new MyDatabaseHelper(AddNewDevices.this);
            database = mMyDatabaseHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                if (json.has(MyDatabaseHelper.COLUMN_ID))
                    cv.put(MyDatabaseHelper.COLUMN_ID, json.getInt(MyDatabaseHelper.COLUMN_ID));
                if (json.has(MyDatabaseHelper.NAME))
                    cv.put(MyDatabaseHelper.NAME, json.getString(MyDatabaseHelper.NAME));
                else
                    cv.put(MyDatabaseHelper.NAME, "Information Not Available");
                if (json.has(MyDatabaseHelper.VERSION))
                    cv.put(MyDatabaseHelper.VERSION, json.getString(MyDatabaseHelper.VERSION));
                else
                    cv.put(MyDatabaseHelper.VERSION, "Information Not Available");
                if (json.has(MyDatabaseHelper.CODE_NAME))
                    cv.put(MyDatabaseHelper.CODE_NAME, json.getString(MyDatabaseHelper.CODE_NAME));
                else
                    cv.put(MyDatabaseHelper.CODE_NAME, "Information Not Available");
                if (json.has(MyDatabaseHelper.TARGET))
                    cv.put(MyDatabaseHelper.TARGET, json.getString(MyDatabaseHelper.TARGET));
                else
                    cv.put(MyDatabaseHelper.TARGET, "Information Not Available");
                if (json.has(MyDatabaseHelper.DISTRIBUTION))
                    cv.put(MyDatabaseHelper.DISTRIBUTION, json.getString(MyDatabaseHelper.DISTRIBUTION));
                else
                    cv.put(MyDatabaseHelper.DISTRIBUTION, "Information Not Available");
                database.insert(MyDatabaseHelper.TABLE_DEVICES, null, cv);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Device info added successfully!",Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
