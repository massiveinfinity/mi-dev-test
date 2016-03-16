package com.janibanez.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.janibanez.database.models.DbDevice;
import com.janibanez.database.models.DbVersion;

/**
 * Created by jwgibanez on 23/01/2016.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MiDevTestDb";
    public static final int DATABASE_VERSION = 1;

    DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DbDevice.TABLE_CREATE);
        sqLiteDatabase.execSQL(DbVersion.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //if (newVersion > oldVersion) {
            /* starting upgrade */
            //if (oldVersion < 2 && newVersion >= 2) {
            //    upgradeToV2(sqLiteDatabase);
            //}
        //}
    }
}
