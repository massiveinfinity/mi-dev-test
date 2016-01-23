package com.janibanez.database.helpers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.janibanez.database.DbContentProvider;
import com.janibanez.database.models.DbVersion;
import com.janibanez.server.models.Version;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jwgibanez on 23/01/2016.
 */
public class VersionDaoHelper {

    public static void insertVersion(Context context, ContentValues values) {
        String selection = DbVersion.COLUMN_ID + " = ?";
        String[] selectionArgs = { values.getAsString(DbVersion.COLUMN_ID) };

        Cursor cursor = context.getContentResolver().query(DbContentProvider.CONTENT_URI_VERSION(context), null, selection, selectionArgs, null);

        if (cursor != null) {
            int count = cursor.getCount();
            cursor.close();
            if (count > 0) {
                context.getContentResolver().update(DbContentProvider.CONTENT_URI_VERSION(context), values, selection, selectionArgs);

            }
        }

        context.getContentResolver().insert(DbContentProvider.CONTENT_URI_VERSION(context), values);
    }

    public static List<Version> getVersions(Context context) {
        List<Version> models = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(DbContentProvider.CONTENT_URI_VERSION(context), null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Version model = new Version();
                model.id = cursor.getInt(cursor.getColumnIndex(DbVersion.COLUMN_ID));
                model.version = cursor.getString(cursor.getColumnIndex(DbVersion.COLUMN_VERSION));
                model.name = cursor.getString(cursor.getColumnIndex(DbVersion.COLUMN_NAME));
                model.codename = cursor.getString(cursor.getColumnIndex(DbVersion.COLUMN_CODENAME));
                model.target = cursor.getString(cursor.getColumnIndex(DbVersion.COLUMN_TARGET));
                model.distribution = cursor.getString(cursor.getColumnIndex(DbVersion.COLUMN_DISTRIBUTION));
                models.add(model);
            }
            cursor.close();
        }

        return models;
    }

    public static boolean deleteVersions(Context context) {
        ContentResolver contentResolver = context.getContentResolver();

        int count = contentResolver.delete(DbContentProvider.CONTENT_URI_VERSION(context), null, null);

        return (count > 0);
    }
}
