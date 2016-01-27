package com.zahid.mitask.devicesinfo.helper;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;

import com.zahid.mitask.devicesinfo.R;
import com.zahid.mitask.devicesinfo.fragments.CompoundAlertDialogFragment;
import com.zahid.mitask.devicesinfo.fragments.SimpleAlertDialogFragment;
import com.zahid.mitask.devicesinfo.interfaces.DialogClickListener;


/**
 * Created by zahid.r on 1/25/2016.
 */
public class AlertDialogHelper {

    public static void showSimpleAlertDialog(Context context, String message) {
        DialogFragment simpleAlertDialogFragment = SimpleAlertDialogFragment.newInstance(
                R.string.empty, message);
        final Activity activity = (Activity) context;
        if (activity != null) {
            try {
                simpleAlertDialogFragment.setCancelable(false);
                simpleAlertDialogFragment.show(activity.getFragmentManager(), "dialog");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void showSimpleAlertDialog(Context context, String message, int tag) {
        DialogFragment simpleAlertDialogFragment = SimpleAlertDialogFragment.newInstance(
                R.string.empty, message, tag);
        final Activity activity = (Activity) context;
        if (activity != null) {
            try {
                simpleAlertDialogFragment.setCancelable(false);
                simpleAlertDialogFragment.show(activity.getFragmentManager(), "dialog");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void showCompoundAlertDialog(Context context, DialogClickListener dialogClickListener, String message, String posButton, String negButton, int tag) {
        CompoundAlertDialogFragment compoundDialogFragment = CompoundAlertDialogFragment.newInstance(
                "", message, posButton, negButton, tag);
        compoundDialogFragment.setDialogListener(dialogClickListener);
        final Activity activity = (Activity) context;
        if (activity != null)
            try {
                compoundDialogFragment.show(activity.getFragmentManager(), "dialog");
            } catch (Exception e) {
                e.printStackTrace();
            }
    }


}
