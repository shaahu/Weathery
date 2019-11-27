package com.shahu.weathery.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shahu.weathery.R;
import com.shahu.weathery.helper.ValuesConverter;
import com.shahu.weathery.model.CitySearchItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahu Ronghe on 27, November, 2019
 * in Weathery
 */
public class SearchDialogListAdapter extends ArrayAdapter {

    public static String TAG = "SearchDialogListAdapter";
    public List<CitySearchItem> searchListItems;
    List<CitySearchItem> suggestions = new ArrayList<>();
    CustomFilter filter = new CustomFilter();
    LayoutInflater inflater;
    private Context mContext;
    private int textviewResourceID;

    public SearchDialogListAdapter(@NonNull Context context, int resource, int textViewResourceId, List objects) {
        super(context, resource, textViewResourceId, objects);
        this.mContext = context;
        this.searchListItems = objects;
        this.textviewResourceID = textViewResourceId;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View inflateview = view;
        if (inflateview == null) {
            inflateview = inflater
                    .inflate(R.layout.items_view_layout, null);

        }
        RelativeLayout relativeLayout = inflateview.findViewById(textviewResourceID);
        TextView city = relativeLayout.findViewById(R.id.city);
        TextView country = relativeLayout.findViewById(R.id.country);
        city.setText(searchListItems.get(i).getCityName());
        country.setText(ValuesConverter.getCountryImage(searchListItems.get(i).getCountryCode()));
        return inflateview;
    }

    @Override
    public int getCount() {
        return searchListItems.size();
    }

    @Override
    public CitySearchItem getItem(int i) {
        return searchListItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return searchListItems.get(i).getId();
    }

    public int getposition(int id) {
        int position = 0;
        for (int i = 0; i < searchListItems.size(); i++) {
            if (id == searchListItems.get(i).getId()) {
                position = i;
            }
        }
        return position;
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            suggestions.clear();
            if (searchListItems != null && constraint != null) { // Check if the Original List and Constraint aren't null.
                for (int i = 0; i < searchListItems.size(); i++) {
                    if (searchListItems.get(i).getCityName().toLowerCase().contains(constraint)) { // Compare item in original searchListItems if it
                        // contains constraints.
                        suggestions.add(searchListItems.get(i)); // If TRUE add item in Suggestions.
                    }
                }
            }
            FilterResults results = new FilterResults(); // Create new Filter Results and return this to publishResults;
            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
