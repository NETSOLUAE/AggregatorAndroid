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
 * Created by macmini on 9/11/17.
 */

public class SpinnerAdapterYOM extends ArrayAdapter<String> {
    private Context mContext;
    private ArrayList<String> yom;
    private SpinnerAdapterYOM myAdapter;
    private boolean isFromView = false;

    public SpinnerAdapterYOM(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.yom = (ArrayList<String>) objects;
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
            convertView = layoutInflator.inflate(R.layout.spinner_item_yom, null);
            holder = new ViewHolder();
            holder.yom = (TextView) convertView
                    .findViewById(R.id.yom);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.yom.setText(yom.get(position));
        return convertView;
    }

    private class ViewHolder {
        private TextView yom;
    }

}
