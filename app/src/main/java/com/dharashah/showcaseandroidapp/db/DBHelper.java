package com.dharashah.showcaseandroidapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dharashah.showcaseandroidapp.model.AndroidHistory;
import com.dharashah.showcaseandroidapp.model.Devices;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Dhara Shah on 21/01/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper mDbHelper;
    private static final String DATABASE_NAME = "ShowCaseApp.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER ";
    private static final String INTEGER_PRIMARY_KEY = " INTEGER PRIMARY KEY ";
    private static final String FLOAT_TYPE=" FLOAT ";
    private static final String COMMA_SEP = ",";

    private static final String CREATE_HISTORY_TABLE =
            "CREATE TABLE " + AndroidVersionContract.AndroidVersionEntry.TABLE_NAME +
            "( " + AndroidVersionContract.AndroidVersionEntry._ID + INTEGER_PRIMARY_KEY +
            COMMA_SEP + AndroidVersionContract.AndroidVersionEntry.COLUMN_NAME_ANDROID_ID + INTEGER_TYPE +
            COMMA_SEP + AndroidVersionContract.AndroidVersionEntry.COLUMN_NAME_ANDROID_NAME + TEXT_TYPE +
            COMMA_SEP + AndroidVersionContract.AndroidVersionEntry.COLUMN_NAME_ANDROID_DISTRIBUTION + TEXT_TYPE +
            COMMA_SEP + AndroidVersionContract.AndroidVersionEntry.COLUMN_NAME_ANDROID_CODE_NAME + TEXT_TYPE +
            COMMA_SEP + AndroidVersionContract.AndroidVersionEntry.COLUMN_NAME_ANDROID_TARGET + TEXT_TYPE + ")";

    private static final String CREATE_DEVICE_TABLE =
            "CREATE TABLE " + DeviceContract.DeviceEntry.TABLE_NAME +
            " ( " + DeviceContract.DeviceEntry._ID + INTEGER_PRIMARY_KEY +
            COMMA_SEP + DeviceContract.DeviceEntry.COLUMN_NAME_DEVICE_ID + INTEGER_TYPE +
            COMMA_SEP + DeviceContract.DeviceEntry.COLUMN_NAME_ANDROID_ID + INTEGER_TYPE +
            COMMA_SEP + DeviceContract.DeviceEntry.COLUMN_NAME_DEVICE_NAME + TEXT_TYPE +
            COMMA_SEP + DeviceContract.DeviceEntry.COLUMN_NAME_DEVICE_DESC + TEXT_TYPE +
            COMMA_SEP + DeviceContract.DeviceEntry.COLUMN_NAME_DEVICE_IMAGE + TEXT_TYPE + ")";

    /**
     * Creates a singleton instance
     * @param context
     * @return
     */
    public static DBHelper getInstance(Context context) {
        if(mDbHelper == null) {
            mDbHelper = new DBHelper(context);
        }
        return mDbHelper;
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_HISTORY_TABLE);
        sqLiteDatabase.execSQL(CREATE_DEVICE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * Inserts a new device to the database
     * @param device
     * @return
     */
    public long insertNewDevice(Devices device){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DeviceContract.DeviceEntry.COLUMN_NAME_DEVICE_ID, device.getDeviceId());
        values.put(DeviceContract.DeviceEntry.COLUMN_NAME_ANDROID_ID, device.getAndroidId());
        values.put(DeviceContract.DeviceEntry.COLUMN_NAME_DEVICE_NAME, device.getDeviceName());
        values.put(DeviceContract.DeviceEntry.COLUMN_NAME_DEVICE_DESC, device.getDeviceDesc());
        values.put(DeviceContract.DeviceEntry.COLUMN_NAME_DEVICE_IMAGE, device.getImageURL());
        long id = db.insert(DeviceContract.DeviceEntry.TABLE_NAME,null,values );
        return id;
    }

    /**
     * Inserts a new Android Version to the database
     * @param androidHistory
     * @return
     */
    public long insertAndroidVersion(AndroidHistory androidHistory){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AndroidVersionContract.AndroidVersionEntry.COLUMN_NAME_ANDROID_ID, androidHistory.getAndroidId());
        values.put(AndroidVersionContract.AndroidVersionEntry.COLUMN_NAME_ANDROID_CODE_NAME,  androidHistory.getCodeName());
        values.put(AndroidVersionContract.AndroidVersionEntry.COLUMN_NAME_ANDROID_DISTRIBUTION, androidHistory.getDistribution());
        values.put(AndroidVersionContract.AndroidVersionEntry.COLUMN_NAME_ANDROID_NAME, androidHistory.getName());
        values.put(AndroidVersionContract.AndroidVersionEntry.COLUMN_NAME_ANDROID_TARGET, androidHistory.getTarget());
        long id= db.insert(AndroidVersionContract.AndroidVersionEntry.TABLE_NAME, null, values);
        return id;
    }

    /**
     * Gets the list of android versions
     * @param sortOrder
     * @return
     */
    public LinkedList<AndroidHistory> getAndroidVersions(String sortOrder) {
        LinkedList<AndroidHistory> androidHistoryList  = new LinkedList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;

        try {
            c = db.query(AndroidVersionContract.AndroidVersionEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    AndroidVersionContract.AndroidVersionEntry.COLUMN_NAME_ANDROID_ID + sortOrder);

            if(c != null && c.moveToFirst()) {
                do {
                    AndroidHistory history = new AndroidHistory();
                    history.setAndroidId(c.getInt(c.getColumnIndex(AndroidVersionContract.AndroidVersionEntry.COLUMN_NAME_ANDROID_ID)));
                    history.setName(c.getString(c.getColumnIndex(AndroidVersionContract.AndroidVersionEntry.COLUMN_NAME_ANDROID_NAME)));
                    history.setCodeName(c.getString
                            (c.getColumnIndex(AndroidVersionContract.AndroidVersionEntry.COLUMN_NAME_ANDROID_CODE_NAME)));
                    history.setDistribution(c.getString
                            (c.getColumnIndex(AndroidVersionContract.AndroidVersionEntry.COLUMN_NAME_ANDROID_DISTRIBUTION)));
                    history.setTarget(c.getString
                            (c.getColumnIndex(AndroidVersionContract.AndroidVersionEntry.COLUMN_NAME_ANDROID_TARGET)));
                    androidHistoryList.add(history);
                }while(c.moveToNext());
            }
        }finally {
            if(c != null) {
                c.close();
            }
        }
        return androidHistoryList;
    }

    /**
     * Gets the list of devices
     * @return
     */
    public LinkedList<Devices> getDevices(){
        LinkedList<Devices> deviceList = new LinkedList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;

        try {
            c = db.query(DeviceContract.DeviceEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    DeviceContract.DeviceEntry.COLUMN_NAME_DEVICE_ID + " DESC ");

            if(c != null && c.moveToFirst()) {
                do {
                    Devices device = new Devices();
                    device.setAndroidId(c.getInt(c.getColumnIndex(DeviceContract.DeviceEntry.COLUMN_NAME_ANDROID_ID)));
                    device.setDeviceDesc(c.getString(c.getColumnIndex(DeviceContract.DeviceEntry.COLUMN_NAME_DEVICE_DESC)));
                    device.setDeviceId(c.getInt(c.getColumnIndex(DeviceContract.DeviceEntry.COLUMN_NAME_DEVICE_ID)));
                    device.setDeviceName(c.getString(c.getColumnIndex(DeviceContract.DeviceEntry.COLUMN_NAME_DEVICE_NAME)));
                    device.setImageURL(c.getString(c.getColumnIndex(DeviceContract.DeviceEntry.COLUMN_NAME_DEVICE_IMAGE)));
                    deviceList.add(device);
                }while(c.moveToNext());
            }
        }finally {
            if(c != null) {
                c.close();
            }
        }
        return deviceList;
    }

    /**
     * Checks if there is data in the database
     * @return
     */
    public boolean hasData() {
        SQLiteDatabase db = this.getReadableDatabase();
        long countOfDevices = DatabaseUtils.queryNumEntries(db, DeviceContract.DeviceEntry.TABLE_NAME);
        long countOfVersions = DatabaseUtils.queryNumEntries(db, AndroidVersionContract.AndroidVersionEntry.TABLE_NAME);

        if(countOfDevices <= 0 && countOfVersions <= 0) {
            return false;
        }else {
            return true;
        }
    }

    /**
     * Adds a new device to the database
     * @param deviceList
     */
    public void addDevices(List<Devices> deviceList) {
        Stack<Devices> stack = new Stack<>();
        stack.addAll(deviceList);

        while(!stack.isEmpty()) {
            insertNewDevice(stack.pop());
        }
    }

    /**
     * Adds an android version to the database
     * @param versionList
     */
    public void addVersions(List<AndroidHistory> versionList){
        Stack<AndroidHistory> stack = new Stack<>();
        stack.addAll(versionList);

        while(!stack.isEmpty()) {
            insertAndroidVersion(stack.pop());
        }
    }

    /**
     * Gets the android version name to display
     * @param androidId
     * @return
     */
    public String getVersionName(int androidId) {
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor c = null;
        String androidName = "";

        try {
            c = db.query(AndroidVersionContract.AndroidVersionEntry.TABLE_NAME,
                    new String[]{AndroidVersionContract.AndroidVersionEntry.COLUMN_NAME_ANDROID_NAME},
                    AndroidVersionContract.AndroidVersionEntry.COLUMN_NAME_ANDROID_ID + " =?",
                    new String[]{String.valueOf(androidId)},
                    null,
                    null,
                    null);

            if(c != null && c.moveToFirst()) {
                androidName = c.getString(c.getColumnIndex(AndroidVersionContract.AndroidVersionEntry.COLUMN_NAME_ANDROID_NAME));
            }
        }finally {
            if(c != null) {
                c.close();
            }
        }

        return androidName;
    }

    /**
     * Delete the device from the database
     * @param deviceId
     */
    public void deleteDevice(int deviceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DeviceContract.DeviceEntry.TABLE_NAME,
                DeviceContract.DeviceEntry.COLUMN_NAME_DEVICE_ID + "=?",
                new String[]{String.valueOf(deviceId)});
    }

    /**
     * Delete the android version from the database
     * @param androidId
     */
    public void deleteAndroidVersion(int androidId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AndroidVersionContract.AndroidVersionEntry.TABLE_NAME,
                AndroidVersionContract.AndroidVersionEntry.COLUMN_NAME_ANDROID_ID + "=?",
                new String[]{String.valueOf(androidId)});
    }
}
