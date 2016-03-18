package com.projects.nir.myapplication.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.projects.nir.myapplication.Entities.BlockedRelation;
import com.projects.nir.myapplication.R;
import com.projects.nir.myapplication.database.JsonParser;
import com.projects.nir.myapplication.interfaces.IDataAccessLayer;
import com.projects.nir.myapplication.search.IAsyncCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zilkha on 25/02/2016.
 */
public class BlockingCarsListAdapter extends BaseAdapter implements Filterable {

    private Activity context;
    private ArrayList<BlockedRelation> list;
    ArrayList<BlockedRelation> mStringFilterList;
    ValueFilter valueFilter;
    IDataAccessLayer _DataAccessLayer;
    // private final int[] bgColors = new int[]{R.color.list_bg_1, R.color.list_bg_2};

    public BlockingCarsListAdapter(Activity con, ArrayList<BlockedRelation> arrList, IDataAccessLayer dataAccessLayer) {
        context = con;
        list = arrList;
        mStringFilterList = arrList;
        _DataAccessLayer = dataAccessLayer;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();

            LayoutInflater inflater = context.getLayoutInflater();

            convertView = inflater.inflate(R.layout.listview_item_blocking_car, parent, false);

            viewHolder.attribTextView = (TextView) convertView.findViewById(R.id.blockingTextView);
            viewHolder.attribImageView = (ImageButton) convertView.findViewById(R.id.blockingImageButton);
            viewHolder.phoneNumberSpinner = (Spinner) convertView.findViewById(R.id.phoneNumberSpinner);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // convertView.setBackgroundResource(bgColors[position % 2]);
        BlockedRelation blockedRelation = list.get(position);
        if (blockedRelation.userPhoneNumberList == null)
            blockedRelation.loadPhoneNumbers(_DataAccessLayer,this);
        else
        {
            if (viewHolder.phoneNumberSpinner.getAdapter() == null)
            {
                viewHolder.userPhoneNumberList = blockedRelation.userPhoneNumberList;
                ArrayAdapter<String> userPhoneNumberAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, blockedRelation.userPhoneNumberList);
                viewHolder.phoneNumberSpinner.setAdapter(userPhoneNumberAdapter);
                blockedRelation.context = context;
            }
        }

        viewHolder.attribTextView.setText(String.valueOf(blockedRelation.BlockingUserName));
        viewHolder.attribImageView.setImageResource(android.R.drawable.sym_action_call);

        viewHolder.phoneNumberSpinner.setOnItemSelectedListener(blockedRelation);

        viewHolder.attribImageView.setOnClickListener(blockedRelation);

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<BlockedRelation> filterList = new ArrayList<BlockedRelation>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if (((String.valueOf(mStringFilterList.get(i).blockingUserId).toUpperCase())
                            .contains(constraint.toString().toUpperCase()))) {

                        BlockedRelation blockedDetails = new BlockedRelation(mStringFilterList.get(i).get_Id(), mStringFilterList.get(i).blockingUserId
                                , mStringFilterList.get(i).blockingUserId);

                        filterList.add(blockedDetails);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            list = (ArrayList<BlockedRelation>) results.values;
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder {
        public TextView attribTextView;
        public ImageButton attribImageView;
        public Spinner phoneNumberSpinner;
        public ArrayList<String> userPhoneNumberList;
    }
}
