package com.janibanez.server.models;

import java.io.Serializable;

/**
 * Created by jwgibanez on 21/01/2016.
 */
public class Version extends ServerModel implements Serializable {

    public int id;
    public String codename, distribution, name, target, version;

    public Version(String name, String version, String codename, String target, String distribution) {
        this.name = name;
        this.version = version;
        this.codename = codename;
        this.target = target;
        this.distribution = distribution;
    }

}
