package com.janibanez.server.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jwgibanez on 21/01/2016.
 */
public class Db extends ServerModel implements Serializable {

    public List<Version> android;
    public List<Device> devices;

}
