package com.rmsllcoman.agg.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rmsllcoman.agg.R;
import com.rmsllcoman.agg.other.AsyncServiceCall;
import com.rmsllcoman.agg.Model.Company;
import com.rmsllcoman.agg.Adapter.CompanyAdapter;
import com.rmsllcoman.agg.Model.CompanyCover;
import com.rmsllcoman.agg.Model.CompanyDetails;
import com.rmsllcoman.agg.other.Constants;
import com.rmsllcoman.agg.Controller.DatabaseManager;
import com.rmsllcoman.agg.Controller.NetworkManager;
import com.rmsllcoman.agg.Adapter.SpinnerAdapterCompany;
import com.rmsllcoman.agg.Model.CompanySpinner;
import com.rmsllcoman.agg.Controller.WebserviceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Created by macmini on 7/22/17.
 */

public class Insurers extends AppCompatActivity implements CompanyAdapter.OnShareClickedListener, SpinnerAdapterCompany.OnCheckboxCheckedChangeListener {

    CompanyAdapter companyAdapter;
    LinearLayout previous;
    LinearLayout next;
    LinearLayout companyDetails;
    LinearLayout companyCompare;
    RadioButton radio_allquote;
    RadioButton lowestFive;
    RadioGroup selectedQuotes;
//    ImageView companyLogoView;
    ListView companyList;
//    TextView about;
    TextView breakup;
    TextView details;
//    TextView claimForm;
    TextView companyName;
    TextView back;
    Spinner spinner;
    Button apply;
    Button compare;
    int currentIndex;
    String CAR_TYPE = "";
    String VehiclePrice = "";
    String[] select_companyCover = new String[0];
    DatabaseManager databaseManager;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    ArrayList<Company> company;
    ArrayList<Company> companyFilter;
    ArrayList<String> companyFilterCoverID;
    ArrayList<CompanyCover> companyCoverList;
    private WebserviceManager _webServiceManager;
    public static ProgressDialog progressDialogReset;
    public static ArrayList<Company> selectedcompanyArrayList;
    public static ArrayList<String> companyCoverNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurers);
        getSupportActionBar().hide();

        databaseManager = new DatabaseManager(this);
        _webServiceManager = new WebserviceManager(this);
        sharedPref = getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        VehiclePrice = sharedPref.getString(Constants.PRICE, "");

        spinner = (Spinner) findViewById(R.id.select_company);

        final ArrayList<CompanySpinner> listVOs = new ArrayList<>();

        companyCoverNames = CompareActivity.companyCoverNames;
        select_companyCover = new String[companyCoverNames.size()+1];
        for (int i = 0; i < companyCoverNames.size(); i++) {
            if (i == 0) {
                select_companyCover[0] = "Select Covers";
            }
            select_companyCover[i+1] = companyCoverNames.get(i);
        }

        for (String aSelect_qualification : select_companyCover) {
            CompanySpinner stateVO = new CompanySpinner();
            stateVO.setTitle(aSelect_qualification);
            stateVO.setSelected(false);
            listVOs.add(stateVO);
        }

        companyList = (ListView)findViewById(R.id.company_list);
//        companyLogoView = (ImageView)findViewById(R.id.company_logo_view);
        previous = (LinearLayout)findViewById(R.id.button_previous);
        next = (LinearLayout)findViewById(R.id.button_next_view);
        companyDetails = (LinearLayout)findViewById(R.id.company_details);
        companyCompare = (LinearLayout)findViewById(R.id.company_compare);
//        about = (TextView)findViewById(R.id.about_company);
        breakup = (TextView)findViewById(R.id.breakup);
        details = (TextView)findViewById(R.id.details);
