package com.zahid.mitask.devicesinfo.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.zahid.mitask.devicesinfo.bean.Device;
import com.zahid.mitask.devicesinfo.bean.Version;
import com.zahid.mitask.devicesinfo.helper.DatabaseHelper;

import java.util.ArrayList;

/**
 * Created by zahid.r on 1/26/2016.
 */
public class SaveVersionDataInDBTask extends AsyncTask<ArrayList<Version>, Void, Void> {
    Context context;

    public SaveVersionDataInDBTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(ArrayList<Version>... params) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.saveVersionDataInDB(params[0]);
        return null;
    }
}
