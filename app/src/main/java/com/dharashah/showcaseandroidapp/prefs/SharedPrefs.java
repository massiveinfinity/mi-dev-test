package com.dharashah.showcaseandroidapp.prefs;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.dharashah.showcaseandroidapp.ShowCaseApp;

/**
 * Sharedpreference class that stores the values in the sharedpreference
 * related to the app
 *
 * @author Dhara Shah
 */
public class SharedPrefs {
    public static final String DEVICE_START_COUNT = "device_start_count";
    public static final String HISTORY_START_COUNT = "history_start_count";
    public static final String DEVICE_END_COUNT = "device_end_count";
    public static final String HISTORY_END_COUNT = "history_end_count";

    private static final String PREFS_NAME = "ShowCaseAndroidApp";

    public static SharedPreferences getPrefs() {
        return ShowCaseApp.getAppContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static String getString(String key, String defaultValue) {
        SharedPreferences sp = getPrefs();
        return sp.getString(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        SharedPreferences sp = getPrefs();
        return sp.getInt(key, defaultValue);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences sp = getPrefs();
        return sp.getBoolean(key, defaultValue);
    }

    public static void putString(String key, String value) {
        SharedPreferences sp = getPrefs();
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void putInt(String key, int value) {
        SharedPreferences sp = getPrefs();
        Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void putStrings(String[] keys, String[] values) {
        SharedPreferences sp = getPrefs();
        Editor editor = sp.edit();

        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            String value = values[i];
            editor.putString(key, value);
        }
        editor.commit();
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences sp = getPrefs();
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
