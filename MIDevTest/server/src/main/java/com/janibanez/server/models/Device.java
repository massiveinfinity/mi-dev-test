package com.janibanez.server.models;

import java.io.Serializable;

/**
 * Created by jwgibanez on 21/01/2016.
 */
public class Device extends ServerModel implements Serializable {

    public int androidId, id;
    public String carrier, imageUrl, name, snippet;

    public Device() {

    }

    public Device(String name, int androidId, String snippet, String carrier, String imageUrl) {
        this.name = name;
        this.androidId = androidId;
        this.snippet = snippet;
        this.carrier = carrier;
        this.imageUrl = imageUrl;
    }

}
