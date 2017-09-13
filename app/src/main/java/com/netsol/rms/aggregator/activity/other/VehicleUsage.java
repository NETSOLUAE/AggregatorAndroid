package com.netsol.rms.aggregator.activity.other;

/**
 * Created by macmini on 8/8/17.
 */

public class VehicleUsage {
    private int id;
    private String vehicleName;

    public VehicleUsage() {

    }

    public VehicleUsage(int id, String vehicleNam){
        this.id = id;
        this.vehicleName = vehicleName;
    }

    public void setID (int id) {
        this.id = id;
    }

    public int getID () {
        return id;
    }

    public void setVehicleName (String vehicleName) {
        this.vehicleName = vehicleName;
    }
    public String getVehicleName () {
        return vehicleName;
    }
}
