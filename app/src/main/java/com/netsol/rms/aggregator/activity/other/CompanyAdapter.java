package com.netsol.rms.aggregator.activity.other;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netsol.rms.aggregator.R;

import java.util.ArrayList;

/**
 * Created by macmini on 7/22/17.
 */
public class CompanyAdapter extends ArrayAdapter<Company> implements View.OnClickListener{

    private OnShareClickedListener mCallback;
    private Context mContext;
    private boolean[] checkBoxState;
    private ArrayList<Company> companyArrayList;

    // View lookup cache
    private static class ViewHolder {
        TextView price;
        TextView companyName;
//        ImageView companyLogo;
        CheckBox selectedCompany;
        Button view;
    }

    public CompanyAdapter(ArrayList<Company> data, Context context) {
        super(context, R.layout.company_row, data);
        this.companyArrayList = data;
        this.mContext=context;
        checkBoxState = new boolean[data.size()];
    }

    public void setOnShareClickedListener(OnShareClickedListener mCallback) {
        this.mCallback = mCallback;
    }

    public interface OnShareClickedListener {
        public void ShareClicked(Company companyDetails);
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Company dataModel=(Company)object;

        switch (v.getId())
        {
            case R.id.view:
                mCallback.ShareClicked(dataModel);
                break;
        }
    }

    @Override
    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Company dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.company_row, parent, false);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            viewHolder.companyName = (TextView) convertView.findViewById(R.id.companyName);
//            viewHolder.companyLogo = (ImageView) convertView.findViewById(R.id.company_logo);
            viewHolder.view = (Button) convertView.findViewById(R.id.view);
            viewHolder.selectedCompany = (CheckBox) convertView.findViewById(R.id.selected_company);

            viewHolder.view.setBackground(mContext.getDrawable(R.drawable.button_view));

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.selectedCompany.setChecked(checkBoxState[position]);

        viewHolder.selectedCompany.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    checkBoxState[position] = true;
                    companyArrayList.get(position).setSelected(true);
                } else {
                    checkBoxState[position] = false;
                    companyArrayList.get(position).setSelected(false);
                }
            }
        });
        assert dataModel != null;
        viewHolder.price.setText(dataModel.getPrice());
        viewHolder.companyName.setText(dataModel.getCompanyName());
//        Glide.with(mContext)
//                .load(dataModel.getCompanyLogo())
//                .placeholder(R.drawable.alliance)
//                .crossFade()
//                .into(viewHolder.companyLogo);
        viewHolder.view.setOnClickListener(this);
        viewHolder.view.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
