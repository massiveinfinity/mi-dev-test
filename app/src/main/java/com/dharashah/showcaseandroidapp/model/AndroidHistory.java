package com.dharashah.showcaseandroidapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by USER on 21-01-2016.
 */
public class AndroidHistory {
    @SerializedName("id")
    private int androidId;
    @SerializedName("name")
    private String name;
    @SerializedName("version")
    private String version;
    @SerializedName("codename")
    private String codeName;
    @SerializedName("target")
    private String target;
    @SerializedName("distribution")
    private String distribution;

    public int getAndroidId() {
        return androidId;
    }

    public void setAndroidId(int androidId) {
        this.androidId = androidId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }
}
