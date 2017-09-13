package com.netsol.rms.aggregator.activity;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.netsol.rms.aggregator.R;
import com.netsol.rms.aggregator.activity.other.AsyncServiceCall;
import com.netsol.rms.aggregator.activity.other.Company;
import com.netsol.rms.aggregator.activity.other.CompanyAdapter;
import com.netsol.rms.aggregator.activity.other.CompanyCover;
import com.netsol.rms.aggregator.activity.other.CompanyDetails;
import com.netsol.rms.aggregator.activity.other.Constants;
import com.netsol.rms.aggregator.activity.other.DatabaseManager;
import com.netsol.rms.aggregator.activity.other.NetworkManager;
import com.netsol.rms.aggregator.activity.other.SpinnerAdapterCompany;
import com.netsol.rms.aggregator.activity.other.CompanySpinner;
import com.netsol.rms.aggregator.activity.other.WebserviceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

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
    RadioGroup selectedQuotes;
//    ImageView companyLogoView;
    ListView companyList;
//    TextView about;
    TextView breakup;
    TextView details;
    TextView claimForm;
    TextView companyName;
    TextView back;
    Spinner spinner;
    Button apply;
    Button compare;
    int currentIndex;
    int breakUpList[];
    String[] select_companyCover = new String[0];
    DatabaseManager databaseManager;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    ArrayList<Company> company;
    ArrayList<Company> companyFilter;
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

        spinner = (Spinner) findViewById(R.id.select_company);

        ArrayList<CompanySpinner> listVOs = new ArrayList<>();

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
        SpinnerAdapterCompany myAdapter = new SpinnerAdapterCompany(Insurers.this, 0,
                listVOs);
        myAdapter.setOnCheckboxChangeListener(this);
        spinner.setAdapter(myAdapter);

        companyList = (ListView)findViewById(R.id.company_list);
//        companyLogoView = (ImageView)findViewById(R.id.company_logo_view);
        previous = (LinearLayout)findViewById(R.id.button_previous);
        next = (LinearLayout)findViewById(R.id.button_next_view);
        companyDetails = (LinearLayout)findViewById(R.id.company_details);
        companyCompare = (LinearLayout)findViewById(R.id.company_compare);
