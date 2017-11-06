package com.rmsllcoman.agg.Controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import com.rmsllcoman.agg.Model.RTO;
import com.rmsllcoman.agg.Model.UserInfo;
import com.rmsllcoman.agg.Model.VehicleMake;
import com.rmsllcoman.agg.Model.VehicleModel;
import com.rmsllcoman.agg.Model.VehicleType;
import com.rmsllcoman.agg.Model.VehicleUsage;
import com.rmsllcoman.agg.Model.VehicleVarient;
import com.rmsllcoman.agg.Model.CompanyCover;
import com.rmsllcoman.agg.Model.CompanyDetails;
import com.rmsllcoman.agg.other.Constants;

/**
 * Created by macmini on 8/8/17.
 */


public class DatabaseManager {
    private static String TAG = Constants.LOG_RMSAGG + DatabaseManager.class.getSimpleName();
    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "RMSAGGDB.db";
    private static final int DATABASE_VERSION = 3;
    private Context context;
    RMSAGGDataHelper openHelper;

    public DatabaseManager (Context context) {
        this.context = context;
//        dbPassword = Encryption.toAscii(context);
    }

    private void openConnection() {
        try {
            if (this.db == null || !this.db.isOpen()) {
                openHelper = new RMSAGGDataHelper(this.context);
                this.db = openHelper.getWritableDatabase();
            }
        } catch (Exception ex) {
            Log.e(TAG, "Exception is " + Log.getStackTraceString(ex));
        }
    }

    private void closeConnection() {
        try {
            if (this.db != null && this.db.isOpen()) {
                db.close();
            }
        } catch (Exception ex) {
            Log.e(TAG, "Exception is " + Log.getStackTraceString(ex));
        }
    }

    public void createDb() {
        openConnection();
        // To Create Tables
        db.execSQL("CREATE Table VEHICLE_USAGE(USAGE_ID TEXT, USAGE_NAME TEXT)");
        db.execSQL("CREATE Table VEHICLE_TYPE(USAGE_ID TEXT, USAGE_NAME TEXT, TYPE_ID TEXT, TYPE_NAME TEXT, GVW TEXT)");
        db.execSQL("CREATE Table VEHICLE_MAKE(MAKE_ID TEXT, MAKE_NAME TEXT)");
        db.execSQL("CREATE Table VEHICLE_MODEL(MAKE_ID TEXT, MAKE_NAME TEXT, MODEL_ID TEXT, MODEL_NAME TEXT)");
        db.execSQL("CREATE Table VEHICLE_VARIANT(MAKE_ID TEXT, MODEL_ID TEXT, VARIENT_ID TEXT, MAKE_NAME TEXT, MODEL_NAME TEXT, " +
                "VARIENT_NAME TEXT, VEHICLE_ATTRIBUTE TEXT, START_YEAR TEXT, END_YEAR TEXT, SC TEXT, VEHICLE_TYPE TEXT, NUM_CYL TEXT, PRICE TEXT)");
        db.execSQL("CREATE Table VEHICLE_RTO(RTO_ID TEXT, RTO_NAME TEXT)");

        db.execSQL("CREATE Table USER_INFO(ID TEXT, NAME TEXT, AGE TEXT, MOBILE TEXT, NATIONAL_ID TEXT, EMAIL TEXT, EMPLOYER TEXT, STATUS TEXT, COMPANY_ID TEXT, " +
                "VEHICLE_USAGE TEXT, VEHICLE_TYPE TEXT, VEHICLE_MAKE TEXT, VEHICLE_MODEL TEXT, VEHICLE_VARIANT TEXT, RTO TEXT, REGISTRATION_DATE TEXT, PRICE TEXT)");

        db.execSQL("CREATE Table COMPANY_DETAILS(PRODUCT_ID TEXT, INSURER_ID TEXT, LOB TEXT, PRODUCT_TYPE TEXT, PRODUCT_NAME TEXT, " +
                "EFFECTIVE_DATE TEXT, END_DATE TEXT, INSURER_NAME TEXT, TAX TEXT, IDV TEXT, SCHEME TEXT, TOTAL_PREMIUM TEXT, PREMIUM_AMOUNT TEXT, ATTRIBUTE TEXT, FEES TEXT, COVER_PREMIUM)");
        db.execSQL("CREATE Table COMPANY_COVER(PRODUCT_ID TEXT, INSURED_OBJECT TEXT, COVER_ID TEXT, COVER_TYPE TEXT, COVER_NAME TEXT)");

        closeConnection();
    }

