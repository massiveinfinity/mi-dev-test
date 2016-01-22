package com.mi.afzaal.showcaseapplication;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.mi.afzaal.models.DeviceData;


/**
 * Created by F ABBASI on 1/21/2016.
 */
public interface iDeviceListPresenter {
    ArrayList<DeviceData> getData();
    void saveDataToDB(Context c, ArrayList<DeviceData> data);
    void onDestroy();

}
