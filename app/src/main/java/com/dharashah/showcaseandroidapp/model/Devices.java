package com.dharashah.showcaseandroidapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by USER on 21-01-2016.
 */
public class Devices {
    @SerializedName("id")
    private int deviceId;
    @SerializedName("androidId")
    private int androidId;
    @SerializedName("imageUrl")
    private String imageURL;
    @SerializedName("name")
    private String deviceName;
    @SerializedName("snippet")
    private String deviceDesc;

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getAndroidId() {
        return androidId;
    }

    public void setAndroidId(int androidId) {
        this.androidId = androidId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceDesc() {
        return deviceDesc;
    }

    public void setDeviceDesc(String deviceDesc) {
        this.deviceDesc = deviceDesc;
    }
}
