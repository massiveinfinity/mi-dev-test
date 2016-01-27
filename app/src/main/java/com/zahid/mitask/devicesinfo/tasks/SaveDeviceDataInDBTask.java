package com.zahid.mitask.devicesinfo.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.zahid.mitask.devicesinfo.bean.Device;
import com.zahid.mitask.devicesinfo.helper.DatabaseHelper;

import java.util.ArrayList;

/**
 * Created by zahid.r on 1/26/2016.
 */
public class SaveDeviceDataInDBTask extends AsyncTask<ArrayList<Device>, Void, Void> {
    Context context;
    public SaveDeviceDataInDBTask(Context context){
        this.context = context;
    }
    @Override
    protected Void doInBackground(ArrayList<Device>... params) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.saveDeviceDataInDB(params[0]);
        return null;
    }
}
