package com.example.parvaznama.utils


fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val R = 6371.0
    val dlat = Math.toRadians(lat2 - lat1)
    val dlon = Math.toRadians(lon2 - lon1)
    val a = Math.sin(dlat/2) * Math.sin(dlat/2) +
            Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
            Math.sin(dlon/2) * Math.sin(dlon/2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    return R * c
}
