package com.rmsllcoman.agg.Model;

import java.util.ArrayList;

/**
 * Created by macmini on 7/22/17.
 */
public class Company {

    private int id;
    private boolean selected;
    private String price;
    private String coverPrice;
    private String companyName;
    private String companyProductName;
    private String companyProductId;
    private String companyProductType;
    private String companyAttribute;
    private String companyFees;
    private String companyTax;
    private String companyCoverPremium;
    private String companySelectedCoverPremium;
    private String premiumAmount;
    private int companyLogo;
    private int companyViewLogo;
    private ArrayList<CompanyCover> companyCoverList;

    public Company(int id, boolean selected, String price, String coverPrice, String companyName, String companyProductName, String companyProductId, String companyProductType,
                   String companyAttribute, String companyFees, String companyTax, String companyCoverPremium, String companySelectedCoverPremium, String premiumAmount, int companyLogo,
                   int companyViewLogo, ArrayList<CompanyCover> companyCoverList) {
        this.id = id;
        this.selected= selected;
        this.price = price;
        this.coverPrice = coverPrice;
        this.companyLogo = companyLogo;
        this.companyViewLogo = companyViewLogo;
        this.companyName = companyName;
        this.companyProductName = companyProductName;
        this.companyProductId = companyProductId;
        this.companyProductType = companyProductType;
        this.companyAttribute = companyAttribute;
        this.companyFees = companyFees;
        this.companyTax = companyTax;
        this.companyCoverPremium = companyCoverPremium;
        this.companySelectedCoverPremium = companySelectedCoverPremium;
        this.premiumAmount = premiumAmount;
        this.companyCoverList = companyCoverList;
    }

    public int getId() {
        return id;
    }

    public void setSelected(boolean selected) {
        this.selected= selected;
    }

    public boolean getSelected() {
        return selected;
    }

    public String getPrice() {
        return price;
    }

    public void setCoverPrice(String coverPrice) {
        this.coverPrice = coverPrice;
    }

    public String getCoverPrice() {
        return coverPrice;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyProductName() {
        return companyProductName;
    }

    public String getCompanyProductId() {
        return companyProductId;
    }

    public String getCompanyProductType() {
        return companyProductType;
    }

    public String getCompanyAttribute() {
        return companyAttribute;
    }

    public String getCompanyFees() {
        return companyFees;
    }

    public String getCompanyTax() {
        return companyTax;
    }

    public String getCompanyCoverPremium() {
        return companyCoverPremium;
    }

    public String getCompanySelectedCoverPremium() {
        return companySelectedCoverPremium;
    }

    public void setCompanySelectedCoverPremium(String companySelectedCoverPremium) {
        this.companySelectedCoverPremium = companySelectedCoverPremium;
    }

    public String getPremiumAmount() {
        return premiumAmount;
    }

    public int getCompanyLogo() {
        return companyLogo;
    }

    public int getCompanyViewLogo() {
        return companyViewLogo;
    }

    public ArrayList<CompanyCover> getCompanyCoverList() {
        return companyCoverList;
    }

}
