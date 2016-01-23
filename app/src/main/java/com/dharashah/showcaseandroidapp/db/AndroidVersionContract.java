package com.dharashah.showcaseandroidapp.db;

import android.provider.BaseColumns;

/**
 * Created by user on 21/01/2016.
 */
public final class AndroidVersionContract {
    public AndroidVersionContract(){}

    public static abstract class AndroidVersionEntry implements BaseColumns {
        public static final String TABLE_NAME = "android_history_master";
        public static final String COLUMN_NAME_ANDROID_ID = "android_id";
        public static final String COLUMN_NAME_ANDROID_NAME = "android_name";
        public static final String COLUMN_NAME_ANDROID_CODE_NAME = "android_code_name";
        public static final String COLUMN_NAME_ANDROID_TARGET = "android_target";
        public static final String COLUMN_NAME_ANDROID_DISTRIBUTION = "android_distribution";
    }
}
