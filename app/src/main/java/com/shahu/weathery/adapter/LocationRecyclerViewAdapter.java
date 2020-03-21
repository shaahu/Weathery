package com.shahu.weathery.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shahu.weathery.R;
import com.shahu.weathery.common.Constants;
import com.shahu.weathery.customui.CitynameTextView;
import com.shahu.weathery.helper.ImageHelper;
import com.shahu.weathery.helper.ValuesConverter;
import com.shahu.weathery.interface2.IRecyclerViewListener;
import com.shahu.weathery.model.CardModel;

import java.util.ArrayList;

/**
 * Created by Shahu Ronghe on 20, September, 2019
 * in Weathery
 */
public class LocationRecyclerViewAdapter extends RecyclerView.Adapter<LocationRecyclerViewAdapter.MyViewHolder> {

    private static final String TAG = "LocationRecyclerViewAda";
    private ArrayList<CardModel> mCardModelArrayList;
    private Context mContext;
    private IRecyclerViewListener mIRecyclerViewListener;

    public LocationRecyclerViewAdapter(ArrayList<CardModel> cardModelArrayList, Context context, IRecyclerViewListener viewListener) {
        mCardModelArrayList = cardModelArrayList;
        mContext = context;
        mIRecyclerViewListener = viewListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.location_card, viewGroup, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        myViewHolder.cardName.setText(mCardModelArrayList.get(i).getName().toUpperCase());
        final String iconUrl = ImageHelper.getDescriptionImageDrawable(mCardModelArrayList.get(i));
        Glide.with(mContext).load(iconUrl).error(R.drawable.default_weather_icon).into(myViewHolder.cardImage);
        myViewHolder.cardTemperature.setText(
                ValuesConverter.convertTemperatureToCelsius(mCardModelArrayList.get(i).getTemperature()) + "\u00B0C");
        myViewHolder.cardDescription.setText(mCardModelArrayList.get(i).getDescription());
        myViewHolder.cityId = mCardModelArrayList.get(i).getCityId();
        myViewHolder.cardFlag.setText(ValuesConverter.getCountryImage(mCardModelArrayList.get(i).getCountryCode()));
        myViewHolder.cardImage.buildDrawingCache();
        if (mCardModelArrayList.get(i).getTime() != 0) {
            String time = ValuesConverter.getTimeForCity(
                    mCardModelArrayList.get(i).getTime(), mCardModelArrayList.get(i).getSecondsShift());
            myViewHolder.cardTime.setText(time);
        }
    }

    @Override
    public int getItemCount() {
        return mCardModelArrayList.size();
    }

    public void clear() {
        mCardModelArrayList.clear();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public String cityId;
        RelativeLayout mainRelativeLayout;
        CitynameTextView cardName;
        TextView cardTemperature;
        TextView cardDescription;
        ImageView cardImage;
        TextView cardFlag;
        TextView cardTime;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardName = itemView.findViewById(R.id.card_name);
            cardTemperature = itemView.findViewById(R.id.card_temperature);
            cardImage = itemView.findViewById(R.id.card_image);
            cardDescription = itemView.findViewById(R.id.card_desc);
            cardFlag = itemView.findViewById(R.id.card_flag);
            cardTime = itemView.findViewById(R.id.card_time);
            mainRelativeLayout = itemView.findViewById(R.id.card_main_RL);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mIRecyclerViewListener != null) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.BUNDLE_CITY_ID, mCardModelArrayList.get(getLayoutPosition()).getCityId());
                bundle.putString(Constants.BUNDLE_CITY_NAME, mCardModelArrayList.get(getLayoutPosition()).getName());
                bundle.putString(Constants.BUNDLE_DAY_NIGHT, mCardModelArrayList.get(getLayoutPosition()).getDayNight());
                bundle.putLong(Constants.BUNDLE_TIME, mCardModelArrayList.get(getLayoutPosition()).getTime());
                bundle.putString(Constants.BUNDLE_DESCRIPTION, mCardModelArrayList.get(getLayoutPosition()).getDescription());
                bundle.putString(Constants.BUNDLE_TEMPERATURE, mCardModelArrayList.get(getLayoutPosition()).getTemperature());
                bundle.putString(Constants.BUNDLE_IMAGE_URL,
                        ImageHelper.getDescriptionImageDrawable(mCardModelArrayList.get(getLayoutPosition())));

                mIRecyclerViewListener.onSingleShortClickListener(bundle);
            }
        }
    }

}
