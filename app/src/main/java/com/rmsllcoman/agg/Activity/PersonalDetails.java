package com.rmsllcoman.agg.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.rmsllcoman.agg.R;
import com.rmsllcoman.agg.other.Constants;

/**
 * Created by macmini on 7/22/17.
 */

public class PersonalDetails extends AppCompatActivity {

    TextView name;
    TextView age;
    TextView mobileNo;
    TextView nationalId;
    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        getSupportActionBar().hide();

        name = (TextView) findViewById(R.id.vehicle_tab_name);
        age = (TextView) findViewById(R.id.vehicle_tab_age);
        mobileNo = (TextView) findViewById(R.id.vehicle_tab_mobile);
        nationalId = (TextView) findViewById(R.id.vehicle_tab_nationalId);
        email = (TextView) findViewById(R.id.vehicle_tab_email);

        SharedPreferences sharedPref = this.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);

        String savedName = sharedPref.getString(Constants.FULL_NAME, "");
        String savedAge = sharedPref.getString(Constants.AGE, "");
        String savedMobile = sharedPref.getString(Constants.MOBILE_NUMBER, "");
        String savedEmail = sharedPref.getString(Constants.EMAIL, "");
        String savedNationalID = sharedPref.getString(Constants.NATIONAL_ID, "");

        name.setText(String.format("Name: %s", savedName));
        age.setText(String.format("Age: %s", savedAge));
        mobileNo.setText(String.format("Mobile No: %s", savedMobile));
        nationalId.setText(String.format("National ID: %s", savedNationalID));
        email.setText(String.format("Email: %s", savedEmail));
    }
}