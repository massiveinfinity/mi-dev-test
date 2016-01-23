package com.dharashah.showcaseandroidapp.db;

import android.provider.BaseColumns;

/**
 * Created by user on 21/01/2016.
 */
public final class DeviceContract {
    public DeviceContract(){}

    public static abstract class DeviceEntry implements BaseColumns {
        public static final String TABLE_NAME = "device_master";
        public static final String COLUMN_NAME_DEVICE_ID = "device_id";
        public static final String COLUMN_NAME_DEVICE_NAME = "device_name";
        public static final String COLUMN_NAME_DEVICE_DESC = "device_desc";
        public static final String COLUMN_NAME_DEVICE_IMAGE = "device_image";
        public static final String COLUMN_NAME_ANDROID_ID = "android_id";
    }

}
