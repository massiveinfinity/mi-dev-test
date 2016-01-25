package com.infinity.massive.model;

/**
 * Created by Ilanthirayan on 25/1/16.
 */
public class ParentResponse {

    private Android[] android;
    private Devices[] devices;

    public Android[] getAndroid() {
        return android;
    }

    public void setAndroid(Android[] android) {
        this.android = android;
    }

    public Devices[] getDevices() {
        return devices;
    }

    public void setDevices(Devices[] devices) {
        this.devices = devices;
    }

    @Override
    public String toString() {
        return "ClassPojo [android = " + android + ", devices = " + devices + "]";
    }
}