    public void clearMasterDetails() {
        openConnection();
        // To clear transaction Tables
        db.execSQL("DELETE FROM VEHICLE_USAGE");
        db.execSQL("DELETE FROM VEHICLE_MAKE");
        db.execSQL("DELETE FROM VEHICLE_MODEL");
        db.execSQL("DELETE FROM VEHICLE_RTO");
        closeConnection();
    }

    public void clearVehicleType() {
        openConnection();
        db.execSQL("DELETE FROM VEHICLE_TYPE");
        closeConnection();
    }

    public void clearVehicleVarient() {
        openConnection();
        db.execSQL("DELETE FROM VEHICLE_VARIANT");
        closeConnection();
    }

    public void clearUserInfo() {
        openConnection();
        db.execSQL("DELETE FROM USER_INFO");
        closeConnection();
    }

    public void clearComapnyDetails() {
        openConnection();
        db.execSQL("DELETE FROM COMPANY_DETAILS");
        db.execSQL("DELETE FROM COMPANY_COVER");
        closeConnection();
    }

    public void updateUserID(String userId, String nationalID) {
        openConnection();
        String updateUserId ="UPDATE USER_INFO SET ID = " + userId + " WHERE NATIONAL_ID = " + nationalID;
        Cursor c = db.rawQuery(updateUserId, null);
        c.moveToFirst();
        c.close();
        closeConnection();

    }

    public String getUserID(String nationalID) {
        String userID = "";
        openConnection();
        Cursor c = db
                .rawQuery(
                        "SELECT ID FROM USER_INFO WHERE NATIONAL_ID = " + nationalID,
                        null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            userID = String.valueOf(c.getString(c
                    .getColumnIndex("ID")));

        }
        c.close();
        closeConnection();
        return userID;

    }

    public String getUsageID(String usageName) {
        String usageID = "";
        openConnection();
        Cursor c = db
                .rawQuery(
                        "SELECT USAGE_ID FROM VEHICLE_USAGE WHERE USAGE_NAME = " + usageName,
                        null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            usageID = String.valueOf(c.getString(c
                    .getColumnIndex("USAGE_ID")));

        }
        c.close();
        closeConnection();
        return usageID;

    }

    public String getMakeID(String makeName) {
        String makeID = "";
        openConnection();
        Cursor c = db
                .rawQuery(
                        "SELECT MAKE_ID FROM VEHICLE_MAKE WHERE MAKE_NAME = " + makeName,
                        null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            makeID = String.valueOf(c.getString(c
                    .getColumnIndex("MAKE_ID")));

        }
        c.close();
        closeConnection();
        return makeID;

    }

    public String getModelID(String modelName) {
        String modelID = "";
        openConnection();
        Cursor c = db
                .rawQuery(
                        "SELECT MODEL_ID FROM VEHICLE_MODEL WHERE MODEL_NAME = " + modelName,
                        null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            modelID = String.valueOf(c.getString(c
                    .getColumnIndex("MODEL_ID")));

        }
        c.close();
        closeConnection();
        return modelID;

    }

    public String getComanyCoverNameByCompanyID(String coverID) {
        String coverName = "";
        openConnection();
        Cursor c = db
                .rawQuery(
                        "SELECT COVER_NAME FROM COMPANY_COVER WHERE COVER_ID = " + coverID,
                        null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            coverName = String.valueOf(c.getString(c
                    .getColumnIndex("COVER_NAME")));

        }
        c.close();
        closeConnection();
        return coverName;

    }

    public String getCoverIDByCoverName(String CoverName) {
        String coverName = "";
        openConnection();
        Cursor c = db
                .rawQuery(
                        "SELECT COVER_ID FROM COMPANY_COVER WHERE COVER_NAME = '" + CoverName + "'",
                        null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            coverName = String.valueOf(c.getString(c
                    .getColumnIndex("COVER_ID")));

        }
        c.close();
        closeConnection();
        return coverName;

    }

