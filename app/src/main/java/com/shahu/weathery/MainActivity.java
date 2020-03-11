package com.shahu.weathery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shahu.weathery.adapter.LocationRecyclerViewAdapter;
import com.shahu.weathery.common.CheckPermissions;
import com.shahu.weathery.common.LocationSharedPreferences;
import com.shahu.weathery.common.VolleyRequest;
import com.shahu.weathery.customui.CustomSearchDialog;
import com.shahu.weathery.customui.TextHolderSubstanceCaps;
import com.shahu.weathery.helper.Locator;
import com.shahu.weathery.helper.RecyclerViewItemHelper;
import com.shahu.weathery.helper.ValuesConverter;
import com.shahu.weathery.interface2.IRecyclerViewListener;
import com.shahu.weathery.interface2.IVolleyResponse;
import com.shahu.weathery.interface2.OnDragListener;
import com.shahu.weathery.interface2.OnSearchItemSelection;
import com.shahu.weathery.interface2.OnSwipeListener;
import com.shahu.weathery.model.CardModel;
import com.shahu.weathery.model.CitySearchItem;
import com.shahu.weathery.model.common.MainResponse;

import net.danlew.android.joda.JodaTimeAndroid;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;

import static com.shahu.weathery.common.Constants.CITIES_DATA_FOR_SEARCH_LIST;
import static com.shahu.weathery.common.Constants.CURRENT_LOCATION_HTTP_REQUEST;
import static com.shahu.weathery.common.Constants.WEATHER_BY_ID_HTTP_REQUEST;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private CheckPermissions mCheckPermissions;
    private LocationSharedPreferences mLocationSharedPreferences;
    private TextHolderSubstanceCaps mDate, mTime;
    private TextView mCityName;
    private RecyclerView mRecyclerViewLocations;
    private ImageView mAddNewButton;
    private ArrayList<CardModel> mCardModelArrayList;
    private VolleyRequest mVolleyRequest;
    private IVolleyResponse mIVolleyResponseCallback = null;
    private LocationRecyclerViewAdapter mLocationRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
        setDateTime();
    }

    /**
     * initialize the variables.
     */
    private void initialization() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode());
        mCheckPermissions = new CheckPermissions(this, MainActivity.this);
        JodaTimeAndroid.init(this);
        mCityName = findViewById(R.id.main_city_name);
        initSharedPref();
        initVolleyCallback();
        mVolleyRequest = new VolleyRequest(this, mIVolleyResponseCallback);
        mCheckPermissions.checkApplicationPermissions();
        mRecyclerViewLocations = findViewById(R.id.locations);
        mAddNewButton = findViewById(R.id.add_new_loc_btn);

        mAddNewButton.setVisibility(View.VISIBLE);
        mAddNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchForNewLocation();
            }
        });
        setCurrentCoordinates();
        mCardModelArrayList = new ArrayList<>();
        initRecyclerView();
    }

    /**
     * Search engine from cursor.
     */
    private void searchForNewLocation() {
        CustomSearchDialog customSearchDialog = new CustomSearchDialog(this,this, new ArrayList<CitySearchItem>());
        customSearchDialog.show();
        customSearchDialog.setOnItemSelected(new OnSearchItemSelection() {
            @Override
            public void onClick(String cityId) {
                if (mLocationSharedPreferences.addNewLocation(cityId))
                    mVolleyRequest.getWeatherByCityId(cityId, WEATHER_BY_ID_HTTP_REQUEST);
                else
                    Toast.makeText(MainActivity.this, "Already Exist", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Method to set the weather of other favourites location.
     *
     * @param jsonObject data
     */
    private void addFavouriteCityWeather(JsonObject jsonObject) {
        Gson gson = new Gson();
        MainResponse mainResponse = gson.fromJson(jsonObject.toString(), MainResponse.class);
        CardModel cardModel = new CardModel();
        String cityName = mainResponse.getName();
        if (cityName.length() > 16) {
            cityName = cityName.substring(0, 16) + "...";
        }
        cardModel.setName(cityName);
        cardModel.setCountryCode(mainResponse.getSys().getCountry());
        cardModel.setPosition(Integer.parseInt(mLocationSharedPreferences.getPositionByCityId(String.valueOf(mainResponse.getId()))));
        cardModel.setTemperature(String.valueOf(mainResponse.getMain().getTemp()));
        cardModel.setTime(mainResponse.getDt());
        cardModel.setSecondsShift(mainResponse.getTimezone());
        cardModel.setWeatherItem(mainResponse.getWeather().get(0));
        cardModel.setDescription(mainResponse.getWeather().get(0).getDescription().toUpperCase());
        cardModel.setCityId(String.valueOf(mainResponse.getId()));
        cardModel.setDayNight(ValuesConverter.getDayNight(mainResponse));
        mCardModelArrayList.add(cardModel);
        Collections.sort(mCardModelArrayList, new Comparator<CardModel>() {
            @Override
            public int compare(CardModel o1, CardModel o2) {
                return o1.getPosition() - o2.getPosition();
            }
        });
        mLocationRecyclerViewAdapter.notifyDataSetChanged();
    }

    /**
     * Method to add current location plus city name to header.
     *
     * @param jsonObject data
     */
    private void addCurrentLocationData(JsonObject jsonObject) {
        Gson gson = new Gson();
        MainResponse mainResponse = gson.fromJson(jsonObject.toString(), MainResponse.class);
        CardModel cardModel = new CardModel();
        cardModel.setName("Current Location");
        cardModel.setCountryCode(mainResponse.getSys().getCountry());
        cardModel.setPosition(0);
        cardModel.setCityId(String.valueOf(mainResponse.getId()));
        cardModel.setTemperature(String.valueOf(mainResponse.getMain().getTemp()));
        cardModel.setWeatherItem(mainResponse.getWeather().get(0));
        cardModel.setDayNight(ValuesConverter.getDayNight(mainResponse));
        cardModel.setDescription(mainResponse.getWeather().get(0).getDescription().toUpperCase());
        mCardModelArrayList.add(cardModel);
        mLocationRecyclerViewAdapter.notifyDataSetChanged();
        mCityName.setText(String.format("%s, %s", mainResponse.getName(), mainResponse.getSys().getCountry()));
    }


    /**
     * Method to fetch all stored location.
     *
     * @param allLocations contain all saved location in sharedPreference.
     */
    private void fetchAllData(Map<String, ?> allLocations) {
        Log.d(TAG, "fetchAllData: " + allLocations);
        for (Map.Entry<String, ?> entry : allLocations.entrySet()) {
            mVolleyRequest.getWeatherByCityId(entry.getValue().toString(), WEATHER_BY_ID_HTTP_REQUEST);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCheckPermissions.checkApplicationPermissions();
    }

    /**
     * Method to set date/time continuously.
     */
    private void setDateTime() {
        mDate = findViewById(R.id.main_date);
        mTime = findViewById(R.id.main_time);
        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final Calendar calendar = Calendar.getInstance();
                final SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMM", Locale.ENGLISH);
                final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aaa", Locale.ENGLISH);
                mDate.setText(dateFormat.format(calendar.getTime()));
                mTime.setText(timeFormat.format(calendar.getTime()));
                someHandler.postDelayed(this, 10000);
            }
        }, 10);
    }

    /**
     * Method to set current location coordinates.
     */
    private void setCurrentCoordinates() {
        //TODO: check for image here
        Locator locationHelper = new Locator(this);
        locationHelper.getLocation(Locator.Method.NETWORK_THEN_GPS, new Locator.Listener() {
            @Override
            public void onLocationFound(Location location) {
                mVolleyRequest.getWeatherByCoords(CURRENT_LOCATION_HTTP_REQUEST, location.getLongitude(), location.getLatitude());
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onLocationNotFound() {
                mCityName.setText("Location not found!");
                fetchAllData(mLocationSharedPreferences.getAllLocations());
                Log.e(TAG, "onLocationNotFound: ");
            }
        });

    }

    /**
     * Method for initializing the volleycalls.
     */
    private void initVolleyCallback() {
        mIVolleyResponseCallback = new IVolleyResponse() {
            @Override
            public void onSuccessResponse(JsonObject jsonObject, String requestType) {
                switch (requestType) {
                    case CURRENT_LOCATION_HTTP_REQUEST:
                        addCurrentLocationData(jsonObject);
                        fetchAllData(mLocationSharedPreferences.getAllLocations());
                        break;
                    case WEATHER_BY_ID_HTTP_REQUEST:
                        addFavouriteCityWeather(jsonObject);
                        break;

                    case "citiesData":
                        Log.d(TAG, "onSuccessResponse: "+jsonObject);
                        break;

                }
            }

            @Override
            public void onRequestFailure(VolleyError volleyError, String requestType) {
                switch (requestType) {
                    case WEATHER_BY_ID_HTTP_REQUEST:
                        break;
                }
                Toast.makeText(MainActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccessJsonArrayResponse(JSONArray jsonObject, String requestType) {
                switch (requestType){
                    case CITIES_DATA_FOR_SEARCH_LIST:
                        Log.d(TAG, "onSuccessJsonArrayResponse: "+jsonObject.toString());
                        break;
                }
            }
        };
    }

    /**
     * initialize the recycler view.
     */
    private void initRecyclerView() {
        IRecyclerViewListener recyclerViewListener = new IRecyclerViewListener() {
            @Override
            public void onSingleShortClickListener(String cityId, long time, String dayNight, String temperature, String description) {
                openDetailedView(cityId, time, dayNight, temperature, description);
            }
        };
        mLocationRecyclerViewAdapter = new LocationRecyclerViewAdapter(mCardModelArrayList, this, recyclerViewListener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewLocations.setLayoutManager(layoutManager);
        mRecyclerViewLocations.setAdapter(mLocationRecyclerViewAdapter);

        RecyclerViewItemHelper<CardModel> touchHelper = new RecyclerViewItemHelper<>(mCardModelArrayList,
                (RecyclerView.Adapter) mLocationRecyclerViewAdapter);
        touchHelper.setRecyclerItemDragEnabled(true).setOnDragItemListener(new OnDragListener() {
            @Override
            public void onDragItemListener(int fromPosition, int toPosition) {
                Log.d(TAG, "onDragItemListener: from: " + fromPosition + ", to: " + toPosition);
                mLocationSharedPreferences.updatePosition(fromPosition, toPosition);
            }
        });
        touchHelper.setRecyclerItemSwipeEnabled(true).setOnSwipeItemListener(new OnSwipeListener() {
            @Override
            public void onSwipeItemListener(RecyclerView.ViewHolder oldPosition) {
                LocationRecyclerViewAdapter.MyViewHolder myViewHolder = (LocationRecyclerViewAdapter.MyViewHolder) oldPosition;
                if (myViewHolder != null) {
                    Log.d(TAG, "onSwipeItemListener: remove location: " + mLocationSharedPreferences.removeLocation(myViewHolder.cityId));
                }
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelper);
        itemTouchHelper.attachToRecyclerView(mRecyclerViewLocations);
    }

    /**
     * Method to initialize the sharedPreference.
     */
    private void initSharedPref() {
        mLocationSharedPreferences = new LocationSharedPreferences(this);
    }

    /**
     * gets called when click on list item to show detailed view.
     *
     * @param cityId      city identifier
     * @param time
     * @param dayNight
     * @param temperature
     * @param description
     */
    private void openDetailedView(String cityId, long time, String dayNight, String temperature, String description) {
        Intent intent = new Intent(this, WeatherDetail.class);
        intent.putExtra("id", cityId);
        intent.putExtra("time", time);
        intent.putExtra("day", dayNight);
        intent.putExtra("temperature", temperature);
        intent.putExtra("desc", description);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
