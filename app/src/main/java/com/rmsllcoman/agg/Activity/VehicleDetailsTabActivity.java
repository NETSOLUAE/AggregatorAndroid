package com.rmsllcoman.agg.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.rmsllcoman.agg.other.Constants;

import com.rmsllcoman.agg.R;

/**
 * Created by macmini on 7/22/17.
 */

public class VehicleDetailsTabActivity extends AppCompatActivity {

    TextView usage;
    TextView type;
    TextView make;
    TextView model;
    TextView variant;
    TextView rto;
    TextView regDate;
    TextView price;
    TextView policyType;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicledetails_tab);
        getSupportActionBar().hide();

        usage = (TextView) findViewById(R.id.vehicle_tab_usage);
        type = (TextView) findViewById(R.id.vehicle_tab_type);
        make = (TextView) findViewById(R.id.vehicle_tab_make);
        model = (TextView) findViewById(R.id.vehicle_tab_model);
        variant = (TextView) findViewById(R.id.vehicle_tab_variant);
        rto = (TextView) findViewById(R.id.vehicle_tab_place);
        regDate = (TextView) findViewById(R.id.vehicle_tab_regdate);
        price = (TextView) findViewById(R.id.vehicle_tab_price);
        policyType = (TextView) findViewById(R.id.vehicle_tab_policy_type);

        sharedPref = this.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);

        String VEHICLE_USAGE = sharedPref.getString(Constants.VEHICLE_USAGE, "");
        String VEHICLE_TYPE = sharedPref.getString(Constants.VEHICLE_TYPE, "");
        String VEHICLE_MAKE = sharedPref.getString(Constants.VEHICLE_MAKE, "");
        String MODEL = sharedPref.getString(Constants.VEHICLE_MODEL, "");
        String VEHICLE_VARIANT = sharedPref.getString(Constants.VEHICLE_VARIANT, "");
        String RTO = sharedPref.getString(Constants.RTO, "");
        String REGISTRATION_DATE = sharedPref.getString(Constants.REGISTRATION_DATE, "");
        String PRICE = sharedPref.getString(Constants.PRICE, "");
        String CAR_TYPE = sharedPref.getString(Constants.CAR_TYPE, "");

        usage.setText(String.format("Vehicle Usage: %s", VEHICLE_USAGE));
        type.setText(String.format("Vehicle Type: %s", VEHICLE_TYPE));
        make.setText(String.format("Vehicle Make: %s", VEHICLE_MAKE));
        model.setText(String.format("Vehicle Model: %s", MODEL));
        variant.setText(String.format("Vehicle Variant: %s", VEHICLE_VARIANT));
        rto.setText(String.format("Place of Registration: %s", RTO));
        regDate.setText(String.format("Date of Registration: %s", REGISTRATION_DATE));
        price.setText(String.format("Price: %s", PRICE));
        policyType.setText(String.format("Policy Type: %s", CAR_TYPE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("******ONPAUSE*******", "*****************");
    }

    @Override
    public void onResume() {
        super.onResume();
        String CAR_TYPE = sharedPref.getString(Constants.CAR_TYPE, "");
        Log.d("******ONRESUME*******", CAR_TYPE);
        policyType.setText(String.format("Policy Type: %s", CAR_TYPE));
    }
}