    public String getIDV() {
        String coverName = "";
        openConnection();
        Cursor c = db
                .rawQuery(
                        "SELECT IDV FROM COMPANY_DETAILS WHERE PRODUCT_TYPE = 'COMPRH'",
                        null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            coverName = String.valueOf(c.getString(c
                    .getColumnIndex("IDV")));

        }
        c.close();
        closeConnection();
        return coverName;

    }

    public void insertVehicleUsage(VehicleUsage _vehivleUsage) {
        openConnection();
        try {
            String qryString = "Insert into VEHICLE_USAGE(USAGE_ID, USAGE_NAME) Values ('"
                    + _vehivleUsage.getID()
                    + "', '"
                    + _vehivleUsage.getVehicleName() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public ArrayList<VehicleUsage> getVehicleUsage() {
        openConnection();
        String query = "SELECT * FROM VEHICLE_USAGE";
        Cursor c = db.rawQuery(query, null);

        ArrayList<VehicleUsage> vehicleUsageArrayList = new ArrayList<VehicleUsage>();

        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                do {
                    VehicleUsage vehicleUsage = new VehicleUsage();
                    vehicleUsage.setID(Integer.parseInt(c.getString(c
                            .getColumnIndex("USAGE_ID"))));
                    vehicleUsage.setVehicleName(c.getString(c
                            .getColumnIndex("USAGE_NAME")));
                    vehicleUsageArrayList.add(vehicleUsage);
                } while (c.moveToNext());
            }
            c.close();
            closeConnection();
            return vehicleUsageArrayList;
        }else{
            c.close();
            closeConnection();
            return null;
        }
    }

    public void insertVehicleType(VehicleType _vehicleType) {
        openConnection();
        try {
            String qryString = "Insert into VEHICLE_TYPE(USAGE_ID, USAGE_NAME, TYPE_ID, TYPE_NAME, GVW) Values ('"
                    + _vehicleType.getID()
                    + "', '"
                    + _vehicleType.getUsageType()
                    + "', '"
                    + _vehicleType.getVehicleId()
                    + "', '"
                    + _vehicleType.getVehicleType()
                    + "', '"
                    + _vehicleType.getGvwRequired() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public ArrayList<VehicleType> getVehicleType() {
        openConnection();
        String query = "SELECT * FROM VEHICLE_TYPE";
        Cursor c = db.rawQuery(query, null);

        ArrayList<VehicleType> vehicleTypeArrayList = new ArrayList<VehicleType>();

        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                do {
                    VehicleType vehicleType = new VehicleType();
                    vehicleType.setID(c.getString(c
                            .getColumnIndex("USAGE_ID")));
                    vehicleType.setUsageType(c.getString(c
                            .getColumnIndex("USAGE_NAME")));
                    vehicleType.setVehicleId(c.getString(c
                            .getColumnIndex("TYPE_ID")));
                    vehicleType.setVehicleType(c.getString(c
                            .getColumnIndex("TYPE_NAME")));
                    vehicleType.setGvwRequired(c.getString(c
                            .getColumnIndex("GVW")));
                    vehicleTypeArrayList.add(vehicleType);
                } while (c.moveToNext());
            }
            c.close();
            closeConnection();
            return vehicleTypeArrayList;
        }else{
            c.close();
            closeConnection();
            return null;
        }
    }

    public void insertVehicleMake(VehicleMake _vehicleMake) {
        openConnection();
        try {
            String qryString = "Insert into VEHICLE_MAKE(MAKE_ID, MAKE_NAME) Values ('"
                    + _vehicleMake.getMakeId()
                    + "', '"
                    + _vehicleMake.getMakeName() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public ArrayList<VehicleMake> getVehicleMake() {
        openConnection();
        String query = "SELECT * FROM VEHICLE_MAKE";
        Cursor c = db.rawQuery(query, null);
        ArrayList<VehicleMake> vehicleMakeArrayList = new ArrayList<VehicleMake>();
        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                do {
                    VehicleMake vehicleMake = new VehicleMake();
                    vehicleMake.setMakeId(c.getString(c
                            .getColumnIndex("MAKE_ID")));
                    vehicleMake.setMakeName(c.getString(c
                            .getColumnIndex("MAKE_NAME")));
                    vehicleMakeArrayList.add(vehicleMake);
                } while (c.moveToNext());
            }
            c.close();
            closeConnection();
            return vehicleMakeArrayList;
        }else{
            c.close();
            closeConnection();
            return null;
        }
    }

