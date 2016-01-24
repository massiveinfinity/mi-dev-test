package com.mi.showcase.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mi.showcase.R;
import com.mi.showcase.exec.ShowcaseMobileTask;
import com.mi.showcase.util.MessageHandler;
import com.mi.showcase.view.dto.MobileDataListModel;

/**
 * @author Sachith Dickwella
 */
public class InformationDialog extends DialogFragment {

    private ImageView imageView;
    private ProgressBar progressBar;
    private View view;

    private MobileDataListModel mobileDataListModel;
    private Thread isolatedThread;

    public void setMobileDataListModel(MobileDataListModel mobileDataListModel) {
        this.mobileDataListModel = mobileDataListModel;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstances) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_information_dialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_information, null);
        alertDialogBuilder.setView(view)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return alertDialogBuilder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        imageView = (ImageView) view.findViewById(R.id.mobilePhoneImage);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        TextView deviceName = (TextView) view.findViewById(R.id.deviceName);
        TextView androidName = (TextView) view.findViewById(R.id.androidName);
        TextView codeName = (TextView) view.findViewById(R.id.codeName);
        TextView distribution = (TextView) view.findViewById(R.id.distribution);
        TextView carrier = (TextView) view.findViewById(R.id.carrier);
        TextView snippet = (TextView) view.findViewById(R.id.snippet);
        TextView version = (TextView) view.findViewById(R.id.version);

        deviceName.setText(mobileDataListModel.getDeviceName());
        androidName.setText(mobileDataListModel.getAndroidName());
        codeName.setText(getString(R.string.code_name) + mobileDataListModel.getCodeName());
        distribution.setText(getString(R.string.distribution) + mobileDataListModel.getDistribution());
        version.setText(getString(R.string.version) + mobileDataListModel.getVersion());
        carrier.setText(getString(R.string.carrier) + mobileDataListModel.getCarrier());
        snippet.setText(mobileDataListModel.getSnippet());

        try {
            isolatedThread = MessageHandler.executeHandler(loadImage);
        } catch (InterruptedException ex) {
            Log.e(ex.getMessage(), ex.toString());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isolatedThread.isAlive()) {
            isolatedThread.interrupt();
        }
    }

    private final Runnable loadImage = new Runnable() {
        @Override
        public void run() {
            ShowcaseMobileTask.LoadImageTask loadImageTask = new ShowcaseMobileTask.LoadImageTask(getActivity(), imageView, progressBar);
            loadImageTask.execute(mobileDataListModel.getImageUrl());
        }
    };
}
