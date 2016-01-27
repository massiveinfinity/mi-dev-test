package com.mi.showcase.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.mi.showcase.R;

/**
 * @author  Sachith Dickwella
 */
public class ConnectionDialog extends DialogFragment {

    public Dialog onCreateDialog(final Bundle savedInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_connection_dialog)
                .setMessage(R.string.message_connection_dialog)
                .setPositiveButton(R.string.dialog_settins, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(settingsIntent, savedInstance);
                    }
                }).setNegativeButton(R.string.dialog_cancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return  builder.create();
    }
}
