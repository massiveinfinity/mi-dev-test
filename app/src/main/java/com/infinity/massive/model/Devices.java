package com.infinity.massive.model;

/**
 * Created by Ilanthirayan on 25/1/16.
 */
public class Devices {

    private String id;
    private String imageUrl;
    private String name;
    private String androidId;
    private String snippet;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", imageUrl = " + imageUrl + ", name = " + name + ", androidId = " + androidId + ", snippet = " + snippet + "]";
    }
}
