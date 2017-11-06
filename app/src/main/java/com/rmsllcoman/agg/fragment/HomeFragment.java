package com.rmsllcoman.agg.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.rmsllcoman.agg.Activity.AboutYourselfActivity;
import com.rmsllcoman.agg.R;

import com.rmsllcoman.agg.other.AsyncServiceCall;
import com.rmsllcoman.agg.other.Constants;
import com.rmsllcoman.agg.Controller.DatabaseManager;
import com.rmsllcoman.agg.Controller.NetworkManager;
import com.rmsllcoman.agg.Model.RTO;
import com.rmsllcoman.agg.Adapter.SpinnerAdapterRTO;
import com.rmsllcoman.agg.Adapter.SpinnerAdapterVUsage;
import com.rmsllcoman.agg.Adapter.SpinnerAdapterVehicleMake;
import com.rmsllcoman.agg.Adapter.SpinnerAdapterVehicleModel;
import com.rmsllcoman.agg.Adapter.SpinnerAdapterVehicleType;
import com.rmsllcoman.agg.Adapter.SpinnerAdapterVehicleVariant;
import com.rmsllcoman.agg.Adapter.SpinnerAdapterYOM;
import com.rmsllcoman.agg.Model.VehicleMake;
import com.rmsllcoman.agg.Model.VehicleModel;
import com.rmsllcoman.agg.Model.VehicleType;
import com.rmsllcoman.agg.Model.VehicleUsage;
import com.rmsllcoman.agg.Model.VehicleVarient;
import com.rmsllcoman.agg.Controller.WebserviceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static String orginalPrice = "";
    static String vehicleOrginalPrice = "";
    RadioGroup carType;
    Button next;
    Spinner spinnerVehicleUsage;
    Spinner spinnerVehicleType;
    Spinner spinnerVehicleMake;
    Spinner spinnerVehicleModel;
    Spinner spinnerVehicleVarient;
    Spinner spinnerRTO;
    Spinner spinnerYOM;
    static EditText homePrice;
    static LinearLayout registrationDate;
    static EditText home_registration_dateText;
    static LinearLayout insuranceDate;
    static EditText home_insurance_dateText;
    public static String errorMessage = "";
    static Context context;
    String selectedUsage;
    static String selectedUsageId;
    String selectedType;
    static String selectedTypeId;
    String selectedMake;
    static String makeId;
    String selectedModel;
    static String modelId;
    String selectedVarient;
    static String selectedVarientId;
    String selectedRTO;
    static String selectedRTOId;
    static String selectedYOM;
    static boolean newCar = true;
    static boolean isVehicleDate = false;
    private Handler mHandler;
    ArrayList<VehicleUsage> vehicleUsage;
    ArrayList<VehicleType> vehicleType;
    ArrayList<VehicleMake> vehiclemake;
    ArrayList<VehicleModel> vehicleModelList;
    ArrayList<VehicleVarient> vehicleVarient;
    ArrayList<RTO> rto;
    static ArrayList<String> yom;
    SharedPreferences sharedPref;
    static SharedPreferences.Editor editor;
    static DatabaseManager databaseManager;
    private static WebserviceManager _webServiceManager;
    public static ProgressDialog progressDialogReset;

    SpinnerAdapterVehicleType typeAdapter;
    SpinnerAdapterVehicleModel adapterVehicleModel;
    SpinnerAdapterVehicleVariant variantAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        spinnerVehicleUsage = (Spinner) view.findViewById(R.id.vehicle_usage);
        spinnerVehicleType = (Spinner) view.findViewById(R.id.vehicle_type);
        spinnerVehicleMake = (Spinner) view.findViewById(R.id.vehicle_make);
        spinnerVehicleModel = (Spinner) view.findViewById(R.id.vehicle_model);
        spinnerVehicleVarient = (Spinner) view.findViewById(R.id.vehicle_varient);
        spinnerRTO = (Spinner) view.findViewById(R.id.vehicle_select_rto);
        spinnerYOM = (Spinner) view.findViewById(R.id.vehicle_yom);
        carType = (RadioGroup) view.findViewById(R.id.myRadioGroup);
        next = (Button) view.findViewById(R.id.next);
        homePrice = (EditText) view.findViewById(R.id.home_price);
        registrationDate = (LinearLayout) view.findViewById(R.id.home_registration_date);
        home_registration_dateText = (EditText) view.findViewById(R.id.home_registration_dateText);
        insuranceDate = (LinearLayout) view.findViewById(R.id.home_insurance_start_date);
        home_insurance_dateText = (EditText) view.findViewById(R.id.home_insurance_start_dateText);

        context = super.getContext();
        mHandler = new Handler();
        databaseManager = new DatabaseManager(context);
        _webServiceManager = new WebserviceManager(context);

        int currentyear = Calendar.getInstance().get(Calendar.YEAR);
        yom = new ArrayList<>();
        yom.add("Select Year");
        int year = currentyear;
        for (int i = 0; i < 30; i++) {
            if (i == 0) {
                yom.add(String.valueOf(year));
            } else {
                year = year - 1;
                yom.add(String.valueOf(year));
            }
        }
        registrationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVehicleDate = true;
                DialogFragment dialogfragment = new DatePickerDialogClass();
                dialogfragment.show(getActivity().getFragmentManager(), "Date Time");
            }
        });

        home_registration_dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVehicleDate = true;
                DialogFragment dialogfragment = new DatePickerDialogClass();
                dialogfragment.show(getActivity().getFragmentManager(), "Date Time");
            }
        });
        insuranceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String vehicleRegistrationYear = "";
                String registrationDateText = home_registration_dateText.getText().toString();

                if (!registrationDateText.equalsIgnoreCase("")) {
                    vehicleRegistrationYear = registrationDateText.substring(6,10);
                }

                if (selectedUsage.equalsIgnoreCase("Select Usage") || selectedType.equalsIgnoreCase("Select Type") || selectedMake.equalsIgnoreCase("Select Make")
                        || selectedModel.equalsIgnoreCase("Select Model") || selectedVarient.equalsIgnoreCase("Select Variant") || selectedRTO.equalsIgnoreCase("Select Place")
                        || selectedYOM.equalsIgnoreCase("Select Year") || registrationDateText.equalsIgnoreCase("")) {
                    Toast.makeText(context, "All fields are mandatory",
                            Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(selectedYOM) > Integer.parseInt(vehicleRegistrationYear)) {
                    Toast.makeText(context, "Year of Manufacture cannot exceed Date of Registration.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    isVehicleDate = false;
                    DialogFragment dialogfragment = new DatePickerDialogClass();
                    dialogfragment.show(getActivity().getFragmentManager(), "Date Time");
                }
            }
        });

        home_insurance_dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vehicleRegistrationYear = "";
                String registrationDateText = home_registration_dateText.getText().toString();

                if (!registrationDateText.equalsIgnoreCase("")) {
                    vehicleRegistrationYear = registrationDateText.substring(6,10);
                }

                if (selectedUsage.equalsIgnoreCase("Select Usage") || selectedType.equalsIgnoreCase("Select Type") || selectedMake.equalsIgnoreCase("Select Make")
                        || selectedModel.equalsIgnoreCase("Select Model") || selectedVarient.equalsIgnoreCase("Select Variant") || selectedRTO.equalsIgnoreCase("Select Place")
                        || selectedYOM.equalsIgnoreCase("Select Year") || registrationDateText.equalsIgnoreCase("")) {
                    Toast.makeText(context, "All fields are mandatory",
                            Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(selectedYOM) > Integer.parseInt(vehicleRegistrationYear)) {
                    Toast.makeText(context, "Year of Manufacture cannot exceed Date of Registration.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    isVehicleDate = false;
                    DialogFragment dialogfragment = new DatePickerDialogClass();
                    dialogfragment.show(getActivity().getFragmentManager(), "Date Time");
                }
            }
        });

        sharedPref = getActivity().getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        vehicleUsage = databaseManager.getVehicleUsage();
