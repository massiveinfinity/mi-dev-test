package com.dharashah.showcaseandroidapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user on 23/01/2016.
 */
public class AllData {
    @SerializedName("android")
    private List<AndroidHistory> androidHistoryList;
    @SerializedName("devices")
    private List<Devices> deviceList;

    public List<AndroidHistory> getAndroidHistoryList() {
        return androidHistoryList;
    }

    public void setAndroidHistoryList(List<AndroidHistory> androidHistoryList) {
        this.androidHistoryList = androidHistoryList;
    }

    public List<Devices> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<Devices> deviceList) {
        this.deviceList = deviceList;
    }
}