//        about = (TextView)findViewById(R.id.about_company);
        breakup = (TextView)findViewById(R.id.breakup);
        details = (TextView)findViewById(R.id.details);
        claimForm = (TextView)findViewById(R.id.claimForm);
        companyName = (TextView)findViewById(R.id.company_logo_name);
        back = (TextView)findViewById(R.id.backToList);
        apply = (Button)findViewById(R.id.apply);
        compare = (Button)findViewById(R.id.compare);
        selectedQuotes =  (RadioGroup) findViewById(R.id.selected_radio_group);
        radio_allquote =  (RadioButton) findViewById(R.id.radio_allquote);

        company = new ArrayList<>();
        companyFilter = new ArrayList<>();

        ArrayList<CompanyDetails> companyDetailsArrayList = databaseManager.getComanyDetails();
        for (int i = 0; i < companyDetailsArrayList.size(); i++) {
            ArrayList<CompanyCover> companyCoverArrayList = databaseManager.getComanyCoverByCompany(companyDetailsArrayList.get(i).getProductID());
            company.add(new Company(i, false, companyDetailsArrayList.get(i).getTotalPremium(), companyDetailsArrayList.get(i).getInsurerName(), companyDetailsArrayList.get(i).getProductName(),
                    companyDetailsArrayList.get(i).getProductID(), companyDetailsArrayList.get(i).getAttribute(), companyDetailsArrayList.get(i).getFees(), companyDetailsArrayList.get(i).getCoverPremium(),
                    R.drawable.alliance, R.drawable.alliance_compare_view, companyCoverArrayList));
        }

        companyAdapter= new CompanyAdapter(company,getApplicationContext());
        companyAdapter.setOnShareClickedListener(this);
        companyList.setAdapter(companyAdapter);

        breakUpList = new int[company.size()];

        for (int i = 0; i < company.size(); i++) {
            breakUpList[i] = Integer.valueOf(company.get(i).getPrice());
        }

        Arrays.sort(breakUpList);
        breakUpList = removeDuplicateVlaue(breakUpList);

        selectedQuotes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                int selectedId = selectedQuotes .getCheckedRadioButtonId();

                RadioButton radioButton = (RadioButton) findViewById(selectedId);

                if (radioButton.getText().toString().equalsIgnoreCase("Lowest 5 Quotes")) {
                    if (companyFilter.size() == 0){
                        companyFilter = new ArrayList<>();
                        int fiveArray[] = getFiveLowest(breakUpList);
                        for (int j = 0; j < fiveArray.length; j++) {
                            for (int i=0; i < company.size(); i++) {
                                int price = Integer.parseInt(company.get(i).getPrice());
                                if (price == fiveArray[j]) {
                                    companyFilter.add(company.get(i));
                                }
                            }
                        }
                        companyAdapter= new CompanyAdapter(companyFilter,getApplicationContext());
                        companyAdapter.setOnShareClickedListener(Insurers.this);
                        companyList.setAdapter(companyAdapter);
                    } else {
                        int breakUpList[] = new int[companyFilter.size()];
                        for (int i = 0; i < companyFilter.size(); i++) {
                            breakUpList[i] = Integer.valueOf(companyFilter.get(i).getPrice());
                        }

                        Arrays.sort(breakUpList);
                        breakUpList = removeDuplicateVlaue(breakUpList);
                        companyFilter = new ArrayList<>();

                        int fiveArray[] = getFiveLowest(breakUpList);
                        for (int j = 0; j < fiveArray.length; j++) {
                            for (int i=0; i < company.size(); i++) {
                                int price = Integer.parseInt(company.get(i).getPrice());
                                if (price == fiveArray[j]) {
                                    companyFilter.add(company.get(i));
                                }
                            }
                        }
                        companyAdapter= new CompanyAdapter(companyFilter,getApplicationContext());
                        companyAdapter.setOnShareClickedListener(Insurers.this);
                        companyList.setAdapter(companyAdapter);
                    }
                } else if (radioButton.getText().toString().equalsIgnoreCase("All Quotes")) {
                    if (companyFilter.size() > 0) {
                        companyAdapter= new CompanyAdapter(company,getApplicationContext());
                        companyAdapter.setOnShareClickedListener(Insurers.this);
                        companyList.setAdapter(companyAdapter);
                    }
                }
                System.out.println(Arrays.toString(getFiveLowest(breakUpList)));

                Toast.makeText(Insurers.this,
                        radioButton.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                companyCoverList = new ArrayList<>();
                companyCoverList = company.get(currentIndex).getCompanyCoverList();
                currentIndex = currentIndex - 1;
                previous.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
                breakup.setText(company.get(currentIndex).getPrice());
                companyName.setText(company.get(currentIndex).getCompanyName());
//                companyLogoView.setBackgroundResource(company.get(currentIndex).getCompanyLogo());
                if (currentIndex == 0) {
                    previous.setVisibility(View.INVISIBLE);
                    next.setVisibility(View.VISIBLE);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                companyCoverList = new ArrayList<>();
                companyCoverList = company.get(currentIndex).getCompanyCoverList();
                currentIndex = currentIndex + 1;
                next.setVisibility(View.VISIBLE);
                previous.setVisibility(View.VISIBLE);
                breakup.setText(company.get(currentIndex).getPrice());
                companyName.setText(company.get(currentIndex).getCompanyName());
//                companyLogoView.setBackgroundResource(company.get(currentIndex).getCompanyLogo());

                if (currentIndex == company.size()-1) {
                    next.setVisibility(View.INVISIBLE);
                    previous.setVisibility(View.VISIBLE);
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
                for (int i=0; i < company.size(); i++) {
                    if (company.get(i).getSelected()) {
                        selectedcompanyArrayList.add(company.get(i));
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

                editor.putString(Constants.TOTAL_PREMIUM, company.get(currentIndex).getPrice());
                editor.putString(Constants.COMPANY_PRODUCT_ID, company.get(currentIndex).getCompanyProductId());
                editor.apply();
                String savedNationalID = sharedPref.getString(Constants.NATIONAL_ID, "");
                String userIdFromDb = databaseManager.getUserID(savedNationalID);
                sendPolicy(userIdFromDb, company.get(currentIndex).getCompanyProductId(), company.get(currentIndex).getCompanyCoverList());
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
    public void ShareClicked(Company companyDetails1) {
        companyCoverList = new ArrayList<>();
        companyCoverList = companyDetails1.getCompanyCoverList();
        currentIndex = companyDetails1.getId();
        companyCompare.setVisibility(View.GONE);
        companyDetails.setVisibility(View.VISIBLE);
        if (currentIndex == 0) {
            previous.setVisibility(View.INVISIBLE);
            next.setVisibility(View.VISIBLE);
        } else if (currentIndex == company.size()-1) {
            next.setVisibility(View.INVISIBLE);
            previous.setVisibility(View.VISIBLE);
        } else {
            next.setVisibility(View.VISIBLE);
            previous.setVisibility(View.VISIBLE);
        }
        breakup.setText(companyDetails1.getPrice());
        companyName.setText(companyDetails1.getCompanyName());
//        companyLogoView.setBackgroundResource(companyDetails1.getCompanyLogo());
    }

    @Override
    public void CheckboxChange(ArrayList<CompanySpinner> listStateSelected) {
        companyFilter = new ArrayList<>();
        for (int j = 0; j < listStateSelected.size(); j++) {
            for (int i=0; i < company.size(); i++) {
                if (company.get(i).getSelected()) {
                    company.get(i).setSelected(false);
                }
                for (int k=0; k < company.get(i).getCompanyCoverList().size(); k++) {
                    if (company.get(i).getCompanyCoverList().get(k).getCoverName().equalsIgnoreCase(listStateSelected.get(j).getTitle())) {
                        companyFilter.add(company.get(i));
                    }
                }
            }
        }
        if (companyFilter.size() > 0) {
            companyAdapter= new CompanyAdapter(companyFilter,getApplicationContext());
            companyAdapter.setOnShareClickedListener(this);
            companyList.setAdapter(companyAdapter);
        } else {
            selectedQuotes.check(radio_allquote.getId());
            companyAdapter= new CompanyAdapter(company,getApplicationContext());
            companyAdapter.setOnShareClickedListener(this);
            companyList.setAdapter(companyAdapter);
        }
        spinner.onFinishTemporaryDetach();
    }

    private static int[] getFiveLowest(int[] array) {
        int[] lowestValues = new int[5];
        if (array.length > 5) {
            System.arraycopy(array, 0, lowestValues, 0, 5);
            return lowestValues;
        } else {
            return array;
        }
    }

    private static int[] removeDuplicateVlaue(int[] array) {
        //example input
        int input[] = array;
        //use list because the size is dynamical can change
        List<Integer> result = new ArrayList<Integer>();

        for(int i=0; i<input.length; i++)
        {
            boolean match = false;
            for(int j=0; j<result.size(); j++)
            {
                //if the list contains any input element make match true
                if(result.get(j) == input[i])
                    match = true;
            }
            //if there is no matching we can add the element to the result list
            if(!match)
                result.add(input[i]);
        }
        int[] finalArray = new int[result.size()];
        // Print the result
        for(int i=0; i<result.size(); i++) {
            finalArray[i] = result.get(i);
        }
        System.out.print(result + ", ");
        return finalArray;
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


                    editor.putString(Constants.COMPANY_NAME, company.get(currentIndex).getCompanyName());
                    editor.putInt(Constants.COMPANY_LOGO, company.get(currentIndex).getCompanyLogo());
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
