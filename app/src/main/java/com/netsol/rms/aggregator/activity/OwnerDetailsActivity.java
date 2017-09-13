package com.netsol.rms.aggregator.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.netsol.rms.aggregator.R;
import com.netsol.rms.aggregator.activity.other.AsyncServiceCall;
import com.netsol.rms.aggregator.activity.other.CompanyCover;
import com.netsol.rms.aggregator.activity.other.Constants;
import com.netsol.rms.aggregator.activity.other.DatabaseManager;
import com.netsol.rms.aggregator.activity.other.NetworkManager;
import com.netsol.rms.aggregator.activity.other.WebserviceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by macmini on 7/24/17.
 */

public class OwnerDetailsActivity extends AppCompatActivity {

    LinearLayout selectedImageLayout;
    TextView name;
    TextView age;
    TextView mobileNo;
    TextView nationalID;
    TextView email;
    EditText address;
    Button camera;
    Button gallery;
    Button buttonContinue;
    Bitmap add;

    int height;
    int width;
//    String companyName;
//    int companyLogo;
    boolean isInsurer = false;
    boolean isCamera = false;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    DatabaseManager databaseManager;
    private WebserviceManager _webServiceManager;
    public static ProgressDialog progressDialogReset;
    public static ArrayList<String> imagesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        camera = (Button) findViewById(R.id.button_choose_camera);
        gallery = (Button) findViewById(R.id.button_choose_gallary);
        buttonContinue = (Button) findViewById(R.id.button_continue);
        selectedImageLayout = (LinearLayout) findViewById(R.id.linear1);
        name = (TextView) findViewById(R.id.apply_name);
        age = (TextView) findViewById(R.id.apply_age);
        mobileNo = (TextView) findViewById(R.id.apply_mobileNo);
        email = (TextView) findViewById(R.id.apply_email);
        nationalID = (TextView) findViewById(R.id.apply_nationalID);
        address = (EditText) findViewById(R.id.address);

        sharedPref = this.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        _webServiceManager = new WebserviceManager(this);
        databaseManager = new DatabaseManager(this);

        String savedName = sharedPref.getString(Constants.FULL_NAME, "");
        String savedAge = sharedPref.getString(Constants.AGE, "");
        String savedMobile = sharedPref.getString(Constants.MOBILE_NUMBER, "");
        String savedEmail = sharedPref.getString(Constants.EMAIL, "");
        String savedNationalId = sharedPref.getString(Constants.NATIONAL_ID, "");

        name.setText(String.format("Name: %s", savedName));
        age.setText(String.format("Age: %s", savedAge));
        mobileNo.setText(String.format("Mobile No: %s", savedMobile));
        email.setText(String.format("Email: %s", savedEmail));
        nationalID.setText(String.format("National ID: %s", savedNationalId));

        add = BitmapFactory.decodeResource(
                getResources(), R.drawable.button_add);
        height = add.getHeight();
        width = add.getWidth();

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null) {
            imagesList = b.getStringArrayList("selectedItems");
            isInsurer = b.getBoolean("FROMINSURER");
            isCamera = b.getBoolean("isCamera");
            if (isInsurer) {
                imagesList = new ArrayList<>();
            }
            for (int i = 0; i < imagesList.size(); i++) {
                ImageView imageView = new ImageView(this);
                imageView.setId(i);
                imageView.setPadding(2, 2, 2, 2);
                    Glide.with(OwnerDetailsActivity.this)
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

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCamera = true;
                Intent apply = new Intent(OwnerDetailsActivity.this, ImageAttachmentActivity.class);
                apply.putExtra("FROMCAMERA", true);
                startActivity(apply);
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCamera = false;
                Intent apply = new Intent(OwnerDetailsActivity.this, MultiPhotoSelectActivity.class);
                apply.putExtra("FROMGALLERY", true);
                apply.putStringArrayListExtra("selectedItems", imagesList);
                startActivity(apply);
            }
        });

        selectedImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent apply = new Intent(OwnerDetailsActivity.this, ImageAttachmentActivity.class);
                apply.putExtra("FROMIMAGE", true);
                apply.putExtra("isCamera", isCamera);
                apply.putStringArrayListExtra("selectedItems", imagesList);
                startActivity(apply);
            }
        });

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String savedNationalID = sharedPref.getString(Constants.NATIONAL_ID, "");
                String userIdFromDb = databaseManager.getUserID(savedNationalID);

                if (imagesList.size() > 0) {
                    sendPolicy(userIdFromDb, imagesList);
                } else {
                    Toast.makeText(OwnerDetailsActivity.this, "Please Upload your Ducuments", Toast.LENGTH_LONG).show();
                }
//                for (int i = 0; i < imagesList.size(); i++) {
//                    String fileName = imagesList.get(i) + i;
//                    editor.putString(Constants.IMAGE_FILE_NAME + i, fileName);
//                }
//                String address1 = address.getText().toString();
//                editor.putString(Constants.ADDRESS, address1);
//                editor.apply();
//                Intent apply = new Intent(OwnerDetailsActivity.this, ReviewActivity.class);
//                apply.putStringArrayListExtra("selectedItems", imagesList);
//                startActivity(apply);
            }
        });

        if(!checkPermission()){
            requestPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (CameraPermission) {
                    Toast.makeText(OwnerDetailsActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(OwnerDetailsActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    public boolean checkPermission() {
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        return SecondPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(OwnerDetailsActivity.this, new String[]
                {
                        WRITE_EXTERNAL_STORAGE
                }, 0);

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

    public void sendPolicy(final String id, final ArrayList<String> imageList) {
        try {
            AsyncServiceCall _companyDetails = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialogReset = ProgressDialog.show(
                            OwnerDetailsActivity.this,
                            OwnerDetailsActivity.this.getResources().getString(
                                    R.string.progress_heading),
                            OwnerDetailsActivity.this.getResources().getString(
                                    R.string.progress_text));
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    return _webServiceManager.sendUserInformation(id, "", "", "", "", "", "", "",
                            "", "", "", "", "", "", "", "", "", imageList);
                }

                @Override
                protected void onPostExecute(Object resultObj) {
                    String result = (String) resultObj;

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
                    for (int i = 0; i < imagesList.size(); i++) {
                        String fileName = imagesList.get(i) + i;
                        editor.putString(Constants.IMAGE_FILE_NAME + i, fileName);
                    }

                    if ((progressDialogReset != null) && (progressDialogReset.isShowing())) {
                        try {
                            progressDialogReset.dismiss();
                        } catch (Exception ex) {
                            Log.e(Constants.LOG_RMSAGG, "Exception is " + Log.getStackTraceString(ex));
                        }
                    }

                    String address1 = address.getText().toString();
                    editor.putString(Constants.ADDRESS, address1);
                    editor.apply();
                    Intent apply = new Intent(OwnerDetailsActivity.this, ReviewActivity.class);
                    apply.putStringArrayListExtra("selectedItems", imagesList);
                    startActivity(apply);
                    super.onPostExecute(result);
                }
            };
            try {

                if (NetworkManager.isNetAvailable(OwnerDetailsActivity.this)) {
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
        new AlertDialog.Builder(OwnerDetailsActivity.this)
                .setTitle(OwnerDetailsActivity.this.getString(R.string.network_availability_heading))
                .setMessage(OwnerDetailsActivity.this.getString(R.string.network_availability))

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true).show();

    }
}
