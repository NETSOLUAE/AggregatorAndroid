package com.rmsllcoman.agg.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.rmsllcoman.agg.R;

/**
 * Created by macmini on 7/26/17.
 */

public class PaymentOptionActivity extends AppCompatActivity {
    Button continuePayment;
//    Spinner bankList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Payment Options");

        continuePayment = (Button) findViewById(R.id.button_payment_option);
//        bankList = (Spinner) findViewById(R.id.bank_list);


//        final String[] select_Bank_type = {
//                "Emirates NDB", "ADCB", "Standard Chartered Bank", "City Bank", "Emirates Islamic"};
//        ArrayList<Bank> listBank = new ArrayList<>();
//        for (String aSelect_qualification : select_Bank_type) {
//            Bank fuel = new Bank();
//            fuel.setNameType(aSelect_qualification);
//            listBank.add(fuel);
//        }
//        SpinnerAdapterBank myAdapter = new SpinnerAdapterBank(this, 0,
//                listBank);
//        bankList.setAdapter(myAdapter);

        continuePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent apply = new Intent(PaymentOptionActivity.this, MakePaymentActivity.class);
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
