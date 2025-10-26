package com.example.parvaznama.view.navigation

import Airline

import FlightArrival
import FlightDeparture
import FlightDetail
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInBack
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.TabNavigator
import coil.compose.AsyncImage
import com.example.parvaznama.R
import com.example.parvaznama.utils.AirportRepository
import com.example.parvaznama.utils.getCoordinateWithCity
import com.example.parvaznama.utils.toPersianDigits
import com.example.parvaznama.view.anim.LoadingAnim
import com.example.parvaznama.view.component.BottomSheetComp
import com.example.parvaznama.view.component.CustomTopAppBar
import com.example.parvaznama.view.component.MapBoxScreen
import com.example.parvaznama.view.navigation.tabbar.AirlineListTab
import com.example.parvaznama.view.navigation.tabbar.AirportSearchTab
import com.example.parvaznama.view.navigation.tabbar.TopNavigation
import com.example.parvaznama.view.viewModel.AirportViewModel
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

object SplashSc : Screen {
    @Composable
    override fun Content() {

        //Animation
        var offsetY2 = remember { Animatable(700f) }
        val alpha2 = remember { Animatable(0f) }
        var targetY2 = -200f
        val targetAlpha2 = 1f

        val yOffset = remember { Animatable(0f) }
        val targetY = -800f
////////////////////////////////////////////
        var showSplash by remember { mutableStateOf(true) }
        val nav = LocalNavigator.currentOrThrow
        LaunchedEffect(Unit) {
            launch {
                yOffset.animateTo(
                    targetValue = targetY,
                    animationSpec = tween(
                        delayMillis = 1500,
                        durationMillis = 1000,
                        easing = EaseInBack
                    )
                )
            }
            delay(1500)
            launch {
                offsetY2.animateTo(

                    targetY2, animationSpec = tween(
                        durationMillis = 1000,

                        easing = EaseInBack
                    )
                )

            }
            launch {
                alpha2.animateTo(
                    targetAlpha2,
                    animationSpec = tween(
                        durationMillis = 1500,

                        easing = EaseInBack
                    )
                )
            }


        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onBackground)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.img_airplane),
                    contentDescription = "",
                    contentScale = ContentScale.Crop

                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(x = 0.dp, y = yOffset.value.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            modifier = Modifier.size(300.dp),
                            painter = painterResource(R.drawable.icon_airplane),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.background,
                        )
                        Text(text = "...", style = MaterialTheme.typography.headlineLarge)
                    }
                }


                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(x = 0.dp, y = offsetY2.value.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LoadingAnim()

                        Card(
                            elevation = CardDefaults.elevatedCardElevation(6.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {


                                Text(
                                    color = MaterialTheme.colorScheme.background,
                                    text = "پروازنما",
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Text(
                                    text = "از تیک آف تا لندینگ لحظه به لحظه با شما",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
                                )


                                OutlinedButton(
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.primary
                                    ),
                                    modifier = Modifier.fillMaxWidth(0.6f),
                                    shape = RoundedCornerShape(8.dp),
                                    onClick = {
                                        nav.replace(HomeSc)
                                        showSplash = false
                                        Hawk.put("splash", showSplash)
                                    }) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "برو بریم",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Icon(

                                            modifier = Modifier.rotate(90f),
                                            painter = painterResource(R.drawable.icon_airplane),
                                            contentDescription = ""
                                        )
                                    }
                                }


                            }
                        }
                    }
                }


            }


        }

    }

}

object HomeSc : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            CustomTopAppBar(icon = true)
            TabNavigator(AirlineListTab) { tabNavigator ->
                Column(modifier = Modifier.fillMaxSize()) {
                    TopNavigation(tabNavigator = tabNavigator)
                    Box(modifier = Modifier.weight(1f)) {
                        tabNavigator.current.Content()
                    }
                }
            }

        }

    }


}


object WorldpMapSc : Screen {
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

data class InformationSc(
    val flight: FlightDetail?,
    val departure: FlightDeparture?,
    val arrival: FlightArrival?,
    val airline: Airline?,
    var departureIata: String?,
    var arrivalIata: String?,
    var repo: AirportRepository
) : Screen {
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    override fun Content() {
        var nav = LocalNavigator.currentOrThrow
        var ctx = LocalContext.current

        var originCity = repo.getCityByIATA(arrivalIata ?: "")
        var destinationCity = repo.getCityByIATA(departureIata ?: "")

        var originCoord = getCoordinateWithCity(originCity ?: "", ctx)
        var destinationCoord = getCoordinateWithCity(destinationCity ?: "", ctx)


        Column() {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                BottomSheetComp(
                    flight = flight,
                    iata = airline?.iata,
                    airline = airline?.name.toString(),
                    departure = departure,
                    arrival = arrival,

                    departureIata = departureIata,
                    arrivalIata = arrivalIata,
                    repo = repo
                )
            }
        }
    }

}

data class AircraftListFromAirlineSc(var airline: String, var iata: String) : Screen {
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    override fun Content() {
        var aircraftViewModel: AirportViewModel = koinViewModel()
        val aircrafts by aircraftViewModel.aircraft.collectAsState()
        LaunchedEffect(Unit) {
            aircraftViewModel.loadAircraftAirlineByIata(iata)
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                style = MaterialTheme.typography.headlineSmall,
                text = " هواپیما های شرکت ${airline}",
                color = MaterialTheme.colorScheme.background
            )
            LazyColumn {
                items(items = aircrafts) {
                    Card(
                        modifier = Modifier
                            .padding(4.dp),
                        elevation = CardDefaults.elevatedCardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary)


                    ) {
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AsyncImage(
                                model = "https://cdn.jetphotos.com/full/6/959274_1695575317.jpg",
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clickable { RoundedCornerShape(16.dp) },
                                contentScale = ContentScale.Crop
                            )
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                horizontalAlignment = Alignment.End,

                                modifier = Modifier.padding(8.dp)
                            ) {

                                TextValue(it.typeName.toString() ?: "", "مدل هواپیما")

                                TextValue(
                                    it.numSeats?.toString()?.toPersianDigits() ?: "",
                                    "ظرفیت صندلی"
                                )


                                TextValue(
                                    value = it.firstFlightDate.toString() ?: "",
                                    label = "تاریخ اولین پرواز"
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = it.numEngines?.toString()?.toPersianDigits()
                                                ?: "",
                                            color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
                                        )
                                        Text(
                                            text = "تعداد موتور",
                                            color = MaterialTheme.colorScheme.background
                                        )
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = it.engineType ?: "",
                                            color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
                                        )

                                        Text(
                                            text = "نوع موتور",
                                            color = MaterialTheme.colorScheme.background
                                        )
                                    }
                                }
                                HorizontalDivider(thickness = 0.5.dp)


                                TextValue(
                                    it.ageYears?.toString()?.toPersianDigits() ?: "",
                                    "سن هواپیما"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}




@Composable
fun TextValue(value: String, label: String) {
    Column(horizontalAlignment = Alignment.End) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = value, color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
            Spacer(Modifier.weight(1f))
            Text(text = "${label}", color = MaterialTheme.colorScheme.background)
        }
        HorizontalDivider(thickness = 0.5.dp)

    }
}



