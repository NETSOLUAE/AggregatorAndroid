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

import com.rmsllcoman.agg.R;

/**
 * Created by macmini on 10/21/17.
 */

public class SpinnerAdapterEmployeer extends ArrayAdapter<String> {
    private Context mContext;
    private ArrayList<String> employer;
    private SpinnerAdapterEmployeer myAdapter;
    private boolean isFromView = false;

    public SpinnerAdapterEmployeer(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.employer = (ArrayList<String>) objects;
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
            convertView = layoutInflator.inflate(R.layout.spinner_item_employer, null);
            holder = new ViewHolder();
            holder.employer = (TextView) convertView
                    .findViewById(R.id.employer);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.employer.setText(employer.get(position));
        return convertView;
    }

    private class ViewHolder {
        private TextView employer;
    }

}
