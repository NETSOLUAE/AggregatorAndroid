package com.rmsllcoman.agg.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import com.rmsllcoman.agg.Adapter.SpinnerAdapterEmployeer;
import com.rmsllcoman.agg.R;
import com.rmsllcoman.agg.other.AsyncServiceCall;
import com.rmsllcoman.agg.Model.CompanyCover;
import com.rmsllcoman.agg.other.Constants;
import com.rmsllcoman.agg.Controller.DatabaseManager;
import com.rmsllcoman.agg.Controller.NetworkManager;
import com.rmsllcoman.agg.other.TextDrawable;
import com.rmsllcoman.agg.Model.UserInfo;
import com.rmsllcoman.agg.Controller.WebserviceManager;

/**
 * Created by macmini on 8/6/17.
 */

public class AboutYourselfActivity extends AppCompatActivity {

    String ageText;
    Button getQuote;
    EditText fullName;
    EditText age;
    EditText mobileNo;
    EditText nationalId;
    EditText email;
    Spinner employeer;
    CheckBox terms;
    static String selectedEmployer;
    static ArrayList<String> employerList;
    public static String errorMessage = "";
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
    private WebserviceManager _webServiceManager;
    private DatabaseManager _dbManager;
    public static ProgressDialog progressDialogReset;
    final int PERMISSION_REQUEST_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_yourself);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("RMS");

        _webServiceManager = new WebserviceManager(this);
        _dbManager = new DatabaseManager(this);

        getQuote = (Button) findViewById(R.id.get_quote);
        fullName = (EditText) findViewById(R.id.fullName);
        age = (EditText) findViewById(R.id.age);
        mobileNo = (EditText) findViewById(R.id.mobileNo);
        nationalId = (EditText) findViewById(R.id.nationalId);
        email = (EditText) findViewById(R.id.email);
        employeer = (Spinner) findViewById(R.id.select_employer);
        terms = (CheckBox) findViewById(R.id.checkBox);

        sharedPref = getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        String code = "968";
        mobileNo.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code, AboutYourselfActivity.this), null, null, null);
