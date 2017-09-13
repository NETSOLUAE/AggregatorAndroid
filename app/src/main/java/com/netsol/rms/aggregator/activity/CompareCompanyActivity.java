package com.netsol.rms.aggregator.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netsol.rms.aggregator.R;
import com.netsol.rms.aggregator.activity.other.AsyncServiceCall;
import com.netsol.rms.aggregator.activity.other.Company;
import com.netsol.rms.aggregator.activity.other.CompanyCover;
import com.netsol.rms.aggregator.activity.other.CompanyDetails;
import com.netsol.rms.aggregator.activity.other.Constants;
import com.netsol.rms.aggregator.activity.other.DatabaseManager;
import com.netsol.rms.aggregator.activity.other.NetworkManager;
import com.netsol.rms.aggregator.activity.other.WebserviceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by macmini on 7/26/17.
 */

public class CompareCompanyActivity extends AppCompatActivity {

    ArrayList<Company> selectedcompanyArrayList;
    ArrayList<CompanyCover> selectedcompanyCoverArrayList;
    ArrayList<String> companyCoverNames;
    ArrayList<String> companyCoverIDUnique;
    Company selectedcompanyList;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    DatabaseManager databaseManager;
    private WebserviceManager _webServiceManager;
    public static ProgressDialog progressDialogReset;

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_compare);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linearLayout = (LinearLayout) findViewById(R.id.linear2);

        databaseManager = new DatabaseManager(this);
        _webServiceManager = new WebserviceManager(this);
        selectedcompanyArrayList = new ArrayList<>();
        selectedcompanyArrayList = Insurers.selectedcompanyArrayList;
        sharedPref = getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        if (selectedcompanyArrayList.size() > 1) {
            selectedcompanyCoverArrayList = new ArrayList<>();
            for (int i = 0; i < selectedcompanyArrayList.size(); i++) {
                ArrayList<CompanyCover> arraySize  = selectedcompanyArrayList.get(i).getCompanyCoverList();
                for (int j = 0; j < arraySize.size(); j++) {
                    selectedcompanyCoverArrayList.add(arraySize.get(j));
                }
            }
        }

        companyCoverNames = new ArrayList<>();
        companyCoverIDUnique = new ArrayList<>();
        ArrayList<String> companyCoverID = new ArrayList<>();
        for (int i = 0; i < selectedcompanyCoverArrayList.size(); i++) {
            boolean isExist = false;
            if (i == 0) {
                String coverName = selectedcompanyCoverArrayList.get(i).getCoverName();
                String coverID = selectedcompanyCoverArrayList.get(i).getCoverId();
                companyCoverNames.add(coverName);
                companyCoverIDUnique.add(coverID);
                companyCoverID.add(coverID);
            } else {
                for (int j = 0; j < companyCoverID.size(); j++) {
                    String savedCoverID = companyCoverID.get(j);
                    if (savedCoverID.equalsIgnoreCase(selectedcompanyCoverArrayList.get(i).getCoverId())) {
                        isExist = true;
                        break;
                    }
                }
                String coverID = selectedcompanyCoverArrayList.get(i).getCoverId();
                companyCoverID.add(coverID);
                if (!isExist) {
                    String coverName = selectedcompanyCoverArrayList.get(i).getCoverName();
                    String coverIDUnique = selectedcompanyCoverArrayList.get(i).getCoverId();
                    companyCoverNames.add(coverName);
                    companyCoverIDUnique.add(coverIDUnique);
                }
            }
        }

        for(int j = 0 ; j <= 7 ; j++) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View inflatedLayout= inflater.inflate(R.layout.comapny_compare_view, null, false);

            LinearLayout text_logo = (LinearLayout) inflatedLayout.findViewById(R.id.imageLogo);
            LinearLayout coverLayout = (LinearLayout) inflatedLayout.findViewById(R.id.coverLayout);

            for(int i = 0 ; i <= selectedcompanyArrayList.size(); i++) {

//                if (i == 0 && j == 0) {
//                    coverLayout.setVisibility(View.GONE);
//
//                    LinearLayout innerLinearLayout = new LinearLayout(this);
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                    TextView textView = new TextView(this);
//                    params.rightMargin = 10;
//                    params.leftMargin = 10;
//                    params.topMargin = 25;
//                    params.bottomMargin = 25;
//                    params.width = 200;
//                    textView.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
//                    textView.setGravity(Gravity.CENTER_VERTICAL);
//                    textView.setTextColor(Color.BLACK);
//                    textView.setText("Features");
//                    textView.setLayoutParams(params);
//                    innerLinearLayout.addView(textView);
//
//                    text_logo.addView(innerLinearLayout);
//
//                } else if (j == 0){
//                    coverLayout.setVisibility(View.GONE);
//
//                    LinearLayout outerLinearLayout = new LinearLayout(this);
//                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                    params1.topMargin = 5;
//                    params1.bottomMargin = 5;
//                    params1.width = 450;
//                    outerLinearLayout.setLayoutParams(params1);
//
//                    LinearLayout innerLinearLayout = new LinearLayout(this);
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//                    ImageView imageView = new ImageView(this);
//                    imageView.setMaxHeight(10);
//                    imageView.setMaxWidth(10);
//                    params.topMargin = 20;
//                    params.bottomMargin = 20;
//                    imageView.setLayoutParams(params);
//                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                    imageView.setImageResource(R.drawable.alliance);
//                    innerLinearLayout.addView(imageView);
//                    outerLinearLayout.addView(innerLinearLayout);
//                    text_logo.addView(outerLinearLayout);
//                } else
                if (j == 0){
                    coverLayout.setVisibility(View.GONE);

                    LinearLayout innerLinearLayout = new LinearLayout(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    TextView textView = new TextView(this);
                    params.topMargin = 20;
                    params.bottomMargin = 20;
                    textView.setTextAppearance(this, android.R.style.TextAppearance_Small);
                    textView.setTextColor(Color.BLACK);

                    String heading = "";
                    if (i == 0) {
                        heading = "Company Name";
                        params.rightMargin = 10;
                        params.leftMargin = 10;
                        params.width = 200;
                    } else {
                        heading = selectedcompanyArrayList.get(i-1).getCompanyName();
                        params.width = 450;
                        textView.setTypeface(null, Typeface.BOLD);
                    }

                    textView.setText(heading);
                    textView.setLayoutParams(params);
                    innerLinearLayout.addView(textView);

                    text_logo.addView(innerLinearLayout);
                } else if (j == 1){
                    coverLayout.setVisibility(View.GONE);

                    LinearLayout innerLinearLayout = new LinearLayout(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    TextView textView = new TextView(this);
                    params.topMargin = 20;
                    params.bottomMargin = 20;

                    String heading = "";
                    if (i == 0) {
                        params.rightMargin = 10;
                        params.leftMargin = 10;
                        params.width = 200;
                        heading = "Product Name";
                    } else {
                        params.width = 450;
                        heading = selectedcompanyArrayList.get(i-1).getCompanyProductName();
                    }

                    textView.setTextAppearance(this, android.R.style.TextAppearance_Small);
                    textView.setTextColor(Color.BLACK);
                    textView.setText(heading);
                    textView.setLayoutParams(params);
                    innerLinearLayout.addView(textView);

                    text_logo.addView(innerLinearLayout);
                } else if (j == 2){
                    coverLayout.setVisibility(View.GONE);

                    LinearLayout innerLinearLayout = new LinearLayout(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    TextView textView = new TextView(this);
                    params.topMargin = 20;
                    params.bottomMargin = 20;

                    String heading = "";
                    if (i == 0) {
                        params.rightMargin = 10;
                        params.leftMargin = 10;
                        params.width = 200;
                        heading = "Primium";
                    } else {
                        params.width = 450;
                        heading = selectedcompanyArrayList.get(i-1).getPrice();
                    }

                    textView.setTextAppearance(this, android.R.style.TextAppearance_Small);
                    textView.setTextColor(Color.BLACK);
                    textView.setText(heading);
                    textView.setLayoutParams(params);
                    innerLinearLayout.addView(textView);

                    text_logo.addView(innerLinearLayout);
                } else if (j == 3){

                    if (i == 0) {

                        coverLayout.setVisibility(View.GONE);

                        LinearLayout innerLinearLayout = new LinearLayout(this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        TextView textView = new TextView(this);
                        params.rightMargin = 10;
                        params.leftMargin = 10;
                        params.topMargin = 20;
                        params.bottomMargin = 20;
                        params.width = 200;
                        textView.setTextAppearance(this, android.R.style.TextAppearance_Small);
                        textView.setTextColor(Color.BLACK);
                        textView.setText("Covers");
                        textView.setLayoutParams(params);
                        innerLinearLayout.addView(textView);

                        text_logo.addView(innerLinearLayout);
                    } else {

                        coverLayout.setVisibility(View.VISIBLE);

                        ArrayList<CompanyCover> companyCoverArrayList = selectedcompanyArrayList.get(i-1).getCompanyCoverList();
                        String companyCover = "";
                        String companyCoverId = "";
                        LinearLayout outerLinearLayout = new LinearLayout(this);
                        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params1.topMargin = 5;
                        params1.bottomMargin = 5;
                        params1.width = 450;
                        outerLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                        outerLinearLayout.setLayoutParams(params1);

                        for (int s = 0; s < companyCoverIDUnique.size(); s++) {
                            boolean isExist = false;
                            companyCover = companyCoverNames.get(s);
                            companyCoverId = companyCoverIDUnique.get(s);
                            for (int k = 0; k < companyCoverArrayList.size(); k++) {
                                if (companyCoverId.equalsIgnoreCase(companyCoverArrayList.get(k).getCoverId())) {
                                    isExist = true;
                                    break;
                                }
                            }
                            LinearLayout innerLinearLayout = new LinearLayout(this);
                            outerLinearLayout.setOrientation(LinearLayout.VERTICAL);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            ImageView imageView = new ImageView(this);
                            imageView.setId(s);
                            imageView.setMaxHeight(10);
                            imageView.setMaxWidth(10);
                            params.gravity = Gravity.TOP;
                            params.height = 20;
                            params.width = 20;
                            params.topMargin = 15;
                            imageView.setLayoutParams(params);
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                            if (!isExist) {
                                imageView.setImageResource(R.drawable.wrong_tick);
                            } else {
                                imageView.setImageResource(R.drawable.right_tick);
                            }

                            innerLinearLayout.addView(imageView);

                            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            TextView textView = new TextView(this);
                            textView.setId(s);
                            params2.rightMargin = 10;
                            params2.leftMargin = 10;
                            params2.topMargin = 5;
                            params2.bottomMargin = 5;
                            textView.setTextAppearance(this, android.R.style.TextAppearance_Small);
                            textView.setTextColor(Color.BLACK);
                            textView.setText(companyCover);
                            textView.setLayoutParams(params2);
                            innerLinearLayout.addView(textView);

                            outerLinearLayout.addView(innerLinearLayout);

                        }
                        text_logo.addView(outerLinearLayout);
                    }
                } else if (j == 4){

                    coverLayout.setVisibility(View.GONE);

                    LinearLayout innerLinearLayout = new LinearLayout(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    TextView textView = new TextView(this);
                    params.topMargin = 20;

                    String heading = "";
                    if (i == 0) {
                        params.width = 200;
                        params.rightMargin = 10;
                        params.leftMargin = 10;
                        heading = "Attributes";
                    } else {
                        params.width = 450;
                        heading = selectedcompanyArrayList.get(i-1).getCompanyAttribute();
                    }

                    textView.setTextAppearance(this, android.R.style.TextAppearance_Small);
                    textView.setTextColor(Color.BLACK);
                    textView.setText(heading);
                    textView.setLayoutParams(params);
                    innerLinearLayout.addView(textView);

                    text_logo.addView(innerLinearLayout);
                } else if (j == 5){

                    coverLayout.setVisibility(View.GONE);

                    LinearLayout innerLinearLayout = new LinearLayout(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    TextView textView = new TextView(this);
                    params.topMargin = 20;

                    String heading = "";
                    if (i == 0) {
                        params.width = 200;
                        params.rightMargin = 10;
                        params.leftMargin = 10;
                        heading = "Fees";
                    } else {
                        params.width = 450;
                        heading = selectedcompanyArrayList.get(i-1).getCompanyFees();
                    }

                    textView.setTextAppearance(this, android.R.style.TextAppearance_Small);
                    textView.setTextColor(Color.BLACK);
                    textView.setText(heading);
                    textView.setLayoutParams(params);
                    innerLinearLayout.addView(textView);

                    text_logo.addView(innerLinearLayout);
                } else if (j == 6){

                    coverLayout.setVisibility(View.GONE);

                    LinearLayout innerLinearLayout = new LinearLayout(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    TextView textView = new TextView(this);
                    params.topMargin = 20;

                    String heading = "";
                    if (i == 0) {
                        params.width = 200;
                        params.rightMargin = 10;
                        params.leftMargin = 10;
                        heading = "Cover Premium";
                    } else {
                        params.width = 450;
                        heading = selectedcompanyArrayList.get(i-1).getCompanyCoverPremium();
                    }

                    textView.setTextAppearance(this, android.R.style.TextAppearance_Small);
                    textView.setTextColor(Color.BLACK);
                    textView.setText(heading);
                    textView.setLayoutParams(params);
                    innerLinearLayout.addView(textView);

                    text_logo.addView(innerLinearLayout);
                } else if (j == 7){

                    LinearLayout innerLinearLayout = new LinearLayout(this);
                    LinearLayout outerLinearLayout = new LinearLayout(this);
                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params1.topMargin = 5;
                    params1.bottomMargin = 5;
                    params1.width = 450;
                    outerLinearLayout.setLayoutParams(params1);

                    if (i == 0) {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        TextView textView = new TextView(this);
                        params.rightMargin = 10;
                        params.leftMargin = 10;
                        params.topMargin = 20;
                        params.bottomMargin = 20;
                        params.width = 200;
                        textView.setTextAppearance(this, android.R.style.TextAppearance_Small);
                        textView.setTextColor(Color.BLACK);
                        textView.setText("");
                        textView.setLayoutParams(params);
                        innerLinearLayout.addView(textView);
                        text_logo.addView(innerLinearLayout);
                    } else {

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        Button apply = new Button(this);
                        params.topMargin = 20;
                        params.bottomMargin = 20;
                        apply.setBackground(this.getDrawable(R.drawable.button_apply_view));
                        apply.setText("Buy");
                        apply.setTextColor(Color.WHITE);
                        apply.setId(i-1);
                        apply.setLayoutParams(params);
                        innerLinearLayout.addView(apply);
                        outerLinearLayout.addView(innerLinearLayout);

                        apply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int id = v.getId();
                                selectedcompanyList = selectedcompanyArrayList.get(id);
                                editor.putString(Constants.TOTAL_PREMIUM, selectedcompanyList.getPrice());
                                editor.putString(Constants.COMPANY_PRODUCT_ID, selectedcompanyList.getCompanyProductId());
                                editor.apply();
                                String savedNationalID = sharedPref.getString(Constants.NATIONAL_ID, "");
                                String userIdFromDb = databaseManager.getUserID(savedNationalID);
                                sendPolicy(userIdFromDb, selectedcompanyList.getCompanyProductId(), selectedcompanyList.getCompanyCoverList());
                            }
                        });
                        text_logo.addView(outerLinearLayout);
                    }
                }
            }
            linearLayout.addView(inflatedLayout);
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
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendPolicy(final String id, final String company_id, final ArrayList<CompanyCover> companyCoverArrayList) {
        try {
            AsyncServiceCall _companyDetails = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    progressDialogReset = ProgressDialog.show(
                            CompareCompanyActivity.this,
                            CompareCompanyActivity.this.getResources().getString(
                                    R.string.progress_heading),
                            CompareCompanyActivity.this.getResources().getString(
                                    R.string.progress_text));
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    return _webServiceManager.sendUserPolicy(id, company_id, companyCoverArrayList);
                }

                @Override
                protected void onPostExecute(Object resultObj) {
                    String result = (String) resultObj;

                    if ((progressDialogReset != null) && (progressDialogReset.isShowing())) {
                        try {
                            progressDialogReset.dismiss();
                        } catch (Exception ex) {
                            Log.e(Constants.LOG_RMSAGG, "Exception is " + Log.getStackTraceString(ex));
                        }
                    }

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
                    Intent apply = new Intent(CompareCompanyActivity.this, OwnerDetailsActivity.class);
                    apply.putExtra("FROMINSURER", true);
                    apply.putExtra("ComapnyName", selectedcompanyList.getCompanyName());
                    apply.putExtra("CompanyLogo", selectedcompanyList.getCompanyLogo());
                    startActivity(apply);
                    super.onPostExecute(result);
                }
            };
            try {

                if (NetworkManager.isNetAvailable(CompareCompanyActivity.this)) {
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
        new AlertDialog.Builder(CompareCompanyActivity.this)
                .setTitle(CompareCompanyActivity.this.getString(R.string.network_availability_heading))
                .setMessage(CompareCompanyActivity.this.getString(R.string.network_availability))

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true).show();
    }
}
