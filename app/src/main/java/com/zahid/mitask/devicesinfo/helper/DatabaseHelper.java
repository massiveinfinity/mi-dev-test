package com.zahid.mitask.devicesinfo.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zahid.mitask.devicesinfo.bean.Device;
import com.zahid.mitask.devicesinfo.utils.DeviceEntry;
import com.zahid.mitask.devicesinfo.bean.Version;
import com.zahid.mitask.devicesinfo.utils.VersionEntry;

import java.util.ArrayList;

/**
 * Created by zahid.r on 1/26/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DeviceInfoDatabase.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_DEVICE_ENTRIES =
            "CREATE TABLE " + DeviceEntry.DEVICE_TABLE_NAME + " (" +
                    DeviceEntry.COLUMN_NAME_DEVICE_ID + INTEGER_TYPE + COMMA_SEP +
                    DeviceEntry.COLUMN_NAME_ANDROID_ID + INTEGER_TYPE + COMMA_SEP +
                    DeviceEntry.COLUMN_NAME_DEVICE_NAME + TEXT_TYPE + COMMA_SEP +
                    DeviceEntry.COLUMN_NAME_IMAGE_URL + TEXT_TYPE + COMMA_SEP +
                    DeviceEntry.COLUMN_NAME_DEVICE_SNIPPET + TEXT_TYPE + COMMA_SEP +
                    DeviceEntry.COLUMN_NAME_DEVICE_CARRIER + TEXT_TYPE + " )";

    private static final String SQL_CREATE_VERSION_ENTRIES =
            "CREATE TABLE " + VersionEntry.VERSION_TABLE_NAME + " (" +
                    VersionEntry.COLUMN_NAME_VERSION_ID + INTEGER_TYPE + COMMA_SEP +
                    VersionEntry.COLUMN_NAME_VERSION_NAME + TEXT_TYPE + COMMA_SEP +
                    VersionEntry.COLUMN_NAME_VERSION_CODE + TEXT_TYPE + COMMA_SEP +
                    VersionEntry.COLUMN_NAME_VERSION_CODE_NAME + TEXT_TYPE + COMMA_SEP +
                    VersionEntry.COLUMN_NAME_VERSION_TARGET + TEXT_TYPE + COMMA_SEP +
                    VersionEntry.COLUMN_NAME_VERSION_DISTRIBUTION + TEXT_TYPE + " )";

    private static final String SQL_DELETE_DEVICE_TABLE_ENTRIES =
            "DROP TABLE IF EXISTS " + DeviceEntry.DEVICE_TABLE_NAME;

    private static final String SQL_DELETE_VERSION_TABLE_ENTRIES =
            "DROP TABLE IF EXISTS " + VersionEntry.VERSION_TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DEVICE_ENTRIES);
        db.execSQL(SQL_CREATE_VERSION_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_DEVICE_TABLE_ENTRIES);
        db.execSQL(SQL_DELETE_VERSION_TABLE_ENTRIES);
        onCreate(db);
    }


    public void saveDeviceDataInDB(ArrayList<Device> devices) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        for (Device device : devices) {
            ContentValues values = new ContentValues();
            values.put(DeviceEntry.COLUMN_NAME_DEVICE_ID, device.getId());
            values.put(DeviceEntry.COLUMN_NAME_ANDROID_ID, device.getAndroidId());
            values.put(DeviceEntry.COLUMN_NAME_DEVICE_NAME, device.getName());
            values.put(DeviceEntry.COLUMN_NAME_IMAGE_URL, device.getImageUrl());
            values.put(DeviceEntry.COLUMN_NAME_DEVICE_SNIPPET, device.getSnippet());
            values.put(DeviceEntry.COLUMN_NAME_DEVICE_CARRIER, device.getCarrier());

            // Insert the new row, returning the primary key value of the new row
            db.insert(
                    DeviceEntry.DEVICE_TABLE_NAME,
                    null,
                    values);
        }
        db.close();
    }


    public void saveVersionDataInDB(ArrayList<Version> versions) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        for (Version version : versions) {
            ContentValues values = new ContentValues();
            values.put(VersionEntry.COLUMN_NAME_VERSION_ID, version.getId());
            values.put(VersionEntry.COLUMN_NAME_VERSION_NAME, version.getName());
            values.put(VersionEntry.COLUMN_NAME_VERSION_CODE, version.getVersion());
            values.put(VersionEntry.COLUMN_NAME_VERSION_CODE_NAME, version.getCodename());
            values.put(VersionEntry.COLUMN_NAME_VERSION_TARGET, version.getTarget());
            values.put(VersionEntry.COLUMN_NAME_VERSION_DISTRIBUTION, version.getDistribution());

            // Insert the new row, returning the primary key value of the new row
            db.insert(
                    VersionEntry.VERSION_TABLE_NAME,
                    null,
                    values);
        }
        db.close();
    }

    public Version getVersionInformationFromDB(int androidID) {
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    VersionEntry.COLUMN_NAME_VERSION_ID,
                    VersionEntry.COLUMN_NAME_VERSION_NAME,
                    VersionEntry.COLUMN_NAME_VERSION_CODE,
                    VersionEntry.COLUMN_NAME_VERSION_CODE_NAME,
                    VersionEntry.COLUMN_NAME_VERSION_TARGET,
                    VersionEntry.COLUMN_NAME_VERSION_DISTRIBUTION,
            };

            // Define 'where' part of query.
            String selection = VersionEntry.COLUMN_NAME_VERSION_ID + " LIKE ?";
            // Specify arguments in placeholder order.
            String[] selectionArgs = {String.valueOf(androidID)};


            Cursor c = db.query(
                    VersionEntry.VERSION_TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            c.moveToFirst();
            Version version = new Version();
            version.setId(c.getInt(c.getColumnIndexOrThrow(VersionEntry.COLUMN_NAME_VERSION_ID)));
            version.setName(c.getString(c.getColumnIndexOrThrow(VersionEntry.COLUMN_NAME_VERSION_NAME)));
            version.setVersion(c.getString(c.getColumnIndexOrThrow(VersionEntry.COLUMN_NAME_VERSION_CODE)));
            version.setCodename(c.getString(c.getColumnIndexOrThrow(VersionEntry.COLUMN_NAME_VERSION_CODE_NAME)));
            version.setTarget(c.getString(c.getColumnIndexOrThrow(VersionEntry.COLUMN_NAME_VERSION_TARGET)));
            version.setDistribution(c.getString(c.getColumnIndexOrThrow(VersionEntry.COLUMN_NAME_VERSION_DISTRIBUTION)));
            c.close();
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            db.close();
        }
    }

    public void updateDeviceData(Device device) {
        SQLiteDatabase db = this.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(DeviceEntry.COLUMN_NAME_DEVICE_ID, device.getId());
        values.put(DeviceEntry.COLUMN_NAME_ANDROID_ID, device.getAndroidId());
        values.put(DeviceEntry.COLUMN_NAME_DEVICE_NAME, device.getName());
        values.put(DeviceEntry.COLUMN_NAME_IMAGE_URL, device.getImageUrl());
        values.put(DeviceEntry.COLUMN_NAME_DEVICE_SNIPPET, device.getSnippet());
        values.put(DeviceEntry.COLUMN_NAME_DEVICE_CARRIER, device.getCarrier());

        // Which row to update, based on the ID
        String selection = DeviceEntry.COLUMN_NAME_DEVICE_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(device.getId())};

        int count = db.update(
                DeviceEntry.DEVICE_TABLE_NAME,
                values,
                selection,
                selectionArgs);
        db.close();
    }

    public void updateVersionData(Version version) {
        SQLiteDatabase db = this.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(VersionEntry.COLUMN_NAME_VERSION_ID, version.getId());
        values.put(VersionEntry.COLUMN_NAME_VERSION_NAME, version.getName());
        values.put(VersionEntry.COLUMN_NAME_VERSION_CODE, version.getVersion());
        values.put(VersionEntry.COLUMN_NAME_VERSION_CODE_NAME, version.getCodename());
        values.put(VersionEntry.COLUMN_NAME_VERSION_TARGET, version.getTarget());
        values.put(VersionEntry.COLUMN_NAME_VERSION_DISTRIBUTION, version.getDistribution());

        // Which row to update, based on the ID
        String selection = VersionEntry.COLUMN_NAME_VERSION_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(version.getId())};

        int count = db.update(
                VersionEntry.VERSION_TABLE_NAME,
                values,
                selection,
                selectionArgs);
        db.close();
    }

    public void deleteDeviceFromTable(Device device) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Define 'where' part of query.
        String selection = DeviceEntry.COLUMN_NAME_DEVICE_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(device.getId())};
        // Issue SQL statement.
        db.delete(DeviceEntry.DEVICE_TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    public void deleteVersionFromTable(Version version) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Define 'where' part of query.
        String selection = VersionEntry.COLUMN_NAME_VERSION_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(version.getId())};
        // Issue SQL statement.
        db.delete(VersionEntry.VERSION_TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    public ArrayList<Device> fetchDeviceListFromDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Device> devices = new ArrayList<>();

        try {
            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    DeviceEntry.COLUMN_NAME_DEVICE_ID,
                    DeviceEntry.COLUMN_NAME_ANDROID_ID,
                    DeviceEntry.COLUMN_NAME_DEVICE_NAME,
                    DeviceEntry.COLUMN_NAME_IMAGE_URL,
                    DeviceEntry.COLUMN_NAME_DEVICE_SNIPPET,
                    DeviceEntry.COLUMN_NAME_DEVICE_CARRIER,
            };

            String query = "SELECT * FROM " + DeviceEntry.DEVICE_TABLE_NAME;


            Cursor c = db.rawQuery(query, null);
            if (c != null) {
                while (c.moveToNext()) {
                    Device device = new Device();
                    device.setId(c.getInt(c.getColumnIndexOrThrow(DeviceEntry.COLUMN_NAME_DEVICE_ID)));
                    device.setAndroidId(c.getInt(c.getColumnIndexOrThrow(DeviceEntry.COLUMN_NAME_ANDROID_ID)));
                    device.setName(c.getString(c.getColumnIndexOrThrow(DeviceEntry.COLUMN_NAME_DEVICE_NAME)));
                    device.setImageUrl(c.getString(c.getColumnIndexOrThrow(DeviceEntry.COLUMN_NAME_IMAGE_URL)));
                    device.setSnippet(c.getString(c.getColumnIndexOrThrow(DeviceEntry.COLUMN_NAME_DEVICE_SNIPPET)));
                    device.setCarrier(c.getString(c.getColumnIndexOrThrow(DeviceEntry.COLUMN_NAME_DEVICE_CARRIER)));
                    devices.add(device);
                }
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return devices;
    }

    public ArrayList<Version> fetchVersionListFromDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Version> versions = new ArrayList<>();

        try {
            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    VersionEntry.COLUMN_NAME_VERSION_ID,
                    VersionEntry.COLUMN_NAME_VERSION_NAME,
                    VersionEntry.COLUMN_NAME_VERSION_CODE,
                    VersionEntry.COLUMN_NAME_VERSION_CODE_NAME,
                    VersionEntry.COLUMN_NAME_VERSION_TARGET,
                    VersionEntry.COLUMN_NAME_VERSION_DISTRIBUTION,
            };

            String query = "SELECT * FROM " + VersionEntry.VERSION_TABLE_NAME;


            Cursor c = db.rawQuery(query, null);
            if (c != null) {
                while (c.moveToNext()) {
                    Version version = new Version();
                    version.setId(c.getInt(c.getColumnIndexOrThrow(VersionEntry.COLUMN_NAME_VERSION_ID)));
                    version.setName(c.getString(c.getColumnIndexOrThrow(VersionEntry.COLUMN_NAME_VERSION_NAME)));
                    version.setVersion(c.getString(c.getColumnIndexOrThrow(VersionEntry.COLUMN_NAME_VERSION_CODE)));
                    version.setCodename(c.getString(c.getColumnIndexOrThrow(VersionEntry.COLUMN_NAME_VERSION_CODE_NAME)));
                    version.setTarget(c.getString(c.getColumnIndexOrThrow(VersionEntry.COLUMN_NAME_VERSION_TARGET)));
                    version.setDistribution(c.getString(c.getColumnIndexOrThrow(VersionEntry.COLUMN_NAME_VERSION_DISTRIBUTION)));
                    versions.add(version);
                }
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return versions;
    }

}
