package com.shahu.weathery.customui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.shahu.weathery.R;
import com.shahu.weathery.adapter.SearchDialogListAdapter;
import com.shahu.weathery.common.VolleyRequest;
import com.shahu.weathery.interface2.IVolleyResponse;
import com.shahu.weathery.interface2.OnSearchItemSelection;
import com.shahu.weathery.model.CitySearchItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.shahu.weathery.common.Constants.CITIES_DATA_FOR_SEARCH_LIST;

/**
 * Created by Shahu Ronghe on 26, November, 2019
 * in Weathery
 */
public class CustomSearchDialog {
    private static final String TAG = "CustomSearchDialog";
    public OnSearchItemSelection mOnSearchItemSelection;
    AlertDialog mAlertDialog;
    CitySearchItem mCountrySearchSingleItem = null;
    SearchDialogListAdapter mSearchDialogListAdapter;
    ListView mListView;
    Context mContext;
    private int mPosition;
    private List<CitySearchItem> mCitySearchItemsList;
    private Activity mActivity;
    private String mDialogTitle = "Enter City Name";
    private VolleyRequest mVolleyRequest;
    private IVolleyResponse mIVolleyResponseCallback = null;

    public CustomSearchDialog(Context context, Activity activity, List<CitySearchItem> searchListItems) {
        this.mCitySearchItemsList = searchListItems;
        this.mActivity = activity;
        mContext = context;
        initVolleyCallback();
        mVolleyRequest = new VolleyRequest(context, mIVolleyResponseCallback);
    }

    private void initVolleyCallback() {
        mIVolleyResponseCallback = new IVolleyResponse() {
            @Override
            public void onSuccessResponse(JsonObject jsonObject, String requestType) {

            }

            @Override
            public void onRequestFailure(VolleyError volleyError, String requestType) {

            }

            @Override
            public void onSuccessJsonArrayResponse(JSONArray jsonArray, String requestType) throws JSONException {
                if (requestType.equals(CITIES_DATA_FOR_SEARCH_LIST)) {
                    List<CitySearchItem> filteredValues = new ArrayList<>();
                    if (jsonArray.length() == 0) {
                        CitySearchItem item = new CitySearchItem(0, "Not found!", "XX");
                        filteredValues.add(item);
                        mSearchDialogListAdapter = new SearchDialogListAdapter(mActivity, R.layout.items_view_layout, R.id.cityCountryRL,
                                filteredValues);
                        mListView.setAdapter(mSearchDialogListAdapter);
                        return;
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        final int id = jsonObject.getInt("id");
                        final String name = jsonObject.getString("name");
                        final String countryCode = jsonObject.getString("country");
                        CitySearchItem item = new CitySearchItem(id, name, countryCode);
                        filteredValues.add(item);
                    }
                    mSearchDialogListAdapter = new SearchDialogListAdapter(mActivity, R.layout.items_view_layout, R.id.cityCountryRL, filteredValues);
                    mListView.setAdapter(mSearchDialogListAdapter);
                }
            }
        }

        ;
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
        final TextView searchGoBtn = view.findViewById(R.id.search_go_btn);
        searchGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVolleyRequest.getCitiesData(searchBox.getText().toString(), CITIES_DATA_FOR_SEARCH_LIST);
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RelativeLayout relativeLayout = view.findViewById(R.id.cityCountryRL);
                TextView t = relativeLayout.findViewById(R.id.city);
                int id = (int) t.getTag();
                try {
                    mOnSearchItemSelection.onClick(String.valueOf(id));
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
                if (charSequence.length() >= 3) {
                    searchGoBtn.setEnabled(true);
                } else {
                    searchGoBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
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
