package com.rmsllcoman.agg.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rmsllcoman.agg.other.Constants;

import java.util.ArrayList;

import com.rmsllcoman.agg.R;

/**
 * Created by macmini on 7/26/17.
 */

public class ReviewActivity extends AppCompatActivity {
    LinearLayout selectedImageLayout;
//    ImageView companyLogo;
    TextView companyName;
    TextView policyStart;
    TextView policyEnd;
    TextView name;
    TextView age;
    TextView mobile;
    TextView nationalId;
    TextView email;
    TextView address;
    TextView usage;
    TextView type;
    TextView make;
    TextView model;
    TextView variant;
    TextView regPlace;
    TextView regDate;
    TextView price;
    TextView review_idv;
    Button makePayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Make Payment");

        selectedImageLayout = (LinearLayout) findViewById(R.id.linear2);
        makePayment = (Button) findViewById(R.id.button_pay_amount);
//        companyLogo = (ImageView) findViewById(R.id.review_company_logo);
        companyName = (TextView) findViewById(R.id.review_company_name);
        policyStart = (TextView) findViewById(R.id.review_policy_start);
        policyEnd = (TextView) findViewById(R.id.review_policy_end);
        name = (TextView) findViewById(R.id.review_name);
        age = (TextView) findViewById(R.id.review_age);
        mobile = (TextView) findViewById(R.id.review_mobileNo);
        nationalId = (TextView) findViewById(R.id.review_nationalID);
        email = (TextView) findViewById(R.id.review_email);
        address = (TextView) findViewById(R.id.review_address);
        usage = (TextView) findViewById(R.id.review_vehicle_usage);
        type = (TextView) findViewById(R.id.review_type);
        make = (TextView) findViewById(R.id.review_make);
        model = (TextView) findViewById(R.id.review_model);
        variant = (TextView) findViewById(R.id.review_variant);
        regPlace = (TextView) findViewById(R.id.review_reg_place);
        regDate = (TextView) findViewById(R.id.review_reg_date);
        price = (TextView) findViewById(R.id.review_price);
        review_idv = (TextView) findViewById(R.id.review_idv);

        SharedPreferences sharedPref = this.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);

//        int savedcompanyLogo = sharedPref.getInt(Constants.COMPANY_LOGO, 1);
        String savedcompanyName = sharedPref.getString(Constants.COMPANY_NAME, "");
        String savedpolicyStart = sharedPref.getString(Constants.POLICY_START_DATE, "");
        String savedpolicyEnd = sharedPref.getString(Constants.POLICY_END_DATE, "");

        String savedName = sharedPref.getString(Constants.FULL_NAME, "");
        String savedAge = sharedPref.getString(Constants.AGE, "");
        String savedMobile = sharedPref.getString(Constants.MOBILE_NUMBER, "");
        String savedEmail = sharedPref.getString(Constants.EMAIL, "");
        String savedNationalID = sharedPref.getString(Constants.NATIONAL_ID, "");
        String savedAddress = sharedPref.getString(Constants.ADDRESS, "");

        String VEHICLE_USAGE = sharedPref.getString(Constants.VEHICLE_USAGE, "");
        String VEHICLE_TYPE = sharedPref.getString(Constants.VEHICLE_TYPE, "");
        String VEHICLE_MAKE = sharedPref.getString(Constants.VEHICLE_MAKE, "");
        String MODEL = sharedPref.getString(Constants.VEHICLE_MODEL, "");
        String VEHICLE_VARIANT = sharedPref.getString(Constants.VEHICLE_VARIANT, "");
        String RTO = sharedPref.getString(Constants.RTO, "");
        String REGISTRATION_DATE = sharedPref.getString(Constants.REGISTRATION_DATE, "");
        String PRICE = sharedPref.getString(Constants.PRICE, "");
        String TOTAL_PREMIUM = sharedPref.getString(Constants.TOTAL_PREMIUM, "");

        companyName.setText(savedcompanyName);
        policyStart.setText(String.format("Policy Start: %s", savedpolicyStart));
        policyEnd.setText(String.format("Policy End: %s", savedpolicyEnd));
