package com.netsol.rms.aggregator.activity.other;

/**
 * Created by macmini on 8/8/17.
 */

public class VehicleMake {
    private String makeId;
    private String makeName;

    public VehicleMake() {

    }

    public VehicleMake(String makeId, String makeName){
        this.makeId = makeId;
        this.makeName = makeName;
    }

    public void setMakeId (String makeId) {
        this.makeId = makeId;
    }

    public String getMakeId () {
        return makeId;
    }

    public void setMakeName (String makeName) {
        this.makeName = makeName;
    }
    public String getMakeName () {
        return makeName;
    }
}
