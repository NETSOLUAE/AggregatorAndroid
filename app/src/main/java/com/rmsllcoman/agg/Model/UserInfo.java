package com.rmsllcoman.agg.Model;

/**
 * Created by macmini on 8/10/17.
 */

public class UserInfo {
    private String id;
    private String full_name;
    private String age;
    private String mobile_no;
    private String national_id;
    private String email;
    private String employer;
    private String status;
    private String company_id;
    private String vehicle_usage;
    private String usage_type;
    private String make;
    private String modal;
    private String variant;
    private String rto;
    private String yom;
    private String price;
    private String registration_date;

    public UserInfo() {

    }

    public UserInfo(String id, String full_name, String age, String mobile_no, String national_id, String email, String employer, String status, String company_id,
                    String vehicle_usage, String usage_type, String make, String modal, String variant, String rto, String yom, String price, String registration_date){
        this.id = id;
        this.full_name = full_name;
        this.age = age;
        this.mobile_no = mobile_no;
        this.national_id = national_id;
        this.email = email;
        this.employer = employer;
        this.status = status;
        this.company_id = company_id;
        this.vehicle_usage = vehicle_usage;
        this.usage_type = usage_type;
        this.make = make;
        this.modal = modal;
        this.variant = variant;
        this.rto = rto;
        this.yom = yom;
        this.price = price;
        this.registration_date = registration_date;
    }

    public void setId (String id) {
        this.id = id;
    }
    public String getId () {
        return id;
    }

    public void setFullName (String full_name) {
        this.full_name = full_name;
    }
    public String getFullName () {
        return full_name;
    }

    public void setAge (String age) {
        this.age = age;
    }
    public String getAge () {
        return age;
    }

    public void setMobileNo (String mobile_no) {
        this.mobile_no = mobile_no;
    }
    public String getMobileNo () {
        return mobile_no;
    }

    public void setNationalId (String national_id) {
        this.national_id = national_id;
    }
    public String getNationalId () {
        return national_id;
    }

    public void setEmail (String email) {
        this.email = email;
    }
    public String getEmail () {
        return email;
    }

    public void setEmployer (String employer) {
        this.employer = employer;
    }
    public String getEmployer () {
        return employer;
    }

    public void setStatus (String status) {
        this.status = status;
    }
    public String getStatus () {
        return status;
    }

    public void setCompanyId (String company_id) {
        this.company_id = company_id;
    }
    public String getCompanyId () {
        return company_id;
    }

    public void setVehicleUsage (String vehicle_usage) {
        this.vehicle_usage = vehicle_usage;
    }
    public String getVehicleUsage () {
        return vehicle_usage;
    }

    public void setUsageType (String usage_type) {
        this.usage_type = usage_type;
    }
    public String getUsageType () {
        return usage_type;
    }

    public void setMake (String make) {
        this.make = make;
    }
    public String getMake () {
        return make;
    }

    public void setModal (String modal) {
        this.modal = modal;
    }
    public String getModal () {
        return modal;
    }

    public void setVariant (String variant) {
        this.variant = variant;
    }
    public String getVariant () {
        return variant;
    }

    public void setRto (String rto) {
        this.rto = rto;
    }
    public String getRto () {
        return rto;
    }

    public void setYom (String yom) {
        this.yom = yom;
    }
    public String getYom () {
        return yom;
    }

    public void setPrice (String price) {
        this.price = price;
    }
    public String getPrice () {
        return price;
    }

    public void setRegistrationDate (String registration_date) {
        this.registration_date = registration_date;
    }
    public String getRegistrationDate () {
        return registration_date;
    }
}
