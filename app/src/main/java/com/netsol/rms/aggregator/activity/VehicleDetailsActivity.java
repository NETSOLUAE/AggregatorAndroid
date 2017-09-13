package com.netsol.rms.aggregator.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.netsol.rms.aggregator.R;
import com.netsol.rms.aggregator.activity.other.Constants;

/**
 * Created by macmini on 7/26/17.
 */

public class VehicleDetailsActivity extends AppCompatActivity {

    Button continueVehicle;
    TextView carName;
    TextView carfuelType;
    TextView carmodel;
    TextView carRegistrationNo;
    TextView carRegistrationDate;
    EditText policyNo;
    EditText engineNo;
    EditText chassisNo;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        continueVehicle = (Button) findViewById(R.id.button_continue_vehicle);
        carName = (TextView) findViewById(R.id.vehicle_name);
        carfuelType = (TextView) findViewById(R.id.vehicle_fueltype);
        carmodel = (TextView) findViewById(R.id.vehicle_model);
        carRegistrationNo = (TextView) findViewById(R.id.car_reg_no);
        carRegistrationDate = (TextView) findViewById(R.id.car_reg_date);
        policyNo = (EditText) findViewById(R.id.vehicle_policy_no);
        engineNo = (EditText) findViewById(R.id.vehicle_car_engine_no);
        chassisNo = (EditText) findViewById(R.id.vehicle_car_chassis_no);

        sharedPref = this.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

//        String savedName = sharedPref.getString(Constants.CAR_BRAND, "");
//        String savedFuel = sharedPref.getString(Constants.FUEL_TYPE, "");
//        String savedModel = sharedPref.getString(Constants.MODEL, "");
        String savedRegNo = sharedPref.getString(Constants.RTO, "");
        String savedRegDate = sharedPref.getString(Constants.REGISTRATION_DATE, "");

//        carName.setText(savedName);
//        carfuelType.setText(String.format("Fuel Type: %s", savedFuel));
//        carmodel.setText(String.format("Model: %s", savedModel));
        carRegistrationNo.setText(savedRegNo);
        carRegistrationDate.setText(savedRegDate);

        continueVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString(Constants.POLICY_NUMBER, policyNo.getText().toString());
                editor.putString(Constants.ENGINE_NO, engineNo.getText().toString());
                editor.putString(Constants.CHASSIS_NO, chassisNo.getText().toString());
                editor.apply();
                Intent apply = new Intent(VehicleDetailsActivity.this, ReviewActivity.class);
                startActivity(apply);
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
}
