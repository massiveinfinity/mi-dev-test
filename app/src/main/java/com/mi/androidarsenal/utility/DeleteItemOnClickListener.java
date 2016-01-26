package com.mi.androidarsenal.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.mi.androidarsenal.R;
import com.mi.androidarsenal.network.RequestOperation;

/**
 * @author Samir Sarosh
 */
@SuppressWarnings("ALL")
public class DeleteItemOnClickListener implements View.OnClickListener {
    private final Context mContext;
    private boolean mIsDevice;
    private String mId;

    public DeleteItemOnClickListener(Context context, String id, boolean isDevice) {
        mContext = context;
        mId = id;
        mIsDevice = isDevice;
    }

    @Override
    public void onClick(View v) {
        showConfirmationDialog();


    }

    private void showConfirmationDialog() {

        final View dialogView = View.inflate(mContext, R.layout.confirmation_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(dialogView)
                .setCancelable(false);


        final AlertDialog mDialog = builder.create();
        dialogView.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
                mDialog.dismiss();
            }
        });


        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.show();
    }

    private void deleteItem() {
        if (AppUtils.isNetworkAvailable(mContext.getApplicationContext())) {
            RequestOperation requestOperation = RequestOperation.getRequestOperationInstance(mContext);
            if (mIsDevice) {
                requestOperation.deleteDeviceData(mId, this);
            } else {
                requestOperation.deleteVersionData(mId, this);
            }
        }
    }

}
