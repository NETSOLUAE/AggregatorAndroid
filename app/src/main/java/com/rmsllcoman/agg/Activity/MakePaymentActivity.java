package com.rmsllcoman.agg.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rmsllcoman.agg.R;
import com.rmsllcoman.agg.other.AsyncServiceCall;
import com.rmsllcoman.agg.other.Constants;
import com.rmsllcoman.agg.Controller.DatabaseManager;
import com.rmsllcoman.agg.Controller.NetworkManager;
import com.rmsllcoman.agg.Controller.WebserviceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by macmini on 7/26/17.
 */

public class MakePaymentActivity extends AppCompatActivity {
    TextView totalPremium;
    EditText cardNo;
    EditText expiry;
    EditText cvv;
    EditText name;
    Button paySecurely;
    SharedPreferences sharedPref;
    DatabaseManager databaseManager;
    private WebserviceManager _webServiceManager;
    public static ProgressDialog progressDialogReset;

    boolean isSlash = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Make Payment");

        paySecurely = (Button) findViewById(R.id.button_pay_securely);
        cardNo = (EditText) findViewById(R.id.card_no);
        expiry = (EditText) findViewById(R.id.card_expiry);
        cvv = (EditText) findViewById(R.id.card_cvv);
        name = (EditText) findViewById(R.id.card_name);
        totalPremium = (TextView) findViewById(R.id.total_premium_make_payment);

        sharedPref = this.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        String TOTAL_PREMIUM = sharedPref.getString(Constants.TOTAL_PREMIUM, "");
        totalPremium.setText(String.format("%s OMR", TOTAL_PREMIUM));

        _webServiceManager = new WebserviceManager(this);
        databaseManager = new DatabaseManager(this);

