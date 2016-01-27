package com.zahid.mitask.devicesinfo.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by zahid.r on 1/25/2015.
 */
public class ProgressDialogHelper {
    private Context context;
    private ProgressDialog pDialog;

    public ProgressDialogHelper(Context context) {
        this.context = context;
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
    }

    public void setCancellable(boolean value) {
        pDialog.setCancelable(value);
    }

    public void showProgressDialog(String message) {
        pDialog.setMessage(message);
        if (!((Activity) context).isFinishing() && !pDialog.isShowing()) {
            try {
                pDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void cancelProgressDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.cancel();
        }
    }
}
