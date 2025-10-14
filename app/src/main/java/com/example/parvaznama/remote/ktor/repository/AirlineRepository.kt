package com.example.parvaznama.remote.ktor.repository

import AirportDto
import FlightArrival
import FlightData
import FlightDeparture
import FlightResponse
import android.util.Log

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.util.date.getTimeMillis
import kotlinx.serialization.json.Json
import java.security.SecureRandom
import kotlin.random.Random

class AirlineRepository(private val client: HttpClient) {
    private val secureRandom = SecureRandom()
    private val token1 = "aee56a801ce67b80d98d391a1222c62e"
    private val token2 = "8d5c6298f7ed617583363f4b4712755c"
    private val token3 = "b2e69fe6dd3cfcfc2a18da16fccbf582"
    private val token4 = "40555d358784ab4c2782db81738f97ed"
    private val listToken = listOf(token1, token2, token3,token4)

    private val baseUrl = "https://api.aviationstack.com/v1/flights?access_key="

    private fun getUrl(): String {
        val token = listToken[secureRandom.nextInt(listToken.size)]
        return "$baseUrl$token"
    }

    suspend fun getDepartureData(): List<FlightDeparture> {
        Log.i("LOG",getUrl())
        return try {
            val response: FlightResponse =
                client.get(getUrl())
                    .body()

            response.data.mapNotNull { it.departure }
                .filter { it.airport != null } // فقط پروازهایی که فرودگاه مشخص دارن
                .distinctBy { it.iata } // حذف تکراری‌ها
                .sortedBy { it.airport } // مرتب‌سازی بر اساس نام فرودگاه
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getArrivalData(): List<FlightArrival> {
        return try {
            val response: FlightResponse =
                client.get(getUrl()).body()
            response.data.mapNotNull { it.arrival }
                .filter { it.airport != null }
                .distinctBy { it.iata }
                .sortedBy { it.airport }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }


    }
    suspend fun getFlights(): List<FlightData> {
        return try {
            val response: FlightResponse = client.get(getUrl()).body()
            response.data
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    suspend fun getAirportCoords():List<AirportDto> {
        return try {
            val response = client.get("https://raw.githubusercontent.com/mwgg/Airports/master/airports.json")
            val text = response.bodyAsText()
            val json = Json { ignoreUnknownKeys = true }
            val map = json.decodeFromString<Map<String, AirportDto>>(text)
            map.values.toList()

        } catch (e: Exception) {
            Log.e("debugX", "Error fetching airport coords", e)
            emptyList()
        }
    }

}


