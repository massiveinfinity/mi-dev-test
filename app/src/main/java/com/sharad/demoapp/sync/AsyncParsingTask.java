package com.sharad.demoapp.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.sharad.demoapp.entity.PhoneDetailEntity;
import com.sharad.demoapp.parser.ParseUtil;
import com.sharad.demoapp.sync.ParseListener;


/**
 * This class is used to Parse the data in background.
 * Extention of parsing is also there for Json data
 *
 * @author Sharad Waghchaure
 */
public class AsyncParsingTask extends AsyncTask<String, Void, Integer> {
    private final String TAG = "AsyncParsingTask";
    private int taskID;
    private ParseListener parseListener;
    private Object mObject;
    // private Singleton singleton;

    private Context mContext;

    public AsyncParsingTask(int taskID, ParseListener parseListener, Context context) {
        this.taskID = taskID;
        this.parseListener = parseListener;
        mContext = context;
    }

    @Override
    protected Integer doInBackground(String... params) {
        String response = params[0];
        switch (taskID) {
            case SyncManager.GET_PHONE_DETAILS :
                PhoneDetailEntity mPhoneDetailEntity = (PhoneDetailEntity) ParseUtil.getObject(response,PhoneDetailEntity.class);
                mObject = mPhoneDetailEntity;
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        parseListener.onParseSuccess(taskID, mObject);

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
