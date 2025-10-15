package com.example.parvaznama.remote.ktor.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AirportSearchResponse(
    @SerialName("searchBy") val searchBy: String,
    @SerialName("count") val count: Int,
    @SerialName("items") val items: List<AirportItem>
)

@Serializable
data class AirportItem(
    @SerialName("icao") val icao: String,
    @SerialName("iata") val iata: String,
    @SerialName("name") val name: String,
    @SerialName("shortName") val shortName: String,
    @SerialName("municipalityName") val municipalityName: String,
    @SerialName("location") val location: AirportLocation,
    @SerialName("countryCode") val countryCode: String,
    @SerialName("timeZone") val timeZone: String
)

@Serializable
data class AirportLocation(
    @SerialName("lat") val lat: Double,
    @SerialName("lon") val lon: Double
)
