package com.janibanez.database;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.janibanez.database.models.DbDevice;
import com.janibanez.database.models.DbVersion;

/**
 * Created by jwgibanez on 23/01/2016.
 */
public class DbContentProvider extends DbContenProviderBase {

    private final String mTAG = "DbContentProvider";

    // cursor to contain 0..x items
    private final String MIME_TYPE_ALL = "vnd.android.cursor.dir";

    // cursor to contain 1 item
    private final String MIME_TYPE_ITEM = "vnd.android.cursor.item";

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (mUriMatcher.match(uri)) {
            case URI_DEVICE:
                DbDevice.deleteDevice(mDatabase, selection, selectionArgs);
                break;
            case URI_VERSION:
                DbVersion.deleteVersion(mDatabase, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return 0;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        switch (mUriMatcher.match(uri)) {
            case URI_DEVICE:
                cursor = DbDevice.getDevice(mDatabase, selection, selectionArgs);
                break;
            case URI_DEVICE_ITEM:
                cursor = DbDevice.getDevice(mDatabase, selection, selectionArgs);
                break;
            case URI_VERSION:
                cursor = DbVersion.getVersion(mDatabase, selection, selectionArgs);
                break;
            case URI_VERSION_ITEM:
                cursor = DbVersion.getVersion(mDatabase, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        if (cursor != null) cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case URI_DEVICE:
                return MIME_TYPE_ALL + "/" + CONTENT_URI_DEVICE(getContext()).getLastPathSegment();
            case URI_DEVICE_ITEM:
                return MIME_TYPE_ITEM + "/" + CONTENT_URI_DEVICE(getContext()).getLastPathSegment();
            case URI_VERSION:
                return MIME_TYPE_ALL + "/" + CONTENT_URI_VERSION(getContext()).getLastPathSegment();
            case URI_VERSION_ITEM:
                return MIME_TYPE_ITEM + "/" + CONTENT_URI_DEVICE(getContext()).getLastPathSegment();
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long returnId;

        switch (mUriMatcher.match(uri)) {
            case URI_DEVICE:
                returnId = DbDevice.insertDevice(mDatabase, values);
                break;
            case URI_VERSION:
                returnId = DbVersion.insertVersion(mDatabase, values);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        Uri returnUri = ContentUris.withAppendedId(uri, returnId);

        return returnUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;

        switch (mUriMatcher.match(uri)) {
            case URI_DEVICE:
                count = DbDevice.updateDevice(mDatabase, values, selection, selectionArgs);
                break;
            case URI_VERSION:
                count = DbVersion.updateVersion(mDatabase, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}
