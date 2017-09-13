package com.netsol.rms.aggregator.activity.other;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.netsol.rms.aggregator.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macmini on 7/23/17.
 */

public class SpinnerAdapterVehicleType extends ArrayAdapter<VehicleType> {
        private Context mContext;
        private ArrayList<VehicleType> vehicleType;
        private SpinnerAdapterVehicleType myAdapter;
        private boolean isFromView = false;

    public SpinnerAdapterVehicleType(Context context, int resource, List<VehicleType> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.vehicleType = (ArrayList<VehicleType>) objects;
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
            convertView = layoutInflator.inflate(R.layout.spinner_item_vehicletype, null);
            holder = new ViewHolder();
            holder.type = (TextView) convertView
                    .findViewById(R.id.type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.type.setText(vehicleType.get(position).getVehicleType());
        return convertView;
    }

    private class ViewHolder {
        private TextView type;
    }
}