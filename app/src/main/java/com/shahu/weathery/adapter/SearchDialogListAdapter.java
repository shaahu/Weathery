package com.shahu.weathery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shahu.weathery.R;
import com.shahu.weathery.helper.ValuesConverter;
import com.shahu.weathery.model.CitySearchItem;

import java.util.List;

/**
 * Created by Shahu Ronghe on 27, November, 2019
 * in Weathery
 */
public class SearchDialogListAdapter extends ArrayAdapter {

    public static String TAG = "SearchDialogListAdapter";
    public List<CitySearchItem> searchListItems;
    LayoutInflater inflater;
    private int textviewResourceID;

    public SearchDialogListAdapter(Context context, int resource, int textViewResourceId, List<CitySearchItem> objects) {
        super(context, resource, textViewResourceId, objects);
        this.searchListItems = objects;
        this.textviewResourceID = textViewResourceId;
        inflater = LayoutInflater.from(context);
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
        city.setTag(searchListItems.get(i).getId());
        final String countryCode = searchListItems.get(i).getCountryCode();
        if (!countryCode.equals("XX"))
            country.setText(ValuesConverter.getCountryImage(countryCode));
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
}
