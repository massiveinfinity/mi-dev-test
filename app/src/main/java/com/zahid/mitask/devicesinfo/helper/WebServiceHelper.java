package com.zahid.mitask.devicesinfo.helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zahid.mitask.devicesinfo.R;
import com.zahid.mitask.devicesinfo.app.AppController;
import com.zahid.mitask.devicesinfo.interfaces.OnWebServiceListener;
import com.zahid.mitask.devicesinfo.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by zahid.r on 1/25/2016.
 */
public class WebServiceHelper {
    private static final String TAG = WebServiceHelper.class.getSimpleName();
    private Context mContext;
    private OnWebServiceListener webServiceListener;

    public WebServiceHelper(Context mContext) {
        this.mContext = mContext;
    }

    public void setOnWebServiceListener(OnWebServiceListener webServiceListener) {
        this.webServiceListener = webServiceListener;
    }

    public void makeJSONArrayRequest(String url, int requestMethod, String params, final int tag) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(requestMethod, url, params,
                new com.android.volley.Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        webServiceListener.onWebServiceResponse(response.toString(), tag);
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null) {
                    webServiceListener.onWebServiceError(error.getMessage(), tag);
                } else {
                    webServiceListener.onWebServiceError(mContext.getResources().getString(R.string.error_occured), tag);
                }
            }
        });

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    public void makeJsonObjectRequest(String url, int requestMethod, String params, final int tag) {
        Log.i("URL", url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(requestMethod, url, params,
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        webServiceListener.onWebServiceResponse(response.toString(), tag);
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null) {
                    webServiceListener.onWebServiceError(error.getMessage(), tag);
                } else {
                    webServiceListener.onWebServiceError(mContext.getResources().getString(R.string.error_occured), tag);
                }
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }
}
