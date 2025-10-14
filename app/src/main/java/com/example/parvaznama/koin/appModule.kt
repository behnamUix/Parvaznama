package com.example.parvaznama.koin

import com.example.parvaznama.remote.ktor.repository.AirlineRepository
import com.example.parvaznama.view.viewModel.AirlineViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    }
                )

            }
            // نصب Timeout
            install(HttpTimeout) {
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 15000
                socketTimeoutMillis = 30000
            }
        }
    }
    //repo
    single { AirlineRepository(get()) }

    //viewModel
    viewModel{AirlineViewModel(get())}
}