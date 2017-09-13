package com.netsol.rms.aggregator.activity.other;

/**
 * Created by macmini on 8/8/17.
 */

public class RTO {
    private String id;
    private String rtoName;

    public RTO() {

    }

    public RTO(String id, String rtoName){
        this.id = id;
        this.rtoName = rtoName;
    }

    public void setID (String id) {
        this.id = id;
    }

    public String getID () {
        return id;
    }

    public void setRtoName (String rtoName) {
        this.rtoName = rtoName;
    }
    public String getRtoName () {
        return rtoName;
    }
}
