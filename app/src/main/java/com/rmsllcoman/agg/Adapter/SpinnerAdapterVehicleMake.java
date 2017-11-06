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

import com.rmsllcoman.agg.Model.VehicleMake;
import com.rmsllcoman.agg.R;

/**
 * Created by macmini on 8/8/17.
 */

public class SpinnerAdapterVehicleMake extends ArrayAdapter<VehicleMake> {
    private Context mContext;
    private ArrayList<VehicleMake> vehicleMake;
    private SpinnerAdapterVehicleMake myAdapter;
    private boolean isFromView = false;

    public SpinnerAdapterVehicleMake(Context context, int resource, List<VehicleMake> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.vehicleMake = (ArrayList<VehicleMake>) objects;
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
            convertView = layoutInflator.inflate(R.layout.spinner_item_vehicle_make, null);
            holder = new ViewHolder();
            holder.make = (TextView) convertView
                    .findViewById(R.id.make);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.make.setText(vehicleMake.get(position).getMakeName());
        return convertView;
    }

    private class ViewHolder {
        private TextView make;
    }
}