    public void insertVehicleModel(VehicleModel _vehicleModel) {
        openConnection();
        try {
            String qryString = "Insert into VEHICLE_MODEL(MAKE_ID, MAKE_NAME, MODEL_ID, MODEL_NAME) Values ('"
                    + _vehicleModel.getMakeId()
                    + "', '"
                    + _vehicleModel.getMakeName()
                    + "', '"
                    + _vehicleModel.getModelId()
                    + "', '"
                    + _vehicleModel.getModelName() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public ArrayList<VehicleModel> getVehicleModel(String makeId) {
        openConnection();
        String query = "SELECT * FROM VEHICLE_MODEL WHERE MAKE_ID = " + makeId;
        Cursor c = db.rawQuery(query, null);
        ArrayList<VehicleModel> vehicleModelArrayList = new ArrayList<VehicleModel>();
        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                do {
                    VehicleModel vehicleModel = new VehicleModel();
                    vehicleModel.setMakeId(c.getString(c
                            .getColumnIndex("MAKE_ID")));
                    vehicleModel.setMakeName(c.getString(c
                            .getColumnIndex("MAKE_NAME")));
                    vehicleModel.setModelId(c.getString(c
                            .getColumnIndex("MODEL_ID")));
                    vehicleModel.setModelName(c.getString(c
                            .getColumnIndex("MODEL_NAME")));
                    vehicleModelArrayList.add(vehicleModel);
                } while (c.moveToNext());
            }
            c.close();
            closeConnection();
            return vehicleModelArrayList;
        }else{
            c.close();
            closeConnection();
            return null;
        }
    }

    public void insertVehicleVarient(VehicleVarient _vehicleVarient) {
        openConnection();
        try {
            String qryString = "Insert into VEHICLE_VARIANT(MAKE_ID, MODEL_ID, VARIENT_ID, MAKE_NAME, MODEL_NAME, VARIENT_NAME, VEHICLE_ATTRIBUTE, START_YEAR, END_YEAR, SC, VEHICLE_TYPE, NUM_CYL, PRICE) Values ('"
                    + _vehicleVarient.getMakeId()
                    + "', '"
                    + _vehicleVarient.getModelId()
                    + "', '"
                    + _vehicleVarient.getVariantId()
                    + "', '"
                    + _vehicleVarient.getMakeName()
                    + "', '"
                    + _vehicleVarient.getModelName()
                    + "', '"
                    + _vehicleVarient.getVariantName()
                    + "', '"
                    + _vehicleVarient.getVehicleAttr()
                    + "', '"
                    + _vehicleVarient.getStartYear()
                    + "', '"
                    + _vehicleVarient.getEndYear()
                    + "', '"
                    + _vehicleVarient.getSc()
                    + "', '"
                    + _vehicleVarient.getVehicleType()
                    + "', '"
                    + _vehicleVarient.getNumCyl()
                    + "', '"
                    + _vehicleVarient.getPrice() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public ArrayList<VehicleVarient> getVehicleVarient() {
        openConnection();
        String query = "SELECT * FROM VEHICLE_VARIANT";
        Cursor c = db.rawQuery(query, null);
        ArrayList<VehicleVarient> vehicleVarientArrayList = new ArrayList<VehicleVarient>();
        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                do {
                    VehicleVarient vehicleVarient = new VehicleVarient();
                    vehicleVarient.setMakeId(c.getString(c
                            .getColumnIndex("MAKE_ID")));
                    vehicleVarient.setModelId(c.getString(c
                            .getColumnIndex("MODEL_ID")));
                    vehicleVarient.setVariantId(c.getString(c
                            .getColumnIndex("VARIENT_ID")));
                    vehicleVarient.setMakeName(c.getString(c
                            .getColumnIndex("MAKE_NAME")));
                    vehicleVarient.setModelName(c.getString(c
                            .getColumnIndex("MODEL_NAME")));
                    vehicleVarient.setVariantName(c.getString(c
                            .getColumnIndex("VARIENT_NAME")));
                    vehicleVarient.setVehicleAttr(c.getString(c
                            .getColumnIndex("VEHICLE_ATTRIBUTE")));
                    vehicleVarient.setStartYear(c.getString(c
                            .getColumnIndex("START_YEAR")));
                    vehicleVarient.setEndYear(c.getString(c
                            .getColumnIndex("END_YEAR")));
                    vehicleVarient.setSc(c.getString(c
                            .getColumnIndex("SC")));
                    vehicleVarient.setVehicleType(c.getString(c
                            .getColumnIndex("VEHICLE_TYPE")));
                    vehicleVarient.setNumCyl(c.getString(c
                            .getColumnIndex("NUM_CYL")));
                    vehicleVarient.setPrice(c.getString(c
                            .getColumnIndex("PRICE")));
                    vehicleVarientArrayList.add(vehicleVarient);

                } while (c.moveToNext());
            }
            c.close();
            closeConnection();
            return vehicleVarientArrayList;
        }else{
            c.close();
            closeConnection();
            return null;
        }
    }

