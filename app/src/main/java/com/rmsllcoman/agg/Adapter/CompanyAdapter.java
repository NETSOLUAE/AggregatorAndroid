package com.rmsllcoman.agg.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import com.rmsllcoman.agg.R;
import com.rmsllcoman.agg.Model.Company;

/**
 * Created by macmini on 7/22/17.
 */
public class CompanyAdapter extends ArrayAdapter<Company> implements View.OnClickListener{

    private OnShareClickedListener mCallback;
    private Context mContext;
    private boolean[] checkBoxState;
    private ArrayList<Company> companyArrayList;
    private ArrayList<Company> companyFilterData;
    private ArrayList<String> companyFilterCoverID;
    public AlertDialog alertDialog;

    // View lookup cache
    private static class ViewHolder {
        TextView price;
        TextView breakUp;
        TextView companyName;
//        ImageView companyLogo;
        CheckBox selectedCompany;
        Button view;
        Button buy;
    }

    public CompanyAdapter(ArrayList<Company> data, ArrayList<Company> companyFilterData, ArrayList<String> companyFilterCoverID, Context context) {
        super(context, R.layout.company_row, data);
        this.companyArrayList = data;
        this.companyFilterData = companyFilterData;
        this.companyFilterCoverID = companyFilterCoverID;
        this.mContext=context;
        checkBoxState = new boolean[data.size()];
        alertDialog = null;
    }

    public void setOnShareClickedListener(OnShareClickedListener mCallback) {
        this.mCallback = mCallback;
    }

    public interface OnShareClickedListener {
        public void ShareClicked(Company companyDetails, String buttonType);
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Company dataModel=(Company)object;

        switch (v.getId())
        {
            case R.id.view:
                mCallback.ShareClicked(dataModel, "view");
                break;
            case R.id.buy:
                mCallback.ShareClicked(dataModel, "buy");
                break;
        }
    }

    @Override
    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        final Company dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.company_row, parent, false);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            viewHolder.breakUp = (TextView) convertView.findViewById(R.id.breakup);
            viewHolder.companyName = (TextView) convertView.findViewById(R.id.companyName);
//            viewHolder.companyLogo = (ImageView) convertView.findViewById(R.id.company_logo);
            viewHolder.view = (Button) convertView.findViewById(R.id.view);
            viewHolder.buy = (Button) convertView.findViewById(R.id.buy);
            viewHolder.selectedCompany = (CheckBox) convertView.findViewById(R.id.selected_company);

            viewHolder.view.setBackground(mContext.getDrawable(R.drawable.button_view_company));
            viewHolder.buy.setBackground(mContext.getDrawable(R.drawable.button_buy));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.selectedCompany.setChecked(checkBoxState[position]);

        String productType = dataModel.getCompanyProductType();
        viewHolder.selectedCompany.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    checkBoxState[position] = true;
                    companyArrayList.get(position).setSelected(true);
                } else {
                    checkBoxState[position] = false;
                    companyArrayList.get(position).setSelected(false);
                }
            }
        });
        assert dataModel != null;
        viewHolder.companyName.setText(dataModel.getCompanyName());

        viewHolder.breakUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Company companyDetails = (Company) getItem(position);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View dialogView = inflater.inflate(R.layout.break_up_dialog, null);
                dialogBuilder.setView(dialogView);


                assert companyDetails != null;
                String breakUPDetails = companyDetails.getCompanyCoverPremium();
                breakUPDetails = breakUPDetails + "Total Premium" + " : " + companyDetails.getPremiumAmount();

                Button okButton = (Button) dialogView.findViewById(R.id.breakup_ok);
                TextView breakup_text = (TextView) dialogView.findViewById(R.id.breakup_text);

                alertDialog = dialogBuilder.create();

                if (breakUPDetails.equals("null") || breakUPDetails.equals("")) {
                    breakup_text.setText("");
                } else {
                    breakup_text.setText(breakUPDetails);
                }

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(alertDialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                alertDialog.show();
                alertDialog.getWindow().setAttributes(lp);
                alertDialog.getWindow().setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.background_shadow));

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

//        Glide.with(mContext)
//                .load(dataModel.getCompanyLogo())
//                .placeholder(R.drawable.alliance)
//                .crossFade()
//                .into(viewHolder.companyLogo);
        viewHolder.view.setOnClickListener(this);
        viewHolder.view.setTag(position);
        viewHolder.buy.setOnClickListener(this);
        viewHolder.buy.setTag(position);

        viewHolder.breakUp.setVisibility(View.GONE);
        float price = Float.parseFloat(dataModel.getCoverPrice());
        viewHolder.price.setText(String.format(Locale.CANADA, "%.2f", price));

//        String[] coverPremiumArray = new String[10];
//        String coverPremium = dataModel.getCompanyCoverPremium();
//        coverPremiumArray = coverPremium.split("\n");
//
//        JSONObject coverJson = new JSONObject();
//
//        for (String aCoverPremiumArray : coverPremiumArray) {
//            try {
//                String key = (aCoverPremiumArray.substring(0,aCoverPremiumArray.lastIndexOf(":"))).trim().toUpperCase();
//                String value = (aCoverPremiumArray.substring(aCoverPremiumArray.lastIndexOf(":")).replace(":", "")).trim();
//                coverJson.put(key, value);
//            } catch (JSONException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//
//        String selectedCoverPremium = "";
//        if (!coverJson.isNull("OD")) {
//            try {
//                float selectedAmount = Float.parseFloat(coverJson.getString("OD"));
//                selectedCoverPremium = selectedCoverPremium + "OD" + " : " + String.valueOf(String.format(Locale.CANADA, "%.2f", selectedAmount)) + "\n";
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else
//        if (!coverJson.isNull("TP")){
//            try {
//                float selectedAmount = Float.parseFloat(coverJson.getString("TP"));
//                selectedCoverPremium = selectedCoverPremium + "TP" + " : " + String.valueOf(String.format(Locale.CANADA, "%.2f", selectedAmount)) + "\n";
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else {
//            selectedCoverPremium = coverPremium;
//        }
//
//        if (companyFilterData.size() > 0) {
//            viewHolder.breakUp.setVisibility(View.GONE);
//            float price = Float.parseFloat(dataModel.getPrice());
//            for (int i = 0; i < companyFilterCoverID.size(); i++) {
//                String coverID = companyFilterCoverID.get(i);
//                if (coverJson.isNull(coverID)) {
//                    Log.d("**COVER ID NOT EXIST***", coverID);
//                } else {
//                    try {
//                        if (!coverID.equalsIgnoreCase("OD") && !coverID.equalsIgnoreCase("TP")) {
//                            String selectedCoverAmount = coverJson.getString(coverID);
//                            float selectedAmount = Float.parseFloat(selectedCoverAmount);
//                            price = price + selectedAmount;
//                            String premium = coverID + " : " + String.valueOf(String.format(Locale.CANADA, "%.2f", selectedAmount)) + "\n";
//                            selectedCoverPremium = selectedCoverPremium + premium;
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            viewHolder.price.setText(String.valueOf(String.format(Locale.CANADA, "%.2f", price)));
//            dataModel.setCoverPrice(String.valueOf(price));
//            dataModel.setCompanySelectedCoverPremium(selectedCoverPremium);
//        } else {
//            float price = Float.parseFloat(dataModel.getPrice());
//            viewHolder.breakUp.setVisibility(View.GONE);
//            viewHolder.price.setText(String.format(Locale.CANADA, "%.2f", price));
//            dataModel.setCompanySelectedCoverPremium(selectedCoverPremium);
//        }

        // Return the completed view to render on screen
        return convertView;
    }
}
