package com.infinity.massive.model.pojo;

/**
 * Created by Ilanthirayan on 25/1/16.
 */
public class Android {

    private String id;
    private String codename;
    private String name;
    private String target;
    private String distribution;
    private String version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", codename = " + codename + ", name = " + name + ", target = " + target + ", distribution = " + distribution + ", version = " + version + "]";
    }
}
