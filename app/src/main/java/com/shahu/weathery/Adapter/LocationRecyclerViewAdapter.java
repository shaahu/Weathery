package com.shahu.weathery.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shahu.weathery.Helper.ImageHelper;
import com.shahu.weathery.Helper.ValuesConverter;
import com.shahu.weathery.Interface.IRecyclerViewListener;
import com.shahu.weathery.Model.CardModel;
import com.shahu.weathery.R;

import java.io.IOException;
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
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.cardName.setText(mCardModelArrayList.get(i).getName().toUpperCase());

        try {
            myViewHolder.cardImage.setImageDrawable(ImageHelper.getDescriptionImageDrawable(mCardModelArrayList.get(i).getImageStr(),mContext));
        } catch (IOException e) {
            e.printStackTrace();
        }

        myViewHolder.cardTemperature.setText(ValuesConverter.convertTemperatureToCelsius(mCardModelArrayList.get(i).getTemperature()) + "\u2103");
        myViewHolder.cardDescription.setText(mCardModelArrayList.get(i).getDescription());
        myViewHolder.cityId = mCardModelArrayList.get(i).getCityId();
    }

    @Override
    public int getItemCount() {
        return mCardModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public int cityId;
        TextView cardName;
        TextView cardTemperature;
        TextView cardDescription;
        ImageView cardImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardName = itemView.findViewById(R.id.card_name);
            cardTemperature = itemView.findViewById(R.id.card_temperature);
            cardImage = itemView.findViewById(R.id.card_image);
            cardDescription = itemView.findViewById(R.id.card_desc);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mIRecyclerViewListener != null)
                mIRecyclerViewListener.onSingleShortClickListener(mCardModelArrayList.get(getLayoutPosition()).getCityId());
        }
    }

}
