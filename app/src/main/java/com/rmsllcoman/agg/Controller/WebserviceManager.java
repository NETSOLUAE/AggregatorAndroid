package com.rmsllcoman.agg.Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.rmsllcoman.agg.Activity.AboutYourselfActivity;
import com.rmsllcoman.agg.Model.RTO;
import com.rmsllcoman.agg.Model.VehicleMake;
import com.rmsllcoman.agg.Model.VehicleModel;
import com.rmsllcoman.agg.Model.VehicleType;
import com.rmsllcoman.agg.Model.VehicleUsage;
import com.rmsllcoman.agg.Model.VehicleVarient;
import com.rmsllcoman.agg.Model.CompanyCover;
import com.rmsllcoman.agg.Model.CompanyDetails;
import com.rmsllcoman.agg.fragment.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by macmini on 8/7/17.
 */

public class WebserviceManager {
    private Context context;

    public WebserviceManager(Context context) {
        this.context = context;
    }

    public String getVehicleDetails () {
        String result = "";
        String query = "http://sample-env-3.xeixwpezdk.us-east-1.elasticbeanstalk.com/rest/master/usageType";
        String json = makeJson();

        URL url = null;
        try {
            url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();

            int status = ((HttpURLConnection) conn).getResponseCode();
            Log.i("","Status : "+status);

            if (status == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                result = response.toString();
//                if (!result.equalsIgnoreCase("")){
                    result = parseVehicleUsage(result);
//                } else {
//                    result = "NoData";
//                }
            } else {
                result = "URLNotFound";
                System.out.println("POST request not worked");
            }
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String parseVehicleUsage(String response) {
        DatabaseManager dbManager = new DatabaseManager(context);
        String statusString = "";
        try {
            dbManager.clearMasterDetails();
            VehicleUsage vehicleUsage0 = new VehicleUsage();
            vehicleUsage0.setID(0);
            vehicleUsage0.setVehicleName("Select Usage");
            dbManager.insertVehicleUsage(vehicleUsage0);

            if (!response.equalsIgnoreCase("")) {
                JSONObject companyResp = new JSONObject(response);
                JSONArray staffDetailsArray = companyResp.getJSONArray("result");
                if (staffDetailsArray.length() > 0) {
                    for (int i = 0; i < staffDetailsArray.length(); i++) {
                        JSONObject staffDetailsObject = staffDetailsArray
                                .getJSONObject(i);
                        VehicleUsage vehicleUsage = new VehicleUsage();
                        vehicleUsage.setID(Integer.parseInt(staffDetailsObject
                                .getString("id")));
                        vehicleUsage.setVehicleName(staffDetailsObject
                                .getString("name"));
                        dbManager.insertVehicleUsage(vehicleUsage);
                    }
                    statusString = "Updated";
                } else {
                    statusString = "NoData";
                }
            } else {
                statusString = "NoData";
            }

        } catch (JSONException e) {
            statusString = "NoData";
            e.printStackTrace();
            Log.v("Exception is " + Log.getStackTraceString(e), "", e);
        }
        return statusString;
    }

    public String getVehicleMake () {
        String result = "";
        String query = "http://sample-env-3.xeixwpezdk.us-east-1.elasticbeanstalk.com/rest/master/makes";
        String json = makeJson();

        URL url = null;
        try {
            url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();

            int status = ((HttpURLConnection) conn).getResponseCode();
            Log.i("","Status : "+status);

            if (status == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                result = response.toString();
//                if (!result.equalsIgnoreCase("")){
                    result = parseVehicleMake(result);
//                } else {
//                    result = "NoData";
//                }
            } else {
                result = "URLNotFound";
                System.out.println("POST request not worked");
            }
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String parseVehicleMake(String response) {
        DatabaseManager dbManager = new DatabaseManager(context);
        String statusString = "";
        try {

            VehicleMake vehicleMake0 = new VehicleMake();
            vehicleMake0.setMakeId("0");
            vehicleMake0.setMakeName("Select Make");
            dbManager.insertVehicleMake(vehicleMake0);

            if (!response.equalsIgnoreCase("")) {
                JSONObject companyResp = new JSONObject(response);
                JSONArray vehiclemake = companyResp.getJSONArray("result");
                if (vehiclemake.length() > 0) {
                    for (int i = 0; i < vehiclemake.length(); i++) {
                        JSONObject vehicleMake = vehiclemake
                                .getJSONObject(i);
                        VehicleMake vehicleMake1 = new VehicleMake();
                        vehicleMake1.setMakeId(vehicleMake
                                .getString("id"));
                        vehicleMake1.setMakeName(vehicleMake
                                .getString("name"));
                        dbManager.insertVehicleMake(vehicleMake1);
                    }
                    statusString = "Updated";
                } else {
                    statusString = "NoData";
                }
            } else {
                statusString = "NoData";
            }

        } catch (JSONException e) {
            statusString = "NoData";
            e.printStackTrace();
            Log.v("Exception is " + Log.getStackTraceString(e), "", e);
        }
        return statusString;
    }

    public String getVehicleModel () {
        String result = "";
        String query = "http://sample-env-3.xeixwpezdk.us-east-1.elasticbeanstalk.com/rest/master/allmodels";
        String json = makeJson();

        URL url = null;
        try {
            url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();

            int status = ((HttpURLConnection) conn).getResponseCode();
            Log.i("","Status : "+status);

            if (status == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                result = response.toString();
//                if (!result.equalsIgnoreCase("")){
                    result = parseVehicleModel(result);
//                } else {
//                    result = "NoData";
//                }
            } else {
                result = "URLNotFound";
                System.out.println("POST request not worked");
            }
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String parseVehicleModel(String response) {
        DatabaseManager dbManager = new DatabaseManager(context);
        String statusString = "";
        try {

            VehicleModel vehicleModel0 = new VehicleModel();
            vehicleModel0.setMakeId("0");
            vehicleModel0.setMakeName("Select Make");
            vehicleModel0.setModelId("0");
            vehicleModel0.setModelName("Select Model");
            dbManager.insertVehicleModel(vehicleModel0);

            if (!response.equalsIgnoreCase("")) {
                JSONObject companyResp = new JSONObject(response);
                JSONArray vehiclemodel = companyResp.getJSONArray("result");
                if (vehiclemodel.length() > 0) {
                    for (int i = 0; i < vehiclemodel.length(); i++) {
                        JSONObject vehicleModel = vehiclemodel
                                .getJSONObject(i);
                        VehicleModel vehicleModel1 = new VehicleModel();
                        vehicleModel1.setMakeId(vehicleModel
                                .getString("make_id"));
                        vehicleModel1.setMakeName(vehicleModel
                                .getString("make_name"));
                        vehicleModel1.setModelId(vehicleModel
                                .getString("model_id"));
                        vehicleModel1.setModelName(vehicleModel
                                .getString("model_name"));
                        dbManager.insertVehicleModel(vehicleModel1);
                    }
                    statusString = "Updated";
                } else {
                    statusString = "NoData";
                }
            } else {
                statusString = "NoData";
            }
        } catch (JSONException e) {
            statusString = "NoData";
            e.printStackTrace();
            Log.v("Exception is " + Log.getStackTraceString(e), "", e);
        }
        return statusString;
    }

    public String getRto () {
        String result = "";
        String query = "http://sample-env-3.xeixwpezdk.us-east-1.elasticbeanstalk.com/rest/master/rto";
        String json = makeJson();

        URL url = null;
        try {
            url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();

            int status = ((HttpURLConnection) conn).getResponseCode();
            Log.i("","Status : "+status);

            if (status == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                result = response.toString();
//                if (!result.equalsIgnoreCase("")){
                    result = parseRto(result);
//                } else {
//                    result = "NoData";
//                }
            } else {
                result = "URLNotFound";
                System.out.println("POST request not worked");
            }
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String parseRto(String response) {
        DatabaseManager dbManager = new DatabaseManager(context);
        String statusString = "";
        try {

            RTO rto0 = new RTO();
            rto0.setID("0");
            rto0.setRtoName("Select Place");
            dbManager.insertRto(rto0);

            if (!response.equalsIgnoreCase("")) {
                JSONObject companyResp = new JSONObject(response);
                JSONArray rto = companyResp.getJSONArray("result");
                if (rto.length() > 0) {
                    for (int i = 0; i < rto.length(); i++) {
                        JSONObject rto1 = rto
                                .getJSONObject(i);
                        RTO rto2 = new RTO();
                        rto2.setID(rto1
                                .getString("id"));
                        rto2.setRtoName(rto1
                                .getString("name"));
                        dbManager.insertRto(rto2);
                    }
                    statusString = "Updated";
                } else {
                    statusString = "NoData";
                }
            } else {
                statusString = "NoData";
            }

        } catch (JSONException e) {
            statusString = "NoData";
            e.printStackTrace();
            Log.v("Exception is " + Log.getStackTraceString(e), "", e);
        }
        return statusString;
    }

    public String getVehicleType (String usageID) {
        String result = "";
        String query = "http://sample-env-3.xeixwpezdk.us-east-1.elasticbeanstalk.com/rest/master/getvehicleType/" + usageID;
        String json = makeJson();

        URL url = null;
        try {
            url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();

            int status = ((HttpURLConnection) conn).getResponseCode();
            Log.i("","Status : "+status);

            if (status == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                result = response.toString();
//                if (!result.equalsIgnoreCase("")){
//                    result = parseVehicleType(result);
//                } else {
                result = parseVehicleType(result);
//                }
            } else {
                result = "URLNotFound";
                System.out.println("POST request not worked");
            }
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String parseVehicleType(String response) {
        DatabaseManager dbManager = new DatabaseManager(context);
        String statusString = "";
        try {
            JSONObject companyResp = new JSONObject(response);

            dbManager.clearVehicleType();
            VehicleType vehicleType0 = new VehicleType();
            vehicleType0.setID("0");
            vehicleType0.setUsageType("");
            vehicleType0.setVehicleId("0");
            vehicleType0.setVehicleType("Select Type");
            vehicleType0.setGvwRequired("");
            dbManager.insertVehicleType(vehicleType0);

            if (!response.equalsIgnoreCase("")) {
                JSONArray staffDetailsArray = companyResp.getJSONArray("result");
                if (staffDetailsArray.length() > 0) {
                    for (int i = 0; i < staffDetailsArray.length(); i++) {
                        JSONObject vehicletype = staffDetailsArray
                                .getJSONObject(i);
                        VehicleType vehicleType = new VehicleType();
                        vehicleType.setID(vehicletype
                                .getString("usageCode"));
                        vehicleType.setUsageType(vehicletype
                                .getString("usageType"));
                        vehicleType.setVehicleId(vehicletype
                                .getString("vehicleCode"));
                        vehicleType.setVehicleType(vehicletype
                                .getString("vehicleType"));
                        vehicleType.setGvwRequired(vehicletype
                                .getString("gvwRequired"));
                        dbManager.insertVehicleType(vehicleType);
                    }
                    statusString = "Updated";
                } else {
                    statusString = "NoData";
                }
            } else {
                statusString = "NoData";
            }

        } catch (JSONException e) {
            statusString = "NoData";
            e.printStackTrace();
            Log.v("Exception is " + Log.getStackTraceString(e), "", e);
        }
        return statusString;
    }

    public String getVehicleVarient (String makeId, String modelId) {
        String result = "";
        String query = "http://sample-env-3.xeixwpezdk.us-east-1.elasticbeanstalk.com/rest/master/variants/" + makeId + "/" + modelId;
        String json = makeJson();

        URL url = null;
        try {
            url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();

            int status = ((HttpURLConnection) conn).getResponseCode();
            Log.i("","Status : "+status);

            if (status == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                result = response.toString();
//                if (!result.equalsIgnoreCase("")){
                    result = parseVehicleVarient(result);
//                } else {
//                    result = "NoData";
//                }
            } else {
                result = "URLNotFound";
                System.out.println("POST request not worked");
            }
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String parseVehicleVarient(String response) {
        DatabaseManager dbManager = new DatabaseManager(context);
        String statusString = "";
        try {
            dbManager.clearVehicleVarient();
            VehicleVarient vehicleVarient0 = new VehicleVarient();
            vehicleVarient0.setMakeId("0");
            vehicleVarient0.setModelId("0");
            vehicleVarient0.setVariantId("0");
            vehicleVarient0.setMakeName("Select Make");
            vehicleVarient0.setModelName("Select Model");
            vehicleVarient0.setVariantName("Select Variant");
            vehicleVarient0.setVehicleAttr("");
            vehicleVarient0.setStartYear("");
            vehicleVarient0.setEndYear("");
            vehicleVarient0.setSc("");
            vehicleVarient0.setVehicleType("");
            vehicleVarient0.setNumCyl("");
            vehicleVarient0.setPrice("");
            dbManager.insertVehicleVarient(vehicleVarient0);

            if (!response.equalsIgnoreCase("")) {
                JSONObject companyResp = new JSONObject(response);
                JSONArray vehicleVarient = companyResp.getJSONArray("result");
                if (vehicleVarient.length() > 0) {
                    for (int i = 0; i < vehicleVarient.length(); i++) {
                        JSONObject vehiclevarient = vehicleVarient
                                .getJSONObject(i);
                        VehicleVarient vehicleVarient1 = new VehicleVarient();
                        vehicleVarient1.setMakeId(vehiclevarient
                                .getString("makeId"));
                        vehicleVarient1.setModelId(vehiclevarient
                                .getString("modelId"));
                        vehicleVarient1.setVariantId(vehiclevarient
                                .getString("variantId"));
                        vehicleVarient1.setMakeName(vehiclevarient
                                .getString("makeName"));
                        vehicleVarient1.setModelName(vehiclevarient
                                .getString("modelName"));
                        vehicleVarient1.setVariantName(vehiclevarient
                                .getString("variantName"));
                        vehicleVarient1.setVehicleAttr(vehiclevarient
                                .getString("vehicleAttr"));
                        vehicleVarient1.setStartYear(vehiclevarient
                                .getString("startYear"));
                        vehicleVarient1.setEndYear(vehiclevarient
                                .getString("endYear"));
                        vehicleVarient1.setSc(vehiclevarient
                                .getString("sc"));
                        vehicleVarient1.setVehicleType(vehiclevarient
                                .getString("vehicleType"));
                        vehicleVarient1.setNumCyl(vehiclevarient
                                .getString("numCyl"));
                        vehicleVarient1.setPrice(vehiclevarient
                                .getString("price"));
                        dbManager.insertVehicleVarient(vehicleVarient1);
                    }
                    statusString = "Updated";
                } else {
                    statusString = "NoData";
                }
            } else {
                statusString = "NoData";
            }

        } catch (JSONException e) {
            statusString = "NoData";
            e.printStackTrace();
            Log.v("Exception is " + Log.getStackTraceString(e), "", e);
        }
        return statusString;
    }

    public String sendUserInformation (String id, String name, String age, String mobile_no, String national_id, String email, String employer, String status, String company_id,
                                       String vehicle_usage, String usage_type, String make, String modal, String variant, String rto, String yom, String price,
                                       String registration_date, String policy_start_date, String policy_end_date, String vehicle_age, String premium,
                                       ArrayList<String> pictureArrayList) {
        String result = "";
        URL url = null;
        HttpURLConnection connection = null;

        if (pictureArrayList.size() > 0) {
            String query = "http://netsolintl.net/aggrigator/aggregate.php?action_id=uploadImages&id=" + id;
            String charset = "UTF-8";
            File sourceFile[] = new File[pictureArrayList.size()];

            for (int i=0;i<pictureArrayList.size();i++){
                File imageFile = new File(pictureArrayList.get(i));
                long originallength = imageFile.length() / 1024; // Size in KB
                imageFile = compressFile(imageFile, context);
                if (imageFile != null) {
                    long length = imageFile.length() / 1024; // Size in KB
                    sourceFile[i] = imageFile;
                } else {
                    sourceFile[i] = new File(pictureArrayList.get(i));
                }
            }

            try {
                FileUploader multipart = new FileUploader(query, charset);

                for (int i=0;i<pictureArrayList.size();i++){
                    multipart.addFilePart("license_back["+i+"]", sourceFile[i]);
                }

                List<String> response = multipart.finish();
                result = String.valueOf(response);
                System.out.println("SERVER REPLIED:");

                for (String line : response) {
                    System.out.println(line);
                }
            } catch (IOException ex) {
                System.err.println(ex);
            }
        } else {
            String query = "http://netsolintl.net/aggrigator/aggregate.php?action_id=saveUser" + "&id=" + id + "&full_name=" + name + "&age=" + age + "&mobile_no=" + mobile_no +
                        "&national_id=" + national_id + "&email=" + email + "&employer=" + employer + "&status=" + status + "&company_id=" + company_id + "&vehicle_usage=" + vehicle_usage + "&usage_type=" + usage_type +
                        "&make=" + make + "&modal=" + modal + "&variant=" + variant + "&rto=" + rto + "&year_of_manufacture=" + yom + "&price=" + price + "&registration_date=" + registration_date
                        + "&policy_start_date=" + policy_start_date + "&policy_end_date=" + policy_end_date + "&vehicle_age=" + vehicle_age + "&premiume_amount=" + premium;
            query = query.replaceAll(" ", "%20");
            try {
                url = new URL(query);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");

                int status1 = ((HttpURLConnection) connection).getResponseCode();
                Log.i("","Status : "+status1);

                if (status1 == HttpURLConnection.HTTP_OK) { //success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            connection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // print result
                    System.out.println(response.toString());
                    result = response.toString();
                    if (result.equalsIgnoreCase("") || result.equalsIgnoreCase(" []")){
                        result = "NoData";
                    }
                } else {
                    result = "URLNotFound";
                    System.out.println("POST request not worked");
                }
            } catch (IOException e) {
                result = "URLNotFound";
                e.printStackTrace();
            }
        }

        return result;
    }

    public String sendUserPolicy (String id, String company_id, ArrayList<CompanyCover> companyCoverArrayList) {
        String result = "";
        URL url = null;
        HttpURLConnection connection = null;
        String query = "http://netsolintl.net/aggrigator/aggregate.php?action_id=savePolicy" + "&id=" + id + "&company_id=" + company_id;
        String addons = "";
        for (int i = 0; i < companyCoverArrayList.size(); i++) {
            addons = addons + "&add_ons[" + i + "][add_on_id]=" + companyCoverArrayList.get(i).getCoverId() +
                    "&add_ons[" + i + "][title]=" + companyCoverArrayList.get(i).getCoverName() +
                    "&add_ons[" + i + "][status]=Yes";
        }
        query = query + addons;
        query = query.replaceAll(" ", "%20");
        try {
            url = new URL(query);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            int status1 = ((HttpURLConnection) connection).getResponseCode();
            Log.i("","Status : "+status1);

            if (status1 == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                result = response.toString();
                if (result.equalsIgnoreCase("") || result.equalsIgnoreCase(" []")){
                    result = "NoData";
                }
            } else {
                result = "URLNotFound";
                System.out.println("POST request not worked");
            }
        } catch (IOException e) {
            result = "URLNotFound";
            e.printStackTrace();
        }
        return result;
    }

    public String getCompanyDetails (String usageId, String vehicleId, String makeId, String modelId, String variantId, String regDate, String vehicleAge,
                                     String price, String policyStartDate, String prePolicyEndDate, String claim, String numClaims, String rtoId, String yom, String insuredAge) {
        String result = "";
        String query = "http://sample-env-3.xeixwpezdk.us-east-1.elasticbeanstalk.com/rest/quote/allquotes/Motor/PC";
        String json = makeComapnyDetailsJson(usageId, vehicleId, makeId, modelId, variantId, regDate, vehicleAge,
                price, policyStartDate, prePolicyEndDate, claim, numClaims, rtoId, yom, insuredAge);

        URL url = null;
        try {
            url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();

            int status1 = ((HttpURLConnection) conn).getResponseCode();
            Log.i("","Status : "+status1);

            if (status1 == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
                result = response.toString();
                if (!result.equalsIgnoreCase("")){
                    result = parseCompanyDetails(result);
                } else {
                    result = "NoData";
                }
            } else {
                result = "URLNotFound";
                System.out.println("POST request not worked");
            }
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String parseCompanyDetails(String response) {
        DatabaseManager dbManager = new DatabaseManager(context);
        String statusString = "";
        try {
            JSONObject companyResp = new JSONObject(response);

            if (companyResp.has("result")) {
                JSONArray companyDetails = companyResp.getJSONArray("result");
                dbManager.clearComapnyDetails();
                if (companyDetails.length() > 0) {
                    for (int i = 0; i < companyDetails.length(); i++) {
                        JSONObject companyDetailsJSONObject = companyDetails
                                .getJSONObject(i);
                        CompanyDetails companyDetails1 = new CompanyDetails();
                        companyDetails1.setProductID(companyDetailsJSONObject
                                .getString("productId"));
                        companyDetails1.setInsurerId(companyDetailsJSONObject
                                .getString("insurerId"));
                        companyDetails1.setLob(companyDetailsJSONObject
                                .getString("lob"));
                        companyDetails1.setProductType(companyDetailsJSONObject
                                .getString("productType"));
                        companyDetails1.setProductName(companyDetailsJSONObject
                                .getString("productName"));
                        companyDetails1.setEffectiveDate(companyDetailsJSONObject
                                .getString("effectiveDate"));
                        companyDetails1.setEndDate(companyDetailsJSONObject
                                .getString("endDate"));
                        companyDetails1.setInsurerName(companyDetailsJSONObject
                                .getString("insurerName"));
                        if (companyDetailsJSONObject.isNull("tax")) {
                            companyDetails1.setTax("None");
                        } else {
                            double price1 = companyDetailsJSONObject
                                    .getDouble("tax");
                            companyDetails1.setTax(String.format(Locale.CANADA, "%.2f", price1));
                        }
                        if (companyDetailsJSONObject.isNull("idv")) {
                            companyDetails1.setIdv("None");
                        } else {
                            companyDetails1.setIdv(String.valueOf(companyDetailsJSONObject
                                    .getInt("idv")));
                        }
                        if (companyDetailsJSONObject.isNull("scheme")) {
                            companyDetails1.setScheme("None");
                        } else {
                            companyDetails1.setScheme(companyDetailsJSONObject
                                    .getString("scheme"));
                        }

                        String attribute = "";
                        if (companyDetailsJSONObject.isNull("attributes")) {
                            Log.d("", "");
                        } else {
                            JSONArray companyCoverArray = companyDetailsJSONObject.getJSONArray("attributes");
                            for (int j = 0; j < companyCoverArray.length(); j++) {
                                JSONObject companyCoverArrayObject = companyCoverArray
                                        .getJSONObject(j);
                                for (String key : iterate(companyCoverArrayObject.keys()))
                                {
                                    String heading = key;
                                    if (key.contains("_")) {
                                        heading = heading.replace("_", " ");
                                    }
                                    attribute = attribute + heading + " : " + companyCoverArrayObject.getString(key) + "\n";
                                }
                            }
                        }
                        companyDetails1.setAttribute(attribute);

                        String fees = "";
                        if (companyDetailsJSONObject.isNull("fees")) {
                            Log.d("", "");
                        } else {
                            JSONArray companyCoverArray = companyDetailsJSONObject.getJSONArray("fees");
                            for (int j = 0; j < companyCoverArray.length(); j++) {
                                JSONObject companyCoverArrayObject = companyCoverArray
                                        .getJSONObject(j);
                                for (String key : iterate(companyCoverArrayObject.keys()))
                                {
                                    fees = fees + key + " : " + companyCoverArrayObject.getString(key) + "\n";
                                }
                            }
                        }
                        companyDetails1.setFees(fees);

                        String coverPremium = "";
                        int coverAmount = 0;
                        if (companyDetailsJSONObject.isNull("coverPremiums")) {
                            Log.d("", "");
                        } else {
                            JSONObject companyCoverArray = companyDetailsJSONObject.getJSONObject("coverPremiums");
                            for (String key : iterate(companyCoverArray.keys()))
                            {
                                coverPremium = coverPremium + key + " : " + companyCoverArray.getString(key) + "\n";
                                coverAmount = coverAmount + companyCoverArray.getInt(key);
                            }
                        }
                        companyDetails1.setCoverPremium(coverPremium);

                        coverAmount = coverAmount + companyDetailsJSONObject.getInt("totalPremium");
                        companyDetails1.setTotalPremium(String.valueOf(coverAmount));
                        companyDetails1.setPremiumAmount(String.valueOf(companyDetailsJSONObject.getDouble("totalPremium")));

                        dbManager.insertComanyDetails(companyDetails1);

                        if (companyDetailsJSONObject.isNull("covers")) {
                            Log.d("", "");
                        } else {
                            JSONArray companyCoverArray = companyDetailsJSONObject.getJSONArray("covers");
                            for (int j = 0; j < companyCoverArray.length(); j++) {
                                JSONObject companyCoverArrayObject = companyCoverArray
                                        .getJSONObject(j);
                                CompanyCover companyCover = new CompanyCover();
                                companyCover.setProductID(companyDetailsJSONObject
                                        .getString("productId"));
                                companyCover.setInsurerObject(companyCoverArrayObject
                                        .getString("insuredObject"));
                                companyCover.setCoverId(companyCoverArrayObject
                                        .getString("coverId"));
                                companyCover.setCoverType(companyCoverArrayObject
                                        .getString("coverType"));
                                companyCover.setCoverName(companyCoverArrayObject
                                        .getString("coverName"));
                                dbManager.insertComanyCover(companyCover);
                            }
                        }
                    }
                    statusString = "Updated";
                } else {
                    statusString = "NoData";
                }
            } else if (companyResp.has("error")){
                String errorMessage = companyResp.getString("error");
                AboutYourselfActivity.errorMessage = errorMessage;
                HomeFragment.errorMessage = errorMessage;
                statusString = "Error";
            } else {
                statusString = "NoDataAvailable";
            }

        } catch (JSONException e) {
            statusString = "NoData";
            e.printStackTrace();
            Log.v("Exception is " + Log.getStackTraceString(e), "", e);
        }
        return statusString;
    }

    private String makeComapnyDetailsJson(String usageId, String vehicleId, String makeId, String modelId, String variantId, String regDate, String vehicleAge,
                                          String price, String policyStartDate, String prePolicyEndDate, String claim, String numClaims, String rtoId, String yom, String insuredAge) {

        String vehicleIdCombined = makeId + "^" + modelId + "^" + variantId;
        JSONObject authenticationObject = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            json.put("accesskey", "RMS");
            json.put("secretkey", "RMS");
            authenticationObject.put("authentication",json);
            authenticationObject.put("usageType",usageId);
            authenticationObject.put("vehicleType",vehicleId);
            authenticationObject.put("vehicleId",vehicleIdCombined);
            authenticationObject.put("vehicleRegistrationDate",regDate);
            authenticationObject.put("vehicleAge",vehicleAge);
            authenticationObject.put("price",price);
            authenticationObject.put("policyStartDate",policyStartDate);
            authenticationObject.put("prePolicyEndDate",prePolicyEndDate);
            authenticationObject.put("claim",claim);
            authenticationObject.put("numClaims",numClaims);
            authenticationObject.put("rto",rtoId);
            authenticationObject.put("yom",yom);
            authenticationObject.put("insuredAge",insuredAge);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return authenticationObject.toString();
    }

    private String makeJson() {

        JSONObject authenticationArray = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            json.put("accesskey", "RMS");
            json.put("secretkey", "RMS");
            authenticationArray.put("authentication",json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return authenticationArray.toString();
    }

    private <T> Iterable<T> iterate(final Iterator<T> i){
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return i;
            }
        };
    }

    public static File compressFile(File file, Context context) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }
}