    public void insertRto(RTO _rto) {
        openConnection();
        try {
            String qryString = "Insert into VEHICLE_RTO(RTO_ID, RTO_NAME) Values ('"
                    + _rto.getID()
                    + "', '"
                    + _rto.getRtoName() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public ArrayList<RTO> getRto() {
        openConnection();
        String query = "SELECT * FROM VEHICLE_RTO";
        Cursor c = db.rawQuery(query, null);
        ArrayList<RTO> rtoList = new ArrayList<RTO>();
        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                do {
                    RTO rto = new RTO();
                    rto.setID(c.getString(c
                            .getColumnIndex("RTO_ID")));
                    rto.setRtoName(c.getString(c
                            .getColumnIndex("RTO_NAME")));
                    rtoList.add(rto);
                } while (c.moveToNext());
            }
            c.close();
            closeConnection();
            return rtoList;
        }else{
            c.close();
            closeConnection();
            return null;
        }
    }

    public void insertComanyDetails(CompanyDetails _companyDetails) {
        openConnection();
        try {
            String qryString = "Insert into COMPANY_DETAILS(PRODUCT_ID, INSURER_ID, LOB, PRODUCT_TYPE, PRODUCT_NAME, EFFECTIVE_DATE, END_DATE, INSURER_NAME, TAX, IDV, SCHEME, TOTAL_PREMIUM, " +
                    "PREMIUM_AMOUNT, ATTRIBUTE, FEES, COVER_PREMIUM) Values ('"
                    + _companyDetails.getProductID()
                    + "', '"
                    + _companyDetails.getInsurerId()
                    + "', '"
                    + _companyDetails.getLob()
                    + "', '"
                    + _companyDetails.getProductType()
                    + "', '"
                    + _companyDetails.getProductName()
                    + "', '"
                    + _companyDetails.getEffectiveDate()
                    + "', '"
                    + _companyDetails.getEndDate()
                    + "', '"
                    + _companyDetails.getInsurerName()
                    + "', '"
                    + _companyDetails.getTax()
                    + "', '"
                    + _companyDetails.getIdv()
                    + "', '"
                    + _companyDetails.getScheme()
                    + "', '"
                    + _companyDetails.getTotalPremium()
                    + "', '"
                    + _companyDetails.getPremiumAmount()
                    + "', '"
                    + _companyDetails.getAttribute()
                    + "', '"
                    + _companyDetails.getFees()
                    + "', '"
                    + _companyDetails.getCoverPremium() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public ArrayList<CompanyDetails> getComanyDetails() {
        openConnection();
        String query = "SELECT * FROM COMPANY_DETAILS";
        Cursor c = db.rawQuery(query, null);
        ArrayList<CompanyDetails> companyDetailsArrayList = new ArrayList<CompanyDetails>();
        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                do {
                    CompanyDetails companyDetails = new CompanyDetails();
                    companyDetails.setProductID(c.getString(c
                            .getColumnIndex("PRODUCT_ID")));
                    companyDetails.setInsurerId(c.getString(c
                            .getColumnIndex("INSURER_ID")));
                    companyDetails.setLob(c.getString(c
                            .getColumnIndex("LOB")));
                    companyDetails.setProductType(c.getString(c
                            .getColumnIndex("PRODUCT_TYPE")));
                    companyDetails.setProductName(c.getString(c
                            .getColumnIndex("PRODUCT_NAME")));
                    companyDetails.setEffectiveDate(c.getString(c
                            .getColumnIndex("EFFECTIVE_DATE")));
                    companyDetails.setEndDate(c.getString(c
                            .getColumnIndex("END_DATE")));
                    companyDetails.setInsurerName(c.getString(c
                            .getColumnIndex("INSURER_NAME")));
                    companyDetails.setTax(c.getString(c
                            .getColumnIndex("TAX")));
                    companyDetails.setIdv(c.getString(c
                            .getColumnIndex("IDV")));
                    companyDetails.setScheme(c.getString(c
                            .getColumnIndex("SCHEME")));
                    companyDetails.setTotalPremium(c.getString(c
                            .getColumnIndex("TOTAL_PREMIUM")));
                    companyDetails.setPremiumAmount(c.getString(c
                            .getColumnIndex("PREMIUM_AMOUNT")));
                    companyDetails.setAttribute(c.getString(c
                            .getColumnIndex("ATTRIBUTE")));
                    companyDetails.setFees(c.getString(c
                            .getColumnIndex("FEES")));
                    companyDetails.setCoverPremium(c.getString(c
                            .getColumnIndex("COVER_PREMIUM")));
                    companyDetailsArrayList.add(companyDetails);

                } while (c.moveToNext());
            }
            c.close();
            closeConnection();
            return companyDetailsArrayList;
        }else{
            c.close();
            closeConnection();
            return null;
        }
    }

