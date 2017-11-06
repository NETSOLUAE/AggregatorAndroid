package com.rmsllcoman.agg.Model;

/**
 * Created by macmini on 8/9/17.
 */

public class CompanyDetails {
    private String productID;
    private String insurerId;
    private String lob;
    private String productType;
    private String productName;
    private String effectiveDate;
    private String endDate;
    private String insurerName;
    private String tax;
    private String idv;
    private String scheme;
    private String totalPremium;
    private String premiumAmount;
    private String attribute;
    private String fees;
    private String coverPremium;

    public CompanyDetails() {

    }

    public CompanyDetails(String productID, String insurerId, String lob, String productType, String productName, String effectiveDate, String endDate,
                          String insurerName, String tax, String idv, String scheme, String totalPremium, String premiumAmount, String attribute, String fees, String coverPremium){
        this.productID = productID;
        this.insurerId = insurerId;
        this.lob = lob;
        this.productType = productType;
        this.productName = productName;
        this.effectiveDate = effectiveDate;
        this.endDate = endDate;
        this.insurerName = insurerName;
        this.tax = tax;
        this.idv = idv;
        this.scheme = scheme;
        this.totalPremium = totalPremium;
        this.premiumAmount = premiumAmount;
        this.attribute = attribute;
        this.fees = fees;
        this.coverPremium = coverPremium;
    }

    public void setProductID (String productID) {
        this.productID = productID;
    }

    public String getProductID () {
        return productID;
    }

    public void setInsurerId (String insurerId) {
        this.insurerId = insurerId;
    }
    public String getInsurerId () {
        return insurerId;
    }

    public void setLob (String lob) {
        this.lob = lob;
    }
    public String getLob () {
        return lob;
    }

    public void setProductType (String productType) {
        this.productType = productType;
    }
    public String getProductType () {
        return productType;
    }

    public void setProductName (String productName) {
        this.productName = productName;
    }

    public String getProductName () {
        return productName;
    }

    public void setEffectiveDate (String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
    public String getEffectiveDate () {
        return effectiveDate;
    }

    public void setEndDate (String endDate) {
        this.endDate = endDate;
    }
    public String getEndDate () {
        return endDate;
    }

    public void setInsurerName (String insurerName) {
        this.insurerName = insurerName;
    }
    public String getInsurerName () {
        return insurerName;
    }

    public void setTax (String tax) {
        this.tax = tax;
    }
    public String getTax () {
        return tax;
    }

    public void setIdv (String idv) {
        this.idv = idv;
    }
    public String getIdv () {
        return idv;
    }

    public void setScheme (String scheme) {
        this.scheme = scheme;
    }
    public String getScheme () {
        return scheme;
    }

    public void setTotalPremium (String totalPremium) {
        this.totalPremium = totalPremium;
    }
    public String getTotalPremium () {
        return totalPremium;
    }

    public void setPremiumAmount (String premiumAmount) {
        this.premiumAmount = premiumAmount;
    }
    public String getPremiumAmount () {
        return premiumAmount;
    }

    public void setAttribute (String attribute) {
        this.attribute = attribute;
    }
    public String getAttribute () {
        return attribute;
    }

    public void setFees (String fees) {
        this.fees = fees;
    }
    public String getFees () {
        return fees;
    }

    public void setCoverPremium (String coverPremium) {
        this.coverPremium = coverPremium;
    }
    public String getCoverPremium () {
        return coverPremium;
    }
}
