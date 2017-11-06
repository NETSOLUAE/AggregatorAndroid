package com.rmsllcoman.agg.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.rmsllcoman.agg.R;
import com.rmsllcoman.agg.Model.CompanySpinner;

/**
 * Created by macmini on 7/22/17.
 */

public class SpinnerAdapterCompany extends ArrayAdapter<CompanySpinner> {
    private OnCheckboxCheckedChangeListener callback;
    private Context mContext;
    private ArrayList<CompanySpinner> listState;
    private SpinnerAdapterCompany myAdapter;
    private boolean isFromView = false;
    private String productType = "";
    private boolean[] checkBoxState;
    private ArrayList<CompanySpinner> listStateSelected;

    public SpinnerAdapterCompany(Context context, int resource, List<CompanySpinner> objects, String productType) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<CompanySpinner>) objects;
        this.myAdapter = this;
        this.productType = productType;
        checkBoxState = new boolean[objects.size()];
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public void setOnCheckboxChangeListener(OnCheckboxCheckedChangeListener callback) {
        this.callback = callback;
    }

    public interface OnCheckboxCheckedChangeListener {
        public void CheckboxChange(ArrayList<CompanySpinner> listStateSelected);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(final int position, View convertView,
                               final ViewGroup parent) {
        final ViewHolder holder;
        listStateSelected = new ArrayList<>();
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinner_item_layout, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView
                    .findViewById(R.id.text);
            holder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.checkbox);
            holder.spinnerDowm = (ImageView) convertView
                    .findViewById(R.id.spinner_down);
            holder.buttonDone = (Button) convertView
                    .findViewById(R.id.button_done);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String title = listState.get(position).getTitle();
        holder.mTextView.setText(title);

        if (productType.equalsIgnoreCase("COMPRH") && (title.equalsIgnoreCase("Comprehensive Cover") || title.equalsIgnoreCase("Third Party Liability"))) {
            checkBoxState[position] = true;
            listState.get(position).setSelected(true);
            holder.mCheckBox.setClickable(false);
            holder.mCheckBox.setEnabled(false);
        } else {
            // To check weather checked event fire from getview() or user input
            isFromView = true;
            holder.mCheckBox.setEnabled(true);
            holder.mCheckBox.setChecked(listState.get(position).isSelected());
            isFromView = false;
        }

        if ((position == 0)) {
            holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            holder.mCheckBox.setVisibility(View.GONE);
        } else {
            holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.text_medium_color));
            holder.mCheckBox.setVisibility(View.VISIBLE);
            holder.spinnerDowm.setVisibility(View.GONE);
        }

        if (position == listState.size()-1) {
            holder.buttonDone.setVisibility(View.VISIBLE);
        } else {
            holder.buttonDone.setVisibility(View.GONE);
        }

//        if (listState.get(position).getTitle().equalsIgnoreCase(listState.get(listState.size()-1).getTitle())) {
//            holder.buttonDone.setVisibility(View.VISIBLE);
//        } else {
//            holder.buttonDone.setVisibility(View.GONE);
//        }

        holder.mCheckBox.setTag(position);

        holder.mCheckBox.setChecked(checkBoxState[position]);

        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    checkBoxState[position] = true;
                    listState.get(position).setSelected(true);
                } else {
                    checkBoxState[position] = false;
                    listState.get(position).setSelected(false);
                }
                listStateSelected = new ArrayList<>();
                for (int i = 0; i < listState.size(); i++) {
                    if (listState.get(i).isSelected()) {
                        listStateSelected.add(listState.get(i));
                    }
                }
                callback.CheckboxChange(listStateSelected);
            }
        });

        holder.buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View root = parent.getRootView();
                root.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
                root.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
        private ImageView spinnerDowm;
        private Button buttonDone;
    }
}