    public void insertComanyCover(CompanyCover _companyCover) {
        openConnection();
        try {
            String qryString = "Insert into COMPANY_COVER(PRODUCT_ID, INSURED_OBJECT, COVER_ID, COVER_TYPE, COVER_NAME) Values ('"
                    + _companyCover.getProductID()
                    + "', '"
                    + _companyCover.getInsurerObject()
                    + "', '"
                    + _companyCover.getCoverId()
                    + "', '"
                    + _companyCover.getCoverType()
                    + "', '"
                    + _companyCover.getCoverName() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public ArrayList<CompanyCover> getComanyCover() {
        openConnection();
        String query = "SELECT * FROM COMPANY_COVER";
        Cursor c = db.rawQuery(query, null);
        ArrayList<CompanyCover> companyCoverArrayList = new ArrayList<CompanyCover>();
        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                do {
                    CompanyCover companyCover = new CompanyCover();
                    companyCover.setProductID(c.getString(c
                            .getColumnIndex("PRODUCT_ID")));
                    companyCover.setInsurerObject(c.getString(c
                            .getColumnIndex("INSURED_OBJECT")));
                    companyCover.setCoverId(c.getString(c
                            .getColumnIndex("COVER_ID")));
                    companyCover.setCoverType(c.getString(c
                            .getColumnIndex("COVER_TYPE")));
                    companyCover.setCoverName(c.getString(c
                            .getColumnIndex("COVER_NAME")));
                    companyCoverArrayList.add(companyCover);

                } while (c.moveToNext());
            }
            c.close();
            closeConnection();
            return companyCoverArrayList;
        }else{
            c.close();
            closeConnection();
            return null;
        }
    }

