package com.mi.androidarsenal.model;


import java.util.ArrayList;

/**
 * @author Samir Sarosh
 */
@SuppressWarnings("ALL")
public class AndroidInfo {
    private ArrayList<Devices> devicePropList;

    private ArrayList<Versions> versionPropList;

    public ArrayList<Devices> getDevicePropList() {
        return devicePropList;
    }

    public void setDevicePropList(ArrayList<Devices> devicePropList) {
        this.devicePropList = devicePropList;
    }

    public ArrayList<Versions> getVersionPropList() {
        return versionPropList;
    }

    public void setVersionPropList(ArrayList<Versions> versionPropList) {
        this.versionPropList = versionPropList;
    }
}
