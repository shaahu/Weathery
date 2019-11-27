package com.shahu.weathery.customui;

import android.app.Activity;
import android.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shahu.weathery.R;
import com.shahu.weathery.adapter.SearchDialogListAdapter;
import com.shahu.weathery.interface2.OnSearchItemSelection;
import com.shahu.weathery.model.CitySearchItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahu Ronghe on 26, November, 2019
 * in Weathery
 */
public class CustomSearchDialog {
    private static final String TAG = "CustomSearchDialog";
    public OnSearchItemSelection mOnSearchItemSelection;
    AlertDialog mAlertDialog;
    private int mPosition;
    CitySearchItem mCountrySearchSingleItem = null;
    SearchDialogListAdapter mSearchDialogListAdapter;
    ListView mListView;
    private List<CitySearchItem> mCitySearchItemsList;
    private Activity mActivity;
    private String mDialogTitle = "Enter City Name";

    public CustomSearchDialog(Activity activity, List<CitySearchItem> searchListItems) {
        this.mCitySearchItemsList = searchListItems;
        this.mActivity = activity;
    }

    /***
     *
     * show the seachable dialog
     */
    public void show() {
        final AlertDialog.Builder adb = new AlertDialog.Builder(mActivity);
        View view = mActivity.getLayoutInflater().inflate(R.layout.search_dialog_layout, null);
        TextView rippleViewClose = (TextView) view.findViewById(R.id.close);
        TextView title = (TextView) view.findViewById(R.id.spinerTitle);
        title.setText(mDialogTitle);
        mListView = (ListView) view.findViewById(R.id.list);

        final EditText searchBox = (EditText) view.findViewById(R.id.searchBox);
        mSearchDialogListAdapter = new SearchDialogListAdapter(mActivity, R.layout.items_view_layout, R.id.cityCountryRL, mCitySearchItemsList);
        mListView.setAdapter(mSearchDialogListAdapter);
        adb.setView(view);
        mAlertDialog = adb.create();
        mAlertDialog.setCancelable(false);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RelativeLayout relativeLayout = view.findViewById(R.id.cityCountryRL);
                TextView t = relativeLayout.findViewById(R.id.city);
                for (int j = 0; j < mCitySearchItemsList.size(); j++) {
                    if (t.getText().toString().equalsIgnoreCase(mCitySearchItemsList.get(j).getCityName().toString())) {
                        mPosition = j;
                        mCountrySearchSingleItem = mCitySearchItemsList.get(mPosition);
                    }
                }
                try {
                    mOnSearchItemSelection.onClick(mPosition, mCountrySearchSingleItem);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
                mAlertDialog.dismiss();
            }
        });

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                List<CitySearchItem> filteredValues = new ArrayList<>();
                for (int i = 0; i < mCitySearchItemsList.size(); i++) {
                    if (mCitySearchItemsList.get(i) != null) {
                        CitySearchItem item = mCitySearchItemsList.get(i);
                        if (item.getCityName().toLowerCase().trim().contains(searchBox.getText().toString().toLowerCase().trim())) {
                            filteredValues.add(item);
                        }
                    }
                }
                mSearchDialogListAdapter = new SearchDialogListAdapter(mActivity, R.layout.items_view_layout, R.id.cityCountryRL, filteredValues);
                mListView.setAdapter(mSearchDialogListAdapter);
            }
        });

        rippleViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });
        mAlertDialog.show();
    }


    /***
     *
     * @param onItemSelected
     * return selected position & item
     */
    public void setOnItemSelected(OnSearchItemSelection onItemSelected) {
        this.mOnSearchItemSelection = onItemSelected;
    }

    /***
     *
     * clear the list
     */
    public void clear() {
        this.mCitySearchItemsList.clear();
    }

    /***
     *
     * refresh the adapter (notifyDataSetChanged)
     */
    public void refresh() {
        mSearchDialogListAdapter.notifyDataSetChanged();
    }


    public SearchDialogListAdapter getAdapter() {
        return mSearchDialogListAdapter;
    }
}
