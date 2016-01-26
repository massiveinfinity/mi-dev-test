package com.mi.androidarsenal.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.mi.androidarsenal.R;

/**
 * This is a utility singleton class which deals with the sharedpreferences
 * transactions
 *
 * @author Samir Sarosh
 */
@SuppressWarnings("ALL")
public class SharedPreferenceUtil {

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private final String OFFLINE_CHECK = "offline";


    private static SharedPreferenceUtil mSharedPreferenceUtilInstance = null;

    /**
     * Passes the same instance of this singleton class
     *
     * @param context
     * @return
     */
    public static SharedPreferenceUtil getSharedPreferenceUtilInstance(
            Context context) {
        if (mSharedPreferenceUtilInstance == null) {
            mSharedPreferenceUtilInstance = new SharedPreferenceUtil(context);
        }
        return mSharedPreferenceUtilInstance;
    }

    private SharedPreferenceUtil(Context context) {
        mContext = context;
        final String preferencesName = context.getResources().getString(
                R.string.preferences);
        mSharedPreferences = mContext.getSharedPreferences(preferencesName,
                Context.MODE_PRIVATE);
    }

    /**
     * save if data is successfully added to the database
     */
    public void saveDbPopulatedSuccess() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(OFFLINE_CHECK, true);
        editor.commit();
    }

    /**
     * check if data is successfully added to the database
     *
     * @return
     */
    public boolean isDbPopulatedSuccess() {
        return mSharedPreferences.getBoolean(OFFLINE_CHECK, false);
    }
}
