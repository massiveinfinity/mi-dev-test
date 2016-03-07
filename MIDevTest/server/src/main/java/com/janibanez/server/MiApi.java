package com.janibanez.server;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.janibanez.server.models.Db;
import com.janibanez.server.models.Device;
import com.janibanez.server.models.ServerModel;
import com.janibanez.server.models.Version;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jwgibanez on 21/01/2016.
 */
public class MiApi {

    private static final String API_URL = "http://mobilesandboxdev.azurewebsites.net";

    private static final String DB = "/db";
    private static final String DEVICES = "/devices";
    private static final String VERSIONS = "/android";
    private static final String DEVICE_RESOURCE = "/devices/%s";
    private static final String VERSION_RESOURCE = "/android/%s";

    public enum Action {
        CreateDevice,
        CreateVersion,
        GetDb,
        DeleteDevice,
        DeleteVersion,
        UpdateDevice,
        UpdateVersion
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

    public void call(Action action, int id, ServerModel model, ICallback callback) {
        switch (action) {
            case CreateDevice:
                createDevice((Device) model, callback);
                break;
            case CreateVersion:
                createVersion((Version) model, callback);
                break;
            case GetDb:
                getDb(callback);
                break;
            case DeleteDevice:
                deleteDevice(id, callback);
                break;
            case DeleteVersion:
                deleteVersion(id, callback);
                break;
            case UpdateDevice:
                updateDevice(id, (Device) model, callback);
                break;
            case UpdateVersion:
                updateVersion(id, (Version) model, callback);
                break;
        }
    }

    private void createDevice(Device device, final ICallback<Device> callback) {

        RequestBody requestBody = new FormBody.Builder()
                .add("name", device.name)
                .add("androidId", String.valueOf(device.androidId))
                .add("carrier", device.carrier)
                .add("imageUrl", device.imageUrl)
                .add("snippet", device.snippet)
                .build();

        Request request = new Request.Builder()
                .url(TextUtils.concat(API_URL, DEVICES).toString())
                .post(requestBody)
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
                        Device result = mGson.fromJson(response.body().string(), Device.class);
                        callback.onResponse(result);
                    } else {
                        callback.onFailure(new Exception("Response is unsuccessful."));
                    }
                }
            }
        });
    }

    private void createVersion(Version version, final ICallback<Version> callback) {

        RequestBody requestBody = new FormBody.Builder()
                .add("name", version.name)
                .add("codename", version.codename)
                .add("version", version.version)
                .add("target", version.target)
                .add("distribution", version.distribution)
                .build();

        Request request = new Request.Builder()
                .url(TextUtils.concat(API_URL, VERSIONS).toString())
                .post(requestBody)
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
                        Version result = mGson.fromJson(response.body().string(), Version.class);
                        callback.onResponse(result);
                    } else {
                        callback.onFailure(new Exception("Response is unsuccessful."));
                    }
                }
            }
        });
    }

    private void getDb(final ICallback<Db> callback) {

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
                        String body = response.body().string();
                        Db db = mGson.fromJson(body, Db.class);
                        callback.onResponse(db);
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

    private void updateDevice(int id, Device device,  final ICallback<Device> callback) {

        RequestBody requestBody = new FormBody.Builder()
                .add("name", device.name)
                .add("androidId", String.valueOf(device.androidId))
                .add("carrier", device.carrier)
                .add("imageUrl", device.imageUrl)
                .add("snippet", device.snippet)
                .build();

        Request request = new Request.Builder()
                .url(TextUtils.concat(API_URL, String.format(DEVICE_RESOURCE, id)).toString())
                .put(requestBody)
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
                        Device result = mGson.fromJson(response.body().string(), Device.class);
                        callback.onResponse(result);
                    } else {
                        callback.onFailure(new Exception("Response is unsuccessful."));
                    }
                }
            }
        });
    }

    private void updateVersion(int id, Version version,  final ICallback<Version> callback) {

        RequestBody requestBody = new FormBody.Builder()
                .add("name", version.name)
                .add("codename", version.codename)
                .add("version", version.version)
                .add("target", version.target)
                .add("distribution", version.distribution)
                .build();

        Request request = new Request.Builder()
                .url(TextUtils.concat(API_URL, String.format(VERSION_RESOURCE, id)).toString())
                .put(requestBody)
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
                        Version result = mGson.fromJson(response.body().string(), Version.class);
                        callback.onResponse(result);
                    } else {
                        callback.onFailure(new Exception("Response is unsuccessful."));
                    }
                }
            }
        });
    }

}
