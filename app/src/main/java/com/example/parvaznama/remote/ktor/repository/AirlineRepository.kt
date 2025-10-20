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
    private val token1 = "25068ab3223c95c886c8f7ca29884159"
    private val token2 = "5e8f6bc628698639ddacc01eb247fe63"
    private val token3 = "e1c8fef322ea219d312d805c6dd991b3"
    private val token4 = "0bd92975af0716a991ec27bc24ae0758"
    private val token5 = "38dd84558007dd1c045ee93897081ae9"
    private val listToken = listOf(token1, token2, token3,token4,token5)

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


