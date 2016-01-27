package com.zahid.mitask.devicesinfo.interfaces;

import org.json.JSONArray;

/**
 * Created by zahid.r on 1/25/2016.
 */
public interface OnWebServiceListener {
    public void onWebServiceResponse(String response, int tag);

    public void onWebServiceError(String error, int tag);
}
