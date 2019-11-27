package com.shahu.weathery;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shahu.weathery.adapter.LocationRecyclerViewAdapter;
import com.shahu.weathery.common.CheckPermissions;
import com.shahu.weathery.common.LocationSharedPreferences;
import com.shahu.weathery.common.VolleyRequest;
import com.shahu.weathery.customui.CustomSearchDialog;
import com.shahu.weathery.helper.DatabaseHandler;
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
import com.shahu.weathery.model.OpenWeatherMainResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;

import static com.shahu.weathery.common.Constants.CURRENT_LOCATION_HTTP_REQUEST;
import static com.shahu.weathery.common.Constants.WEATHER_HTTP_REQUEST_BY_ID;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private CheckPermissions mCheckPermissions;
    private LocationSharedPreferences mLocationSharedPreferences;
    private TextView mDate, mTime, mCityName;
    private RecyclerView mRecyclerViewLocations;
    private ImageView mAddNewButton;
    private ArrayList<CardModel> mCardModelArrayList;
    private DatabaseHandler mDatabaseHandler;
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
        mCityName = findViewById(R.id.main_city_name);
        initSharedPref();
        initVolleyCallback();
        initDatabase();
        mVolleyRequest = new VolleyRequest(this, mIVolleyResponseCallback);
        mCheckPermissions.checkApplicationPermissions();
        mRecyclerViewLocations = findViewById(R.id.locations);
        mAddNewButton = findViewById(R.id.add_new_loc_btn);
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
        CustomSearchDialog customSearchDialog = new CustomSearchDialog(this, mDatabaseHandler.getAllCities());
        customSearchDialog.show();
        customSearchDialog.setOnItemSelected(new OnSearchItemSelection() {
            @Override
            public void onClick(int position, CitySearchItem citySearchItem) {
                String cityId = String.valueOf(citySearchItem.getId());
                if (mLocationSharedPreferences.addNewLocation(cityId))
                    mVolleyRequest.getWeatherByCityId(cityId, WEATHER_HTTP_REQUEST_BY_ID);
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
        OpenWeatherMainResponse openWeatherMainResponse = gson.fromJson(jsonObject.toString(), OpenWeatherMainResponse.class);
        CardModel cardModel = new CardModel();
        cardModel.setName(openWeatherMainResponse.getName());
        cardModel.setCountryCode(openWeatherMainResponse.getSys().getCountry());
        cardModel.setPosition(Integer.parseInt(mLocationSharedPreferences.getPositionByCityId(openWeatherMainResponse.getId())));
        cardModel.setTemperature(String.valueOf(openWeatherMainResponse.getMain().getTemp()));
        cardModel.setWeatherItem(openWeatherMainResponse.getWeather().get(0));
        cardModel.setDescription(openWeatherMainResponse.getWeather().get(0).getDescription().toUpperCase());
        cardModel.setCityId(openWeatherMainResponse.getId());
        cardModel.setDayNight(ValuesConverter.getDayNight(openWeatherMainResponse));
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
        OpenWeatherMainResponse openWeatherMainResponse = gson.fromJson(jsonObject.toString(), OpenWeatherMainResponse.class);
        CardModel cardModel = new CardModel();
        cardModel.setName("Current Location");
        cardModel.setPosition(0);
        cardModel.setCityId(openWeatherMainResponse.getId());
        cardModel.setTemperature(String.valueOf(openWeatherMainResponse.getMain().getTemp()));
        cardModel.setWeatherItem(openWeatherMainResponse.getWeather().get(0));
        cardModel.setDayNight(ValuesConverter.getDayNight(openWeatherMainResponse));
        cardModel.setDescription(openWeatherMainResponse.getWeather().get(0).getDescription().toUpperCase());
        mCardModelArrayList.add(cardModel);
        mLocationRecyclerViewAdapter.notifyDataSetChanged();
        mCityName.setText(String.format("%s, %s", openWeatherMainResponse.getName(), openWeatherMainResponse.getSys().getCountry()));
    }


    /**
     * Method to fetch all stored location.
     *
     * @param allLocations contain all saved location in sharedPreference.
     */
    private void fetchAllData(Map<String, ?> allLocations) {
        Log.d(TAG, "fetchAllData: " + allLocations);
        for (Map.Entry<String, ?> entry : allLocations.entrySet()) {
            mVolleyRequest.getWeatherByCityId(entry.getValue().toString(), WEATHER_HTTP_REQUEST_BY_ID);
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
                    case WEATHER_HTTP_REQUEST_BY_ID:
                        addFavouriteCityWeather(jsonObject);
                        break;
                }
            }

            @Override
            public void onRequestFailure(VolleyError volleyError, String requestType) {
                Toast.makeText(MainActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    /**
     * Method to initialize cities list from db with id.
     */
    private void initDatabase() {
        new Thread() {
            @Override
            public void run() {
                mDatabaseHandler = new DatabaseHandler(MainActivity.this);
                mAddNewButton.setClickable(true);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAddNewButton.setVisibility(View.VISIBLE);
                    }
                });
            }
        }.start();
    }

    /**
     * initialize the recycler view.
     */
    private void initRecyclerView() {
        IRecyclerViewListener recyclerViewListener = new IRecyclerViewListener() {
            @Override
            public void onSingleShortClickListener(int cityId) {
                Log.d(TAG, "onSingleShortClickListener: " + cityId);
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
        Log.d(TAG, "initSharedPref: " + mLocationSharedPreferences.getAllLocations());
    }
}
