package com.janibanez.database;

import android.content.ContentProvider;
import android.content.Context;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by jwgibanez on 23/01/2016.
 */
public abstract class DbContenProviderBase extends ContentProvider {

    private final String mTAG = "DbContenProviderBase";

    private static String CONTENT_PROVIDER_AUTHORITY = "";
    private static String CONTENT_URL = "";

    private static final String
            CONTENT_DEVICE = "device",
            CONTENT_VERSION = "version";

    private static void initContentProvider(Context context) {
        if(CONTENT_PROVIDER_AUTHORITY.equals("")) {
            CONTENT_PROVIDER_AUTHORITY =  context.getResources().getString(R.string.db_content_provider);
        }

        if(CONTENT_URL.equals("")) {
            CONTENT_URL = "content://" + CONTENT_PROVIDER_AUTHORITY + "/";
        }
    }

    // content provider modules
    public static Uri CONTENT_URI_DEVICE(Context context) {
        initContentProvider(context);
        return Uri.parse(CONTENT_URL + CONTENT_DEVICE);
    }

    public static Uri CONTENT_URI_VERSION(Context context) {
        initContentProvider(context);
        return Uri.parse(CONTENT_URL + CONTENT_VERSION);
    }

    // content provider sub-modules
    public static final int
            URI_DEVICE          = 100,
            URI_DEVICE_ITEM     = 101,
            URI_VERSION         = 200,
            URI_VERSION_ITEM    = 201;

    public static final String METHOD_EXEC_SQL = "execSQL";

    public static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static SQLiteDatabase mDatabase;

    private void initUriMatcher(Context context) {
        initContentProvider(context);
        mUriMatcher.addURI(CONTENT_PROVIDER_AUTHORITY, CONTENT_DEVICE, URI_DEVICE);
        mUriMatcher.addURI(CONTENT_PROVIDER_AUTHORITY, CONTENT_DEVICE + "/#", URI_DEVICE_ITEM);
        mUriMatcher.addURI(CONTENT_PROVIDER_AUTHORITY, CONTENT_VERSION, URI_VERSION);
        mUriMatcher.addURI(CONTENT_PROVIDER_AUTHORITY, CONTENT_DEVICE + "/#", URI_VERSION_ITEM);
    }

    @Override
    public boolean onCreate() {
        Context context = getContext().getApplicationContext();

        DbHelper databaseHelper = new DbHelper(context);

        initUriMatcher(context);

        try {
            mDatabase = databaseHelper.getWritableDatabase();
        } catch (Exception e) {
            Log.e(mTAG, "Failed to create content provider!", e);
        }

        return mDatabase != null;
    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        if (METHOD_EXEC_SQL.equals(method) && arg != null) {
            try {
                mDatabase.beginTransaction();
                mDatabase.execSQL(arg);
                mDatabase.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mDatabase.endTransaction();
            }
        }

        return (null);
    }

    public static SQLiteDatabase getDatabase() {
        return mDatabase;
    }
}
