package com.example.parvaznama.remote.ktor.model

import kotlinx.serialization.Serializable

@Serializable
data class FullDetailFlightResponse(
    val greatCircleDistance: GreatCircleDistance,
    val departure: FlightPoint,
    val arrival: FlightPoint,
    val lastUpdatedUtc: String? = null,
    val number: String? = null,
    val callSign: String? = null,
    val status: String? = null,
    val codeshareStatus: String? = null,
    val isCargo: Boolean = false,
    val aircraft: Aircraft? = null,
    val airline: Airline? = null
)

@Serializable
data class GreatCircleDistance(
    val meter: Double,
    val km: Double,
    val mile: Double,
    val nm: Double,
    val feet: Double
)

@Serializable
data class FlightPoint(
    val airport: Airport,
    val scheduledTime: TimeData? = null,
    val revisedTime: TimeData? = null,
    val predictedTime: TimeData? = null,
    val checkInDesk: String? = null,
    val quality: List<String> = emptyList()
)

@Serializable
data class Airport(
    val icao: String? = null,
    val iata: String? = null,
    val localCode: String? = null,
    val name: String? = null,
    val shortName: String? = null,
    val municipalityName: String? = null,
    val location: Location? = null,
    val countryCode: String? = null,
    val timeZone: String? = null
)

@Serializable
data class Location(
    val lat: Double,
    val lon: Double
)

@Serializable
data class TimeData(
    val utc: String? = null,
    val local: String? = null
)

@Serializable
data class Aircraft(
    val reg: String? = null,
    val modeS: String? = null,
    val model: String? = null
)

@Serializable
data class Airline(
    val name: String? = null,
    val iata: String? = null,
    val icao: String? = null
)
