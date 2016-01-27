package com.zahid.mitask.devicesinfo.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.zahid.mitask.devicesinfo.bean.Device;
import com.zahid.mitask.devicesinfo.helper.DatabaseHelper;

/**
 * Created by zahid.r on 1/26/2016.
 */
public class DeleteDeviceFromTableTask extends AsyncTask<Device, Void, Void> {
    Context context;
    public DeleteDeviceFromTableTask(Context context){
        this.context = context;
    }
    @Override
    protected Void doInBackground(Device... params) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.deleteDeviceFromTable(params[0]);
        return null;
    }
}
