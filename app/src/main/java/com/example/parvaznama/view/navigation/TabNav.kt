package com.example.parvaznama.view.navigation

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.parvaznama.R
import com.example.parvaznama.utils.AirportRepository
import com.example.parvaznama.view.component.item.AirlineListItem
import com.example.parvaznama.view.component.MapBoxScreen
import com.example.parvaznama.view.component.MapBoxScreenAirport
import com.example.parvaznama.view.component.item.AirportListCardItem
import com.example.parvaznama.view.viewModel.AirlineViewModel
import com.example.parvaznama.view.viewModel.AirportViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.androidx.compose.koinViewModel

object AirlineListTab : Tab {
    private fun readResolve(): Any = AirlineListTab

    override val options: TabOptions
        @Composable
        get() {
            val title = "پرواز های جاری"
            return remember {
                TabOptions(index = 0u, title = title)
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val repo = AirportRepository(context)
        val viewModel: AirlineViewModel = koinViewModel()

        val departures by viewModel.departures.collectAsState()
        val arrivals by viewModel.arrivals.collectAsState()
        val flights by viewModel.flights.collectAsState()
        var isLoading = departures.isEmpty() || arrivals.isEmpty() || flights.isEmpty()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(16.dp)
        ) {
            when {
                isLoading -> {

                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {

                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                            )
                            Text(
                                text = "در حال دریافت لیست پرواز ها",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.background
                            )
                        }

                    }
                }

                else -> {

                    SwipeRefresh(
                        state = rememberSwipeRefreshState(isRefreshing = isLoading),
                        onRefresh = {
                            isLoading = true
                            Toast.makeText(context, "در حال دریافت داده جدید", Toast.LENGTH_SHORT)
                                .show()
                            viewModel.loadAirlineData()
                            isLoading = false
                        }
                    ) {
                        val count = minOf(departures.size, arrivals.size, flights.size)
                        LazyColumn(Modifier.fillMaxHeight()) {
                            items(count) { index ->
                                // راه‌حل اول: قبل از دسترسی بررسی می‌کنیم که ایندکس معتبر باشه
                                if (index < departures.size && index < arrivals.size && index < flights.size) {
                                    val dep = departures[index]
                                    val arr = arrivals[index]
                                    val flight = flights[index]

                                    AirlineListItem(

                                        status = flight.flightStatus,
                                        departures = flight.departure,
                                        arrival = flight.arrival,
                                        airline = flight.airline,
                                        date = flight.flightDate ?: "",
                                        repo = repo,
                                        departureIata = dep.iata,
                                        arrivalIata = arr.iata
                                    )
                                }
                                // در غیر این صورت آیتمی رسم نمی‌شود (امنیت در برابر تغییرات همزمان لیست)
                            }
                        }

                    }


                }
            }
        }
    }
}


object AirlineMapTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = "نقشه ماهواره ای"

            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                )
            }
        }

    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary)
        ) {

            MapBoxScreen()


        }

    }
}

object AirportSearchTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = "فرودگاه ها"

            return remember {
                TabOptions(
                    index = 2u,
                    title = title,
                )
            }
        }

    @Composable
    override fun Content() {
        var lon by remember { mutableStateOf(37.5) }
        var lat by remember { mutableStateOf(45.3) }
        var query by rememberSaveable { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(true) }
        var airportViewModel: AirportViewModel = koinViewModel()
        val airports by airportViewModel.airports.collectAsState()
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary)
        ) {
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                MapBoxScreenAirport(
                    lon, lat
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    modifier = Modifier.border(0.5.dp,MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp)),
                    onClick = {
                            airportViewModel.loadAirport(query)
                            isLoading = false

                    }
                ) {
                    Icon(
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(0.1f),
                        painter = painterResource(R.drawable.icon_search),
                        contentDescription = ""

                    )
                }

                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = MaterialTheme.colorScheme.background),

                    placeholder = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.bodySmall,
                            text = "فرودگاه مورد نظر را جستجو کنید(انگلیسی)"
                        )
                    },
                    shape = RoundedCornerShape(8.dp),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .weight(0.8f)
                        .fillMaxWidth()
                        .padding(8.dp),
                    value = query,
                    onValueChange = {
                        isLoading=false
                        query = it

                    }
                )

            }
            if (isLoading) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column {

                    LazyColumn {
                        items(items = airports) {
                            Log.d("debugX", it.shortName.toString())

                            Log.d("debugX", "${lat.toString()},${lon.toString()}")
                            AirportListCardItem(it, itemClick = {
                                lon = it.location.lon
                                lat = it.location.lat
                            })
                        }
                    }
                }
            }


        }

    }


}

@Composable
fun TopNavigation(tabNavigator: TabNavigator) {
    // تعریف لیستی از تمام تب‌ها
    val tabs = listOf(AirlineListTab, AirlineMapTab, AirportSearchTab)

    NavigationBar(
        modifier = Modifier.height(80.dp),

        containerColor = MaterialTheme.colorScheme.onPrimary
    ) {
        tabs.forEach { tab ->
            val isSelected = tabNavigator.current.options.index == tab.options.index

            NavigationBarItem(
                colors =
                    NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    ),
                icon = {

                },
                label = {
                    Text(tab.options.title)
                },
                selected = isSelected,
                onClick = {
                    // تغییر تب فعال با یک فراخوانی ساده
                    tabNavigator.current = tab
                }
            )
        }
    }
}
