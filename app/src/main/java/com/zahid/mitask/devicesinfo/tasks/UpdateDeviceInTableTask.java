package com.zahid.mitask.devicesinfo.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.zahid.mitask.devicesinfo.bean.Device;
import com.zahid.mitask.devicesinfo.helper.DatabaseHelper;

import java.util.ArrayList;

/**
 * Created by zahid.r on 1/26/2016.
 */
public class UpdateDeviceInTableTask extends AsyncTask<Device, Void, Void> {
    Context context;
    public UpdateDeviceInTableTask(Context context){
        this.context = context;
    }
    @Override
    protected Void doInBackground(Device... params) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.updateDeviceData(params[0]);
        return null;
    }
}
