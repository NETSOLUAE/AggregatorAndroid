package com.rmsllcoman.agg.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.rmsllcoman.agg.Model.VehicleVarient;
import com.rmsllcoman.agg.R;

/**
 * Created by macmini on 8/8/17.
 */

public class SpinnerAdapterVehicleVariant extends ArrayAdapter<VehicleVarient> {
    private Context mContext;
    private ArrayList<VehicleVarient> vehicleVarient;
    private SpinnerAdapterVehicleVariant myAdapter;
    private boolean isFromView = false;

    public SpinnerAdapterVehicleVariant(Context context, int resource, List<VehicleVarient> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.vehicleVarient = (ArrayList<VehicleVarient>) objects;
        this.myAdapter = this;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(final int position, View convertView,
                               ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinner_item_vehicle_varient, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView
                    .findViewById(R.id.variant_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (vehicleVarient.get(position).getVariantName().equalsIgnoreCase("") && vehicleVarient.get(position).getVehicleAttr().equalsIgnoreCase("")) {
            holder.name.setText("");
        } else if(vehicleVarient.get(position).getVariantName().equalsIgnoreCase("Select Variant")){
            holder.name.setText("Select Variant");
        }else {
            holder.name.setText(vehicleVarient.get(position).getVariantName() + " (" + vehicleVarient.get(position).getVehicleAttr() + ")");
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView name;
    }

}
