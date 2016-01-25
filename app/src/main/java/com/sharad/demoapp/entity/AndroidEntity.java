package com.sharad.demoapp.entity;

/**
 * Created by SharadW on 22-01-2016.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AndroidEntity {

    @SerializedName("id")
    @Expose
    private Integer id;
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
     * The version
     */
    public String getVersion() {
        return version;
    }

    /**
     *
     * @param version
     * The version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     *
     * @return
     * The codename
     */
    public String getCodename() {
        return codename;
    }

    /**
     *
     * @param codename
     * The codename
     */
    public void setCodename(String codename) {
        this.codename = codename;
    }

    /**
     *
     * @return
     * The target
     */
    public String getTarget() {
        return target;
    }

    /**
     *
     * @param target
     * The target
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     *
     * @return
     * The distribution
     */
    public String getDistribution() {
        return distribution;
    }

    /**
     *
     * @param distribution
     * The distribution
     */
    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

}
