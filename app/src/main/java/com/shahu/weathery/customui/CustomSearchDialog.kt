package com.shahu.weathery.customui

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import com.shahu.weathery.R
import com.shahu.weathery.adapter.SearchDialogListAdapter
import com.shahu.weathery.interface2.OnSearchItemSelection
import com.shahu.weathery.model.CitySearchItem
import com.shahu.weathery.retrofit.DataService
import com.shahu.weathery.retrofit.RetrofitInstance
import kotlinx.android.synthetic.main.search_dialog_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


/**
 * Created by Shahu Ronghe on 26, November, 2019
 * in Weathery
 */
class CustomSearchDialog(private val mContext: Context, private val mActivity: Activity, private val mCitySearchItemsList: MutableList<CitySearchItem>) {
    private var mOnSearchItemSelection: OnSearchItemSelection? = null
    var adapter: SearchDialogListAdapter? = null
    var mListView: ListView? = null
    private val mDialogTitle = "Enter City Name"
    private var mSearchProgressBar: ProgressBar? = null
    private var mEmptyListFlag = false

    private fun populateSearchResults(response: String?) {
        if (mEmptyListFlag) {
            mSearchProgressBar!!.visibility = View.GONE
            return
        }
        var responseNew = response
        mSearchProgressBar!!.visibility = View.INVISIBLE
        responseNew = responseNew?.replace("\n", "")
        responseNew = responseNew?.replace("[", "")
        responseNew = responseNew?.replace("]", "")
        var filteredValues: MutableList<CitySearchItem> = ArrayList()
        if (responseNew?.isEmpty()!!) {
            val item = CitySearchItem(0, "Not found!", "XX")
            filteredValues.add(item)
            adapter = SearchDialogListAdapter(
                    mActivity, R.layout.items_view_layout, R.id.cityCountryRL, filteredValues)
            mListView!!.adapter = adapter
            return
        }
        filteredValues = getCitySearchItemList(responseNew)
        adapter = SearchDialogListAdapter(mActivity,
                R.layout.items_view_layout,
                R.id.cityCountryRL,
                filteredValues)
        mListView!!.adapter = adapter

    }

    private fun getCitySearchItemList(response: String): MutableList<CitySearchItem> {
        var response = response
        val returnList: MutableList<CitySearchItem> = ArrayList()
        response = response.replace("\'", "")
        val items = response.split(", ").toTypedArray()
        for (stringLine in items) {
            val splitString = stringLine.split("#").toTypedArray()
            if (splitString.isNotEmpty()) {
                if (splitString[1] != "") {
                    val cityName = splitString[0]
                    val id = splitString[1].toInt()
                    val countryCode = splitString[2]
                    val citySearchItem = CitySearchItem(id, cityName, countryCode)
                    if (!checkForDuplicate(returnList, citySearchItem)) {
                        returnList.add(citySearchItem)
                    }
                }
            }
        }
        return returnList
    }

    private fun checkForDuplicate(returnList: List<CitySearchItem>, citySearchItem: CitySearchItem): Boolean {
        for (item in returnList) {
            if (item.cityName == citySearchItem.cityName && item.countryCode == citySearchItem.countryCode)
                return true
        }
        return false
    }

    /***
     *
     * show the searchable dialog
     */
    fun show() {
        val adb = AlertDialog.Builder(mActivity)
        val view = mActivity.layoutInflater.inflate(R.layout.search_dialog_layout, null)
        val rippleViewClose = view.findViewById<View>(R.id.close) as TextView
        val title = view.findViewById<View>(R.id.spinerTitle) as TextView
        title.text = mDialogTitle
        mListView = view.findViewById<View>(R.id.list) as ListView
        mSearchProgressBar = view.findViewById(R.id.search_progress_bar)
        val searchBox = view.findViewById<View>(R.id.searchBox) as EditText
        adapter = SearchDialogListAdapter(mActivity, R.layout.items_view_layout, R.id.cityCountryRL, mCitySearchItemsList)
        mListView!!.adapter = adapter
        adb.setView(view)
        val mAlertDialog: AlertDialog?
        mAlertDialog = adb.create()
        mAlertDialog.setCancelable(false)
        val searchGoBtn = view.findViewById<TextView>(R.id.search_go_btn)
        searchGoBtn.setOnClickListener { }
        mListView!!.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
            val relativeLayout = view.findViewById<RelativeLayout>(R.id.cityCountryRL)
            val t = relativeLayout.findViewById<TextView>(R.id.city)
            val id = t.tag as Int
            try {
                mOnSearchItemSelection!!.onClick(id.toString())
            } catch (e: Exception) {
                Log.e(TAG, "exception in click: ", e)
            }
            mAlertDialog.dismiss()
        }
        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length <= 2) {
                    mListView!!.adapter = null
                    mEmptyListFlag = true
                } else {
                    mEmptyListFlag = false
                }
            }

            override fun afterTextChanged(editable: Editable) {
                mListView!!.adapter = null
                if (editable.length > 2) {
                    val service: DataService = RetrofitInstance(mContext).retrofitInstance2.create(DataService::class.java)
                    val call: Call<String> = service.searchCity(searchBox.text.toString())
                    call.enqueue(object : Callback<String> {
                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Log.d(TAG, call.request().url().toString())
                            Log.e(TAG, "request onFailure", t)
                        }

                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            if (response.code() == 200)
                                populateSearchResults(response.body())
                        }
                    })

                    view.search_progress_bar.visibility = View.VISIBLE
                }
            }
        })
        rippleViewClose.setOnClickListener { mAlertDialog.dismiss() }
        mAlertDialog.show()
        mAlertDialog.setOnKeyListener(DialogInterface.OnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                mAlertDialog.dismiss()
            }
            true
        })
    }


    /***
     *
     * clear the list
     */
    fun clear() {
        mCitySearchItemsList.clear()
    }

    /***
     *
     * refresh the adapter (notifyDataSetChanged)
     */
    fun refresh() {
        adapter!!.notifyDataSetChanged()
    }

    companion object {
        private const val TAG = "CustomSearchDialog"
    }

    fun setOnItemSelected(onItemSelected: OnSearchItemSelection?) {
        mOnSearchItemSelection = onItemSelected
    }
}