//        if (vehicleUsage.size() > 0) {
        SpinnerAdapterVUsage myAdapter = new SpinnerAdapterVUsage(getActivity(), 0, vehicleUsage);
        spinnerVehicleUsage.setAdapter(myAdapter);
//        } else {
//            VehicleUsage vehicleType1 = new VehicleUsage();
//            vehicleType1.setID(0);
//            vehicleType1.setVehicleName("");
//            vehicleUsage.add(vehicleType1);
//            SpinnerAdapterVUsage myAdapter = new SpinnerAdapterVUsage(getActivity(), 0, vehicleUsage);
//            spinnerVehicleUsage.setAdapter(myAdapter);
//        }

        vehicleType = databaseManager.getVehicleType();
        if (vehicleType != null && vehicleType.size() > 0) {
            typeAdapter = new SpinnerAdapterVehicleType(getActivity(), 0, vehicleType);
            spinnerVehicleType.setAdapter(typeAdapter);
            spinnerVehicleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedType = vehicleType.get(position).getVehicleType();
                    selectedTypeId = vehicleType.get(position).getVehicleId();
                    home_insurance_dateText.setText("");
                    homePrice.setText("");
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
//      else {
//            vehicleType = new ArrayList<>();
//            VehicleType vehicleType1 = new VehicleType();
//            vehicleType1.setID("None");
//            vehicleType1.setGvwRequired("None");
//            vehicleType1.setVehicleId("None");
//            vehicleType1.setVehicleType("None");
//            vehicleType1.setUsageType("None");
//            vehicleType.add(vehicleType1);
//            typeAdapter = new SpinnerAdapterVehicleType(getActivity(), 0, vehicleType);
//            spinnerVehicleType.setAdapter(typeAdapter);
//        }

        vehicleVarient = databaseManager.getVehicleVarient();
        if (vehicleVarient != null && vehicleVarient.size() > 0) {
            variantAdapter = new SpinnerAdapterVehicleVariant(getActivity(), 0, vehicleVarient);
            spinnerVehicleVarient.setAdapter(variantAdapter);
        }
//        else {
//            vehicleVarient = new ArrayList<>();
//            VehicleVarient vehicleVarient1 = new VehicleVarient();
//            vehicleVarient1.setMakeId("None");
//            vehicleVarient1.setModelId("None");
//            vehicleVarient1.setVariantId("None");
//            vehicleVarient1.setMakeName("None");
//            vehicleVarient1.setModelName("None");
//            vehicleVarient1.setVariantName("None");
//            vehicleVarient1.setVehicleAttr("None");
//            vehicleVarient1.setStartYear("None");
//            vehicleVarient1.setEndYear("None");
//            vehicleVarient1.setSc("None");
//            vehicleVarient1.setVehicleType("None");
//            vehicleVarient1.setNumCyl("None");
//            vehicleVarient1.setPrice("None");
//            vehicleVarient.add(vehicleVarient1);
//            variantAdapter = new SpinnerAdapterVehicleVariant(getActivity(), 0, vehicleVarient);
//            spinnerVehicleVarient.setAdapter(variantAdapter);
//        }

        vehiclemake = databaseManager.getVehicleMake();
        if (vehiclemake != null && vehiclemake.size() > 0) {
            SpinnerAdapterVehicleMake adapterVehicleMake = new SpinnerAdapterVehicleMake(getActivity(), 0, vehiclemake);
            spinnerVehicleMake.setAdapter(adapterVehicleMake);
        }
//        else {
//            vehiclemake = new ArrayList<>();
//            VehicleMake vehicleType1 = new VehicleMake();
//            vehicleType1.setMakeId("None");
//            vehicleType1.setMakeName("None");
//            vehiclemake.add(vehicleType1);
//            SpinnerAdapterVehicleMake adapterVehicleMake = new SpinnerAdapterVehicleMake(getActivity(), 0, vehiclemake);
//            spinnerVehicleMake.setAdapter(adapterVehicleMake);
//        }

        spinnerVehicleUsage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUsage = String.valueOf(vehicleUsage.get(position).getVehicleName());
                selectedUsageId = String.valueOf(vehicleUsage.get(position).getID());
                home_insurance_dateText.setText("");
                homePrice.setText("");
                getVehicleType(selectedUsageId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerVehicleMake.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMake = vehiclemake.get(position).getMakeName();
                makeId = vehiclemake.get(position).getMakeId();
                home_insurance_dateText.setText("");
                homePrice.setText("");

                if (vehicleModelList != null && vehicleModelList.size() > 0) {
                    vehicleModelList.clear();
                    adapterVehicleModel.notifyDataSetChanged();
                }

                if (!makeId.equalsIgnoreCase("0")) {
                    ArrayList<VehicleModel> vehicleModelArrayList0 = new ArrayList<VehicleModel>();
                    vehicleModelArrayList0 = databaseManager.getVehicleModel("0");
                    vehicleModelList = vehicleModelArrayList0;
                    vehicleModelList.addAll(databaseManager.getVehicleModel(makeId));
                } else {
                    vehicleModelList = databaseManager.getVehicleModel(makeId);
                }

                adapterVehicleModel = new SpinnerAdapterVehicleModel(getActivity(), 0, vehicleModelList);
                spinnerVehicleModel.setAdapter(adapterVehicleModel);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerVehicleModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedModel = vehicleModelList.get(position).getModelName();
                modelId = vehicleModelList.get(position).getModelId();
                home_insurance_dateText.setText("");
                homePrice.setText("");

                getVehicleVarient(makeId, modelId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerVehicleVarient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedVarient = vehicleVarient.get(position).getVariantName();
                selectedVarientId = vehicleVarient.get(position).getVariantId();
                orginalPrice = vehicleVarient.get(position).getPrice();
                vehicleOrginalPrice = vehicleVarient.get(position).getPrice();
                home_insurance_dateText.setText("");
                homePrice.setText("");
//                homePrice.setText(orginalPrice);

                String startYear = vehicleVarient.get(position).getStartYear();
                String endYear = vehicleVarient.get(position).getEndYear();
                if (!startYear.equalsIgnoreCase("") && !endYear.equalsIgnoreCase("")) {
                    if (yom.size() > 0 ){
                        yom.clear();
                    }
                    int currentyear = Integer.parseInt(endYear);
                    yom = new ArrayList<>();
                    yom.add("Select Year");
                    int year = currentyear;
                    int noOfYears = (currentyear - Integer.parseInt(startYear))+1;
                    for (int i = 0; i < noOfYears; i++) {
                        if (i == 0) {
                            yom.add(String.valueOf(year));
                        } else {
                            year = year - 1;
                            yom.add(String.valueOf(year));
                        }
                    }
                    if (yom != null && yom.size() > 0) {
                        SpinnerAdapterYOM yomAdapter = new SpinnerAdapterYOM(getActivity(), 0, yom);
                        spinnerYOM.setAdapter(yomAdapter);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rto = databaseManager.getRto();
        if (rto != null && rto.size() > 0) {
            SpinnerAdapterRTO rtoAdapter = new SpinnerAdapterRTO(getActivity(), 0, rto);
            spinnerRTO.setAdapter(rtoAdapter);
        }
//        else {
//            rto = new ArrayList<>();
//            RTO vehicleType1 = new RTO();
//            vehicleType1.setID("None");
//            vehicleType1.setRtoName("None");
//            rto.add(vehicleType1);
//            SpinnerAdapterRTO rtoAdapter = new SpinnerAdapterRTO(getActivity(), 0, rto);
//            spinnerRTO.setAdapter(rtoAdapter);
//        }
        spinnerRTO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRTO = rto.get(position).getRtoName();
                selectedRTOId = rto.get(position).getID();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (yom != null && yom.size() > 0) {
            SpinnerAdapterYOM yomAdapter = new SpinnerAdapterYOM(getActivity(), 0, yom);
            spinnerYOM.setAdapter(yomAdapter);
        }
//        else {
//            rto = new ArrayList<>();
//            RTO vehicleType1 = new RTO();
//            vehicleType1.setID("None");
//            vehicleType1.setRtoName("None");
//            rto.add(vehicleType1);
//            SpinnerAdapterRTO rtoAdapter = new SpinnerAdapterRTO(getActivity(), 0, rto);
//            spinnerRTO.setAdapter(rtoAdapter);
//        }
        spinnerYOM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYOM = yom.get(position);
                home_insurance_dateText.setText("");
                homePrice.setText("");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable mPendingRunnable = new Runnable() {
                    @Override
                    public void run() {
//                        startActivity(new Intent(getActivity(), WebViewActivity.class));

                        String vehicleRegistrationYear = "";
                        String registrationDateText = home_registration_dateText.getText().toString();
                        String insuranceDateText = home_insurance_dateText.getText().toString();
                        String price = homePrice.getText().toString();

                        if (!registrationDateText.equalsIgnoreCase("")) {
                            vehicleRegistrationYear = registrationDateText.substring(6,10);
                        }

                        if (selectedUsage.equalsIgnoreCase("Select Usage") || selectedType.equalsIgnoreCase("Select Type") || selectedMake.equalsIgnoreCase("Select Make")
                                || selectedModel.equalsIgnoreCase("Select Model") || selectedVarient.equalsIgnoreCase("Select Variant") || selectedRTO.equalsIgnoreCase("Select Place")
                                || selectedYOM.equalsIgnoreCase("Select Year") || registrationDateText.equalsIgnoreCase("") || insuranceDateText.equalsIgnoreCase("") || price.equalsIgnoreCase("")) {
                            Toast.makeText(context, "All fields are mandatory",
                                    Toast.LENGTH_SHORT).show();
                        } else if (Integer.parseInt(selectedYOM) > Integer.parseInt(vehicleRegistrationYear)) {
                            Toast.makeText(context, "Year of Manufacture cannot exceed Date of Registration.",
                                    Toast.LENGTH_SHORT).show();
                        } else if (!checkRange(orginalPrice, price)) {
                            Toast.makeText(context, "Price should be + or - 10 percent of Current Vehicle Price",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            if (newCar) {
                                editor.putString(Constants.CAR_TYPE, "Motor Comprehensive");
                            } else {
                                editor.putString(Constants.CAR_TYPE, "Motor Third Party");
                            }
                            editor.putString(Constants.VEHICLE_USAGE, selectedUsage);
                            editor.putString(Constants.VEHICLE_TYPE, selectedType);
                            editor.putString(Constants.VEHICLE_MAKE, selectedMake);
                            editor.putString(Constants.VEHICLE_MODEL, selectedModel);
                            editor.putString(Constants.VEHICLE_VARIANT, selectedVarient);
                            editor.putString(Constants.RTO, selectedRTO);
                            editor.putString(Constants.VEHICLE_USAGEID, selectedUsageId);
                            editor.putString(Constants.VEHICLE_TYPEID, selectedTypeId);
                            editor.putString(Constants.VEHICLE_MAKEID, makeId);
                            editor.putString(Constants.VEHICLE_MODELID, modelId);
                            editor.putString(Constants.VEHICLE_VARIANTID, selectedVarientId);
                            editor.putString(Constants.RTOID, selectedRTOId);
                            editor.putString(Constants.YOM, selectedYOM);
                            editor.putString(Constants.REGISTRATION_DATE, registrationDateText);
                            editor.putString(Constants.POLICY_START_DATE, insuranceDateText);

                            String currentYear = insuranceDateText.substring(6,10);
                            String currentDate = insuranceDateText.substring(0,2);
                            String currentMonth = insuranceDateText.substring(3,5);

                            String nextYear = "";
                            String nextMonth = "";
                            String nextDate = "";
                            if (currentDate.equalsIgnoreCase("01") && currentMonth.equalsIgnoreCase("01")) {
                                nextYear = currentYear;
                                nextMonth = "12";
                                nextDate = "31";
                            } else if (currentDate.equalsIgnoreCase("01")) {
                                nextYear = String.valueOf(Integer.parseInt(currentYear)+1);
                                nextMonth = String.valueOf(Integer.parseInt(currentMonth)-1);
                                if (nextMonth.length() == 1) {
                                    nextMonth = "0" + nextMonth;
                                }
                                if (currentMonth.equalsIgnoreCase("03") || currentMonth.equalsIgnoreCase("05") || currentMonth.equalsIgnoreCase("07")
                                        || currentMonth.equalsIgnoreCase("08") || currentMonth.equalsIgnoreCase("10") || currentMonth.equalsIgnoreCase("12")) {
                                    nextDate = "31";
                                } else if (currentMonth.equalsIgnoreCase("04") || currentMonth.equalsIgnoreCase("06") || currentMonth.equalsIgnoreCase("09")
                                        || currentMonth.equalsIgnoreCase("11")) {
                                    nextDate = "30";
                                } else if (currentMonth.equalsIgnoreCase("02")) {
                                    if (Integer.parseInt(nextYear) % 4 == 0) {
                                        nextDate = "29";
                                    } else {
                                        nextDate = "28";
                                    }
                                }
                            } else {
                                nextYear = String.valueOf(Integer.parseInt(currentYear)+1);
                                nextDate = String.valueOf(Integer.parseInt(currentDate)-1);
                                nextMonth = currentMonth;
                            }


                            if (nextDate.length() == 1) {
                                nextDate = "0" + nextDate;
                            }
                            if (nextMonth.length() == 1) {
                                nextMonth = "0" + nextMonth;
                            }

                            String policyEndYear = nextDate + "-" + nextMonth + "-" + nextYear;
                            editor.putString(Constants.POLICY_END_DATE, policyEndYear);

                            editor.putString(Constants.PRICE, price);
                            editor.apply();

                            startActivity(new Intent(getActivity(), AboutYourselfActivity.class));
                        }
                    }
                };
                if (mPendingRunnable != null) {
                    mHandler.post(mPendingRunnable);
                }
            }
        });

        carType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                home_insurance_dateText.setText("");
                homePrice.setText("");
                if(checkedId == R.id.radio_button_newcar) {
                    newCar = true;
//                    Toast.makeText(context, "choice: New Car",
//                            Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.radio_button_existing_car) {
                    newCar = false;
//                    Toast.makeText(context, "choice: Existing Car",
//                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static class DatePickerDialogClass extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),this,year,month,day);
            if (isVehicleDate) {
                datepickerdialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
            } else {
                datepickerdialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            }
            return datepickerdialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day){
            month = month + 1;
            if (isVehicleDate) {
                home_insurance_dateText.setText("");
                homePrice.setText("");
                if(month<10) {
                    home_registration_dateText.setText((day>=10?day:("0"+day)) + "-" + "0"+(month) + "-" + year);
                }else{
                    home_registration_dateText.setText((day>=10?day:("0"+day)) + "-" + (month) + "-" + year);
                }
            } else {
                if(month<10) {
                    home_insurance_dateText.setText((day>=10?day:("0"+day)) + "-" + "0"+(month) + "-" + year);
                }else{
                    home_insurance_dateText.setText((day>=10?day:("0"+day)) + "-" + (month) + "-" + year);
                }

                int currentyear = Calendar.getInstance().get(Calendar.YEAR);
                if (newCar && !selectedYOM.equalsIgnoreCase(String.valueOf(currentyear))) {
                    getCompanyDetails(home_registration_dateText.getText().toString(), home_insurance_dateText.getText().toString());
                } else {
                    orginalPrice = vehicleOrginalPrice;
                    homePrice.setText(vehicleOrginalPrice);
                }
            }
        }
    }

    public boolean checkRange(String orginalPrice, String price) {
        double amount = Double.parseDouble(String.valueOf(orginalPrice));
        double res = (amount / 100.0f) * 10;
        double realPrice = Double.parseDouble(String.valueOf(orginalPrice));
        double difference = realPrice - Double.parseDouble(String.valueOf(price));
//        Toast.makeText(context, "" + res, Toast.LENGTH_SHORT).show();

        return !(difference > res || difference < -res);

    }

    public void getVehicleType(final String usageID) {
        try {
            AsyncServiceCall _vehicleUsage = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    progressDialogReset = ProgressDialog.show(
                            context,
                            context.getResources().getString(
                                    R.string.progress_heading),
                            context.getResources().getString(
                                    R.string.progress_text));
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    return _webServiceManager.getVehicleType(usageID);
                }

                @Override
                protected void onPostExecute(Object resultObj) {
                    home_insurance_dateText.setText("");
                    homePrice.setText("");
                    String result = (String) resultObj;
                    if ((progressDialogReset != null) && (progressDialogReset.isShowing())) {
                        try {
                            progressDialogReset.dismiss();
                        } catch (Exception ex) {
                            Log.e(Constants.LOG_RMSAGG, "Exception is " + Log.getStackTraceString(ex));
                        }
                    }
                    if (result.equalsIgnoreCase("Updated") || result.equalsIgnoreCase("NoData")) {
                        if (vehicleType != null && vehicleType.size() > 0) {
                            vehicleType.clear();
                            typeAdapter.notifyDataSetChanged();
                        }
                        vehicleType = databaseManager.getVehicleType();
                        typeAdapter = new SpinnerAdapterVehicleType(getActivity(), 0, vehicleType);
                        spinnerVehicleType.setAdapter(typeAdapter);
                        spinnerVehicleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                selectedType = vehicleType.get(position).getVehicleType();
                                selectedTypeId = vehicleType.get(position).getVehicleId();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
//                    else if (result.equalsIgnoreCase("NoData")) {
//                        VehicleType vehicleType1 = new VehicleType();
//                        vehicleType1.setID("None");
//                        vehicleType1.setGvwRequired("None");
//                        vehicleType1.setVehicleId("None");
//                        vehicleType1.setVehicleType("None");
//                        vehicleType1.setUsageType("None");
//                        vehicleType.add(vehicleType1);
//                        typeAdapter = new SpinnerAdapterVehicleType(getActivity(), 0, vehicleType);
//                        spinnerVehicleType.setAdapter(typeAdapter);
//                        spinnerVehicleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                selectedType = vehicleType.get(position).getVehicleType();
//                                selectedTypeId = vehicleType.get(position).getVehicleId();
//                            }
//                            @Override
//                            public void onNothingSelected(AdapterView<?> parent) {
//
//                            }
//                        });
//                    }
                    else {
                        new AlertDialog.Builder(context)
                                .setTitle("Alert")
                                .setMessage(context.getString(R.string.network_failure))

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

                if (NetworkManager.isNetAvailable(context)) {
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

    public void getVehicleVarient(final String makeID, final String modelID) {
        try {
            AsyncServiceCall _vehicleVarient = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {
                    return _webServiceManager.getVehicleVarient(makeID, modelID);
                }

                @Override
                protected void onPostExecute(Object resultObj) {

                    home_insurance_dateText.setText("");
                    homePrice.setText("");
                    String result = (String) resultObj;
                    if ((progressDialogReset != null) && (progressDialogReset.isShowing())) {
                        try {
                            progressDialogReset.dismiss();
                        } catch (Exception ex) {
                            Log.e(Constants.LOG_RMSAGG, "Exception is " + Log.getStackTraceString(ex));
                        }
                    }
                    if (result.equalsIgnoreCase("Updated") || result.equalsIgnoreCase("NoData")) {
                        if (vehicleVarient != null && vehicleVarient.size() > 0) {
                            vehicleVarient.clear();
                            variantAdapter.notifyDataSetChanged();
                        }
                        vehicleVarient = databaseManager.getVehicleVarient();
                        variantAdapter = new SpinnerAdapterVehicleVariant(getActivity(), 0, vehicleVarient);
                        spinnerVehicleVarient.setAdapter(variantAdapter);
                    }
//                    else if(result.equalsIgnoreCase("NoData")) {
//                        VehicleVarient vehicleVarient1 = new VehicleVarient();
//                        vehicleVarient1.setMakeId("None");
//                        vehicleVarient1.setModelId("None");
//                        vehicleVarient1.setVariantId("None");
//                        vehicleVarient1.setMakeName("None");
//                        vehicleVarient1.setModelName("None");
//                        vehicleVarient1.setVariantName("None");
//                        vehicleVarient1.setVehicleAttr("None");
//                        vehicleVarient1.setStartYear("None");
//                        vehicleVarient1.setEndYear("None");
//                        vehicleVarient1.setSc("None");
//                        vehicleVarient1.setVehicleType("None");
//                        vehicleVarient1.setNumCyl("None");
//                        vehicleVarient1.setPrice("None");
//                        vehicleVarient.add(vehicleVarient1);
//                        variantAdapter = new SpinnerAdapterVehicleVariant(getActivity(), 0, vehicleVarient);
//                        spinnerVehicleVarient.setAdapter(variantAdapter);
//                    }
                    else {
                        if ((progressDialogReset != null) && (progressDialogReset.isShowing())) {
                            try {
                                progressDialogReset.dismiss();
                            } catch (Exception ex) {
                                Log.e(Constants.LOG_RMSAGG, "Exception is " + Log.getStackTraceString(ex));
                            }
                        }
                        new AlertDialog.Builder(context)
                                .setTitle("Alert")
                                .setMessage(context.getString(R.string.network_failure))

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

                if (NetworkManager.isNetAvailable(context)) {
                    _vehicleVarient.execute(0);
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

    public static void getCompanyDetails(final String vehicleRegDate, final String policyDate) {
        try {
            AsyncServiceCall _companyDetails = new AsyncServiceCall() {

                @Override
                protected void onPreExecute() {
                    progressDialogReset = ProgressDialog.show(
                            context,
                            "Getting Price",
                            context.getResources().getString(
                                    R.string.progress_text));
                    super.onPreExecute();
                }

                @Override
                protected Object doInBackground(Integer... params) {

                    //Date Formatting Start
                    Date curDate = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    String todayDate = format.format(curDate);
                    String currentYear = todayDate.substring(0,4);
                    System.out.println(todayDate);

                    String inputPattern = "dd-MM-yyyy";
                    String outputPattern = "yyyy-MM-dd";
                    SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
                    SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);

                    Date date = null;
                    Date policydate = null;
                    String registrationDate = null;
                    String policyStartDate = null;

                    try {
                        date = inputFormat.parse(vehicleRegDate);
                        policydate = inputFormat.parse(policyDate);
                        registrationDate = outputFormat.format(date);
                        policyStartDate = outputFormat.format(policydate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //Date Formatting End

                    //Previous Policy End Date Calculation Start
                    String startDate = policyDate.substring(0,2);
                    String startMonth = policyDate.substring(3,5);
                    String startYear = policyDate.substring(6,10);

                    String prePolicyYear = "";
                    String prePolicyMonth = "";
                    String prePolicyDate = "";
                    if (startDate.equalsIgnoreCase("01") && startMonth.equalsIgnoreCase("01")) {
                        prePolicyYear = String.valueOf(Integer.parseInt(startYear)-1);
                        prePolicyMonth = "12";
                        prePolicyDate = "31";
                    } else if (startDate.equalsIgnoreCase("01")) {
                        prePolicyYear = startYear;
                        prePolicyMonth = String.valueOf(Integer.parseInt(startMonth)-1);
                        if (prePolicyMonth.length() == 1) {
                            prePolicyMonth = "0" + prePolicyMonth;
                        }
                        if (prePolicyMonth.equalsIgnoreCase("03") || prePolicyMonth.equalsIgnoreCase("05") || prePolicyMonth.equalsIgnoreCase("07")
                                || prePolicyMonth.equalsIgnoreCase("08") || prePolicyMonth.equalsIgnoreCase("10") || prePolicyMonth.equalsIgnoreCase("12")) {
                            prePolicyDate = "31";
                        } else if (prePolicyMonth.equalsIgnoreCase("04") || prePolicyMonth.equalsIgnoreCase("06") || prePolicyMonth.equalsIgnoreCase("09")
                                || prePolicyMonth.equalsIgnoreCase("11")) {
                            prePolicyDate = "30";
                        } else if (prePolicyMonth.equalsIgnoreCase("02")) {
                            if (Integer.parseInt(prePolicyYear) % 4 == 0) {
                                prePolicyDate = "29";
                            } else {
                                prePolicyDate = "28";
                            }
                        }
                    } else {
                        prePolicyYear = startYear;
                        prePolicyDate = String.valueOf(Integer.parseInt(startDate)-1);
                        prePolicyMonth = startMonth;
                    }


                    if (prePolicyDate.length() == 1) {
                        prePolicyDate = "0" + prePolicyDate;
                    }
                    if (prePolicyMonth.length() == 1) {
                        prePolicyMonth = "0" + prePolicyMonth;
                    }

                    String prePolicyEndYear = prePolicyYear + "-" + prePolicyMonth + "-" + prePolicyDate;
                    editor.putString(Constants.PRE_POLICY_END_DATE, prePolicyEndYear);
                    editor.apply();
                    //Previous Policy End Date Calculation Completed

                    //Vehicle Age Calculation Start
                    int currentyear = Calendar.getInstance().get(Calendar.YEAR);
                    String vehicleRegYear = vehicleRegDate.substring(6,10);
                    String vehicleAge = String.valueOf(currentyear - Integer.parseInt(vehicleRegYear));
                    editor.putString(Constants.VEHICLE_AGE, vehicleAge);
                    editor.apply();
                    //Vehicle Age Calculation End
                    return _webServiceManager.getCompanyDetails(selectedUsageId, selectedTypeId, makeId, modelId, selectedVarientId, registrationDate, vehicleAge,
                            orginalPrice, policyStartDate, prePolicyEndYear, "N", "0", selectedRTOId, selectedYOM, "27");
                }

                @Override
                protected void onPostExecute(Object resultObj) {
                    if ((progressDialogReset != null) && (progressDialogReset.isShowing())) {
                        try {
                            progressDialogReset.dismiss();
                        } catch (Exception ex) {
                            Log.e(Constants.LOG_RMSAGG, "Exception is " + Log.getStackTraceString(ex));
                        }
                    }

                    String result = (String) resultObj;
                    if (result.equalsIgnoreCase("Updated")) {
                        String idv = databaseManager.getIDV();
                        orginalPrice = idv;
                        homePrice.setText(orginalPrice);
                    } else if (result.equalsIgnoreCase("NoDataAvailable")) {
                        new AlertDialog.Builder(context)
                                .setTitle("Alert")
                                .setMessage(context.getString(R.string.no_data_error))

                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setCancelable(true).show();
                    } else if (result.equalsIgnoreCase("Error")) {
                        new AlertDialog.Builder(context)
                                .setTitle("Alert")
                                .setMessage(errorMessage)

                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setCancelable(true).show();
                    } else {
                        new AlertDialog.Builder(context)
                                .setTitle("Alert")
                                .setMessage(context.getString(R.string.network_failure))

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

                if (NetworkManager.isNetAvailable(context)) {
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

    private static void showNoNetworkAlert() {

        if ((progressDialogReset != null) && (progressDialogReset.isShowing())) {
            try {
                progressDialogReset.dismiss();
            } catch (Exception ex) {
                Log.e(Constants.LOG_RMSAGG, "Exception is " + Log.getStackTraceString(ex));
            }
        }
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.network_availability_heading))
                .setMessage(context.getString(R.string.network_availability))

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true).show();

    }
}
