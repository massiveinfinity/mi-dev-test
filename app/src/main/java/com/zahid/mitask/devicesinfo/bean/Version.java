package com.zahid.mitask.devicesinfo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by zahid.r on 1/25/2016.
 */
public class Version implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("codename")
    @Expose
    private String codename;
    @SerializedName("target")
    @Expose
    private String target;
    @SerializedName("distribution")
    @Expose
    private String distribution;


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
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


    public String getCodename() {
        return codename;
    }


    public void setCodename(String codename) {
        this.codename = codename;
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
