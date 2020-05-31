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
import com.shahu.weathery.common.Constants
import com.shahu.weathery.customui.TextHolderRegular
import com.shahu.weathery.customui.TextHolderRegularGreyout
import com.shahu.weathery.helper.ImageHelper
import com.shahu.weathery.model.DailyModel


/**
 * Created by Shahu Ronghe on 20, September, 2019
 * in Weathery
 */
class DailyRecyclerViewAdapter(private val mDailyList: ArrayList<DailyModel>,
                               private val mContext: Context)
    : RecyclerView.Adapter<DailyRecyclerViewAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_daily_forecast, viewGroup, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        val daily = mDailyList[i]
        myViewHolder.dayOfTheWeek.text = daily.value
        myViewHolder.maxTemp.text = daily.max
        myViewHolder.minTemp.text = daily.min

        val iconUrl = ImageHelper.getDescriptionImageDrawable(daily.mainDescription,
                daily.subDescription,
                Constants.DAY)

        Glide.with(mContext)
                .load(iconUrl)
                .apply(RequestOptions().override(100, 100))
                .error(R.drawable.default_weather_icon)
                .into(myViewHolder.icon)

    }

    override fun getItemCount(): Int {
        return mDailyList.size
    }

    fun clear() {
        mDailyList.clear()
    }

    inner class MyViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayOfTheWeek: TextHolderRegular = itemView.findViewById(R.id.day_of_week)
        val icon: ImageView = itemView.findViewById(R.id.daily_img)
        val maxTemp: TextHolderRegular = itemView.findViewById(R.id.daily_max)
        val minTemp: TextHolderRegularGreyout = itemView.findViewById(R.id.daily_min)
    }

    companion object {
        private const val TAG = "HourlyRecyclerViewAdapter"
    }

}