    public ArrayList<CompanyCover> getComanyCoverByCompany(String companyID) {
        openConnection();
        String query = "SELECT * FROM COMPANY_COVER WHERE PRODUCT_ID = '" + companyID + "'";
        Cursor c = db.rawQuery(query, null);
        ArrayList<CompanyCover> companyCoverArrayList = new ArrayList<CompanyCover>();
        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                do {
                    CompanyCover companyCover = new CompanyCover();
                    companyCover.setProductID(c.getString(c
                            .getColumnIndex("PRODUCT_ID")));
                    companyCover.setInsurerObject(c.getString(c
                            .getColumnIndex("INSURED_OBJECT")));
                    companyCover.setCoverId(c.getString(c
                            .getColumnIndex("COVER_ID")));
                    companyCover.setCoverType(c.getString(c
                            .getColumnIndex("COVER_TYPE")));
                    companyCover.setCoverName(c.getString(c
                            .getColumnIndex("COVER_NAME")));
                    companyCoverArrayList.add(companyCover);

                } while (c.moveToNext());
            }
            c.close();
            closeConnection();
            return companyCoverArrayList;
        }else{
            c.close();
            closeConnection();
            return null;
        }
    }

    public void insertUserInfo(UserInfo _userInfo) {
        openConnection();
        try {
            String qryString = "Insert into USER_INFO(ID, NAME, AGE, MOBILE, NATIONAL_ID, EMAIL, EMPLOYER, STATUS, COMPANY_ID, " +
                    "VEHICLE_USAGE, VEHICLE_TYPE, VEHICLE_MAKE, VEHICLE_MODEL, VEHICLE_VARIANT, RTO, REGISTRATION_DATE, PRICE) Values ('"
                    + _userInfo.getId()
                    + "', '"
                    + _userInfo.getFullName()
                    + "', '"
                    + _userInfo.getAge()
                    + "', '"
                    + _userInfo.getMobileNo()
                    + "', '"
                    + _userInfo.getNationalId()
                    + "', '"
                    + _userInfo.getEmail()
                    + "', '"
                    + _userInfo.getEmployer()
                    + "', '"
                    + _userInfo.getStatus()
                    + "', '"
                    + _userInfo.getCompanyId()
                    + "', '"
                    + _userInfo.getVehicleUsage()
                    + "', '"
                    + _userInfo.getUsageType()
                    + "', '"
                    + _userInfo.getMake()
                    + "', '"
                    + _userInfo.getModal()
                    + "', '"
                    + _userInfo.getVariant()
                    + "', '"
                    + _userInfo.getRto()
                    + "', '"
                    + _userInfo.getRegistrationDate()
                    + "', '"
                    + _userInfo.getPrice() + "')";
            db.execSQL(qryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public ArrayList<UserInfo> getUserInfo() {
        openConnection();
        String query = "SELECT * FROM USER_INFO";
        Cursor c = db.rawQuery(query, null);
        ArrayList<UserInfo> userInfoArrayList = new ArrayList<UserInfo>();
        if ((c != null) && (c.getCount() > 0)) {
            if (c.moveToFirst()) {
                do {
                    UserInfo userInfo = new UserInfo();
                    userInfo.setId(c.getString(c
                            .getColumnIndex("ID")));
                    userInfo.setFullName(c.getString(c
                            .getColumnIndex("NAME")));
                    userInfo.setAge(c.getString(c
                            .getColumnIndex("AGE")));
                    userInfo.setMobileNo(c.getString(c
                            .getColumnIndex("MOBILE")));
                    userInfo.setNationalId(c.getString(c
                            .getColumnIndex("NATIONAL_ID")));
                    userInfo.setEmail(c.getString(c
                            .getColumnIndex("EMAIL")));
                    userInfo.setEmployer(c.getString(c
                            .getColumnIndex("EMPLOYER")));
                    userInfo.setStatus(c.getString(c
                            .getColumnIndex("STATUS")));
                    userInfo.setCompanyId(c.getString(c
                            .getColumnIndex("COMPANY_ID")));
                    userInfo.setVehicleUsage(c.getString(c
                            .getColumnIndex("VEHICLE_USAGE")));
                    userInfo.setUsageType(c.getString(c
                            .getColumnIndex("VEHICLE_TYPE")));
                    userInfo.setMake(c.getString(c
                            .getColumnIndex("VEHICLE_MAKE")));
                    userInfo.setModal(c.getString(c
                            .getColumnIndex("VEHICLE_MODEL")));
                    userInfo.setVariant(c.getString(c
                            .getColumnIndex("VEHICLE_VARIANT")));
                    userInfo.setRto(c.getString(c
                            .getColumnIndex("RTO")));
                    userInfo.setRegistrationDate(c.getString(c
                            .getColumnIndex("REGISTRATION_DATE")));
                    userInfo.setPrice(c.getString(c
                            .getColumnIndex("PRICE")));
                    userInfoArrayList.add(userInfo);
                } while (c.moveToNext());
            }
            c.close();
            closeConnection();
            return userInfoArrayList;
        }else{
            c.close();
            closeConnection();
            return null;
        }
    }

    // Sqlite DB Helper Class
    private static class RMSAGGDataHelper extends SQLiteOpenHelper {
        RMSAGGDataHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
