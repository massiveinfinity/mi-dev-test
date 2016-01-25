package com.sharad.demoapp.sync;


import android.app.Activity;
import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sharad.demoapp.constant.URLConstants;
import com.sharad.demoapp.network.DownloadHandler;
import com.sharad.demoapp.network.DownloadListener;

import cz.msebera.android.httpclient.message.BasicHeader;


/**
 * This class is responsible for request and handle response.
 * This is the single point of controller where all events meets.
 *
 * @author Sharad waghchaure
 */

public class SyncManager implements ParseListener, DownloadListener {

    private SyncListener syncListener;
    private BasicHeader[] headers;
    private int type;
    private static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    //task ID8
    public static final int LOGIN = 0, REGISTRATION = 2, GET_PHONE_DETAILS = 3;

    public static final int TYPE_AUTO_SYNC = 0, TYPE_MANUAL_SYNC = 1;
    public static final int SYNC_SUCCESS = 0, SYNC_FAIL = 1, NO_INTERNET = 2, SERVER_DOWN = 3;
    private Context ctx;


    public SyncManager(Context context, int type, SyncListener jobListener) {
        this.ctx = context;
        this.type = type;
        this.syncListener = jobListener;
    }

    public SyncManager(Activity context, int type, SyncListener jobListener) {
        this.ctx = context;
        this.type = type;
        this.syncListener = jobListener;

    }

    public void onNewParseJob(int taskID, String response) {

    }

    public void onParseSuccess(int taskID, Object mObject) {
        syncListener.onSyncSuccess(SYNC_SUCCESS, taskID, mObject);

    }

    public void onParseFailed(int taskID) {

    }


    public void onDownloadFailure(int taskID, Throwable throwable, String response) {

        syncListener.onSyncFailure(SERVER_DOWN, taskID, "Server time out");
    }

    @Override
    public void onDownloadSuccess(int taskID, String response) {
        AsyncParsingTask asyncParsingTask = new AsyncParsingTask(taskID, SyncManager.this, ctx);
        asyncParsingTask.execute(response);

    }


    public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        asyncHttpClient.setTimeout(30000);
        asyncHttpClient.get(url, params, responseHandler);
    }

    public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        asyncHttpClient.setTimeout(30000);
        asyncHttpClient.post(url, params, responseHandler);
    }

    /**
     * example : how to request and create params in your Activity or SyncManager
     * This function is used to register with server.
     */
    public void doRegistration(String username, String email, String password) {
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", password);
        params.put("username", username);
        get(URLConstants.URL_REGISTRATION, params, new DownloadHandler(REGISTRATION, SyncManager.this));
    }

    public void getPhoneDetaills() {

        get(URLConstants.URL_GET_PHONE_DETAILS, null, new DownloadHandler(GET_PHONE_DETAILS, SyncManager.this));
    }
}
