package com.network;

import android.content.Context;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by user on 14/10/15.
 */
public final class NetworkClassHandler {
    public void OnGetRequest(final NetworkInterface mInterface, final int requestCode, String url){
        Request profileRequest = new Request.Builder().url(url).get().build();
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.newCall(profileRequest).enqueue(new Callback() {
            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    // handle error
                } else {
                    mInterface.onResponse(response.body().string(), requestCode);
                }
            }
            @Override
            public void onFailure(Request request, IOException e) {
                // handle error
            }
        });
    }
    public void OnPostRequestWithParams(final NetworkInterface mInterface, final int requestCode, HashMap<String, Object> params, String url){
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonBody = new JSONObject();
        try {
            for(Map.Entry<String,Object> entry : params.entrySet()){
                if(entry.getValue() instanceof Integer){
                    jsonBody.put(entry.getKey(), Integer.parseInt(entry.getValue().toString()));
                } else if(entry.getValue() instanceof String) {
                    jsonBody.put(entry.getKey(), entry.getValue());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, jsonBody.toString());
        Request profileRequest = new Request.Builder().url(url).post(body).build();
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.newCall(profileRequest).enqueue(new Callback() {
            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    // handle error
                } else {
                    mInterface.onResponse(response.body().string(), requestCode);
                }
            }
            @Override
            public void onFailure(Request request, IOException e) {
                // handle error
            }
        });
    }
}
