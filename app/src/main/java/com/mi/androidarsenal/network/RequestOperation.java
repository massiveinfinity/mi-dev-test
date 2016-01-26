package com.mi.androidarsenal.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mi.androidarsenal.activity.MainActivity;
import com.mi.androidarsenal.utility.AppConstants;
import com.mi.androidarsenal.utility.AndroidResponseObserver;
import com.mi.androidarsenal.utility.DeleteItemOnClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * This class is responsible for all the network requests. It uses volley with okhttp as its transport for faster networking
 *
 * @author Samir Sarosh
 */
@SuppressWarnings("ALL")
public class RequestOperation implements AppConstants {

    private final Context context;

    private static RequestOperation mRequestOperation = null;
    private AndroidResponseObserver mAndroidResponseObserver;

    /**
     *
     */
    public interface OnRefreshDbListener {
        public void onRefreshDb();
    }

    private static OnRefreshDbListener mOnRefreshDbListener;

    public static void setOnRefreshDbListener(OnRefreshDbListener observer) {
        mOnRefreshDbListener = observer;
    }

    /**
     *
     */
    public interface DeleteItemFromDeviceObserver {
        public void onDeleteItemFromDeviceTable(String id);
    }

    private static DeleteItemFromDeviceObserver mDeleteItemFromDeviceObserver;

    public static void setDeleteItemFromDeviceObserver(DeleteItemFromDeviceObserver observer) {
        mDeleteItemFromDeviceObserver = observer;
    }

    /**
     *
     */
    public interface DeleteItemFromVersionObserver {
        public void onDeleteItemFromVersionTable(String id);
    }

    private static DeleteItemFromVersionObserver mDeleteItemFromVersionObserver;

    public static void setDeleteItemFromVersionObserver(DeleteItemFromVersionObserver observer) {
        mDeleteItemFromVersionObserver = observer;
    }

    /**
     * singleton implementation
     *
     * @param context
     * @return
     */
    public static RequestOperation getRequestOperationInstance(Context context) {
        if (mRequestOperation == null) {
            mRequestOperation = new RequestOperation(context);
        }
        return mRequestOperation;
    }

    private RequestOperation(Context context) {
        this.context = context;
    }

    /**
     * GET request
     *
     * @param mainActivity
     */
    public void getAllData(MainActivity mainActivity) {
        try {
            mAndroidResponseObserver = (AndroidResponseObserver) mainActivity;
        } catch (ClassCastException e) {

        }

        StringBuilder url = new StringBuilder(BASE_URL);
        url.append(ALL_SUB_URL);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url.toString(), getAllDataSuccess, getAllDataError);

