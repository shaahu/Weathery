package com.shahu.weathery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.shahu.weathery.R
import com.shahu.weathery.customui.TextHolderRegular
import com.shahu.weathery.helper.ImageHelper
import com.shahu.weathery.model.HourlyModel


/**
 * Created by Shahu Ronghe on 20, September, 2019
 * in Weathery
 */
class HourlyRecyclerViewAdapter(private val mHourlyList: ArrayList<HourlyModel>, private val mContext: Context) : RecyclerView
.Adapter<HourlyRecyclerViewAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_hourly_forecast, viewGroup, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        val hourly = mHourlyList[i]
        myViewHolder.hour.text = hourly.value
        myViewHolder.temperature.text = hourly.temperature + "°"

        if (hourly.mainDescription == "inva") {
            if (hourly.timeOfDay == "sunrise")
                myViewHolder.icon.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_sunrise))
            if (hourly.timeOfDay == "sunset")
                myViewHolder.icon.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_sunset))
        } else {
            val iconUrl = ImageHelper.getDescriptionImageDrawable(hourly.mainDescription,
                    hourly.subDescription,
                    hourly.timeOfDay)

            Glide.with(mContext)
                    .load(iconUrl)
                    .apply(RequestOptions().override(100, 100))
                    .error(R.drawable.default_weather_icon)
                    .into(myViewHolder.icon)
        }
    }

    override fun getItemCount(): Int {
        return mHourlyList.size
    }

    fun clear() {
        mHourlyList.clear()
    }

    inner class MyViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hour: TextHolderRegular = itemView.findViewById(R.id.hourly_hour)
        val icon: ImageView = itemView.findViewById(R.id.hourly_img)
        val temperature: TextHolderRegular = itemView.findViewById(R.id.hourly_temperature)
    }

    companion object {
        private const val TAG = "HourlyRecyclerViewAdapter"
    }

}