        cardNo.addTextChangedListener(new TextWatcher() {
            private static final char space = ' ';

            @Override
            public void afterTextChanged(Editable s) {

                // Remove spacing char
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (space == c) {
                        s.delete(s.length() - 1, s.length());
                    }
                }
                // Insert char where needed.
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    char c = s.charAt(s.length() - 1);
                    // Only if its a digit where there should be a space we insert a space
                    if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                        s.insert(s.length() - 1, String.valueOf(space));
                    }
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });

        expiry.addTextChangedListener(new TextWatcher() {
            private static final char space = ' ';

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    formatCardExpiringDate(s);
                }catch(NumberFormatException e){
                    s.clear();
                    Toast.makeText(MakePaymentActivity.this, "Please Enter Valid Date Format",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });

        paySecurely.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (saveCard.isChecked()) {
                    String cardNoText = cardNo.getText().toString();
                    String expiryText = expiry.getText().toString();
                    String cvvText = cvv.getText().toString();
                    String nameText = name.getText().toString();

//                    String expdate[] = expiryText.split("/");

                    if (cardNoText.equalsIgnoreCase("") || expiryText.equalsIgnoreCase("")
                            || cvvText.equalsIgnoreCase("") || nameText.equalsIgnoreCase("") ) {
                        Toast.makeText(MakePaymentActivity.this, "All fields are mandatory",
                                Toast.LENGTH_SHORT).show();
                    } else if (cvv.length() < 3) {
                        Toast.makeText(MakePaymentActivity.this, "Please Enter Valid CVV Number",
                                Toast.LENGTH_SHORT).show();
                    } else
                        if(expiryText.length() < 7){
                            Toast.makeText(MakePaymentActivity.this, "Please Enter Valid Expiry Date",
                                Toast.LENGTH_SHORT).show();
                    } else {

                            String FULL_NAME = sharedPref.getString(Constants.FULL_NAME, "");
                            String AGE = sharedPref.getString(Constants.AGE, "");
                            String MOBILE_NUMBER = sharedPref.getString(Constants.MOBILE_NUMBER, "");
                            String NATIONAL_ID = sharedPref.getString(Constants.NATIONAL_ID, "");
                            String EMAIL = sharedPref.getString(Constants.EMAIL, "");
                            String EMPLOYER = sharedPref.getString(Constants.EMPLOYER, "");
                            String VEHICLE_USAGE = sharedPref.getString(Constants.VEHICLE_USAGE, "");
                            String VEHICLE_TYPE = sharedPref.getString(Constants.VEHICLE_TYPE, "");
                            String VEHICLE_MAKE = sharedPref.getString(Constants.VEHICLE_MAKE, "");
                            String MODEL = sharedPref.getString(Constants.VEHICLE_MODEL, "");
                            String VEHICLE_VARIANT = sharedPref.getString(Constants.VEHICLE_VARIANT, "");
                            String RTO = sharedPref.getString(Constants.RTO, "");
                            String YOM = sharedPref.getString(Constants.YOM, "");
                            String REGISTRATION_DATE = sharedPref.getString(Constants.REGISTRATION_DATE, "");
                            String PRICE = sharedPref.getString(Constants.PRICE, "");
                            String COMPANY_PRODUCT_ID = sharedPref.getString(Constants.COMPANY_PRODUCT_ID, "");
                            String TOTAL_PREMIUM = sharedPref.getString(Constants.TOTAL_PREMIUM, "");
                            String POLICY_START_DATE = sharedPref.getString(Constants.POLICY_START_DATE, "");
                            String POLICY_END_DATE = sharedPref.getString(Constants.POLICY_END_DATE, "");
                            String VEHICLE_AGE = sharedPref.getString(Constants.VEHICLE_AGE, "");

                            String savedNationalID = sharedPref.getString(Constants.NATIONAL_ID, "");
                            String userIdFromDb = databaseManager.getUserID(savedNationalID);
                            sendUserInformation(userIdFromDb, FULL_NAME, AGE, MOBILE_NUMBER, NATIONAL_ID, EMAIL, EMPLOYER, "Complete", COMPANY_PRODUCT_ID, VEHICLE_USAGE, VEHICLE_TYPE, VEHICLE_MAKE, MODEL,
                                    VEHICLE_VARIANT, RTO, YOM, PRICE, REGISTRATION_DATE, POLICY_START_DATE, POLICY_END_DATE, VEHICLE_AGE, TOTAL_PREMIUM);
                    }
//                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void formatCardExpiringDate(Editable s){
        String input = s.toString();
        String mLastInput = "";

        SimpleDateFormat formatter = new SimpleDateFormat("MM/yy", Locale.ENGLISH);
        Calendar expiryDateDate = Calendar.getInstance();

        try {
            expiryDateDate.setTime(formatter.parse(input));
        } catch (ParseException e) {
            if (s.length() == 2 && !mLastInput.endsWith("/") && isSlash) {
                isSlash = false;
                int month = Integer.parseInt(input);
                if (month <= 12) {
                    expiry.setText(expiry.getText().toString().substring(0, 1));
                    expiry.setSelection(expiry.getText().toString().length());
                } else {
                    s.clear();
                    expiry.setText("");
                    expiry.setSelection(expiry.getText().toString().length());
                    Toast.makeText(MakePaymentActivity.this.getApplicationContext(), "Enter a valid month", Toast.LENGTH_LONG).show();
                }
            }else if (s.length() == 2 && !mLastInput.endsWith("/") && !isSlash) {
                isSlash = true;
                int month = Integer.parseInt(input);
                if (month <= 12) {
                    expiry.setText(String.format("%s/", expiry.getText().toString()));
                    expiry.setSelection(expiry.getText().toString().length());
                }else if(month > 12){
                    expiry.setText("");
                    expiry.setSelection(expiry.getText().toString().length());
                    s.clear();
                    Toast.makeText(MakePaymentActivity.this, "Invalid Month",
                            Toast.LENGTH_SHORT).show();
                }


            } else if (s.length() == 1) {

                int month = Integer.parseInt(input);
                if (month > 1 && month < 12) {
                    isSlash = true;
                    expiry.setText("0" + expiry.getText().toString() + "/");
                    expiry.setSelection(expiry.getText().toString().length());
                }
            }

            mLastInput = expiry.getText().toString();
            return;
        }
    }

    public void sendUserInformation(final String id, final String name, final String age, final String mobile_no, final String national_id, final String email, final String employer,
                                    final String status, final String company_id, final String vehicle_usage, final String usage_type, final String make, final String modal,
                                    final String variant, final String rto, final String yom, final String price, final String registration_date, final String policy_start_date,
                                    final String policy_end_date, final String vehicle_age, final String total_premium) {
        try {
            AsyncServiceCall _companyDetails = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialogReset = ProgressDialog.show(
                            MakePaymentActivity.this,
                            MakePaymentActivity.this.getResources().getString(
                                    R.string.progress_redirecting),
                            MakePaymentActivity.this.getResources().getString(
                                    R.string.progress_text));
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    ArrayList<String> stringArrayList = new ArrayList<>();
                    return _webServiceManager.sendUserInformation(id, name, age, mobile_no, national_id, email, employer, status, company_id,
                            vehicle_usage, usage_type, make, modal, variant, rto, yom, price, registration_date, policy_start_date,
                            policy_end_date, vehicle_age, total_premium, stringArrayList);
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

                            if (status.equalsIgnoreCase("success")) {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Intent apply = new Intent(MakePaymentActivity.this, ThankyouActivity.class);
                    startActivity(apply);
                }
            };
            try {

                if (NetworkManager.isNetAvailable(MakePaymentActivity.this)) {
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
        new AlertDialog.Builder(MakePaymentActivity.this)
                .setTitle(MakePaymentActivity.this.getString(R.string.network_availability_heading))
                .setMessage(MakePaymentActivity.this.getString(R.string.network_availability))

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true).show();

    }
}