//        claimForm = (TextView)findViewById(R.id.claimForm);
        companyName = (TextView)findViewById(R.id.company_logo_name);
        back = (TextView)findViewById(R.id.backToList);
        apply = (Button)findViewById(R.id.apply);
        compare = (Button)findViewById(R.id.compare);
        selectedQuotes =  (RadioGroup) findViewById(R.id.selected_radio_group);
        radio_allquote =  (RadioButton) findViewById(R.id.radio_allquote);
        lowestFive =  (RadioButton) findViewById(R.id.radio_fivequote);

        CAR_TYPE = sharedPref.getString(Constants.CAR_TYPE, "");
        if (!CAR_TYPE.equalsIgnoreCase("Motor Comprehensive")) {
            lowestFive.setChecked(true);
            SpinnerAdapterCompany myAdapter = new SpinnerAdapterCompany(Insurers.this, 0,
                    listVOs, "TP");
            myAdapter.setOnCheckboxChangeListener(this);
            spinner.setAdapter(myAdapter);
        } else {
            lowestFive.setChecked(false);
            SpinnerAdapterCompany myAdapter = new SpinnerAdapterCompany(Insurers.this, 0,
                    listVOs, "COMPRH");
            myAdapter.setOnCheckboxChangeListener(this);
            spinner.setAdapter(myAdapter);
        }
        getCompanyDetails();
        companyFilter = new ArrayList<>();
        companyFilterCoverID = new ArrayList<>();

        companyAdapter= new CompanyAdapter(company,companyFilter,companyFilterCoverID,Insurers.this);
        companyAdapter.setOnShareClickedListener(this);
        companyList.setAdapter(companyAdapter);

        selectedQuotes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                int selectedId = selectedQuotes .getCheckedRadioButtonId();
                companyFilter.clear();
                companyFilterCoverID.clear();

                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                listVOs.clear();
                for (String aSelect_qualification : select_companyCover) {
                    CompanySpinner stateVO = new CompanySpinner();
                    stateVO.setTitle(aSelect_qualification);
                    stateVO.setSelected(false);
                    listVOs.add(stateVO);
                }

                if (radioButton.getText().toString().equalsIgnoreCase("Third Party")) {
                    CAR_TYPE = "Motor Third Party";
                    editor.putString(Constants.CAR_TYPE, CAR_TYPE);
                    SpinnerAdapterCompany myAdapter = new SpinnerAdapterCompany(Insurers.this, 0,
                            listVOs, "TP");
                    myAdapter.setOnCheckboxChangeListener(Insurers.this);
                    spinner.setAdapter(myAdapter);
                } else if (radioButton.getText().toString().equalsIgnoreCase("Comprehensive Insurance")) {
                    CAR_TYPE = "Motor Comprehensive";
                    editor.putString(Constants.CAR_TYPE, CAR_TYPE);
                    SpinnerAdapterCompany myAdapter = new SpinnerAdapterCompany(Insurers.this, 0,
                            listVOs, "COMPRH");
                    myAdapter.setOnCheckboxChangeListener(Insurers.this);
                    spinner.setAdapter(myAdapter);
                }
                editor.apply();
                getCompanyDetails();
                companyAdapter= new CompanyAdapter(company,companyFilter,companyFilterCoverID,Insurers.this);
                companyAdapter.setOnShareClickedListener(Insurers.this);
                companyList.setAdapter(companyAdapter);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (companyFilter.size() > 0) {
                    companyCoverList = new ArrayList<>();
                    companyCoverList = companyFilter.get(currentIndex).getCompanyCoverList();
                    currentIndex = currentIndex - 1;
                    previous.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                    if (companyFilter.get(currentIndex).getCoverPrice().equalsIgnoreCase("0")) {
                        float price = Float.parseFloat(companyFilter.get(currentIndex).getPrice());
                        breakup.setText(String.valueOf(String.format(Locale.CANADA, "%.2f", price)));
                    } else {
                        float price = Float.parseFloat(companyFilter.get(currentIndex).getCoverPrice());
                        breakup.setText(String.valueOf(String.format(Locale.CANADA, "%.2f", price)));
                    }
                    companyName.setText(companyFilter.get(currentIndex).getCompanyName());
//                companyLogoView.setBackgroundResource(company.get(currentIndex).getCompanyLogo());
                    if (currentIndex == 0) {
                        previous.setVisibility(View.INVISIBLE);
                        next.setVisibility(View.VISIBLE);
                    }
                } else {
                    companyCoverList = new ArrayList<>();
                    companyCoverList = company.get(currentIndex).getCompanyCoverList();
                    currentIndex = currentIndex - 1;
                    previous.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                    if (company.get(currentIndex).getCoverPrice().equalsIgnoreCase("0")) {
                        float price = Float.parseFloat(company.get(currentIndex).getPrice());
                        breakup.setText(String.valueOf(String.format(Locale.CANADA, "%.2f", price)));
                    } else {
                        float price = Float.parseFloat(company.get(currentIndex).getCoverPrice());
                        breakup.setText(String.valueOf(String.format(Locale.CANADA, "%.2f", price)));
                    }
                    companyName.setText(company.get(currentIndex).getCompanyName());
//                companyLogoView.setBackgroundResource(company.get(currentIndex).getCompanyLogo());
                    if (currentIndex == 0) {
                        previous.setVisibility(View.INVISIBLE);
                        next.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (companyFilter.size() > 0) {
                    companyCoverList = new ArrayList<>();
                    companyCoverList = companyFilter.get(currentIndex).getCompanyCoverList();
                    currentIndex = currentIndex + 1;
                    next.setVisibility(View.VISIBLE);
                    previous.setVisibility(View.VISIBLE);
                    if (companyFilter.get(currentIndex).getCoverPrice().equalsIgnoreCase("0")) {
                        float price = Float.parseFloat(companyFilter.get(currentIndex).getPrice());
                        breakup.setText(String.valueOf(String.format(Locale.CANADA, "%.2f", price)));
                    } else {
                        float price = Float.parseFloat(companyFilter.get(currentIndex).getCoverPrice());
                        breakup.setText(String.valueOf(String.format(Locale.CANADA, "%.2f", price)));
                    }
                    companyName.setText(companyFilter.get(currentIndex).getCompanyName());
//                companyLogoView.setBackgroundResource(company.get(currentIndex).getCompanyLogo());

                    if (currentIndex == companyFilter.size()-1) {
                        next.setVisibility(View.INVISIBLE);
                        previous.setVisibility(View.VISIBLE);
                    }
                } else {
                    companyCoverList = new ArrayList<>();
                    companyCoverList = company.get(currentIndex).getCompanyCoverList();
                    currentIndex = currentIndex + 1;
                    next.setVisibility(View.VISIBLE);
                    previous.setVisibility(View.VISIBLE);
                    if (company.get(currentIndex).getCoverPrice().equalsIgnoreCase("0")) {
                        float price = Float.parseFloat(company.get(currentIndex).getPrice());
                        breakup.setText(String.valueOf(String.format(Locale.CANADA, "%.2f", price)));
                    } else {
                        float price = Float.parseFloat(company.get(currentIndex).getCoverPrice());
                        breakup.setText(String.valueOf(String.format(Locale.CANADA, "%.2f", price)));
                    }
                    companyName.setText(company.get(currentIndex).getCompanyName());
//                companyLogoView.setBackgroundResource(company.get(currentIndex).getCompanyLogo());

                    if (currentIndex == company.size()-1) {
                        next.setVisibility(View.INVISIBLE);
                        previous.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                companyCompare.setVisibility(View.VISIBLE);
                companyDetails.setVisibility(View.GONE);
            }
        });

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coverDetails = "";
                for (int i =0; i < companyCoverList.size(); i++) {
                    if (i == 0) {
                        coverDetails = "- " + companyCoverList.get(i).getCoverName();
                    } else {
                        coverDetails = coverDetails + "\n" + "- " + companyCoverList.get(i).getCoverName();
                    }
                }
                new AlertDialog.Builder(Insurers.this)
                        .setTitle("Cover Details")
                        .setMessage(coverDetails)

                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setCancelable(true).show();
            }
        });

        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedcompanyArrayList = new ArrayList<>();
                if (companyFilter.size() > 0) {
                    for (int i=0; i < companyFilter.size(); i++) {
                        if (companyFilter.get(i).getSelected()) {
                            selectedcompanyArrayList.add(companyFilter.get(i));
                        }
                    }
                } else {
                    for (int i=0; i < company.size(); i++) {
                        if (company.get(i).getSelected()) {
                            selectedcompanyArrayList.add(company.get(i));
                        }
                    }
                }
                if (selectedcompanyArrayList.size() > 1) {
                    Intent compare = new Intent(Insurers.this, CompareCompanyActivity.class);
                    startActivity(compare);
                } else {
                    Toast.makeText(getApplicationContext(),"Please select more than 1 company to compare", Toast.LENGTH_SHORT).show();
                }
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (companyFilter.size() > 0) {
                    if (companyFilter.get(currentIndex).getCoverPrice().equalsIgnoreCase("0")) {
                        float price = Float.parseFloat(companyFilter.get(currentIndex).getPrice());
                        editor.putString(Constants.TOTAL_PREMIUM, String.valueOf(String.format(Locale.CANADA, "%.2f", price)));
                    } else {
                        float price = Float.parseFloat(companyFilter.get(currentIndex).getCoverPrice());
                        editor.putString(Constants.TOTAL_PREMIUM, String.valueOf(String.format(Locale.CANADA, "%.2f", price)));
                    }
                    editor.putString(Constants.COMPANY_PRODUCT_ID, companyFilter.get(currentIndex).getCompanyProductId());
                    editor.apply();
                    String savedNationalID = sharedPref.getString(Constants.NATIONAL_ID, "");
                    String userIdFromDb = databaseManager.getUserID(savedNationalID);
                    sendPolicy(userIdFromDb, companyFilter.get(currentIndex).getCompanyProductId(), companyFilter.get(currentIndex).getCompanyCoverList());
                } else {
                    if (company.get(currentIndex).getCoverPrice().equalsIgnoreCase("0")) {
                        float price = Float.parseFloat(company.get(currentIndex).getPrice());
                        editor.putString(Constants.TOTAL_PREMIUM, String.valueOf(String.format(Locale.CANADA, "%.2f", price)));
                    } else {
                        float price = Float.parseFloat(company.get(currentIndex).getCoverPrice());
                        editor.putString(Constants.TOTAL_PREMIUM, String.valueOf(String.format(Locale.CANADA, "%.2f", price)));
                    }
                    editor.putString(Constants.COMPANY_PRODUCT_ID, company.get(currentIndex).getCompanyProductId());
                    editor.apply();
                    String savedNationalID = sharedPref.getString(Constants.NATIONAL_ID, "");
                    String userIdFromDb = databaseManager.getUserID(savedNationalID);
                    sendPolicy(userIdFromDb, company.get(currentIndex).getCompanyProductId(), company.get(currentIndex).getCompanyCoverList());
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void ShareClicked(Company companyDetails1, String buttonType) {
        companyCoverList = new ArrayList<>();
        companyCoverList = companyDetails1.getCompanyCoverList();
        currentIndex = companyDetails1.getId();
        if (buttonType.equalsIgnoreCase("view")) {
            companyCompare.setVisibility(View.GONE);
            companyDetails.setVisibility(View.VISIBLE);
            int size = 0;
            if (companyFilter.size() > 0) {
                size = companyFilter.size();
            } else {
                size = company.size();
            }
            if (currentIndex == 0) {
                previous.setVisibility(View.INVISIBLE);
                next.setVisibility(View.VISIBLE);
            } else if (currentIndex == size-1) {
                next.setVisibility(View.INVISIBLE);
                previous.setVisibility(View.VISIBLE);
            } else {
                next.setVisibility(View.VISIBLE);
                previous.setVisibility(View.VISIBLE);
            }
            if (companyDetails1.getCoverPrice().equalsIgnoreCase("0")) {
                float price = Float.parseFloat(companyDetails1.getPrice());
                breakup.setText(String.valueOf(String.format(Locale.CANADA, "%.2f", price)));
            } else {
                float price = Float.parseFloat(companyDetails1.getCoverPrice());
                breakup.setText(String.valueOf(String.format(Locale.CANADA, "%.2f", price)));
            }
            companyName.setText(companyDetails1.getCompanyName());
//        companyLogoView.setBackgroundResource(companyDetails1.getCompanyLogo());
        } else {

            if (companyFilter.size() > 0) {
                if (companyFilter.get(currentIndex).getCoverPrice().equalsIgnoreCase("0")) {
                    float price = Float.parseFloat(companyFilter.get(currentIndex).getPrice());
                    editor.putString(Constants.TOTAL_PREMIUM, String.valueOf(String.format(Locale.CANADA, "%.2f", price)));
                } else {
                    float price = Float.parseFloat(companyFilter.get(currentIndex).getCoverPrice());
                    editor.putString(Constants.TOTAL_PREMIUM, String.valueOf(String.format(Locale.CANADA, "%.2f", price)));
                }
                editor.putString(Constants.COMPANY_PRODUCT_ID, companyFilter.get(currentIndex).getCompanyProductId());
                editor.apply();
                String savedNationalID = sharedPref.getString(Constants.NATIONAL_ID, "");
                String userIdFromDb = databaseManager.getUserID(savedNationalID);
                sendPolicy(userIdFromDb, companyFilter.get(currentIndex).getCompanyProductId(), companyFilter.get(currentIndex).getCompanyCoverList());
            } else {
                if (company.get(currentIndex).getCoverPrice().equalsIgnoreCase("0")) {
                    float price = Float.parseFloat(company.get(currentIndex).getPrice());
                    editor.putString(Constants.TOTAL_PREMIUM, String.valueOf(String.format(Locale.CANADA, "%.2f", price)));
                } else {
                    float price = Float.parseFloat(company.get(currentIndex).getCoverPrice());
                    editor.putString(Constants.TOTAL_PREMIUM, String.valueOf(String.format(Locale.CANADA, "%.2f", price)));
                }
                editor.putString(Constants.COMPANY_PRODUCT_ID, company.get(currentIndex).getCompanyProductId());
                editor.apply();
                String savedNationalID = sharedPref.getString(Constants.NATIONAL_ID, "");
                String userIdFromDb = databaseManager.getUserID(savedNationalID);
                sendPolicy(userIdFromDb, company.get(currentIndex).getCompanyProductId(), company.get(currentIndex).getCompanyCoverList());
            }
        }
    }

    @Override
    public void CheckboxChange(ArrayList<CompanySpinner> listStateSelected) {
        companyFilter = new ArrayList<>();
        companyFilterCoverID = new ArrayList<>();
        getCompanyDetails();
        for (int j = 0; j < listStateSelected.size(); j++) {
            for (int i=0; i < company.size(); i++) {
                if (company.get(i).getSelected()) {
                    company.get(i).setSelected(false);
                }
                if (!listStateSelected.get(j).getTitle().equalsIgnoreCase("Comprehensive Cover") && !listStateSelected.get(j).getTitle().equalsIgnoreCase("Third Party Liability")) {
                    for (int k=0; k < company.get(i).getCompanyCoverList().size(); k++) {
                        if (company.get(i).getCompanyCoverList().get(k).getCoverName().equalsIgnoreCase(listStateSelected.get(j).getTitle())) {
                            companyFilter.add(company.get(i));
                        }
                    }
                }
            }
            companyFilterCoverID.add(databaseManager.getCoverIDByCoverName(listStateSelected.get(j).getTitle()));
        }

        Collections.sort(company, new Comparator<Company>() {
            @Override
            public int compare(Company lhs, Company rhs) {
                return lhs.getCoverPrice().compareTo(rhs.getCoverPrice());
            }
        });
        ArrayList<Company> sortedCompany = new ArrayList<>();
        for (int i = 0; i < company.size(); i++) {
            sortedCompany.add(new Company(i, company.get(i).getSelected(), company.get(i).getPrice(), company.get(i).getCoverPrice(), company.get(i).getCompanyName(),
                    company.get(i).getCompanyProductName(), company.get(i).getCompanyProductId(), company.get(i).getCompanyProductType(),
                    company.get(i).getCompanyAttribute(), company.get(i).getCompanyFees(), company.get(i).getCompanyTax(),
                    company.get(i).getCompanyCoverPremium(),company.get(i).getCompanySelectedCoverPremium(), company.get(i).getPremiumAmount(), R.drawable.alliance,
                    R.drawable.alliance_compare_view, company.get(i).getCompanyCoverList()));
        }
        company = sortedCompany;

        Set<Company> uniqueCompanyList = new LinkedHashSet<>(companyFilter);
        companyFilter = new ArrayList<>();
        companyFilter.addAll(uniqueCompanyList);
        if (companyFilter.size() > 0) {
            for (int j = 0; j < companyFilter.size(); j++) {
                String[] coverPremiumArray = new String[10];
                String coverPremium = companyFilter.get(j).getCompanyCoverPremium();
                String selectedCoverPremium = companyFilter.get(j).getCompanySelectedCoverPremium();
                coverPremiumArray = coverPremium.split("\n");

                JSONObject coverJson = new JSONObject();

                for (String aCoverPremiumArray : coverPremiumArray) {
                    try {
                        String key = (aCoverPremiumArray.substring(0,aCoverPremiumArray.lastIndexOf(":"))).trim().toUpperCase();
                        String value = (aCoverPremiumArray.substring(aCoverPremiumArray.lastIndexOf(":")).replace(":", "")).trim();
                        coverJson.put(key, value);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                float price = Float.parseFloat(companyFilter.get(j).getPrice());
                for (int i = 0; i < companyFilterCoverID.size(); i++) {
                    String coverID = companyFilterCoverID.get(i);
                    if (coverJson.isNull(coverID)) {
                        Log.d("**COVER ID NOT EXIST***", coverID);
                    } else {
                        try {
                            if (!coverID.equalsIgnoreCase("OD") && !coverID.equalsIgnoreCase("TP")) {
                                String selectedCoverAmount = coverJson.getString(coverID);
                                float selectedAmount = Float.parseFloat(selectedCoverAmount);
                                price = price + selectedAmount;
                                String premium = coverID + " : " + String.valueOf(String.format(Locale.CANADA, "%.2f", selectedAmount)) + "\n";
                                selectedCoverPremium = selectedCoverPremium + premium;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                companyFilter.get(j).setCoverPrice(String.valueOf(price));
                companyFilter.get(j).setCompanySelectedCoverPremium(selectedCoverPremium);
            }

            Collections.sort(companyFilter, new Comparator<Company>() {
                @Override
                public int compare(Company lhs, Company rhs) {
                    return lhs.getCoverPrice().compareTo(rhs.getCoverPrice());
                }
            });
            ArrayList<Company> sortedCompanyFilter = new ArrayList<>();
            for (int i = 0; i < companyFilter.size(); i++) {
                sortedCompanyFilter.add(new Company(i, companyFilter.get(i).getSelected(), companyFilter.get(i).getPrice(), companyFilter.get(i).getCoverPrice(),
                        companyFilter.get(i).getCompanyName(), companyFilter.get(i).getCompanyProductName(), companyFilter.get(i).getCompanyProductId(),
                        companyFilter.get(i).getCompanyProductType(), companyFilter.get(i).getCompanyAttribute(), companyFilter.get(i).getCompanyFees(),
                        companyFilter.get(i).getCompanyTax(), companyFilter.get(i).getCompanyCoverPremium(),companyFilter.get(i).getCompanySelectedCoverPremium(),
                        companyFilter.get(i).getPremiumAmount(), R.drawable.alliance, R.drawable.alliance_compare_view, companyFilter.get(i).getCompanyCoverList()));
            }
            companyFilter = sortedCompanyFilter;
            companyAdapter= new CompanyAdapter(companyFilter,companyFilter,companyFilterCoverID,Insurers.this);
            companyAdapter.setOnShareClickedListener(this);
            companyList.setAdapter(companyAdapter);
        } else if (companyFilter.size() == 0 && listStateSelected.size() > 0){
            if (!CAR_TYPE.equalsIgnoreCase("Motor Comprehensive")) {
                company.clear();
            }
            companyAdapter= new CompanyAdapter(company,companyFilter,companyFilterCoverID,Insurers.this);
            companyAdapter.setOnShareClickedListener(this);
            companyList.setAdapter(companyAdapter);
        } else {
            companyFilter.clear();
            companyAdapter= new CompanyAdapter(company,companyFilter,companyFilterCoverID,Insurers.this);
            companyAdapter.setOnShareClickedListener(this);
            companyList.setAdapter(companyAdapter);
        }
        spinner.onFinishTemporaryDetach();
    }

    private void getCompanyDetails() {
        company = new ArrayList<>();
        int j = 0;
        ArrayList<CompanyDetails> companyDetailsArrayList = databaseManager.getComanyDetails();
        for (int i = 0; i < companyDetailsArrayList.size(); i++) {
            String productType = companyDetailsArrayList.get(i).getProductType();
            String productString = "";
            if (CAR_TYPE.equalsIgnoreCase("Motor Comprehensive")) {
                productString = "COMPRH";
            } else {
                productString = "TP";
            }
            if (productType.equalsIgnoreCase(productString)) {
                Log.d("PRODUCT_TYPE*******",productType);
                ArrayList<CompanyCover> companyCoverArrayList = databaseManager.getComanyCoverByCompany(companyDetailsArrayList.get(i).getProductID());

                String[] coverPremiumArray = new String[10];
                String coverPremium = companyDetailsArrayList.get(i).getCoverPremium();
                coverPremiumArray = coverPremium.split("\n");

                JSONObject coverJson = new JSONObject();

                for (String aCoverPremiumArray : coverPremiumArray) {
                    try {
                        String key = (aCoverPremiumArray.substring(0,aCoverPremiumArray.lastIndexOf(":"))).trim().toUpperCase();
                        String value = (aCoverPremiumArray.substring(aCoverPremiumArray.lastIndexOf(":")).replace(":", "")).trim();
                        coverJson.put(key, value);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                String selectedCoverPremium = "";
                if (!coverJson.isNull("OD")) {
                    try {
                        float selectedAmount = Float.parseFloat(coverJson.getString("OD"));
                        selectedCoverPremium = selectedCoverPremium + "OD" + " : " + String.valueOf(String.format(Locale.CANADA, "%.2f", selectedAmount)) + "\n";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else
                if (!coverJson.isNull("TP")){
                    try {
                        float selectedAmount = Float.parseFloat(coverJson.getString("TP"));
                        selectedCoverPremium = selectedCoverPremium + "TP" + " : " + String.valueOf(String.format(Locale.CANADA, "%.2f", selectedAmount)) + "\n";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    selectedCoverPremium = coverPremium;
                }

                company.add(new Company(j, false, companyDetailsArrayList.get(i).getPremiumAmount(), companyDetailsArrayList.get(i).getPremiumAmount(), companyDetailsArrayList.get(i).getInsurerName(),
                        companyDetailsArrayList.get(i).getProductName(), companyDetailsArrayList.get(i).getProductID(), companyDetailsArrayList.get(i).getProductType(),
                        companyDetailsArrayList.get(i).getAttribute(), companyDetailsArrayList.get(i).getFees(), companyDetailsArrayList.get(i).getTax(),
                        companyDetailsArrayList.get(i).getCoverPremium(),selectedCoverPremium, companyDetailsArrayList.get(i).getPremiumAmount(), R.drawable.alliance, R.drawable.alliance_compare_view,
                        companyCoverArrayList));
                j = j + 1;
            }
        }
    }

    public void sendPolicy(final String id, final String company_id, final ArrayList<CompanyCover> companyCoverArrayList) {
        try {
            AsyncServiceCall _companyDetails = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    progressDialogReset = ProgressDialog.show(
                            Insurers.this,
                            Insurers.this.getResources().getString(
                                    R.string.progress_heading),
                            Insurers.this.getResources().getString(
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


                    if (companyFilter.size() > 0) {
                        editor.putString(Constants.COMPANY_NAME, companyFilter.get(currentIndex).getCompanyName());
                        editor.putInt(Constants.COMPANY_LOGO, companyFilter.get(currentIndex).getCompanyLogo());
                    } else {
                        editor.putString(Constants.COMPANY_NAME, company.get(currentIndex).getCompanyName());
                        editor.putInt(Constants.COMPANY_LOGO, company.get(currentIndex).getCompanyLogo());
                    }

                    editor.apply();

                    Intent apply = new Intent(Insurers.this, OwnerDetailsActivity.class);
                    apply.putExtra("FROMINSURER", true);
                    startActivity(apply);
                    super.onPostExecute(result);
                }
            };
            try {

                if (NetworkManager.isNetAvailable(Insurers.this)) {
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
        new AlertDialog.Builder(Insurers.this)
                .setTitle(Insurers.this.getString(R.string.network_availability_heading))
                .setMessage(Insurers.this.getString(R.string.network_availability))

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true).show();
    }
}
