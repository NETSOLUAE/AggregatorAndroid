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

import com.rmsllcoman.agg.Model.VehicleUsage;
import com.rmsllcoman.agg.R;

/**
 * Created by macmini on 7/23/17.
 */

public class SpinnerAdapterVUsage extends ArrayAdapter<VehicleUsage> {
    private Context mContext;
    private ArrayList<VehicleUsage> vehicleUsage;
    private SpinnerAdapterVUsage myAdapter;
    private boolean isFromView = false;

    public SpinnerAdapterVUsage(Context context, int resource, List<VehicleUsage> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.vehicleUsage = (ArrayList<VehicleUsage>) objects;
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
            convertView = layoutInflator.inflate(R.layout.sinner_item_vehicleusage, null);
            holder = new ViewHolder();
            holder.VehicleName = (TextView) convertView
                    .findViewById(R.id.vehicleName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.VehicleName.setText(vehicleUsage.get(position).getVehicleName());
        return convertView;
    }

    private class ViewHolder {
        private TextView VehicleName;
    }
}