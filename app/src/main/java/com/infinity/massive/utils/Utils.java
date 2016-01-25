package com.infinity.massive.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.infinity.massive.ApplicationMassiveInfinity;

/**
 * Created by Ilanthirayan on 25/1/16.
 */
public class Utils {

    /**
     * Checks whether there's connection to Internet.
     *
     * @return true if there's connection or false otherwise
     */
    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) ApplicationMassiveInfinity.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}