//        name.setText(savedName);

        StyleSpan bold = new StyleSpan(android.graphics.Typeface.BOLD);

        SpannableStringBuilder nameText = new SpannableStringBuilder("Name: " + savedName);
        nameText.setSpan(bold, 0, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        name.setText(nameText);

        SpannableStringBuilder ageText = new SpannableStringBuilder("Age: " + savedAge);
        ageText.setSpan(bold, 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        age.setText(ageText);

        SpannableStringBuilder mobileNoText = new SpannableStringBuilder("Mobile No: " + savedMobile);
        mobileNoText.setSpan(bold, 0, 10, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mobile.setText(mobileNoText);

        SpannableStringBuilder emailText = new SpannableStringBuilder("Email: " + savedEmail);
        emailText.setSpan(bold, 0, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        email.setText(emailText);

        SpannableStringBuilder nationalIdText = new SpannableStringBuilder("National ID: " + savedNationalID);
        nationalIdText.setSpan(bold, 0, 11, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        nationalId.setText(nationalIdText);

        if (savedAddress.equalsIgnoreCase("")) {
            address.setVisibility(View.GONE);
        } else {
            address.setVisibility(View.VISIBLE);
            address.setText(savedAddress);
        }
//        usage.setText(VEHICLE_USAGE);

        SpannableStringBuilder vehicleUsageText = new SpannableStringBuilder("Vehicle Usage: " + VEHICLE_USAGE);
        vehicleUsageText.setSpan(bold, 0, 12, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        usage.setText(vehicleUsageText);

        SpannableStringBuilder vehicleTypeText = new SpannableStringBuilder("Vehicle Type: " + VEHICLE_TYPE);
        vehicleTypeText.setSpan(bold, 0, 12, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        type.setText(vehicleTypeText);

        SpannableStringBuilder vehicleMakeText = new SpannableStringBuilder("Vehicle Make: " + VEHICLE_MAKE);
        vehicleMakeText.setSpan(bold, 0, 12, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        make.setText(vehicleMakeText);

        SpannableStringBuilder vehicleModelText = new SpannableStringBuilder("Vehicle Model: " + MODEL);
        vehicleModelText.setSpan(bold, 0, 13, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        model.setText(vehicleModelText);

        SpannableStringBuilder vehicleVaraiantText = new SpannableStringBuilder("Vehicle Variant: " + VEHICLE_VARIANT);
        vehicleVaraiantText.setSpan(bold, 0, 16, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        variant.setText(vehicleVaraiantText);

        SpannableStringBuilder vehicleRtoText = new SpannableStringBuilder("Place of Registration: " + RTO);
        vehicleRtoText.setSpan(bold, 0, 21, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        regPlace.setText(vehicleRtoText);

        SpannableStringBuilder vehicleRegText = new SpannableStringBuilder("Date of Registration: " + REGISTRATION_DATE);
        vehicleRegText.setSpan(bold, 0, 20, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        regDate.setText(vehicleRegText);

        SpannableStringBuilder priceText = new SpannableStringBuilder("Price: " + PRICE + " OMR");
        priceText.setSpan(bold, 0, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        price.setText(priceText);

        review_idv.setText(String.format("%s OMR", TOTAL_PREMIUM));


        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null) {
            Bitmap add = BitmapFactory.decodeResource(
                    getResources(), R.drawable.button_add);
            int height = add.getHeight();
            int width = add.getWidth();
            ArrayList<String> imagesList = b.getStringArrayList("selectedItems");
            for (int i = 0; i < imagesList.size(); i++) {
                ImageView imageView = new ImageView(this);
                imageView.setId(i);
                imageView.setPadding(2, 2, 2, 2);
                Glide.with(ReviewActivity.this)
                        .load("file://"+imagesList.get(i))
                        .crossFade()
                        .placeholder(R.drawable.preview)
                        .into(imageView);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                selectedImageLayout.addView(imageView);

                imageView.requestLayout();
                imageView.getLayoutParams().height = height;
                imageView.getLayoutParams().width = width;
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }

        makePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent apply = new Intent(ReviewActivity.this, PaymentOptionActivity.class);
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