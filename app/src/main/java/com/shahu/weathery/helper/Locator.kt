package com.shahu.weathery.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.shahu.weathery.common.Connectivity.isConnected
import com.shahu.weathery.common.Connectivity.isConnectedMobile

/**
 * Created by Shahu Ronghe on 20, September, 2019
 * in Weathery
 */
class Locator(private val context: Context) : LocationListener {
    private val locationManager: LocationManager
    private var method: Method? = null
    private var callback: Listener? = null
    fun getLocation(method: Method?, callback: Listener?) {
        this.method = method
        this.callback = callback
        when (this.method) {
            Method.NETWORK, Method.NETWORK_THEN_GPS -> {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                val networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (networkLocation != null) {
                    Log.d(LOG_TAG, "Last known location found for network provider : $networkLocation")
                    this.callback!!.onLocationFound(networkLocation)
                } else {
                    Log.d(LOG_TAG, "Request updates from network provider.")
                    requestUpdates(LocationManager.NETWORK_PROVIDER)
                }
            }
            Method.GPS -> {
                val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (gpsLocation != null) {
                    Log.d(LOG_TAG, "Last known location found for GPS provider : $gpsLocation")
                    this.callback!!.onLocationFound(gpsLocation)
                } else {
                    Log.d(LOG_TAG, "Request updates from GPS provider.")
                    requestUpdates(LocationManager.GPS_PROVIDER)
                }
            }
        }
    }

    private fun requestUpdates(provider: String) {
        if (locationManager.isProviderEnabled(provider)) {
            if (provider.contentEquals(LocationManager.NETWORK_PROVIDER)
                    && isConnected(context)) {
                Log.d(LOG_TAG, "Network connected, start listening : $provider")
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                locationManager.requestLocationUpdates(provider, TIME_INTERVAL.toLong(), DISTANCE_INTERVAL.toFloat(), this)
            } else if (provider.contentEquals(LocationManager.GPS_PROVIDER)
                    && isConnectedMobile(context)) {
                Log.d(LOG_TAG, "Mobile network connected, start listening : $provider")
                locationManager.requestLocationUpdates(provider, TIME_INTERVAL.toLong(), DISTANCE_INTERVAL.toFloat(), this)
            } else {
                Log.d(LOG_TAG, "Proper network not connected for provider : $provider")
                onProviderDisabled(provider)
            }
        } else {
            onProviderDisabled(provider)
        }
    }

    fun cancel() {
        Log.d(LOG_TAG, "Locating canceled.")
        locationManager.removeUpdates(this)
    }

    override fun onLocationChanged(location: Location) {
        Log.d(LOG_TAG, "Location found : " + location.latitude + ", " + location.longitude + if (location.hasAccuracy()) " : +- " + location.accuracy + " meters" else "")
        locationManager.removeUpdates(this)
        callback!!.onLocationFound(location)
    }

    override fun onProviderDisabled(provider: String) {
        Log.d(LOG_TAG, "Provider disabled : $provider")
        if (method == Method.NETWORK_THEN_GPS
                && provider.contentEquals(LocationManager.NETWORK_PROVIDER)) {
            // Network provider disabled, try GPS
            Log.d(LOG_TAG, "Request updates from GPS provider, network provider disabled.")
            requestUpdates(LocationManager.GPS_PROVIDER)
        } else {
            locationManager.removeUpdates(this)
            callback!!.onLocationNotFound()
        }
    }

    override fun onProviderEnabled(provider: String) {
        Log.d(LOG_TAG, "Provider enabled : $provider")
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        Log.d(LOG_TAG, "Provided status changed : $provider : status : $status")
    }

    enum class Method {
        NETWORK, GPS, NETWORK_THEN_GPS
    }

    interface Listener {
        fun onLocationFound(location: Location?)
        fun onLocationNotFound()
    }

    companion object {
        private const val LOG_TAG = "locator"
        private const val TIME_INTERVAL = 100
        private const val DISTANCE_INTERVAL = 1
    }

    init {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
}