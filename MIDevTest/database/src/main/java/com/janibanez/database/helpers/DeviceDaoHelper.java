package com.janibanez.database.helpers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.janibanez.database.DbContentProvider;
import com.janibanez.database.models.DbDevice;
import com.janibanez.server.models.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jwgibanez on 23/01/2016.
 */
public class DeviceDaoHelper {

    public static void insertDevice(Context context, ContentValues values) {
        String selection = DbDevice.COLUMN_ID + " = ?";
        String[] selectionArgs = {values.getAsString(DbDevice.COLUMN_ID)};

        Cursor cursor = context.getContentResolver().query(DbContentProvider.CONTENT_URI_DEVICE(context), null, selection, selectionArgs, null);

        if (cursor != null) {
            int count = cursor.getCount();
            cursor.close();
            if (count > 0) {
                context.getContentResolver().update(DbContentProvider.CONTENT_URI_DEVICE(context), values, selection, selectionArgs);

            }
        }

        context.getContentResolver().insert(DbContentProvider.CONTENT_URI_DEVICE(context), values);
    }

    public static List<Device> getDevices(Context context) {
        List<Device> models = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(DbContentProvider.CONTENT_URI_DEVICE(context), null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Device model = new Device();
                model.id = cursor.getInt(cursor.getColumnIndex(DbDevice.COLUMN_ID));
                model.androidId = cursor.getString(cursor.getColumnIndex(DbDevice.COLUMN_ANDROID_ID));
                model.name = cursor.getString(cursor.getColumnIndex(DbDevice.COLUMN_NAME));
                model.snippet = cursor.getString(cursor.getColumnIndex(DbDevice.COLUMN_SNIPPET));
                model.carrier = cursor.getString(cursor.getColumnIndex(DbDevice.COLUMN_CARRIER));
                model.imageUrl = cursor.getString(cursor.getColumnIndex(DbDevice.COLUMN_IMAGE_URL));
                models.add(model);
            }
            cursor.close();
        }

        return models;
    }

    public static boolean deleteDevices(Context context) {
        ContentResolver contentResolver = context.getContentResolver();

        int count = contentResolver.delete(DbContentProvider.CONTENT_URI_DEVICE(context), null, null);

        return (count > 0);
    }

}