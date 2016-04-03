package com.projects.nir.myapplication.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.projects.nir.myapplication.R;
import com.projects.nir.myapplication.Entities.UserProfile;
import com.projects.nir.myapplication.ThirdParty.DebouncedOnClickListener;
import com.projects.nir.myapplication.interfaces.IDataAccessLayer;

import java.util.ArrayList;

/**
 * Created by zilkha on 25/02/2016.
 */
public class BlockedUnblockCarsListAdapter extends BaseAdapter implements Filterable {

    private Activity context;
    private ArrayList<UserProfile> list;
    ArrayList<UserProfile> mStringFilterList;
    ValueFilter valueFilter;
    IDataAccessLayer _DataAccessLayer;
    int _userId;
    private boolean _block;
    OptionInterface _optionCallBack;

    public BlockedUnblockCarsListAdapter(Activity con, int userId, IDataAccessLayer dataAccessLayer, ArrayList<UserProfile> arrList,boolean block,OptionInterface optionCallBack) {
        context = con;
        list = arrList;
        mStringFilterList = arrList;
        _DataAccessLayer = dataAccessLayer;
        _userId = userId;
        _block = block;
        _optionCallBack =optionCallBack;
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

            if (_block)
            {
                convertView = inflater.inflate(R.layout.listview_item_block_car, parent, false);
                viewHolder.actionButton = (Button) convertView.findViewById(R.id.blockButton);
            }
            else
            {
                convertView = inflater.inflate(R.layout.listview_item_unblock_car, parent, false);
                viewHolder.actionButton = (Button) convertView.findViewById(R.id.unblockButton);
            }

            viewHolder.attribTextView = (TextView) convertView.findViewById(R.id.attribTextView);
            viewHolder.attribImageView = (ImageView) convertView.findViewById(R.id.attribImageView);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // convertView.setBackgroundResource(bgColors[position % 2]);
        final UserProfile requesetdProfile = list.get(position);

        viewHolder.attribTextView.setText(requesetdProfile.attrib);
        viewHolder.attribImageView.setImageResource(R.drawable.car_icon);

        if (_block) {
           viewHolder.actionButton.setEnabled(requesetdProfile.isBlocked == 0);
        }
        viewHolder.actionButton.setOnClickListener(new DebouncedOnClickListener(3000) {
            @Override
            public void onDebouncedClick(View v) {
                _optionCallBack.callBack(v,requesetdProfile);
            }
        });

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
        public Button actionButton;
    }
}
