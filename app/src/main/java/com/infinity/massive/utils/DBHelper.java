package com.infinity.massive.utils;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.infinity.massive.ApplicationMassiveInfinity;
import com.infinity.massive.model.pojo.Devices;

/**
 * Created by Ilanthirayan on 25/1/16.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "redmart_container.db";
    private static final int DB_VERSION = 1;
    private static final String TAG = DBHelper.class.getSimpleName();
    private static final String CREATE_INDEX = "CREATE INDEX IF NOT EXISTS ";

    enum INSTANCE {
        INSTANCE;
        final DBHelper instance = new DBHelper();
    }

    private DBHelper() {
        super(ApplicationMassiveInfinity.getContext(), DB_NAME, null, DB_VERSION);
    }

    public static DBHelper getInstance() {
        return INSTANCE.INSTANCE.instance;
    }

    private static final String CREATE_TABLE_DEVICE = "CREATE TABLE "
            + Devices.TABLE_NAME + " ("
            + Devices.Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Devices.Columns.DEVICE_ID + " INTEGER, "
            + Devices.Columns.IMAGE_URL + " TEXT, "
            + Devices.Columns.NAME + " TEXT, "
            + Devices.Columns.ANDROID_ID + " TEXT, "
            + Devices.Columns.SNIPPET + " TEXT); ";

    private static final String CREATE_DEVICE_INDEX_ID = CREATE_INDEX
            + " index_based_id ON "
            + Devices.TABLE_NAME + "("
            + Devices.Columns.DEVICE_ID + ");";

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "db onCreate");
        //CREATE TABLES
        db.execSQL(CREATE_TABLE_DEVICE);

        //CREATE INDICES
        db.execSQL(CREATE_DEVICE_INDEX_ID);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "db onUpgrade");
        //UPGRADE
    }
}
