package com.netsol.rms.aggregator.activity.fragment;

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

import com.netsol.rms.aggregator.R;
import com.netsol.rms.aggregator.activity.AboutYourselfActivity;
import com.netsol.rms.aggregator.activity.other.AsyncServiceCall;
import com.netsol.rms.aggregator.activity.other.Constants;
import com.netsol.rms.aggregator.activity.other.DatabaseManager;
import com.netsol.rms.aggregator.activity.other.NetworkManager;
import com.netsol.rms.aggregator.activity.other.RTO;
import com.netsol.rms.aggregator.activity.other.SpinnerAdapterRTO;
import com.netsol.rms.aggregator.activity.other.SpinnerAdapterVUsage;
import com.netsol.rms.aggregator.activity.other.SpinnerAdapterVehicleMake;
import com.netsol.rms.aggregator.activity.other.SpinnerAdapterVehicleModel;
import com.netsol.rms.aggregator.activity.other.SpinnerAdapterVehicleType;
import com.netsol.rms.aggregator.activity.other.SpinnerAdapterVehicleVariant;
import com.netsol.rms.aggregator.activity.other.SpinnerAdapterYOM;
import com.netsol.rms.aggregator.activity.other.VehicleMake;
import com.netsol.rms.aggregator.activity.other.VehicleModel;
import com.netsol.rms.aggregator.activity.other.VehicleType;
import com.netsol.rms.aggregator.activity.other.VehicleUsage;
import com.netsol.rms.aggregator.activity.other.VehicleVarient;
import com.netsol.rms.aggregator.activity.other.WebserviceManager;

import java.util.ArrayList;
import java.util.Calendar;


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
    RadioGroup carType;
    Button next;
    Spinner spinnerVehicleUsage;
    Spinner spinnerVehicleType;
    Spinner spinnerVehicleMake;
    Spinner spinnerVehicleModel;
    Spinner spinnerVehicleVarient;
    Spinner spinnerRTO;
    Spinner spinnerYOM;
    EditText homePrice;
    static LinearLayout registrationDate;
    static EditText home_registration_dateText;
    Context context;
    String selectedUsage;
    String selectedUsageId;
    String selectedType;
    String selectedTypeId;
    String selectedMake;
    String makeId;
    String selectedModel;
    String modelId;
    String selectedVarient;
    String selectedVarientId;
    String selectedRTO;
    String selectedRTOId;
    String selectedYOM;
    boolean newCar = true;
    private Handler mHandler;
    ArrayList<VehicleUsage> vehicleUsage;
    ArrayList<VehicleType> vehicleType;
    ArrayList<VehicleMake> vehiclemake;
    ArrayList<VehicleModel> vehicleModelList;
    ArrayList<VehicleVarient> vehicleVarient;
    ArrayList<RTO> rto;
    ArrayList<String> yom;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    DatabaseManager databaseManager;
    private WebserviceManager _webServiceManager;
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
                DialogFragment dialogfragment = new DatePickerDialogClass();
                dialogfragment.show(getActivity().getFragmentManager(), "Date Time");
            }
        });

        home_registration_dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerDialogClass();
                dialogfragment.show(getActivity().getFragmentManager(), "Date Time");
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
                String price = vehicleVarient.get(position).getPrice();
                homePrice.setText(price);
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
                        String registrationDateText = home_registration_dateText.getText().toString();
                        String price = homePrice.getText().toString();
                        if (selectedUsage.equalsIgnoreCase("") || selectedType.equalsIgnoreCase("") || selectedMake.equalsIgnoreCase("") || selectedModel.equalsIgnoreCase("") ||
                                selectedVarient.equalsIgnoreCase("") || selectedRTO.equalsIgnoreCase("")  || selectedYOM.equalsIgnoreCase("") || registrationDateText.equalsIgnoreCase("") ||
                                price.equalsIgnoreCase("")) {
                            Toast.makeText(context, "All fields are mandatory",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            if (newCar) {
                                editor.putString(Constants.CAR_TYPE, "New Car Policy");
                            } else {
                                editor.putString(Constants.CAR_TYPE, "Existing Car Policy");
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
                if(checkedId == R.id.radio_button_newcar) {
                    newCar = true;
                    Toast.makeText(context, "choice: New Car",
                            Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.radio_button_existing_car) {
                    newCar = false;
                    Toast.makeText(context, "choice: Existing Car",
                            Toast.LENGTH_SHORT).show();
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
            datepickerdialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return datepickerdialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day){
                if(month<10) {
                    home_registration_dateText.setText((day>=10?day:("0"+day)) + "-" + "0"+(month + 1) + "-" + year);
                }else{
                    home_registration_dateText.setText((day>=10?day:("0"+day)) + "-" + (month + 1) + "-" + year);
                }
        }
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

    private void showNoNetworkAlert() {

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
