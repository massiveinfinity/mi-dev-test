package com.zahid.mitask.devicesinfo.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.zahid.mitask.devicesinfo.interfaces.PopUpMenuClickListener;
import com.zahid.mitask.devicesinfo.utils.Constants;


/**
 * Created by zahid.r on 1/25/2016.
 */
public class PopUpDialogFragment extends DialogFragment {
    private int tag;
    private PopUpMenuClickListener popUpMenuClickListener;

    public static PopUpDialogFragment newInstance(String title, String[] items) {
        PopUpDialogFragment frag = new PopUpDialogFragment();
        Bundle args = new Bundle();
        args.putString(Constants.POPUP_DIALOG_TITLE, title);
        args.putStringArray(Constants.POPUP_DIALOG_ITEMS, items);
        frag.setArguments(args);
        return frag;
    }

    public static PopUpDialogFragment newInstance(String title, String[] items, int tag) {
        PopUpDialogFragment frag = new PopUpDialogFragment();
        Bundle args = new Bundle();
        args.putString(Constants.POPUP_DIALOG_TITLE, title);
        args.putStringArray(Constants.POPUP_DIALOG_ITEMS, items);
        args.putInt(Constants.ALERT_DIALOG_TAG, tag);
        frag.setArguments(args);
        return frag;
    }

    public void setOnPopUpMenuListener(PopUpMenuClickListener popUpMenuClickListener) {
        this.popUpMenuClickListener = popUpMenuClickListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        String[] items = args.getStringArray(Constants.POPUP_DIALOG_ITEMS);
        String title = args.getString(Constants.POPUP_DIALOG_TITLE);
        tag = args.getInt(Constants.ALERT_DIALOG_TAG, 0);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setItems(items, mListener);
        return builder.create();
    }

    DialogInterface.OnClickListener mListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            popUpMenuClickListener.onPopUpMenuItemClicked(which, tag);
        }
    };
}
