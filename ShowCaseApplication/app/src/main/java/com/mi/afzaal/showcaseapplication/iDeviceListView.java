package com.mi.afzaal.showcaseapplication;

import com.mi.afzaal.models.DeviceData;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by F ABBASI on 1/21/2016.
 */
public interface iDeviceListView {

    void showProgress();
    void hideProgress();
    void setData(ArrayList<DeviceData> dataList);
    void setonError();
    void setonSuccess();
    void savetoDB(ArrayList<DeviceData> list);

}
