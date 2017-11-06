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

import com.rmsllcoman.agg.Model.VehicleModel;
import com.rmsllcoman.agg.R;

/**
 * Created by macmini on 8/8/17.
 */

public class SpinnerAdapterVehicleModel extends ArrayAdapter<VehicleModel> {
        private Context mContext;
        private ArrayList<VehicleModel> vehicleModel;
        private SpinnerAdapterVehicleModel myAdapter;
        private boolean isFromView = false;

    public SpinnerAdapterVehicleModel(Context context, int resource, List<VehicleModel> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.vehicleModel = (ArrayList<VehicleModel>) objects;
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
            convertView = layoutInflator.inflate(R.layout.spinner_item_vehicle_model, null);
            holder = new ViewHolder();
            holder.model = (TextView) convertView
                    .findViewById(R.id.model);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.model.setText(vehicleModel.get(position).getModelName());
        return convertView;
    }

    private class ViewHolder {
        private TextView model;
    }

}
