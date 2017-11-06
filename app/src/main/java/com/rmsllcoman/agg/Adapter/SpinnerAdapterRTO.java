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

import com.rmsllcoman.agg.Model.RTO;
import com.rmsllcoman.agg.R;

/**
 * Created by macmini on 8/8/17.
 */

public class SpinnerAdapterRTO extends ArrayAdapter<RTO> {
    private Context mContext;
    private ArrayList<RTO> rto;
    private SpinnerAdapterRTO myAdapter;
    private boolean isFromView = false;

    public SpinnerAdapterRTO(Context context, int resource, List<RTO> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.rto = (ArrayList<RTO>) objects;
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
            convertView = layoutInflator.inflate(R.layout.spinner_item_rto, null);
            holder = new ViewHolder();
            holder.rto = (TextView) convertView
                    .findViewById(R.id.rto);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.rto.setText(rto.get(position).getRtoName());
        return convertView;
    }

    private class ViewHolder {
        private TextView rto;
    }

}
