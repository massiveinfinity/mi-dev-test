package com.sharad.demoapp.entity;

/**
 * Created by SharadW on 22-01-2016.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceEntity {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("androidId")
    @Expose
    private Integer androidId;
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

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The androidId
     */
    public Integer getAndroidId() {
        return androidId;
    }

    /**
     *
     * @param androidId
     * The androidId
     */
    public void setAndroidId(Integer androidId) {
        this.androidId = androidId;
    }

    /**
     *
     * @return
     * The imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     *
     * @param imageUrl
     * The imageUrl
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The snippet
     */
    public String getSnippet() {
        return snippet;
    }

    /**
     *
     * @param snippet
     * The snippet
     */
    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    /**
     *
     * @return
     * The carrier
     */
    public String getCarrier() {
        return carrier;
    }

    /**
     *
     * @param carrier
     * The carrier
     */
    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

}
