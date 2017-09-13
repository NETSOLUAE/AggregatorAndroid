package com.netsol.rms.aggregator.activity.other;

/**
 * Created by macmini on 8/8/17.
 */

public class VehicleType {
    private String usageId;
    private String usageType;
    private String vehicleId;
    private String vehicleType;
    private String gvwRequired;

    public VehicleType() {

    }

    public VehicleType(String usageId, String usageType, String vehicleId, String vehicleType, String gvwRequired){
        this.usageId = usageId;
        this.usageType = usageType;
        this.vehicleId = vehicleId;
        this.vehicleType = vehicleType;
        this.gvwRequired = gvwRequired;
    }

    public void setID (String usageId) {
        this.usageId = usageId;
    }

    public String getID () {
        return usageId;
    }

    public void setUsageType (String usageType) {
        this.usageType = usageType;
    }
    public String getUsageType () {
        return usageType;
    }

    public void setVehicleId (String vehicleId) {
        this.vehicleId = vehicleId;
    }
    public String getVehicleId () {
        return vehicleId;
    }

    public void setVehicleType (String vehicleType) {
        this.vehicleType = vehicleType;
    }
    public String getVehicleType () {
        return vehicleType;
    }

    public void setGvwRequired (String gvwRequired) {
        this.gvwRequired = gvwRequired;
    }
    public String getGvwRequired () {
        return gvwRequired;
    }

}
