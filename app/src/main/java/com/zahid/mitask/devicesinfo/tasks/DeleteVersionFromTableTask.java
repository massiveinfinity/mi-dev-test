package com.zahid.mitask.devicesinfo.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.zahid.mitask.devicesinfo.bean.Version;
import com.zahid.mitask.devicesinfo.helper.DatabaseHelper;

/**
 * Created by zahid.r on 1/26/2016.
 */
public class DeleteVersionFromTableTask extends AsyncTask<Version, Void, Void> {
    Context context;
    public DeleteVersionFromTableTask(Context context){
        this.context = context;
    }
    @Override
    protected Void doInBackground(Version... params) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.deleteVersionFromTable(params[0]);
        return null;
    }
}
