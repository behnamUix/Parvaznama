package com.example.parvaznama.view.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parvaznama.remote.ktor.model.AirportItem
import com.example.parvaznama.remote.ktor.repository.AirportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AirportViewModel(val repo: AirportRepository) : ViewModel() {
    private val _airports = MutableStateFlow<List<AirportItem>>(emptyList())
    val airports: StateFlow<List<AirportItem>> = _airports

     fun loadAirport(query:String){
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
}
