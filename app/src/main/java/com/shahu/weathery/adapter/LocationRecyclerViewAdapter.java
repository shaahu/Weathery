package com.shahu.weathery.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shahu.weathery.R;
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
        String iconUrl = ImageHelper.getDescriptionImageDrawable(mCardModelArrayList.get(i));
        Log.d(TAG, "onBindViewHolder: image URL: " + iconUrl);
        Glide.with(mContext).load(iconUrl).error(R.drawable.default_weather_icon).into(myViewHolder.cardImage);
        myViewHolder.cardTemperature.setText(ValuesConverter.convertTemperatureToCelsius(mCardModelArrayList.get(i).getTemperature()) +
                "\u00B0C");
        myViewHolder.cardDescription.setText(mCardModelArrayList.get(i).getDescription());
        myViewHolder.cityId = mCardModelArrayList.get(i).getCityId();
        myViewHolder.cardFlag.setText(ValuesConverter.getCountryImage(mCardModelArrayList.get(i).getCountryCode()));
        myViewHolder.cardImage.buildDrawingCache();
        if (( myViewHolder.cardImage.getDrawable()) != null) {
            Bitmap bitmap = ((BitmapDrawable) myViewHolder.cardImage.getDrawable()).getBitmap();
            Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    if (palette != null) {
                        GradientDrawable relativeLayoutBackground = (GradientDrawable) myViewHolder.mainRelativeLayout.getBackground();
                        int color = palette.getVibrantColor(mContext.getResources().getColor(R.color.black));
                        relativeLayoutBackground.setColor(color);
                        relativeLayoutBackground.setAlpha(75);
                    }
                }
            });
        }
        if (mCardModelArrayList.get(i).getTime() != 0) {
            String time = ValuesConverter.getTimeForCity(mCardModelArrayList.get(i).getTime(), mCardModelArrayList.get(i).getSecondsShift());
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
            if (mIRecyclerViewListener != null)
                mIRecyclerViewListener.onSingleShortClickListener(mCardModelArrayList.get(getLayoutPosition()).getCityId(),
                        mCardModelArrayList.get(getLayoutPosition()).getTime(),
                        mCardModelArrayList.get(getLayoutPosition()).getDayNight(),
                        mCardModelArrayList.get(getLayoutPosition()).getTemperature(),
                        mCardModelArrayList.get(getLayoutPosition()).getDescription());
        }
    }

}
