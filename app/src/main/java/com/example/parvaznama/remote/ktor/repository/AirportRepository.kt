package com.example.parvaznama.remote.ktor.repository

import android.util.Log
import com.example.parvaznama.remote.ktor.model.AirportItem
import com.example.parvaznama.remote.ktor.model.AirportSearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlin.random.Random

class AirportRepository(private val client: HttpClient) {

    // لیست توکن‌ها
    private val apiKeys = listOf(
        "cmgrsyt2l000kjm04ur48rbfz",
        "cmgrsue6a000dl804tnshw7bx",
        "cmgrt4tzz000ojm04qvrlzydm",
        "cmgrt9rbf000vjm04ulkb8r0o",
        "cmgrxfpet0001lf04bneeb1tm",
    )

    private fun getRandomApiKey(): String = apiKeys.random()


    suspend fun getAirportByName(query: String): List<AirportItem> {
        return try {
            val token = getRandomApiKey() // انتخاب تصادفی توکن
            Log.d("AirportRepo", "Using API key: $token")

            val response: AirportSearchResponse =
                client.get("https://prod.api.market/api/v1/aedbx/aerodatabox/airports/search/term?q=$query&limit=10&withFlightInfoOnly=false&withSearchByCode=true") {
                    header("accept", "application/json")
                    header("x-api-market-key", token)
                }.body()

            response.items
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
