package com.janibanez.server.models;

import java.io.Serializable;

/**
 * Created by jwgibanez on 21/01/2016.
 */
public class Version implements Serializable {

    public int id;
    public String codename, distribution, name, target, version;

}
