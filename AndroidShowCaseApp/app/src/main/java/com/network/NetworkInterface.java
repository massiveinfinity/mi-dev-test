package com.network;

import org.json.JSONObject;

/**
 * Created by user on 14/10/15.
 */
public interface NetworkInterface {
    void onResponse(String response, int requestCode);
}
