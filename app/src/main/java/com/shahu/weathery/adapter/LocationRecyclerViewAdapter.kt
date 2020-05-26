package com.shahu.weathery.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.shahu.weathery.R
import com.shahu.weathery.adapter.LocationRecyclerViewAdapter.MyViewHolder
import com.shahu.weathery.common.Constants
import com.shahu.weathery.customui.CitynameTextView
import com.shahu.weathery.helper.ImageHelper
import com.shahu.weathery.helper.ValuesConverter.convertTemperatureToCelsius
import com.shahu.weathery.helper.ValuesConverter.getCountryImage
import com.shahu.weathery.helper.ValuesConverter.getTimeForCity
import com.shahu.weathery.interface2.IRecyclerViewListener
import com.shahu.weathery.model.CardModel
import java.util.*

/**
 * Created by Shahu Ronghe on 20, September, 2019
 * in Weathery
 */
class LocationRecyclerViewAdapter(private val mCardModelArrayList: ArrayList<CardModel>, private val mContext: Context, private val mIRecyclerViewListener: IRecyclerViewListener?) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.location_card, viewGroup, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        myViewHolder.cardName.text = mCardModelArrayList[i].name?.toUpperCase(Locale.ROOT)
        val iconUrl = ImageHelper.getDescriptionImageDrawable(mCardModelArrayList[i])
        Glide.with(mContext).load(iconUrl).error(R.drawable.default_weather_icon).into(myViewHolder.cardImage)
        myViewHolder.cardTemperature.text = mCardModelArrayList[i].temperature?.let { convertTemperatureToCelsius(it) } + "\u00B0C"
        myViewHolder.cardDescription.text = mCardModelArrayList[i].description
        myViewHolder.cityId = mCardModelArrayList[i].cityId.toString()
        myViewHolder.cardFlag.text = getCountryImage(mCardModelArrayList[i].countryCode)
        myViewHolder.cardImage.buildDrawingCache()
        if (mCardModelArrayList[i].time != 0L) {
            val time = getTimeForCity(
                    mCardModelArrayList[i].time, mCardModelArrayList[i].secondsShift)
            myViewHolder.cardTime.text = time
        }
    }

    override fun getItemCount(): Int {
        return mCardModelArrayList.size
    }

    fun clear() {
        mCardModelArrayList.clear()
    }

    inner class MyViewHolder internal constructor(itemView: View) : ViewHolder(itemView), View.OnClickListener {
        lateinit var cityId: String
        private var mainRelativeLayout: RelativeLayout = itemView.findViewById(R.id.card_main_RL)
        var cardName: CitynameTextView = itemView.findViewById(R.id.card_name)
        var cardTemperature: TextView = itemView.findViewById(R.id.card_temperature)
        var cardDescription: TextView = itemView.findViewById(R.id.card_desc)
        var cardImage: ImageView = itemView.findViewById(R.id.card_image)
        var cardFlag: TextView = itemView.findViewById(R.id.card_flag)
        var cardTime: TextView = itemView.findViewById(R.id.card_time)
        override fun onClick(v: View) {
            if (mIRecyclerViewListener != null) {
                val bundle = Bundle()
                bundle.putString(Constants.BUNDLE_CITY_ID, mCardModelArrayList[layoutPosition].cityId)
                bundle.putString(Constants.BUNDLE_CITY_NAME, mCardModelArrayList[layoutPosition].name)
                bundle.putString(Constants.BUNDLE_DAY_NIGHT, mCardModelArrayList[layoutPosition].dayNight)
                bundle.putLong(Constants.BUNDLE_TIME, mCardModelArrayList[layoutPosition].time)
                bundle.putString(Constants.BUNDLE_DESCRIPTION, mCardModelArrayList[layoutPosition].description)
                bundle.putString(Constants.BUNDLE_TEMPERATURE, mCardModelArrayList[layoutPosition].temperature)
                bundle.putString(Constants.BUNDLE_IMAGE_URL,
                        ImageHelper.getDescriptionImageDrawable(mCardModelArrayList[layoutPosition]))
                mIRecyclerViewListener.onSingleShortClickListener(bundle)
            }
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    companion object {
        private const val TAG = "LocationRecyclerViewAda"
    }

}