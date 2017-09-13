package com.netsol.rms.aggregator.activity;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.netsol.rms.aggregator.R;

import java.util.ArrayList;

/**
 * Created by macmini on 7/22/17.
 */

public class CompareActivity extends AppCompatActivity {

    TabHost tabHost;
    LocalActivityManager mLocalActivityManager;
    static Resources ressources;
    public static ArrayList<String> companyCoverNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabHost = (TabHost) findViewById(R.id.tabHost);
        mLocalActivityManager = new LocalActivityManager(this, false);

        tabHost.setup(mLocalActivityManager);
        mLocalActivityManager.dispatchCreate(savedInstanceState); //after the tab's setup is called, you have to call this or it wont work
        ressources = getResources();

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null) {
            companyCoverNames = b.getStringArrayList("companyCoverNames");
        }

        Intent intentMemberDetails = new Intent(CompareActivity.this, Insurers.class);
        TabHost.TabSpec tabSpecMemberDetails = tabHost
                .newTabSpec("Insurers")
                .setIndicator("Insurers")
                .setContent(intentMemberDetails);
        tabHost.addTab(tabSpecMemberDetails);

        Intent intentInformation = new Intent().setClass(this, PersonalDetails.class);
        TabHost.TabSpec tabSpecInformation = tabHost
                .newTabSpec("PersonalDetails")
                .setIndicator("Personal Details")
                .setContent(intentInformation);
        tabHost.addTab(tabSpecInformation);

        Intent intentPolicyDetails = new Intent().setClass(this, VehicleDetailsTabActivity.class);
        TabHost.TabSpec tabSpecPolicyDetails = tabHost
                .newTabSpec("VehicleDetails")
                .setIndicator("Vehicle Details")
                .setContent(intentPolicyDetails);
        tabHost.addTab(tabSpecPolicyDetails);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                TabWidget widget = tabHost.getTabWidget();
                for(int i = 0; i < widget.getChildCount(); i++) {
                    View v = widget.getChildAt(i);
                    if (tabId.equalsIgnoreCase("Insurers") && i == 0){
                        TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                        tv.setTextColor(ressources.getColor(R.color.colorPrimary));
                        tv.setAllCaps(false);
                    } else if (tabId.equalsIgnoreCase("PersonalDetails") && i == 1){
                        TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                        tv.setTextColor(ressources.getColor(R.color.colorPrimary));
                        tv.setAllCaps(false);
                    } else if (tabId.equalsIgnoreCase("VehicleDetails") && i == 2){
                        TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                        tv.setTextColor(ressources.getColor(R.color.colorPrimary));
                        tv.setAllCaps(false);
                    } else {
                        TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                        tv.setTextColor(ressources.getColor(R.color.black));
                        tv.setAllCaps(false);
                    }
                }
            }
        });

        TextView tv1 = (TextView) tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tv1.setTextAppearance(R.style.TextAppearance_AppCompat_Widget_ActionBar_Subtitle);
        } else {
            tv1.setTextAppearance(this, R.style.TextAppearance_AppCompat_Widget_ActionBar_Subtitle);
        }
        tv1.setTextColor(ressources.getColor(R.color.black));
        tv1.setAllCaps(false);

        TextView tv2 = (TextView) tabHost.getTabWidget().getChildAt(2).findViewById(android.R.id.title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tv2.setTextAppearance(R.style.TextAppearance_AppCompat_Widget_ActionBar_Subtitle);
        } else {
            tv2.setTextAppearance(this, R.style.TextAppearance_AppCompat_Widget_ActionBar_Subtitle);
        }
        tv2.setTextColor(ressources.getColor(R.color.black));
        tv2.setAllCaps(false);

        tabHost.setCurrentTab(0);
        TextView tv = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tv.setTextAppearance(R.style.TextAppearance_AppCompat_Widget_ActionBar_Subtitle);
        } else {
            tv.setTextAppearance(this, R.style.TextAppearance_AppCompat_Widget_ActionBar_Subtitle);
        }
        tv.setTextColor(ressources.getColor(R.color.colorPrimary));
        tv.setAllCaps(false);
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

    @Override
    protected void onPause() {
        super.onPause();
        mLocalActivityManager.dispatchPause(isFinishing()); //you have to manually dispatch the pause msg
    }

    @Override
    public void onResume() {
        super.onResume();
        mLocalActivityManager.dispatchResume(); //you have to manually dispatch the resume msg
    }
}
