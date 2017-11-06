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

import com.rmsllcoman.agg.Model.Bank;
import com.rmsllcoman.agg.R;

/**
 * Created by macmini on 7/26/17.
 */

public class SpinnerAdapterBank extends ArrayAdapter<Bank> {
    private Context mContext;
    private ArrayList<Bank> listState;
    private SpinnerAdapterBank myAdapter;
    private boolean isFromView = false;

    public SpinnerAdapterBank(Context context, int resource, List<Bank> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<Bank>) objects;
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
            convertView = layoutInflator.inflate(R.layout.spinner_item_bank, null);
            holder = new ViewHolder();
            holder.bankType = (TextView) convertView
                    .findViewById(R.id.bankType);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bankType.setText(listState.get(position).getNameType());
        return convertView;
    }

    private class ViewHolder {
        private TextView bankType;
    }
}