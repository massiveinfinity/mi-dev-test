package com.infinity.massive.utils;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.infinity.massive.ApplicationMassiveInfinity;

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

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "db onCreate");
        //CREATE TABLES
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "db onUpgrade");
        //UPGRADE
    }
}
