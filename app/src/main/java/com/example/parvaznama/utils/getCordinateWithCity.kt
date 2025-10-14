package com.example.parvaznama.utils

import android.content.Context
import android.location.Geocoder
import java.util.Locale

fun getCoordinateWithCity(city:String,context:Context): Pair<Double, Double>? {
    return try {
        val geocode=Geocoder(context, Locale.getDefault())
        val addresses=geocode.getFromLocationName(city,1)
        if (!addresses.isNullOrEmpty()) {
            val location = addresses[0]
            Pair(location.longitude, location.latitude)
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

}