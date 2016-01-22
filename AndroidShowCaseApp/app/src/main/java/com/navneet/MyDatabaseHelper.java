package com.navneet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 1/8/16.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "devicesDB.db";

    public static final String TABLE_DEVICES = "devices";

    public static final String COLUMN_ID = "id";
    public static final String NAME = "name";
    public static final String VERSION = "version";
    public static final String CODE_NAME = "codename";
    public static final String TARGET = "target";
    public static final String DISTRIBUTION = "distribution";

    private static final String DATABASE_CREATE = "create table " + TABLE_DEVICES + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_ID + " integer, " + NAME + " text, " + VERSION + " text, " + CODE_NAME + " text, " + TARGET + " text, " + DISTRIBUTION + " text);";
    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
