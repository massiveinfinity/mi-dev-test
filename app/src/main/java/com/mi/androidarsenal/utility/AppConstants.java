package com.mi.androidarsenal.utility;

/**
 * @author Samir Sarosh
 */
@SuppressWarnings("ALL")
public interface AppConstants {

    public final String BASE_URL = "http://mobilesandboxdev.azurewebsites.net";
    public final String ALL_SUB_URL = "/db";
    public final String DEVICES_SUB_URL = "/devices";
    public final String VERSIONS_SUB_URL = "/android";

    // Database Version
    public final int DATABASE_VERSION = 1;

    // Database Name
    public final String DATABASE_NAME = "AndroidArsenal";

    // Devices,Versions table name
    public final String TABLE_DEVICES = "AndroidDevices";
    public final String TABLE_VERSIONS = "AndroidVersions";

    public final String KEY_ID = "_id";

    // Devices Table Columns names
    public final String KEY_DEVICE_ID = "device_id";
    public final String KEY_ANDROID_ID = "android_id";
    public final String KEY_NAME = "name";
    public final String KEY_SNIPPET = "snippet";
    public final String KEY_IMAGE_URL = "image_url";
    public final String KEY_CARRIER = "carrier";

    // versions Table Columns names
    public final String KEY_VERSION_ID = "version_id";
    public final String KEY_VERSION_NAME = "name";
    public final String KEY_VERSION = "version";
    public final String KEY_CODENAME = "codename";
    public final String KEY_DESTRIBUTION = "destribution";
    public final String KEY_TARGET = "target";

    public final String NOT_AVAILABLE_TEXT = "N/A";

    public final String EDIT_BUNDLE_DB_TYPE = "db_type";

    public final String EMPTY_FIELDS = "All fields are empty";

    public final String NO_NETWORK = "No network available";
}
