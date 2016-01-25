package com.sharad.demoapp.entity;

/**
 * Created by SharadW on 22-01-2016.
 */
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhoneDetailEntity {

    @SerializedName("android")
    @Expose
    private List<AndroidEntity> android = new ArrayList<AndroidEntity>();
    @SerializedName("devices")
    @Expose
    private List<DeviceEntity> devices = new ArrayList<DeviceEntity>();

    /**
     *
     * @return
     * The android
     */
    public List<AndroidEntity> getAndroid() {
        return android;
    }

    /**
     *
     * @param android
     * The android
     */
    public void setAndroid(List<AndroidEntity> android) {
        this.android = android;
    }

    /**
     *
     * @return
     * The devices
     */
    public List<DeviceEntity> getDevices() {
        return devices;
    }

    /**
     *
     * @param devices
     * The devices
     */
    public void setDevices(List<DeviceEntity> devices) {
        this.devices = devices;
    }

}