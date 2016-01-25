package com.infinity.massive;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by Ilanthirayan on 25/1/16.
 */
public class ApplicationMassiveInfinity extends Application {

    private static final String TAG = ApplicationMassiveInfinity.class.getSimpleName();

    private static LocalBroadcastManager sLbm;

    protected static Context sContext;
    protected static Activity sActivity;


    @Override
    public void onCreate() {

        super.onCreate();
        Log.i(TAG, "onCreate: Called");
        sContext = getApplicationContext();

        sLbm = LocalBroadcastManager.getInstance(sContext);

    }

    public static LocalBroadcastManager getLbm(){
        return sLbm;
    }

    protected static void setCurrentActivity(Activity currentActivity) {
        sActivity = currentActivity;
    }

    public static Context getContext() {
        return sContext;
    }

    protected static Activity getCurrentActivity() {
        return sActivity;
    }
}
