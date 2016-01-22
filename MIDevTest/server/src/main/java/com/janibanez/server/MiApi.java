package com.janibanez.server;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.janibanez.server.http.response.DbResponse;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jwgibanez on 21/01/2016.
 */
public class MiApi {

    private static final String API_URL = "http://mobilesandboxdev.azurewebsites.net";

    private static final String DB = "/db";
    private static final String DEVICE_RESOURCE = "/devices/%s";
    private static final String VERSION_RESOURCE = "/android/%s";

    public enum Action {
        GetDb,
        DeleteDevice,
        DeleteVersion
    }

    Context mContext;
    Gson mGson;
    Handler mHandler;
    OkHttpClient mClient;

    public MiApi(Context context) {
        mContext = context;
        mClient = new OkHttpClient();
        mGson = new Gson();
        mHandler = new Handler();
    }

    public void call(Action action, int id, ICallback callback) {
        switch (action) {
            case GetDb:
                getDb(callback);
                break;
            case DeleteDevice:
                deleteDevice(id, callback);
                break;
            case DeleteVersion:
                deleteVersion(id, callback);
                break;
        }
    }

    private void getDb(final ICallback<DbResponse> callback) {

        Request request = new Request.Builder()
                .url(TextUtils.concat(API_URL, DB).toString())
                .build();

        Call call = mClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onFailure(e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (callback != null) {
                    if (response.isSuccessful()) {
                        DbResponse dbResponse = mGson.fromJson(response.body().string(), DbResponse.class);
                        callback.onResponse(dbResponse);
                    } else {
                        callback.onFailure(new Exception("Response is unsuccessful."));
                    }
                }
            }
        });
    }

    private void deleteDevice(int id, final ICallback callback) {

        Request request = new Request.Builder()
                .url(TextUtils.concat(API_URL, String.format(DEVICE_RESOURCE, id)).toString())
                .delete()
                .build();

        Call call = mClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onFailure(e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (callback != null) {
                    if (response.isSuccessful()) {
                        callback.onResponse(null);
                    } else {
                        callback.onFailure(new Exception("Response is unsuccessful."));
                    }
                }
            }
        });
    }

    private void deleteVersion(int id, final ICallback callback) {

        Request request = new Request.Builder()
                .url(TextUtils.concat(API_URL, String.format(VERSION_RESOURCE, id)).toString())
                .delete()
                .build();

        Call call = mClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onFailure(e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (callback != null) {
                    if (response.isSuccessful()) {
                        callback.onResponse(null);
                    } else {
                        callback.onFailure(new Exception("Response is unsuccessful."));
                    }
                }
            }
        });
    }

}
