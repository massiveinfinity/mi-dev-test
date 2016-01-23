package com.dharashah.showcaseandroidapp.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Dhara shah on 22/12/2015.</br>
 * SingleTon class that initializes the queue to
 * load the images from the network</br>
 */
public class ApiCalls {
    private static ApiCalls mApiCall;
    private static Context mContext;

    /**
     * Instantiates ApiCalls instance
     * @param context
     * @return
     */
    public static ApiCalls getInstance(Context context) {
        mContext = context;
        if(mApiCall == null) {
            mApiCall = new ApiCalls();
        }
        return mApiCall;
    }

    private RequestQueue queue;

    /**
     * Instantiates the queue
     * @return
     */
    public RequestQueue getRequestQueue() {
        if(queue == null) {
            queue = Volley.newRequestQueue(mContext.getApplicationContext());
            queue.start();
        }
        return queue;
    }
}
