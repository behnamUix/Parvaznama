package com.example.parvaznama.view.viewModel

import AirportDto
import FlightArrival
import FlightData
import FlightDeparture
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.parvaznama.remote.ktor.repository.AirlineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AirlineViewModel(val repo: AirlineRepository) : ViewModel() {
    private val _departures = MutableStateFlow<List<FlightDeparture>>(emptyList())
    val departures: StateFlow<List<FlightDeparture>> = _departures

    private val _arrivals = MutableStateFlow<List<FlightArrival>>(emptyList())
    val arrivals: StateFlow<List<FlightArrival>> = _arrivals

    private val _flights = MutableStateFlow<List<FlightData>>(emptyList())
    val flights: StateFlow<List<FlightData>> = _flights

    private val _coords = MutableStateFlow<List<AirportDto>>(emptyList())
    val coords: StateFlow<List<AirportDto>> = _coords


    init {
        loadDepartureAirlines()
        loadArrivalAirlines()
        loadFlights()
        loadAirportsCoords()
    }
    fun loadAirlineData(){
        loadDepartureAirlines()
        loadArrivalAirlines()
        loadFlights()
        loadAirportsCoords()
    }

    fun loadFlights() {
        viewModelScope.launch {
            kotlin.runCatching { repo.getFlights() }
                .onSuccess { flights ->
                    _flights.value = flights
                }
                .onFailure {
                    _flights.value = emptyList()
                    Log.e("debugX", "Error loading flights", it)
                }
        }
    }


    fun loadDepartureAirlines() {
        viewModelScope.launch {
            kotlin.runCatching { repo.getDepartureData() }
                .onSuccess { _departures.value = it }
                .onFailure {
                    _departures.value = emptyList()
                    Log.e("debugX", "Error loading departures", it)
                }
        }
    }

    fun loadArrivalAirlines() {
        viewModelScope.launch {
            kotlin.runCatching { repo.getArrivalData() }
                .onSuccess { _arrivals.value = it }
                .onFailure {
                    _arrivals.value = emptyList()
                    Log.e("debugX", "Error loading arrivals", it)
                }
        }
    }

    fun loadAirportsCoords() {
        viewModelScope.launch {
            runCatching { repo.getAirportCoords() }
                .onSuccess { coords ->
                    _coords.value = coords
                }
                .onFailure { error ->
                    _coords.value = emptyList()
                    Log.e("debugX", "Error loading airport coords", error)
                }
        }
    }


}