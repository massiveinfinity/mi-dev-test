package com.mi.afzaal.models;



/**
 * Created by F ABBASI on 1/21/2016.
 */
public class DeviceData
{
    private String id;

    private String codename;

    private String name;

    private String target;

    private String distribution;

    private String version;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getCodename ()
    {
        return codename;
    }

    public void setCodename (String codename)
    {
        this.codename = codename;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getTarget ()
    {
        return target;
    }

    public void setTarget (String target)
    {
        this.target = target;
    }

    public String getDistribution ()
    {
        return distribution;
    }

    public void setDistribution (String distribution)
    {
        this.distribution = distribution;
    }

    public String getVersion ()
    {
        return version;
    }

    public void setVersion (String version)
    {
        this.version = version;
    }

    @Override
    public String toString()
    {
        return "id = "+id+", codename = "+codename + ", \n name = "+name+", target = "+target+",\n distribution = "+distribution+", version = "+version+"";
    }
}
