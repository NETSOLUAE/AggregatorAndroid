package com.rmsllcoman.agg.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.rmsllcoman.agg.other.AsyncServiceCall;
import com.rmsllcoman.agg.other.Constants;
import com.rmsllcoman.agg.Controller.DatabaseManager;
import com.rmsllcoman.agg.Controller.NetworkManager;
import com.rmsllcoman.agg.Controller.WebserviceManager;

import java.io.File;

import com.rmsllcoman.agg.R;

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {

    private WebserviceManager _webServiceManager;
    private DatabaseManager _dbManager;
    private static int SPLASH_TIME_OUT = 500;
    Animation animation;
    ImageView imageview;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        _webServiceManager = new WebserviceManager(this);
        _dbManager = new DatabaseManager(this);
        imageview=(ImageView)findViewById(R.id.zoom_image);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        // create database
        if (!isDatabaseExist()) {
            createDatabase();
        } else {
            _dbManager.clearUserInfo();
        }

        getVehicleUsage();
    }

    // Check whether the database exists
    private boolean isDatabaseExist() {
        File dbFile = getApplicationContext().getDatabasePath("RMSAGGDB.db");
        return dbFile.exists();
    }

    // Create the tables
    private void createDatabase() {
        _dbManager.createDb();
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent login = new Intent (SplashActivity.this, MainActivity.class);
                startActivity(login);
                finish();
            }
        }, 100);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void getVehicleUsage() {
        try {
            AsyncServiceCall _vehicleUsage = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    return _webServiceManager.getVehicleDetails();
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    String result = (String) resultObj;
                    if (result.equalsIgnoreCase("Updated")) {
                        getVehicleMake();
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        new AlertDialog.Builder(SplashActivity.this)
                                .setTitle("Alert")
                                .setMessage(SplashActivity.this.getString(R.string.network_failure))

                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setCancelable(true).show();
                    }
                    super.onPostExecute(result);
                }

            };
            try {

                if (NetworkManager.isNetAvailable(SplashActivity.this)) {
                    _vehicleUsage.execute(0);
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

    public void getVehicleMake() {
        try {
            AsyncServiceCall _vehicleMake = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    return _webServiceManager.getVehicleMake();
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    String result = (String) resultObj;
                    if (result.equalsIgnoreCase("Updated")) {
                        getVehicleModel();
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        new AlertDialog.Builder(SplashActivity.this)
                                .setTitle("Alert")
                                .setMessage(SplashActivity.this.getString(R.string.network_failure))

                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setCancelable(true).show();
                    }
                    super.onPostExecute(result);
                }

            };
            try {

                if (NetworkManager.isNetAvailable(SplashActivity.this)) {
                    _vehicleMake.execute(0);
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

    public void getVehicleModel() {
        try {
            AsyncServiceCall _vehicleModel = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    return _webServiceManager.getVehicleModel();
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    String result = (String) resultObj;
                    if (result.equalsIgnoreCase("Updated")) {
                        getRto();
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        new AlertDialog.Builder(SplashActivity.this)
                                .setTitle("Alert")
                                .setMessage(SplashActivity.this.getString(R.string.network_failure))

                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setCancelable(true).show();
                    }
                    super.onPostExecute(result);
                }

            };
            try {

                if (NetworkManager.isNetAvailable(SplashActivity.this)) {
                    _vehicleModel.execute(0);
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

    public void getRto() {
        try {
            AsyncServiceCall _rto = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    return _webServiceManager.getRto();
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    progressBar.setVisibility(View.INVISIBLE);
                    String result = (String) resultObj;
                    if (result.equalsIgnoreCase("Updated")) {

                        animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                imageview.startAnimation(animation);
                            }
                        }, SPLASH_TIME_OUT);
                        animation.setAnimationListener(SplashActivity.this);
                    } else {
                        new AlertDialog.Builder(SplashActivity.this)
                                .setTitle("Alert")
                                .setMessage(SplashActivity.this.getString(R.string.network_failure))

                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setCancelable(true).show();
                    }
                    super.onPostExecute(result);
                }

            };
            try {

                if (NetworkManager.isNetAvailable(SplashActivity.this)) {
                    _rto.execute(0);
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

        progressBar.setVisibility(View.INVISIBLE);
        new AlertDialog.Builder(SplashActivity.this)
                .setTitle(SplashActivity.this.getString(R.string.network_availability_heading))
                .setMessage(SplashActivity.this.getString(R.string.network_availability))

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true).show();

    }
}
