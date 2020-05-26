package com.shahu.weathery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.shahu.weathery.helper.ValuesConverter.getCountryImage
import com.shahu.weathery.model.CitySearchItem
import kotlinx.android.synthetic.main.items_view_layout.view.*

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
        var holder: ViewHolder
        var retView: View

        if(convertView == null) {
            retView = vi.inflate(resource, null)
            holder = ViewHolder()

            holder.city?.text = list[i].cityName
            holder.city?.tag = list[i].id
            val countryCode = list[i].countryCode
            if (countryCode != "XX") holder.country?.text = getCountryImage(countryCode)
        }

        else {
            retView = convertView
        }

        return retView
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

    internal class ViewHolder {
        var city: TextView? = null
        var country : TextView? =null
    }

}