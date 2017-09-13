package com.netsol.rms.aggregator.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.netsol.rms.aggregator.activity.other.Company;
import com.netsol.rms.aggregator.activity.other.CompanyCover;
import com.netsol.rms.aggregator.activity.other.CompanyDetails;

import java.util.ArrayList;

/**
 * Created by macmini on 7/26/17.
 */

public class dummy extends AppCompatActivity {

    ArrayList<Company> selectedcompanyArrayList;
    ArrayList<CompanyCover> selectedcompanyCoverArrayList;
    ArrayList<String> companyCoverNames;
    ArrayList<String> companyCoverIDUnique;
    Company selectedcompanyList;

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_compare);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linearLayout = (LinearLayout) findViewById(R.id.linear2);

        selectedcompanyArrayList = new ArrayList<>();
        selectedcompanyArrayList = Insurers.selectedcompanyArrayList;

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

//        for(int i = 0 ; i <= selectedcompanyArrayList.size(); i++) {
//
//            Company arraySize  = selectedcompanyArrayList.get(i);
//
//            for(int j = 0 ; j <= 7 ; j++) {
//
//            }
//
//            LayoutInflater inflater = LayoutInflater.from(this);
//            View inflatedLayout= inflater.inflate(R.layout.comapny_compare_view, null, false);
//
//            ImageView logo = (ImageView) inflatedLayout.findViewById(R.id.company_compare_logo);
//            TextView name = (TextView) inflatedLayout.findViewById(R.id.company_compare_name);
//            TextView primium = (TextView) inflatedLayout.findViewById(R.id.company_compare_primium);
//            TextView product = (TextView) inflatedLayout.findViewById(R.id.company_compare_product);
//            TextView name_vlaue = (TextView) inflatedLayout.findViewById(R.id.company_compare_name_vlaue);
//            TextView primium_vlaue = (TextView) inflatedLayout.findViewById(R.id.company_compare_primium_vlaue);
//            TextView product_vlaue = (TextView) inflatedLayout.findViewById(R.id.company_compare_product_vlaue);
//
//            Button companyComapreApply = (Button) inflatedLayout.findViewById(R.id.button_company_compare_apply);
//
//            LinearLayout text_logo = (LinearLayout) inflatedLayout.findViewById(R.id.imageLogo);
//            LinearLayout imageViewLogo = (LinearLayout) inflatedLayout.findViewById(R.id.imageViewLogo);
//            LinearLayout image_company_compare_cover = (LinearLayout) inflatedLayout.findViewById(R.id.image_company_compare_cover);
//            LinearLayout company_compare_attribute_value = (LinearLayout) inflatedLayout.findViewById(R.id.company_compare_attribute_value);
//            LinearLayout company_compare_fees_value = (LinearLayout) inflatedLayout.findViewById(R.id.company_compare_fees_value);
//            LinearLayout company_compare_coverPremium_value = (LinearLayout) inflatedLayout.findViewById(R.id.company_compare_coverPremium_value);
//
//            TextView heading = (TextView) inflatedLayout.findViewById(R.id.heading);
//            TextView heading_cashless = (TextView) inflatedLayout.findViewById(R.id.heading_cashless);
//            TextView heading_attributes = (TextView) inflatedLayout.findViewById(R.id.heading_attributes);
//            TextView heading_fees = (TextView) inflatedLayout.findViewById(R.id.heading_fees);
//            TextView heading_coverPremium = (TextView) inflatedLayout.findViewById(R.id.heading_coverPremium);
//
//            if (i == 0) {
//                imageViewLogo.setVisibility(View.GONE);
//                image_company_compare_cover.setVisibility(View.GONE);
//                company_compare_attribute_value.setVisibility(View.GONE);
//                company_compare_fees_value.setVisibility(View.GONE);
//                company_compare_coverPremium_value.setVisibility(View.GONE);
//                companyComapreApply.setVisibility(View.GONE);
//                name_vlaue.setVisibility(View.GONE);
//                primium_vlaue.setVisibility(View.GONE);
//                product_vlaue.setVisibility(View.GONE);
//
//                name.setVisibility(View.VISIBLE);
//                primium.setVisibility(View.VISIBLE);
//                product.setVisibility(View.VISIBLE);
//                text_logo.setVisibility(View.VISIBLE);
//                heading_cashless.setVisibility(View.VISIBLE);
//                heading_attributes.setVisibility(View.VISIBLE);
//                heading_fees.setVisibility(View.VISIBLE);
//                heading_coverPremium.setVisibility(View.VISIBLE);
//
//                name.setText("Company Name");
//                primium.setText("Primium");
//                product.setText("Product Name");
//            } else {
//                text_logo.setVisibility(View.GONE);
//                heading_cashless.setVisibility(View.GONE);
//                heading_attributes.setVisibility(View.GONE);
//                heading_fees.setVisibility(View.GONE);
//                heading_coverPremium.setVisibility(View.GONE);
//                name.setVisibility(View.GONE);
//                primium.setVisibility(View.GONE);
//                product.setVisibility(View.GONE);
//
//                name_vlaue.setVisibility(View.VISIBLE);
//                primium_vlaue.setVisibility(View.VISIBLE);
//                product_vlaue.setVisibility(View.VISIBLE);
//                imageViewLogo.setVisibility(View.VISIBLE);
//                image_company_compare_cover.setVisibility(View.VISIBLE);
//                company_compare_attribute_value.setVisibility(View.VISIBLE);
//                company_compare_fees_value.setVisibility(View.VISIBLE);
//                company_compare_coverPremium_value.setVisibility(View.VISIBLE);
//                companyComapreApply.setVisibility(View.VISIBLE);
//
//                logo.setBackgroundResource(selectedcompanyArrayList.get(i-1).getCompanyViewLogo());
//                name_vlaue.setText(selectedcompanyArrayList.get(i-1).getCompanyName());
//                primium_vlaue.setText(selectedcompanyArrayList.get(i-1).getPrice());
//                ArrayList<CompanyCover> companyCoverArrayList = selectedcompanyArrayList.get(i-1).getCompanyCoverList();
//
//                String companyCover = "";
//                String companyCoverId = "";
//                for (int j = 0; j < companyCoverIDUnique.size(); j++) {
//                    boolean isExist = false;
//                    companyCover = companyCoverNames.get(j);
//                    companyCoverId = companyCoverIDUnique.get(j);
//                    for (int k = 0; k < companyCoverArrayList.size(); k++) {
//                        if (companyCoverId.equalsIgnoreCase(companyCoverArrayList.get(k).getCoverId())) {
//                            isExist = true;
//                            break;
//                        }
//                    }
//                    LinearLayout innerLinearLayout = new LinearLayout(this);
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//                    ImageView imageView = new ImageView(this);
//                    imageView.setId(j);
//                    imageView.setMaxHeight(10);
//                    imageView.setMaxWidth(10);
//                    params.topMargin = 5;
//                    params.bottomMargin = 5;
//                    imageView.setLayoutParams(params);
//                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//
//                    if (!isExist) {
//                        imageView.setImageResource(R.drawable.wrong_tick);
//                    } else {
//                        imageView.setImageResource(R.drawable.right_tick);
//                    }
//
//                    innerLinearLayout.addView(imageView);
//
//                    TextView textView = new TextView(this);
//                    textView.setId(j);
//                    params.rightMargin = 10;
//                    params.leftMargin = 10;
//                    params.topMargin = 5;
//                    params.bottomMargin = 5;
//                    textView.setTextAppearance(this, android.R.style.TextAppearance_Small);
//                    textView.setTextColor(Color.BLACK);
//                    textView.setText(companyCover);
//                    textView.setLayoutParams(params);
//                    innerLinearLayout.addView(textView);
//
//                    image_company_compare_cover.addView(innerLinearLayout);
//                }
//
//                product_vlaue.setText(selectedcompanyArrayList.get(i-1).getCompanyProductName());
//
//                LinearLayout innerLinearLayout = new LinearLayout(this);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                TextView textView = new TextView(this);
//                textView.setId(i);
//                params.rightMargin = 10;
//                params.leftMargin = 10;
//                params.topMargin = 5;
//                params.bottomMargin = 5;
//                textView.setTextAppearance(this, android.R.style.TextAppearance_Small);
//                textView.setTextColor(Color.BLACK);
//                textView.setText(selectedcompanyArrayList.get(i-1).getCompanyAttribute());
//                textView.setLayoutParams(params);
//                innerLinearLayout.addView(textView);
//                company_compare_attribute_value.addView(innerLinearLayout);
//
//                LinearLayout innerLinearLayout1 = new LinearLayout(this);
//                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                TextView textView1 = new TextView(this);
//                textView1.setId(i);
//                params1.rightMargin = 10;
//                params1.leftMargin = 10;
//                params1.topMargin = 5;
//                params1.bottomMargin = 5;
//                textView1.setTextAppearance(this, android.R.style.TextAppearance_Small);
//                textView1.setTextColor(Color.BLACK);
//                textView1.setText(selectedcompanyArrayList.get(i-1).getCompanyFees());
//                textView1.setLayoutParams(params1);
//                innerLinearLayout1.addView(textView1);
//                company_compare_fees_value.addView(innerLinearLayout1);
//
//                LinearLayout innerLinearLayout2 = new LinearLayout(this);
//                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                TextView textView2 = new TextView(this);
//                textView2.setId(i);
//                params2.rightMargin = 10;
//                params2.leftMargin = 10;
//                params2.topMargin = 5;
//                params2.bottomMargin = 5;
//                textView2.setTextAppearance(this, android.R.style.TextAppearance_Small);
//                textView2.setTextColor(Color.BLACK);
//                textView2.setText(selectedcompanyArrayList.get(i-1).getCompanyCoverPremium());
//                textView2.setLayoutParams(params2);
//                innerLinearLayout2.addView(textView2);
//                company_compare_coverPremium_value.addView(innerLinearLayout2);
//
//                companyComapreApply.setId(i-1);
//            }
//            linearLayout.addView(inflatedLayout);
//
//            companyComapreApply.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int id = v.getId();
//                    selectedcompanyList = selectedcompanyArrayList.get(id);
////                    Intent apply = new Intent(CompareCompanyActivity.this, OwnerDetailsActivity.class);
////                    apply.putExtra("FROMINSURER", true);
////                    apply.putExtra("ComapnyName", selectedcompanyList.getCompanyName());
////                    apply.putExtra("CompanyLogo", selectedcompanyList.getCompanyLogo());
////                    startActivity(apply);
//                }
//            });
//        }
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