        // Add the request to the RequestQueue.
        VolleyHelper
                .getInstance(
                        this.context.getApplicationContext())
                .getRequestQueue().add(stringRequest);
    }

    /**
     * Sends a DELETE request to the server from the device database
     *
     * @param id
     * @param deleteItemOnClickListener
     */
    public void deleteDeviceData(String id, DeleteItemOnClickListener deleteItemOnClickListener) {
        StringBuilder url = new StringBuilder(BASE_URL);
        url.append(DEVICES_SUB_URL);
        url.append("/");
        url.append(id);

        DeleteDeviceItemSuccess deleteDeviceItemSuccess = new DeleteDeviceItemSuccess(id, true);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE,
                url.toString(), deleteDeviceItemSuccess, deleteDeviceItemError);

        // Add the request to the RequestQueue.
        VolleyHelper
                .getInstance(
                        this.context.getApplicationContext())
                .getRequestQueue().add(stringRequest);

    }

    /**
     * Sends a DELETE request to the server from the version database
     *
     * @param id
     * @param deleteItemOnClickListener
     */
    public void deleteVersionData(String id, DeleteItemOnClickListener deleteItemOnClickListener) {
        StringBuilder url = new StringBuilder(BASE_URL);
        url.append(VERSIONS_SUB_URL);
        url.append("/");
        url.append(id);

        DeleteDeviceItemSuccess deleteDeviceItemSuccess = new DeleteDeviceItemSuccess(id, false);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE,
                url.toString(), deleteDeviceItemSuccess, deleteDeviceItemError);

        // Add the request to the RequestQueue.
        VolleyHelper
                .getInstance(
                        this.context.getApplicationContext())
                .getRequestQueue().add(stringRequest);

    }

    /**
     * Configures the POST request to the device database of server
     *
     * @param name
     * @param snippet
     * @param carrier
     * @param android_id
     * @param imageUrl
     */
    public void postDeviceInfo(String name, String snippet, String carrier, String android_id, String imageUrl) {

        StringBuilder url = new StringBuilder(BASE_URL);
        url.append(DEVICES_SUB_URL);

        JSONObject json = new JSONObject();
        try {
            json.put("androidId", android_id);
            json.put("carrier", carrier);
            json.put("imageUrl", imageUrl);
            json.put("name", name);
            json.put("snippet", snippet);
        } catch (JSONException e) {
            // handle exception (not supposed to happen)
        }
        postData(url.toString(), json.toString());
    }

    /**
     * Configures the POST request to the Version database of server
     *
     * @param name
     * @param version
     * @param codename
     * @param target
     * @param distribution
     */
    public void postVersionInfo(String name, String version, String codename, String target, String distribution) {
        StringBuilder url = new StringBuilder(BASE_URL);
        url.append(VERSIONS_SUB_URL);

        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
            json.put("version", version);
            json.put("codename", codename);
            json.put("target", target);
            json.put("distribution", distribution);
        } catch (JSONException e) {
            // handle exception (not supposed to happen)
        }
        postData(url.toString(), json.toString());
    }

    /**
     * Sends a POST request to the server according to the database configured
     *
     * @param url
     * @param jsonString
     */
    private void postData(String url, String jsonString) {
        PostDataSuccess postDataSuccess = new PostDataSuccess();

        // Request a string response from the provided URL.
        PostDataRequest stringRequest = new PostDataRequest(Request.Method.POST,
                url, jsonString, postDataSuccess, postDataError);

        try {
            stringRequest.getHeaders();
        } catch (AuthFailureError e) {
            e.printStackTrace();
        }

        // Add the request to the RequestQueue.
        VolleyHelper
                .getInstance(
                        this.context.getApplicationContext())
                .getRequestQueue().add(stringRequest);
    }

    /**
     * POST request
     */
    public class PostDataRequest extends StringRequest {

        private final String jsonString;

        public PostDataRequest(int method, String url, String jsonString,
                               Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
            this.jsonString = jsonString;
        }

        @Override
        public String getBodyContentType() {
            return "application/json";
        }


        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> header = new HashMap<String, String>();
            header.put("Content-Type", "application/json");
            return header;
        }

        @Override
        public byte[] getBody() throws AuthFailureError {

            try {
                return jsonString.getBytes("utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Configures the PUT request to the device database of server
     *
     * @param id
     * @param name
     * @param snippet
     * @param carrier
     * @param android_id
     * @param imageUrl
     */
    public void putDeviceInfo(String id, String name, String snippet, String carrier, String android_id, String imageUrl) {
        StringBuilder url = new StringBuilder(BASE_URL);
        url.append(DEVICES_SUB_URL);
        url.append("/");
        url.append(id);

        JSONObject json = new JSONObject();
        try {
            json.put("androidId", android_id);
            json.put("carrier", carrier);
            json.put("imageUrl", imageUrl);
            json.put("name", name);
            json.put("snippet", snippet);
        } catch (JSONException e) {
            // handle exception (not supposed to happen)
        }
        putData(url.toString(), json.toString());
    }

    /**
     * Configures the PUT request to the version database of server
     *
     * @param id
     * @param name
     * @param version
     * @param codename
     * @param target
     * @param distribution
     */
    public void putVersionInfo(String id, String name, String version, String codename, String target, String distribution) {
        StringBuilder url = new StringBuilder(BASE_URL);
        url.append(VERSIONS_SUB_URL);
        url.append("/");
        url.append(id);

        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
            json.put("version", version);
            json.put("codename", codename);
            json.put("target", target);
            json.put("distribution", distribution);
        } catch (JSONException e) {
            // handle exception (not supposed to happen)
        }
        putData(url.toString(), json.toString());
    }

    /**
     * Sends PUT request
     *
     * @param url
     * @param jsonString
     */
    private void putData(String url, String jsonString) {
        PostDataSuccess postDataSuccess = new PostDataSuccess();

        // Request a string response from the provided URL.
        PutDataRequest stringRequest = new PutDataRequest(Request.Method.PUT,
                url, jsonString, postDataSuccess, postDataError);


        try {
            stringRequest.getHeaders();
        } catch (AuthFailureError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Add the request to the RequestQueue.
        VolleyHelper
                .getInstance(
                        this.context.getApplicationContext())
                .getRequestQueue().add(stringRequest);
    }

    /**
     * PUT request
     */
    public class PutDataRequest extends StringRequest {

        private final String jsonString;

        public PutDataRequest(int method, String url, String jsonString,
                              Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
            this.jsonString = jsonString;
        }

        @Override
        public String getBodyContentType() {
            return "application/json";
        }


        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> header = new HashMap<String, String>();
            header.put("Content-Type", "application/json");
            return header;
        }

        @Override
        public byte[] getBody() throws AuthFailureError {

            try {
                return jsonString.getBytes("utf-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }
    }


    private Response.Listener<String> getAllDataSuccess = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            Log.v("", "RESPONSE : " + response);
            if (mAndroidResponseObserver != null) {
                mAndroidResponseObserver.onAllDataGetResponse(response);
            }
        }

    };

    private Response.ErrorListener getAllDataError = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    };

    private Response.Listener<String> getVersionsSuccess = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            Log.v("", "RESPONSE : " + response);


        }

    };

    private Response.ErrorListener getVersionsError = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    };


    class PostDataSuccess implements Response.Listener<String> {

        @Override
        public void onResponse(String response) {
            Log.v("", "RESPONSE : " + response);
            if (mOnRefreshDbListener != null)
                mOnRefreshDbListener.onRefreshDb();
        }
    }

    private Response.ErrorListener postDataError = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    };

    /**
     *
     */
    class DeleteDeviceItemSuccess implements Response.Listener<String> {

        private String mId;
        private boolean mIsDevice;

        public DeleteDeviceItemSuccess(String id, boolean isDevice) {
            mId = id;
            mIsDevice = isDevice;
        }

        @Override
        public void onResponse(String response) {
            Log.v("", "RESPONSE : " + response);
            if (mIsDevice) {
                if (mDeleteItemFromDeviceObserver != null)
                    mDeleteItemFromDeviceObserver.onDeleteItemFromDeviceTable(mId);
            } else {
                if (mDeleteItemFromVersionObserver != null)
                    mDeleteItemFromVersionObserver.onDeleteItemFromVersionTable(mId);
            }
        }
    }

    private Response.ErrorListener deleteDeviceItemError = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    };
}
