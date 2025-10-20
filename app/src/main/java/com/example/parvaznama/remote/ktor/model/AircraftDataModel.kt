package com.example.parvaznama.remote.ktor.model

import kotlinx.serialization.Serializable

@Serializable
data class AircraftResponse(
    val totalCount: Int,
    val pageOffset: Int,
    val pageSize: Int,
    val hasNextPage: Boolean,
    val count: Int,
    val items: List<AircraftItem>
)

@Serializable
data class AircraftItem(
    val id: Int,
    val reg: String,
    val active: Boolean,
    val serial: String? = null,
    val hexIcao: String? = null,
    val airlineName: String? = null,
    val iataCodeShort: String? = null,
    val icaoCode: String? = null,
    val model: String? = null,
    val modelCode: String? = null,
    val numSeats: Int? = null,
    val rolloutDate: String? = null,
    val firstFlightDate: String? = null,
    val deliveryDate: String? = null,
    val registrationDate: String? = null,
    val typeName: String? = null,
    val numEngines: Int? = null,
    val engineType: String? = null,
    val isFreighter: Boolean? = null,
    val productionLine: String? = null,
    val ageYears: Double? = null,
    val verified: Boolean? = null,
    val numRegistrations: Int? = null
)

