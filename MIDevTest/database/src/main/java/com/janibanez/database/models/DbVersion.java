package com.janibanez.database.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jwgibanez on 23/01/2016.
 */
public class DbVersion {

    public static final String
            TABLE_NAME          = "Version",
            COLUMN_ID           = "_Id",
            COLUMN_NAME         = "Name",
            COLUMN_CODENAME     = "Codename",
            COLUMN_VERSION      = "Version",
            COLUMN_TARGET       = "Target",
            COLUMN_DISTRIBUTION = "Distribution";

    public static final String
            TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
                COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                COLUMN_NAME + " VARCHAR, " +
                COLUMN_CODENAME + " VARCHAR, " +
                COLUMN_VERSION + " VARCHAR, " +
                COLUMN_TARGET + " VARCHAR, " +
                COLUMN_DISTRIBUTION + " VARCHAR )";

    public static final String[]
            COLUMNS_ALL = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_CODENAME,
                COLUMN_VERSION,
                COLUMN_TARGET,
                COLUMN_DISTRIBUTION
            };

    public static Cursor getVersion(SQLiteDatabase db, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        try {
            String[] columns = COLUMNS_ALL;
            String sortOrder = COLUMN_ID + " ASC";
            cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, sortOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public static long insertVersion(SQLiteDatabase db, ContentValues values) {
        long returnId = 0;
        try {
            db.beginTransaction();
            returnId = db.insert(TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return returnId;
    }

    public static int updateVersion(SQLiteDatabase db, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        try {
            db.beginTransaction();
            count = db.update(TABLE_NAME, values, selection, selectionArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    public static void deleteVersion(SQLiteDatabase db, String selection, String[] selectionArgs) {
        try {
            db.beginTransaction();
            db.delete(TABLE_NAME, selection, selectionArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
}
