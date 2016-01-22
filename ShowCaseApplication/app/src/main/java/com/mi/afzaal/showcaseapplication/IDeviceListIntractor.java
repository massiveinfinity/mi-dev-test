package com.mi.afzaal.showcaseapplication;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.mi.afzaal.models.DeviceData;


/**
 * Created by F ABBASI on 1/21/2016.
 */
public interface IDeviceListIntractor {
    ArrayList<DeviceData> getDeviceList(String url, OnDataReceivedListener listener);
    void saveToDb(Context c, ArrayList<DeviceData> devicelist);
}
