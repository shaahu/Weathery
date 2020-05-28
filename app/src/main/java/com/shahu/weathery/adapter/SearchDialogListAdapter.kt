package com.shahu.weathery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.shahu.weathery.R
import com.shahu.weathery.helper.ValuesConverter.getCountryImage
import com.shahu.weathery.model.CitySearchItem


/**
 * Created by Shahu Ronghe on 27, November, 2019
 * in Weathery
 */
open class SearchDialogListAdapter(context: Context, resource: Int,
                                   textviewResourceID: Int,
                                   searchListItems: List<CitySearchItem>) :
        ArrayAdapter<CitySearchItem>(context, resource, textviewResourceID, searchListItems) {

    var resource: Int
    var list: ArrayList<CitySearchItem>
    var vi: LayoutInflater

    init {
        this.resource = resource
        this.list = searchListItems as ArrayList<CitySearchItem>
        this.vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }


    override fun getView(i: Int, convertView: View?, viewGroup: ViewGroup): View? {
        val inflater = LayoutInflater.from(context)
        val rowView = inflater.inflate(R.layout.items_view_layout, null, true)
        val city = rowView.findViewById(R.id.city) as TextView
        val country = rowView.findViewById(R.id.country) as TextView
        city.text = list[i].cityName
        city.tag = list[i].id
        val countryCode = list[i].countryCode
        if (countryCode != "XX") country.text = getCountryImage(countryCode)
        return rowView
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(i: Int): CitySearchItem? {
        return list[i]
    }

    override fun getItemId(i: Int): Long {
        return list[i].id.toLong()
    }

    companion object {
        var TAG = "SearchDialogListAdapter"
    }
}