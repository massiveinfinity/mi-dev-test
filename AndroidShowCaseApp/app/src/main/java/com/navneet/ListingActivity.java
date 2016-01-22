package com.navneet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.network.NetworkClassHandler;
import com.network.NetworkInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;


public class ListingActivity extends Activity implements NetworkInterface, AdapterView.OnItemClickListener, View.OnClickListener{
    private ListView listView;
    private ProgressBar progressBar;
    private SQLiteDatabase database;
    private final int SERVER_SYNC_REQUEST = 1;
    private SimpleCursorAdapter mSimpleCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        listView = (ListView) findViewById(R.id.listView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        View footer = View.inflate(this, R.layout.add_new, null);
        footer.setOnClickListener(this);
        listView.addFooterView(footer);
        listView.setOnItemClickListener(this);
        File databaseFile = getDatabasePath(MyDatabaseHelper.DATABASE_NAME);
        if(!databaseFile.exists()){
            progressBar.setVisibility(View.VISIBLE);
            new NetworkClassHandler().OnGetRequest(ListingActivity.this, SERVER_SYNC_REQUEST, "http://mobilesandboxdev.azurewebsites.net/android/");
        } else {
            MyDatabaseHelper mMyDatabaseHelper = new MyDatabaseHelper(ListingActivity.this);
            database = mMyDatabaseHelper.getWritableDatabase();
            Cursor cursor =  database.rawQuery("SELECT _id, " + MyDatabaseHelper.NAME + " FROM " + MyDatabaseHelper.TABLE_DEVICES + "", null);
            listView.setAdapter(mSimpleCursorAdapter = new SimpleCursorAdapter(ListingActivity.this, R.layout.row, cursor, new String[]{MyDatabaseHelper.NAME}, new int[]{R.id.textView}, 0));
        }
    }
    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(this, AddNewDevices.class), 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10 && resultCode==RESULT_OK){
            Cursor cursor = database.rawQuery("SELECT _id," + MyDatabaseHelper.NAME + " FROM " + MyDatabaseHelper.TABLE_DEVICES + "", null);
            mSimpleCursorAdapter.changeCursor(cursor);
            mSimpleCursorAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResponse(final String response, final int requestCode) {
        System.out.print(response);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (requestCode) {
                    case SERVER_SYNC_REQUEST:
                        try {
                            JSONArray json = new JSONArray(response);
                            MyDatabaseHelper mMyDatabaseHelper = new MyDatabaseHelper(ListingActivity.this);
                            database = mMyDatabaseHelper.getWritableDatabase();
                            for (int i = 0; i < json.length(); i++) {
                                ContentValues cv = new ContentValues();
                                if (json.getJSONObject(i).has(MyDatabaseHelper.COLUMN_ID))
                                    cv.put(MyDatabaseHelper.COLUMN_ID, json.getJSONObject(i).getInt(MyDatabaseHelper.COLUMN_ID));
                                if (json.getJSONObject(i).has(MyDatabaseHelper.NAME))
                                    cv.put(MyDatabaseHelper.NAME, json.getJSONObject(i).getString(MyDatabaseHelper.NAME));
                                else
                                    cv.put(MyDatabaseHelper.NAME, "Information Not Available");
                                if (json.getJSONObject(i).has(MyDatabaseHelper.VERSION))
                                    cv.put(MyDatabaseHelper.VERSION, json.getJSONObject(i).getString(MyDatabaseHelper.VERSION));
                                else
                                    cv.put(MyDatabaseHelper.VERSION, "Information Not Available");
                                if (json.getJSONObject(i).has(MyDatabaseHelper.CODE_NAME))
                                    cv.put(MyDatabaseHelper.CODE_NAME, json.getJSONObject(i).getString(MyDatabaseHelper.CODE_NAME));
                                else
                                    cv.put(MyDatabaseHelper.CODE_NAME, "Information Not Available");
                                if (json.getJSONObject(i).has(MyDatabaseHelper.TARGET))
                                    cv.put(MyDatabaseHelper.TARGET, json.getJSONObject(i).getString(MyDatabaseHelper.TARGET));
                                else
                                    cv.put(MyDatabaseHelper.TARGET, "Information Not Available");
                                if (json.getJSONObject(i).has(MyDatabaseHelper.DISTRIBUTION))
                                    cv.put(MyDatabaseHelper.DISTRIBUTION, json.getJSONObject(i).getString(MyDatabaseHelper.DISTRIBUTION));
                                else
                                    cv.put(MyDatabaseHelper.DISTRIBUTION, "Information Not Available");
                                database.insert(MyDatabaseHelper.TABLE_DEVICES, null, cv);
                            }
                            Cursor cursor = database.rawQuery("SELECT _id," + MyDatabaseHelper.NAME + " FROM " + MyDatabaseHelper.TABLE_DEVICES + "", null);
                            listView.setAdapter(mSimpleCursorAdapter = new SimpleCursorAdapter(ListingActivity.this, R.layout.row, cursor, new String[]{MyDatabaseHelper.NAME}, new int[]{R.id.textView}, 0));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressBar.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Cursor c = database.rawQuery("SELECT * FROM " + MyDatabaseHelper.TABLE_DEVICES + " WHERE _id=" + id, null);
        c.moveToFirst();
        new AlertDialog.Builder(ListingActivity.this)
                .setTitle("Device Info:")
                .setMessage("Serial No: " + c.getInt(c.getColumnIndexOrThrow("_id"))
                        + "\nDevice Name: " + c.getString(c.getColumnIndexOrThrow(MyDatabaseHelper.NAME))
                        + "\nDevice Version: " + c.getString(c.getColumnIndexOrThrow(MyDatabaseHelper.VERSION))
                        + "\nDevice Code: " + c.getString(c.getColumnIndexOrThrow(MyDatabaseHelper.CODE_NAME))
                        + "\nDevice Target: " + c.getString(c.getColumnIndexOrThrow(MyDatabaseHelper.TARGET))
                        + "\nDevice Distribution: " + c.getString(c.getColumnIndexOrThrow(MyDatabaseHelper.DISTRIBUTION)))
                .setCancelable(true)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Whatever...
                    }
                }).create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
