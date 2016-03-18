package com.projects.nir.myapplication.adapters;

import android.app.Activity;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.projects.nir.myapplication.R;
import com.projects.nir.myapplication.Entities.UserProfile;
import com.projects.nir.myapplication.database.DataBaseConstants;

import java.util.ArrayList;

/**
 * Created by zilkha on 25/02/2016.
 */
public class UserProfileListAdapter extends BaseAdapter implements Filterable {

    private Activity context;
    private ArrayList<UserProfile> list;
    ArrayList<UserProfile> mStringFilterList;
    ValueFilter valueFilter;

    // private final int[] bgColors = new int[]{R.color.list_bg_1, R.color.list_bg_2};

    public UserProfileListAdapter(Activity con, ArrayList<UserProfile> arrList) {
        context = con;
        list = arrList;
        mStringFilterList = arrList;
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

            convertView = inflater.inflate(R.layout.listview_item_profile, parent, false);

            viewHolder.attribTextView = (TextView) convertView.findViewById(R.id.attribTextView);
            viewHolder.attribImageView = (ImageView) convertView.findViewById(R.id.attribImageView);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // convertView.setBackgroundResource(bgColors[position % 2]);
        UserProfile userDetails = list.get(position);

        viewHolder.attribTextView.setText(userDetails.attrib);
        if (userDetails.attribType == DataBaseConstants.ProfileTable.AttribType.PHONE_NUM)
            viewHolder.attribImageView.setImageResource(android.R.drawable.sym_action_call);
        else
            viewHolder.attribImageView.setImageResource(R.drawable.car_icon);

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
        protected android.widget.Filter.FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<UserProfile> filterList = new ArrayList<UserProfile>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if (((mStringFilterList.get(i).attrib.toUpperCase())
                            .contains(constraint.toString().toUpperCase()))) {

                        UserProfile userDetails = new UserProfile(mStringFilterList.get(i).get_Id(), mStringFilterList.get(i).userId
                                , mStringFilterList.get(i).attrib
                                , mStringFilterList.get(i).attribType);

                        filterList.add(userDetails);
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
            list = (ArrayList<UserProfile>) results.values;
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder {
        public TextView attribTextView;
        public ImageView attribImageView;
    }
}
