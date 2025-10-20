package com.example.parvaznama.view.viewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parvaznama.remote.ktor.model.AircraftItem
import com.example.parvaznama.remote.ktor.model.AirportItem
import com.example.parvaznama.remote.ktor.model.FullDetailFlightResponse
import com.example.parvaznama.remote.ktor.repository.AirportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AirportViewModel(val repo: AirportRepository) : ViewModel() {
    private val _airports = MutableStateFlow<List<AirportItem>>(emptyList())
    val airports: StateFlow<List<AirportItem>> = _airports

    private val _aircraft = MutableStateFlow<List<AircraftItem>>(emptyList())
    val aircraft: StateFlow<List<AircraftItem>> = _aircraft

    private val _fullDetail = MutableStateFlow<List<FullDetailFlightResponse>>(emptyList())
    val fullDetail: StateFlow<List<FullDetailFlightResponse>> = _fullDetail
    fun loadAirport(query: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                val rawJson = repo.getAirportByName(query)
                Log.d("debugX", rawJson.toString()) // ✅ ببین چه چیزی آمده
                repo.getAirportByName(query)
            }
                .onSuccess {
                    _airports.value = it
                }
                .onFailure {
                    _airports.value = emptyList()
                    Log.e("debugX", "Error loading airport", it)
                }

        }

    }

    fun loadAircraftAirlineByIata(iata: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                repo.getAircraftAirlineByIATA(iata)

            }.onSuccess {
                _aircraft.value = it

            }.onFailure {
                _aircraft.value = emptyList()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadFullDetailFlightByIata(iata: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                repo.getFullDetailFlight(iata)
            }
                .onSuccess { response ->
                    Log.d("debugX", "✅ response size: ${response.size}")
                    _fullDetail.value = response
                }
                .onFailure { e ->
                    _fullDetail.value = emptyList()
                    Log.e("debugX", "❌ Error loading full detail", e)
                }
        }
    }

}
