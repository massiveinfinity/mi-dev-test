package com.janibanez.midevtest.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Window;

import com.janibanez.midevtest.R;

/**
 * Created by jwgibanez on 22/01/2016.
 */
public class DialogUtilities {

    public static void showConfirmation(Context context, String title, String message,
                                        DialogInterface.OnClickListener neutralClickListener,
                                        DialogInterface.OnClickListener positiveClickListener) {

        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, context.getString(R.string.text_no), neutralClickListener);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.text_yes), positiveClickListener);

        if (TextUtils.isEmpty(title)) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        dialog.show();
    }

    public static void showMessageDialog(Context context, String title, String message,
                                         DialogInterface.OnClickListener listener) {

        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, context.getString(R.string.text_close), listener);

        if (TextUtils.isEmpty(title)) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        dialog.show();
    }

}
