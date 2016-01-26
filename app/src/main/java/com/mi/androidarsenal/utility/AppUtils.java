package com.mi.androidarsenal.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * @author Samir Sarosh
 */
@SuppressWarnings("ALL")
public class AppUtils implements AppConstants {

    public static OnEditItemListener mOnEditItemListener;

    public static void setOnEditItemListener(OnEditItemListener observer) {
        mOnEditItemListener = observer;
    }

    /**
     * Checks the network connectivity
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();

        boolean isAvailable = activeNetworkInfo != null
                && activeNetworkInfo.isConnected();

        if (!isAvailable) {
            Toast.makeText(ctx, NO_NETWORK, Toast.LENGTH_LONG).show();
        }

        return isAvailable;
    }

    public static int getColorFromRes(FragmentActivity activity, int colorNeeded) {
        int color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = activity.getColor(colorNeeded);
        } else {
            //noinspection deprecation
            color = activity.getResources().getColor(colorNeeded);
        }
        return color;
    }


}
