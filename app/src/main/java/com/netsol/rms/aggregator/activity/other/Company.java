package com.netsol.rms.aggregator.activity.other;

import java.util.ArrayList;

/**
 * Created by macmini on 7/22/17.
 */
public class Company {

    private int id;
    private boolean selected;
    private String price;
    private String companyName;
    private String companyProductName;
    private String companyProductId;
    private String companyAttribute;
    private String companyFees;
    private String companyCoverPremium;
    private int companyLogo;
    private int companyViewLogo;
    private ArrayList<CompanyCover> companyCoverList;

    public Company(int id, boolean selected, String price, String companyName, String companyProductName, String companyProductId, String companyAttribute, String companyFees, String companyCoverPremium,
                   int companyLogo, int companyViewLogo, ArrayList<CompanyCover> companyCoverList) {
        this.id = id;
        this.selected= selected;
        this.price = price;
        this.companyLogo = companyLogo;
        this.companyViewLogo = companyViewLogo;
        this.companyName = companyName;
        this.companyProductName = companyProductName;
        this.companyProductId = companyProductId;
        this.companyAttribute = companyAttribute;
        this.companyFees = companyFees;
        this.companyCoverPremium = companyCoverPremium;
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

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyProductName() {
        return companyProductName;
    }

    public String getCompanyProductId() {
        return companyProductId;
    }

    public String getCompanyAttribute() {
        return companyAttribute;
    }

    public String getCompanyFees() {
        return companyFees;
    }

    public String getCompanyCoverPremium() {
        return companyCoverPremium;
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
