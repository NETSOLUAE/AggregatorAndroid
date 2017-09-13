package com.netsol.rms.aggregator.activity.other;

/**
 * Created by macmini on 8/8/17.
 */

public class VehicleVarient {
    private String makeId;
    private String modelId;
    private String variantId;
    private String makeName;
    private String modelName;
    private String variantName;
    private String vehicleAttr;
    private String startYear;
    private String endYear;
    private String sc;
    private String vehicleType;
    private String numCyl;
    private String price;

    public VehicleVarient() {

    }

    public VehicleVarient(String makeId, String makeName, String modelId, String modelName, String variantId, String variantName, String vehicleAttr,
                          String startYear, String endYear, String sc, String vehicleType, String numCyl, String price){
        this.makeId = makeId;
        this.makeName = makeName;
        this.modelId = modelId;
        this.modelName = modelName;
        this.variantId = variantId;
        this.variantName = variantName;
        this.vehicleAttr = vehicleAttr;
        this.startYear = startYear;
        this.endYear = endYear;
        this.sc = sc;
        this.vehicleType = vehicleType;
        this.numCyl = numCyl;
        this.price = price;
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

    public void setModelId (String modelId) {
        this.modelId = modelId;
    }
    public String getModelId () {
        return modelId;
    }

    public void setModelName (String modelName) {
        this.modelName = modelName;
    }
    public String getModelName () {
        return modelName;
    }

    public void setVariantId (String variantId) {
        this.variantId = variantId;
    }

    public String getVariantId () {
        return variantId;
    }

    public void setVariantName (String variantName) {
        this.variantName = variantName;
    }
    public String getVariantName () {
        return variantName;
    }

    public void setVehicleAttr (String vehicleAttr) {
        this.vehicleAttr = vehicleAttr;
    }
    public String getVehicleAttr () {
        return vehicleAttr;
    }

    public void setStartYear (String startYear) {
        this.startYear = startYear;
    }
    public String getStartYear () {
        return startYear;
    }

    public void setEndYear (String endYear) {
        this.endYear = endYear;
    }
    public String getEndYear () {
        return endYear;
    }

    public void setSc (String sc) {
        this.sc = sc;
    }
    public String getSc () {
        return sc;
    }

    public void setVehicleType (String vehicleType) {
        this.vehicleType = vehicleType;
    }
    public String getVehicleType () {
        return vehicleType;
    }

    public void setNumCyl (String numCyl) {
        this.numCyl = numCyl;
    }
    public String getNumCyl () {
        return numCyl;
    }

    public void setPrice (String price) {
        this.price = price;
    }
    public String getPrice () {
        return price;
    }
}
