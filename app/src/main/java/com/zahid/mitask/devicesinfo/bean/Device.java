package com.zahid.mitask.devicesinfo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by zahid.r on 1/25/2016.
 */
public class Device implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("androidId")
    @Expose
    private int androidId;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("snippet")
    @Expose
    private String snippet;
    @SerializedName("carrier")
    @Expose
    private String carrier;


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public int getAndroidId() {
        return androidId;
    }


    public void setAndroidId(int androidId) {
        this.androidId = androidId;
    }


    public String getImageUrl() {
        return imageUrl;
    }


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getSnippet() {
        return snippet;
    }


    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }


    public String getCarrier() {
        return carrier;
    }


    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

}

