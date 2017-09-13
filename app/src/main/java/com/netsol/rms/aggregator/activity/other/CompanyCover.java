package com.netsol.rms.aggregator.activity.other;

/**
 * Created by macmini on 8/9/17.
 */

public class CompanyCover {
    private String productID;
    private String insurerObject;
    private String coverId;
    private String coverType;
    private String coverName;

    public CompanyCover() {

    }

    public CompanyCover(String productID, String insurerObject, String coverId, String coverType, String coverName){
        this.productID = productID;
        this.insurerObject = insurerObject;
        this.coverId = coverId;
        this.coverType = coverType;
        this.coverName = coverName;
    }

    public void setProductID (String productID) {
        this.productID = productID;
    }

    public String getProductID () {
        return productID;
    }

    public void setInsurerObject (String insurerObject) {
        this.insurerObject = insurerObject;
    }
    public String getInsurerObject () {
        return insurerObject;
    }

    public void setCoverId (String coverId) {
        this.coverId = coverId;
    }
    public String getCoverId () {
        return coverId;
    }

    public void setCoverType (String coverType) {
        this.coverType = coverType;
    }
    public String getCoverType () {
        return coverType;
    }

    public void setCoverName (String coverName) {
        this.coverName = coverName;
    }

    public String getCoverName () {
        return coverName;
    }
}
