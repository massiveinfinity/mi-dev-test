package com.mi.androidarsenal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mi.androidarsenal.model.AndroidInfo;
import com.mi.androidarsenal.model.Devices;
import com.mi.androidarsenal.model.Versions;
import com.mi.androidarsenal.utility.AppConstants;

import java.util.ArrayList;

/**
 * This deals with all the SQlite operations, basically for offline support
 *
 * @author Samir Sarosh
 */
@SuppressWarnings("ALL")
public class DatabaseHandler extends SQLiteOpenHelper implements AppConstants {

    private static DatabaseHandler mDatabaseHandler = null;

    /**
     * Fetches the same instance of the Fragment sub-class
     *
     * @return The same instance of the Fragment sub-class every time
     */
    public static DatabaseHandler getDatabaseHandlerInstance(Context context) {
        if (mDatabaseHandler == null) {
            mDatabaseHandler = new DatabaseHandler(context);
        }
        return mDatabaseHandler;
    }

    /**
     * Private constructor to fulfill the singleton design pattern
     */
    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DEVICES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_DEVICES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DEVICE_ID + " TEXT," + KEY_ANDROID_ID + " TEXT,"
                + KEY_NAME + " TEXT," + KEY_SNIPPET + " TEXT,"
                + KEY_IMAGE_URL + " TEXT," + KEY_CARRIER + " TEXT" + ")";

        // creating version table.
        String CREATE_VERSIONS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_VERSIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_VERSION_ID + " TEXT," + KEY_VERSION_NAME
                + " TEXT," + KEY_VERSION + " TEXT," + KEY_CODENAME + " TEXT,"
                + KEY_DESTRIBUTION + " TEXT," + KEY_TARGET + " TEXT" + ")";

        db.execSQL(CREATE_DEVICES_TABLE);
        db.execSQL(CREATE_VERSIONS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VERSIONS);

        // Create tables again
        onCreate(db);

    }

    /**
     * Adds all the data to the in memory database for oofline support
     *
     * @param androidInfo
     */
    public void addAndroidInfo(AndroidInfo androidInfo) {
        ArrayList<Devices> devicePropList = androidInfo.getDevicePropList();
        addDevices(devicePropList);
        ArrayList<Versions> versionPropList = androidInfo.getVersionPropList();
        addVersions(versionPropList);
    }

    // Adding devices
    public void addDevices(ArrayList<Devices> devicePropList) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.delete(TABLE_DEVICES, null, null);
            ContentValues values = new ContentValues();

            for (Devices devices : devicePropList) {
                values.put(KEY_DEVICE_ID, devices.getId());
                values.put(KEY_ANDROID_ID, devices.getAndroidId());
                values.put(KEY_NAME, devices.getName());
                values.put(KEY_SNIPPET, devices.getSnippet());

                values.put(KEY_IMAGE_URL, devices.getImageUrl());
                values.put(KEY_CARRIER, devices.getCarrier());
                // Inserting Row
                db.insert(TABLE_DEVICES, null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null)
                db.close();
        }


    }

    // Adding versions
    public void addVersions(ArrayList<Versions> versionPropList) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.delete(TABLE_VERSIONS, null, null);
            ContentValues values = new ContentValues();

            for (Versions versions : versionPropList) {
                values.put(KEY_VERSION_ID, versions.getId());
                values.put(KEY_VERSION_NAME, versions.getName());
                values.put(KEY_VERSION, versions.getVersion());
                values.put(KEY_CODENAME, versions.getCodename());

                values.put(KEY_DESTRIBUTION, versions.getDistribution());
                values.put(KEY_TARGET, versions.getTarget());

                // Inserting Row
                db.insert(TABLE_VERSIONS, null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null)
                db.close();
        }
    }

    public Cursor getDevicesCursor() {
        Cursor cursor = null;
        try {
            // Select All Query
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.query(TABLE_DEVICES, null, null, null, null,
                    null, null);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return cursor;
    }

    public Cursor getVersionsCursor() {
        Cursor cursor = null;
        try {
            // Select All Query
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.query(TABLE_VERSIONS, null, null, null, null,
                    null, null);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return cursor;
    }

    /**
     * deletes a row from the table
     *
     * @param tableName
     * @param key
     * @param id
     * @return
     */
    public boolean deleteRow(String tableName, String key, String id) {
        try {
            String table = tableName;
            String whereClause = key + "=?";
            String[] whereArgs = new String[]{id};
            SQLiteDatabase db = this.getWritableDatabase();
            int result = db.delete(table, whereClause, whereArgs);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