//        mobileNo.setCompoundDrawablePadding(code.length()*10);

        String savedName = sharedPref.getString(Constants.FULL_NAME, "");
        String savedAge = sharedPref.getString(Constants.AGE, "");
        String savedMobile = sharedPref.getString(Constants.MOBILE_NUMBER, "");
        String savedEmail = sharedPref.getString(Constants.EMAIL, "");
        String savedNationalID = sharedPref.getString(Constants.NATIONAL_ID, "");
        if (!savedMobile.equalsIgnoreCase("") && savedMobile.length() > 3) {
            savedMobile = savedMobile.substring(3);
        }
        fullName.setText(savedName);
        age.setText(savedAge);
        mobileNo.setText(savedMobile);
        nationalId.setText(savedNationalID);

        employerList = new ArrayList<>();
        employerList.add("Select Employer");
        employerList.add("Al Mamary");
        employerList.add("ISS");
        employerList.add("MOD");
        employerList.add("Muscat Pharmacy");
        employerList.add("Private and Trade");
        employerList.add("ROP");
        employerList.add("Royal Office");
        employerList.add("Other/None");

        email.setText(savedEmail);
        if (employerList != null && employerList.size() > 0) {
            SpinnerAdapterEmployeer yomAdapter = new SpinnerAdapterEmployeer(this, 0, employerList);
            employeer.setAdapter(yomAdapter);
        }
        employeer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedEmployer = employerList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullNameText = fullName.getText().toString();
                ageText = age.getText().toString();
                String mobileNoText = mobileNo.getText().toString();
                String emailText = email.getText().toString();
                String nationalIdText = nationalId.getText().toString();
                if (fullNameText.equalsIgnoreCase("") || ageText.equalsIgnoreCase("") || mobileNoText.equalsIgnoreCase("") || emailText.equalsIgnoreCase("")
                        || nationalIdText.equalsIgnoreCase("")) {
                    Toast.makeText(AboutYourselfActivity.this, "All fields are mandatory",
                            Toast.LENGTH_SHORT).show();
                } else if(!checkEmail(emailText)){
                    Toast.makeText(AboutYourselfActivity.this,"You have entered an invalid email address. Please try again.",Toast.LENGTH_LONG).show();
                } else if (!isValidMobile(mobileNoText)) {
                    Toast.makeText(AboutYourselfActivity.this, "Please enter valid mobile number",
                            Toast.LENGTH_SHORT).show();
                } else if (!terms.isChecked()) {
                    Toast.makeText(AboutYourselfActivity.this, "Please agree to terms and condition",
                            Toast.LENGTH_SHORT).show();
                } else if (selectedEmployer.equalsIgnoreCase("Select Employer")) {
                    Toast.makeText(AboutYourselfActivity.this, "Please Select Employeer",
                            Toast.LENGTH_SHORT).show();
                } else if (selectedEmployer.equalsIgnoreCase("Other/None")) {
                    sendUser();
                } else {
                    String popUpMessage = AboutYourselfActivity.this.getString(R.string.contact_rms_error) + " " + selectedEmployer + " " + AboutYourselfActivity.this.getString(R.string.contact_rms_error1);
                    new AlertDialog.Builder(AboutYourselfActivity.this)
                            .setTitle("")
                            .setMessage(popUpMessage)

                            .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= 23) {
                                        // Marshmallow+
                                        if (!checkCallPhonePermission() || !checkReadStatePermission()) {
                                            requestPermission();
                                        } else {
                                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                                            callIntent.setData(Uri.parse("tel:+96824762679"));
                                            startActivity(callIntent);
                                        }
                                    } else {
                                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                                        callIntent.setData(Uri.parse("tel:+96824762679"));
                                        startActivity(callIntent);
                                    }
                                }
                            })
                            .setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sendUser();
                                }
                            })
                            .setCancelable(true).show();
                }
            }
        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]
                        {Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE},
                PERMISSION_REQUEST_CODE);
    }


    private boolean checkCallPhonePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkReadStatePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String
            permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:+96824762679"));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(intent);
                }
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            String fullNameText = fullName.getText().toString();
            ageText = age.getText().toString();
            String mobileNoText = mobileNo.getText().toString();
            mobileNoText = "968" + mobileNoText;
            String emailText = email.getText().toString();
            String nationalIdText = nationalId.getText().toString();
            editor.putString(Constants.FULL_NAME, fullNameText);
            editor.putString(Constants.AGE, ageText);
            editor.putString(Constants.MOBILE_NUMBER, mobileNoText);
            editor.putString(Constants.NATIONAL_ID, nationalIdText);
            editor.putString(Constants.EMAIL, emailText);
            editor.apply();

            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendUser() {
        String fullNameText = fullName.getText().toString();
        ageText = age.getText().toString();
        String mobileNoText = mobileNo.getText().toString();
        String emailText = email.getText().toString();
        String nationalIdText = nationalId.getText().toString();
        if (fullNameText.equalsIgnoreCase("") || ageText.equalsIgnoreCase("") || mobileNoText.equalsIgnoreCase("") || emailText.equalsIgnoreCase("")
                || nationalIdText.equalsIgnoreCase("")) {
            Toast.makeText(AboutYourselfActivity.this, "All fields are mandatory",
                    Toast.LENGTH_SHORT).show();
        } else if(!checkEmail(emailText)){
            Toast.makeText(AboutYourselfActivity.this,"You have entered an invalid email address. Please try again.",Toast.LENGTH_LONG).show();
        } else if (!isValidMobile(mobileNoText)) {
            Toast.makeText(AboutYourselfActivity.this, "Please enter valid mobile number",
                    Toast.LENGTH_SHORT).show();
        } else if (!terms.isChecked()) {
            Toast.makeText(AboutYourselfActivity.this, "Please agree to terms and condition",
                    Toast.LENGTH_SHORT).show();
        } else if (selectedEmployer.equalsIgnoreCase("Select Employer")) {
            Toast.makeText(AboutYourselfActivity.this, "Please Select Employeer",
                    Toast.LENGTH_SHORT).show();
        } else {
            selectedEmployer = "Other/None";
            mobileNoText = "968" + mobileNoText;
            editor.putString(Constants.FULL_NAME, fullNameText);
            editor.putString(Constants.AGE, ageText);
            editor.putString(Constants.MOBILE_NUMBER, mobileNoText);
            editor.putString(Constants.NATIONAL_ID, nationalIdText);
            editor.putString(Constants.EMAIL, emailText);
            editor.putString(Constants.EMPLOYER, selectedEmployer);
            editor.apply();

            MainActivity.name.setText(fullNameText);
            MainActivity.phone.setText(mobileNoText);

            String VEHICLE_USAGE = sharedPref.getString(Constants.VEHICLE_USAGE, "");
            String VEHICLE_TYPE = sharedPref.getString(Constants.VEHICLE_TYPE, "");
            String VEHICLE_MAKE = sharedPref.getString(Constants.VEHICLE_MAKE, "");
            String MODEL = sharedPref.getString(Constants.VEHICLE_MODEL, "");
            String VEHICLE_VARIANT = sharedPref.getString(Constants.VEHICLE_VARIANT, "");
            String RTO = sharedPref.getString(Constants.RTO, "");
            String YOM = sharedPref.getString(Constants.YOM, "");
            String REGISTRATION_DATE = sharedPref.getString(Constants.REGISTRATION_DATE, "");
            String POLICY_START_DATE = sharedPref.getString(Constants.POLICY_START_DATE, "");
            String POLICY_END_DATE = sharedPref.getString(Constants.POLICY_END_DATE, "");
            String PRICE = sharedPref.getString(Constants.PRICE, "");

            //Previous Policy End Date Calculation Start
            String startDate = POLICY_START_DATE.substring(0, 2);
            String startMonth = POLICY_START_DATE.substring(3, 5);
            String startYear = POLICY_START_DATE.substring(6, 10);

            String prePolicyYear = "";
            String prePolicyMonth = "";
            String prePolicyDate = "";
            if (startDate.equalsIgnoreCase("01") && startMonth.equalsIgnoreCase("01")) {
                prePolicyYear = String.valueOf(Integer.parseInt(startYear) - 1);
                prePolicyMonth = "12";
                prePolicyDate = "31";
            } else if (startDate.equalsIgnoreCase("01")) {
                prePolicyYear = startYear;
                prePolicyMonth = String.valueOf(Integer.parseInt(startMonth) - 1);
                if (prePolicyMonth.length() == 1) {
                    prePolicyMonth = "0" + prePolicyMonth;
                }
                if (prePolicyMonth.equalsIgnoreCase("03") || prePolicyMonth.equalsIgnoreCase("05") || prePolicyMonth.equalsIgnoreCase("07")
                        || prePolicyMonth.equalsIgnoreCase("08") || prePolicyMonth.equalsIgnoreCase("10") || prePolicyMonth.equalsIgnoreCase("12")) {
                    prePolicyDate = "31";
                } else if (prePolicyMonth.equalsIgnoreCase("04") || prePolicyMonth.equalsIgnoreCase("06") || prePolicyMonth.equalsIgnoreCase("09")
                        || prePolicyMonth.equalsIgnoreCase("11")) {
                    prePolicyDate = "30";
                } else if (prePolicyMonth.equalsIgnoreCase("02")) {
                    if (Integer.parseInt(prePolicyYear) % 4 == 0) {
                        prePolicyDate = "29";
                    } else {
                        prePolicyDate = "28";
                    }
                }
            } else {
                prePolicyYear = startYear;
                prePolicyDate = String.valueOf(Integer.parseInt(startDate) - 1);
                prePolicyMonth = startMonth;
            }


            if (prePolicyDate.length() == 1) {
                prePolicyDate = "0" + prePolicyDate;
            }
            if (prePolicyMonth.length() == 1) {
                prePolicyMonth = "0" + prePolicyMonth;
            }

            String prePolicyEndYear = prePolicyYear + "-" + prePolicyMonth + "-" + prePolicyDate;
            editor.putString(Constants.PRE_POLICY_END_DATE, prePolicyEndYear);
            editor.apply();
            //Previous Policy End Date Calculation Completed

            //Vehicle Age Calculation Start
            int currentyear = Calendar.getInstance().get(Calendar.YEAR);
            String vehicleRegYear = REGISTRATION_DATE.substring(6, 10);
            String vehicleAge = String.valueOf(currentyear - Integer.parseInt(vehicleRegYear));
            editor.putString(Constants.VEHICLE_AGE, vehicleAge);
            editor.apply();
            //Vehicle Age Calculation End

            sendUserInfo("0", fullNameText, ageText, mobileNoText, nationalIdText, emailText, selectedEmployer, "Incomplete", "0", VEHICLE_USAGE, VEHICLE_TYPE, VEHICLE_MAKE, MODEL,
                    VEHICLE_VARIANT, RTO, YOM, PRICE, REGISTRATION_DATE, POLICY_START_DATE, POLICY_END_DATE, vehicleAge, "0");
        }

    }

    public boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    private boolean isValidMobile(String phone) {
        boolean check=false;
        String number = phone;
//        if (phone.contains("968") || phone.contains("971") ) {
//            number = phone.substring(3);
//        }
//        if (number.substring(0,1).equalsIgnoreCase("0")) {
//            number = number.substring(1);
//        }
        int length = number.length();
        if(length > 12) {
            check = false;
        } else {
            check = true;
        }
        return check;
    }

    public void sendUserInfo(final String id, final String name, final String age, final String mobile_no, final String national_id, final String email, final String employeer, final String status,
                             final String company_id, final String vehicle_usage, final String usage_type, final String make, final String modal, final String variant, final String rto,
                             final String yom, final String price, final String registration_date, final String polciy_start_date, final String policy_end_date, final String vehicle_age,
                             final String premium) {
        try {
            AsyncServiceCall _companyDetails = new AsyncServiceCall() {
                String userId = id;
                @Override
                protected void onPreExecute() {
                    progressDialogReset = ProgressDialog.show(
                            AboutYourselfActivity.this,
                            "Getting Quotes",
                            AboutYourselfActivity.this.getResources().getString(
                                    R.string.progress_text));
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    ArrayList<UserInfo> userInfoArrayList = new ArrayList<>();
                    userInfoArrayList = _dbManager.getUserInfo();
                    if (userInfoArrayList != null) {
                        if (userInfoArrayList.size() > 0) {
                            String userIdFromDb = _dbManager.getUserID(national_id);
                            if (!userIdFromDb.equalsIgnoreCase("")){
                                userId = userIdFromDb;
                            }

                        }
                    }

                    UserInfo userInfo = new UserInfo();
                    userInfo.setId(userId);
                    userInfo.setFullName(name);
                    userInfo.setAge(age);
                    userInfo.setMobileNo(mobile_no);
                    userInfo.setNationalId(national_id);
                    userInfo.setEmail(email);
                    userInfo.setEmployer(employeer);
                    userInfo.setStatus(status);
                    userInfo.setCompanyId(company_id);
                    userInfo.setVehicleUsage(vehicle_usage);
                    userInfo.setUsageType(usage_type);
                    userInfo.setMake(make);
                    userInfo.setModal(modal);
                    userInfo.setVariant(variant);
                    userInfo.setRto(rto);
                    userInfo.setYom(yom);
                    userInfo.setPrice(price);
                    userInfo.setRegistrationDate(registration_date);
                    _dbManager.insertUserInfo(userInfo);

                    ArrayList<String> stringArrayList = new ArrayList<>();
                    return _webServiceManager.sendUserInformation(userId, name, age, mobile_no, national_id, email, employeer, status, company_id,
                            vehicle_usage, usage_type, make, modal, variant, rto, yom, price, registration_date, polciy_start_date, policy_end_date, premium, vehicle_age, stringArrayList);
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    String result = (String) resultObj;

                    if (!result.equalsIgnoreCase("") || !result.equalsIgnoreCase(" []")) {
                        JSONObject smsResp = null;
                        try {
                            smsResp = new JSONObject(result);
                            String status = smsResp
                                    .getString("status");
                            String id = smsResp
                                    .getString("id");

                            if (status.equalsIgnoreCase("success")) {
                                if (userId.equalsIgnoreCase("0")) {
                                    _dbManager.updateUserID(id, national_id);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    String VEHICLE_USAGEID = sharedPref.getString(Constants.VEHICLE_USAGEID, "");
                    String VEHICLE_TYPEID = sharedPref.getString(Constants.VEHICLE_TYPEID, "");
                    String VEHICLE_MAKEID = sharedPref.getString(Constants.VEHICLE_MAKEID, "");
                    String VEHICLE_MODELID = sharedPref.getString(Constants.VEHICLE_MODELID, "");
                    String VEHICLE_VARIANTID = sharedPref.getString(Constants.VEHICLE_VARIANTID, "");
                    String RTOID = sharedPref.getString(Constants.RTOID, "");
                    String PRICE = sharedPref.getString(Constants.PRICE, "");
                    String REGISTRATION_DATE = sharedPref.getString(Constants.REGISTRATION_DATE, "");
                    String POLICY_START_DATE = sharedPref.getString(Constants.POLICY_START_DATE, "");
                    String PRE_POLICY_END_DATE = sharedPref.getString(Constants.PRE_POLICY_END_DATE, "");
                    String VEHICLE_AGE = sharedPref.getString(Constants.VEHICLE_AGE, "");

                    //Date Formatting Start
                    Date curDate = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    String todayDate = format.format(curDate);
                    String currentYear = todayDate.substring(0,4);
                    System.out.println(todayDate);

                    String inputPattern = "dd-MM-yyyy";
                    String outputPattern = "yyyy-MM-dd";
                    SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
                    SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);

                    Date date = null;
                    Date policydate = null;
                    String registrationDate = null;
                    String policyStartDate = null;

                    try {
                        date = inputFormat.parse(REGISTRATION_DATE);
                        policydate = inputFormat.parse(POLICY_START_DATE);
                        registrationDate = outputFormat.format(date);
                        policyStartDate = outputFormat.format(policydate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //Date Formatting End

                    getCompanyDetails(VEHICLE_USAGEID, VEHICLE_TYPEID, VEHICLE_MAKEID, VEHICLE_MODELID, VEHICLE_VARIANTID, registrationDate, VEHICLE_AGE, PRICE,
                            policyStartDate, PRE_POLICY_END_DATE, "N", "0", RTOID, yom, ageText);

                    super.onPostExecute(result);
                }

            };
            try {

                if (NetworkManager.isNetAvailable(AboutYourselfActivity.this)) {
                    _companyDetails.execute(0);
                } else {
                    showNoNetworkAlert();
                }
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMSAGG, "Exception is " + Log.getStackTraceString(ex));
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_RMSAGG, "Exception is " + Log.getStackTraceString(e));
        }
    }


    public void getCompanyDetails(final String usageId, final String vehicleId, final String makeId, final String modelId, final String variantId, final String regDate, final String vehicleAge,
                                  final String price, final String policyStartDate, final String prePolicyEndDate, final String claim, final String numClaims, final String rtoId, final String yom, final String insuredAge) {
        try {
            AsyncServiceCall _companyDetails = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    return _webServiceManager.getCompanyDetails(usageId, vehicleId, makeId, modelId, variantId, regDate, vehicleAge,
                            price, policyStartDate, prePolicyEndDate, claim, numClaims, rtoId, yom, insuredAge);
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    String result = (String) resultObj;
                    if (result.equalsIgnoreCase("Updated")) {
                        if ((progressDialogReset != null) && (progressDialogReset.isShowing())) {
                            try {
                                progressDialogReset.dismiss();
                            } catch (Exception ex) {
                                Log.e(Constants.LOG_RMSAGG, "Exception is " + Log.getStackTraceString(ex));
                            }
                        }
                        ArrayList<CompanyCover> companyCoverList = _dbManager.getComanyCover();
                        ArrayList<String> companyCoverNames = new ArrayList<>();
                        ArrayList<String> companyCoverID = new ArrayList<>();
                        for (int i = 0; i < companyCoverList.size(); i++) {
                            boolean isExist = false;
                            if (i == 0) {
                                String coverName = companyCoverList.get(i).getCoverName();
                                String coverID = companyCoverList.get(i).getCoverId();
                                companyCoverNames.add(coverName);
                                companyCoverID.add(coverID);
                            } else {
                                for (int j = 0; j < companyCoverID.size(); j++) {
                                    String savedCoverID = companyCoverID.get(j);
                                    if (savedCoverID.equalsIgnoreCase(companyCoverList.get(i).getCoverId())) {
                                        isExist = true;
                                        break;
                                    }
                                }
                                String coverID = companyCoverList.get(i).getCoverId();
                                companyCoverID.add(coverID);
                                if (!isExist) {
                                    String coverName = companyCoverList.get(i).getCoverName();
                                    companyCoverNames.add(coverName);
                                }
                            }
                        }
                        Intent apply = new Intent(AboutYourselfActivity.this, CompareActivity.class);
                        apply.putStringArrayListExtra("companyCoverNames", companyCoverNames);
                        startActivity(apply);
                    } else if (result.equalsIgnoreCase("NoDataAvailable")) {
                        if ((progressDialogReset != null) && (progressDialogReset.isShowing())) {
                            try {
                                progressDialogReset.dismiss();
                            } catch (Exception ex) {
                                Log.e(Constants.LOG_RMSAGG, "Exception is " + Log.getStackTraceString(ex));
                            }
                        }
                        new AlertDialog.Builder(AboutYourselfActivity.this)
                                .setTitle("Alert")
                                .setMessage(AboutYourselfActivity.this.getString(R.string.no_data_error))

                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setCancelable(true).show();
                    } else if (result.equalsIgnoreCase("Error")) {
                        if ((progressDialogReset != null) && (progressDialogReset.isShowing())) {
                            try {
                                progressDialogReset.dismiss();
                            } catch (Exception ex) {
                                Log.e(Constants.LOG_RMSAGG, "Exception is " + Log.getStackTraceString(ex));
                            }
                        }
                        new AlertDialog.Builder(AboutYourselfActivity.this)
                                .setTitle("Alert")
                                .setMessage(errorMessage)

                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setCancelable(true).show();
                    } else {
                        if ((progressDialogReset != null) && (progressDialogReset.isShowing())) {
                            try {
                                progressDialogReset.dismiss();
                            } catch (Exception ex) {
                                Log.e(Constants.LOG_RMSAGG, "Exception is " + Log.getStackTraceString(ex));
                            }
                        }
                        new AlertDialog.Builder(AboutYourselfActivity.this)
                                .setTitle("Alert")
                                .setMessage(AboutYourselfActivity.this.getString(R.string.network_failure))

                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setCancelable(true).show();
                    }
                    super.onPostExecute(result);
                }

            };
            try {

                if (NetworkManager.isNetAvailable(AboutYourselfActivity.this)) {
                    _companyDetails.execute(0);
                } else {
                    showNoNetworkAlert();
                }
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMSAGG, "Exception is " + Log.getStackTraceString(ex));
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_RMSAGG, "Exception is " + Log.getStackTraceString(e));
        }
    }

    private void showNoNetworkAlert() {

        if ((progressDialogReset != null) && (progressDialogReset.isShowing())) {
            try {
                progressDialogReset.dismiss();
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMSAGG, "Exception is " + Log.getStackTraceString(ex));
            }
        }
        new AlertDialog.Builder(AboutYourselfActivity.this)
                .setTitle(AboutYourselfActivity.this.getString(R.string.network_availability_heading))
                .setMessage(AboutYourselfActivity.this.getString(R.string.network_availability))

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true).show();

    }
}
