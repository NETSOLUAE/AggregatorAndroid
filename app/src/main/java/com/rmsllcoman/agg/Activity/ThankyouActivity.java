package com.rmsllcoman.agg.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.rmsllcoman.agg.Controller.DatabaseManager;

import com.rmsllcoman.agg.R;

/**
 * Created by macmini on 7/26/17.
 */

public class ThankyouActivity extends AppCompatActivity {
    Button goBack;
    private DatabaseManager _dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thank You");

        goBack = (Button) findViewById(R.id.button_go_back);
        _dbManager = new DatabaseManager(this);
        _dbManager.clearUserInfo();
        OwnerDetailsActivity.imagesList.clear();

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent apply = new Intent(ThankyouActivity.this, MainActivity.class);
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
            Intent apply = new Intent(ThankyouActivity.this, MainActivity.class);
            startActivity(apply);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}