package com.example.parvaznama.view.component

import FlightArrival
import FlightDeparture
import FlightDetail
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.EaseInOutBack
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.parvaznama.R
import com.example.parvaznama.utils.AirportRepository
import com.example.parvaznama.utils.calculateDistance
import com.example.parvaznama.utils.convertEngToPerStats
import com.example.parvaznama.utils.toPersianDigits
import com.example.parvaznama.view.navigation.AircraftListFromAirlineSc
import com.example.parvaznama.view.viewModel.AirportViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomSheetComp(
    flight: FlightDetail?,
    iata: String?,
    airline: String?,
    departure: FlightDeparture?,
    arrival: FlightArrival?,
    departureIata: String?,
    arrivalIata: String?,
    repo: AirportRepository
) {
    val viewModel: AirportViewModel = koinViewModel()
    val fullDetail by viewModel.fullDetail.collectAsState()

    var showFirstBox by remember { mutableStateOf(true) }
    var showSecondBox by remember { mutableStateOf(false) }
    var arrivalAirportName = arrival?.airport.toString()
    var departureAirportName = departure?.airport.toString()

    var deTerminal = departure?.terminal ?: "نامعلوم"
    var deGate = departure?.gate ?: "نامعلوم"
    var arrTerminal = arrival?.terminal ?: "نامعلوم"
    var arrGate = arrival?.gate ?: "نامعلوم"

    //animation
    var offsetY = remember { Animatable(700f) }
    var scope = rememberCoroutineScope()
    val expandedY = 0f
    val collapsedY = 700f

    var nav = LocalNavigator.currentOrThrow



    LaunchedEffect(flight?.iata ?: flight?.number) {
        val code = flight?.iata ?: flight?.number
        if (!code.isNullOrEmpty()) {
            Log.d("debugX23", "Requesting full detail for: $code")
            viewModel.loadFullDetailFlightByIata(code)
        } else {
            Log.e("debugX23", "❌ Flight code is null or empty")
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        CustomTopAppBar(title = airline ?: "پروازنما")
        val detail = fullDetail.firstOrNull()
        val departureLoc = detail?.departure?.airport?.location
        val arrivalLoc = detail?.arrival?.airport?.location
        var dist = calculateDistance(
            departureLoc?.lat ?: 1.0,
            departureLoc?.lon ?: 0.1,
            arrivalLoc?.lat ?: 0.1,
            arrivalLoc?.lon ?: 0.1
        )
        Log.d("debugX", departureLoc?.lon.toString() ?: "0.1")
        Log.d("debugX", departureLoc?.lat.toString() ?: "0.1")
        Box {
            if (departureLoc != null && arrivalLoc != null) {
                MapBoxScreen(
                    Ulocation = false,
                    long1 = departureLoc.lon,
                    lat1 = departureLoc.lat,
                    long2 = arrivalLoc.lon,
                    lat2 = arrivalLoc.lat
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    MapBoxScreen(
                        Ulocation = false,
                        long1 = 0.1,
                        lat1 = 0.1,
                        long2 = 0.1,
                        lat2 = 0.1
                    )
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(8.dp),
                            color = MaterialTheme.colorScheme.primary
                        )

                    }
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
            ) {
                if (showFirstBox) {
                    Box() {
                        Card(
                            modifier = Modifier
                                .fillMaxHeight(0.5f)
                                .alpha(0.9f)
                                .fillMaxSize()
                                .padding(4.dp),
                            elevation = CardDefaults.elevatedCardElevation(4.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary)
                        ) {
                            Button(
                                shape = RoundedCornerShape(
                                    bottomStart = 0.dp,
                                    bottomEnd = 0.dp,
                                    topStart = 8.dp,
                                    topEnd = 8.dp
                                ), modifier = Modifier.fillMaxWidth(), onClick = {
                                    scope.launch {
                                        showSecondBox =
                                            true // کارت دوم رو فعال کن تا بتونه حرکت کنه
                                        offsetY.animateTo(
                                            targetValue = expandedY, animationSpec = tween(
                                                durationMillis = 1500, easing = EaseInOutBack
                                            )
                                        )
                                        showFirstBox = false
                                    }
                                }) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        modifier = Modifier.padding(6.dp),

                                        text = "اطلاعات کامل",
                                    )
                                    Icon(
                                        painter = painterResource(R.drawable.icon_4dots),
                                        contentDescription = ""
                                    )

                                }

                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                        Text(
                                            style = MaterialTheme.typography.labelLarge,
                                            text = "مبدا",

                                            color = MaterialTheme.colorScheme.background
                                        )
                                        Text(
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.width(150.dp),
                                            style = MaterialTheme.typography.labelMedium,
                                            text = "${repo.getCityByIATA(departureIata ?: "")}",

                                            color = MaterialTheme.colorScheme.background
                                        )


                                        Text(
                                            style = MaterialTheme.typography.headlineSmall,
                                            text = departureIata ?: "NUL",
                                            color = MaterialTheme.colorScheme.background
                                        )

                                    }
                                    Spacer(Modifier.weight(1f))
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                        Text(
                                            style = MaterialTheme.typography.labelLarge,
                                            text = "مقصد",

                                            color = MaterialTheme.colorScheme.background
                                        )
                                        Text(
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.width(150.dp),
                                            style = MaterialTheme.typography.labelMedium,
                                            text = "${repo.getCityByIATA(arrivalIata ?: "")}",

                                            color = MaterialTheme.colorScheme.background
                                        )

                                        Text(
                                            style = MaterialTheme.typography.headlineSmall,
                                            text = arrivalIata ?: "NULL",
                                            color = MaterialTheme.colorScheme.background
                                        )

                                    }

                                }
                                Text(
                                    text = " مسافت تخمینی: ${Math.round(dist)}کیلومتر ".toPersianDigits(),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.background
                                )
                                if (!fullDetail.isEmpty()) {
                                    var listUnit = listOf(
                                        "${fullDetail.first().greatCircleDistance.km} کیلومتر ",
                                        "${fullDetail.first().greatCircleDistance.mile}مایل ",
                                        "${fullDetail.first().greatCircleDistance.meter} متر ",
                                        "${fullDetail.first().greatCircleDistance.feet} فوت ",
                                        "${fullDetail.first().greatCircleDistance.nm} گره دریایی ",
                                    )

                                    LazyRow {
                                        items(items = listUnit) {
                                            Text(
                                                modifier = Modifier.padding(8.dp),
                                                text = " مسافت دقیق: ${it} ".toPersianDigits(),
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                }
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                    modifier = Modifier
                                        .fillMaxWidth(1f)
                                        .padding(8.dp)
                                )
                                //--------------------------------------------------------
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(4.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = " فرودگاه مبدا",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.background
                                        )
                                        CopyableText(departureAirportName)


                                    }
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Row {

                                            Text(
                                                text = "${deGate}".toPersianDigits(),
                                                style = MaterialTheme.typography.labelLarge,
                                                color = MaterialTheme.colorScheme.background.copy(
                                                    alpha = 0.5f
                                                )
                                            )
                                            Text(
                                                text = "گیت:",
                                                style = MaterialTheme.typography.labelLarge,
                                                color = MaterialTheme.colorScheme.background
                                            )
                                        }
                                        Row {

                                            Text(
                                                text = "${deTerminal}".toPersianDigits(),
                                                style = MaterialTheme.typography.labelLarge,
                                                color = MaterialTheme.colorScheme.background.copy(
                                                    alpha = 0.5f
                                                )
                                            )
                                            Text(
                                                text = "ترمینال:",
                                                style = MaterialTheme.typography.labelLarge,
                                                color = MaterialTheme.colorScheme.background
                                            )
                                        }

                                    }
                                    //------------------
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(4.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {


                                        Text(
                                            text = " فرودگاه مقصد",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.background
                                        )
                                        CopyableText(arrivalAirportName)

                                    }
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Row {
                                            Text(
                                                text = "${arrGate}".toPersianDigits(),
                                                style = MaterialTheme.typography.labelLarge,
                                                color = MaterialTheme.colorScheme.background.copy(
                                                    alpha = 0.5f
                                                )
                                            )
                                            Text(
                                                text = "گیت:",
                                                style = MaterialTheme.typography.labelLarge,
                                                color = MaterialTheme.colorScheme.background
                                            )
                                        }
                                        Row {

                                            Text(
                                                text = "${arrTerminal}".toPersianDigits(),
                                                style = MaterialTheme.typography.labelLarge,
                                                color = MaterialTheme.colorScheme.background.copy(
                                                    alpha = 0.5f
                                                )
                                            )
                                            Text(
                                                text = "ترمینال:",
                                                style = MaterialTheme.typography.labelLarge,
                                                color = MaterialTheme.colorScheme.background
                                            )
                                        }

                                    }


                                }

                                TextButton({
                                    nav.push(
                                        AircraftListFromAirlineSc(
                                            airline = airline.toString(),
                                            iata = iata.toString() ?: ""
                                        )
                                    )
                                }) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            tint = MaterialTheme.colorScheme.primary,
                                            painter = painterResource(R.drawable.icon_airplane),
                                            contentDescription = ""
                                        )
                                        Text(
                                            "نمایش هواپیماهای این ایرلاین",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                if (showSecondBox) {
                    Card(
                        modifier = Modifier
                            .offset(x = offsetY.value.dp)
                            .alpha(if (showSecondBox) 0.9f else 0f)
                            .fillMaxHeight(0.5f)
                            .padding(4.dp),
                        elevation = CardDefaults.elevatedCardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary)
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(
                                bottomStart = 0.dp, bottomEnd = 0.dp, topStart = 0.dp, topEnd = 8.dp
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Start),
                            onClick = {
                                scope.launch {
                                    offsetY.animateTo(
                                        targetValue = collapsedY, animationSpec = tween(
                                            durationMillis = 1500, easing = EaseInOutBack
                                        )
                                    )
                                    showSecondBox = false
                                    showFirstBox = true
                                }
                            }) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.icon_back),
                                    contentDescription = ""
                                )
                                Text(
                                    modifier = Modifier
                                        .padding(6.dp)
                                        .fillMaxWidth(),

                                    text = "برگشت",
                                )


                            }

                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier

                                .fillMaxSize()
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            when {
                                fullDetail.isEmpty() -> {
                                    Box(
                                        Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = "اطلاعات وجود ندارد!",
                                            color = MaterialTheme.colorScheme.background,
                                            style = MaterialTheme.typography.labelMedium,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                                else -> {
                                    var detail = fullDetail.first()
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Text(

                                            modifier = Modifier.fillMaxWidth(),
                                            text = detail.lastUpdatedUtc ?: "",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                       Box( modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                                           Row(
                                               verticalAlignment = Alignment.CenterVertically,

                                               horizontalArrangement = Arrangement.spacedBy(8.dp),
                                           ) {
                                               var topData = listOf(
                                                   detail.aircraft?.model ?: "null",
                                                   detail.status ?: "null",
                                               )
                                               //card1
                                               Card(
                                                   modifier = Modifier.weight(1f),
                                                   elevation = CardDefaults.elevatedCardElevation(4.dp),
                                                   shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp),
                                                   colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary)

                                               ) {
                                                   Box(
                                                       Modifier.fillMaxSize(),
                                                       contentAlignment = Alignment.Center
                                                   ) {
                                                       if (detail.isCargo) {
                                                          Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                              Text(
                                                                  style = MaterialTheme.typography.labelSmall,

                                                                  modifier = Modifier.padding(4.dp),
                                                                  text = " نوع هواپیما",
                                                                  color = MaterialTheme.colorScheme.background
                                                              )
                                                              Icon(
                                                                  modifier = Modifier.size(24.dp),

                                                                  painter = painterResource(R.drawable.icon_cargo),
                                                                  contentDescription = "",
                                                                  tint = MaterialTheme.colorScheme.primary
                                                              )
                                                          }


                                                       } else {
                                                           Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                                               Text(
                                                                   style = MaterialTheme.typography.labelSmall,

                                                                   modifier = Modifier.padding(4.dp),
                                                                   text = "نوع",
                                                                   color = MaterialTheme.colorScheme.background
                                                               )
                                                               Icon(
                                                                   modifier = Modifier.size(34.dp),
                                                                   painter = painterResource(R.drawable.icon_passanger),
                                                                   contentDescription = "",
                                                                   tint = MaterialTheme.colorScheme.primary
                                                               )
                                                           }
                                                       }


                                                   }

                                               }
                                               //card2
                                               Card(
                                                   elevation = CardDefaults.elevatedCardElevation(4.dp),
                                                   modifier = Modifier.weight(1f),
                                                   shape = RoundedCornerShape(4.dp),
                                                   colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary)

                                               ) {
                                                   Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                                       Text(
                                                           style = MaterialTheme.typography.labelSmall,

                                                           modifier = Modifier.padding(4.dp),
                                                           text = "مدل هواپیما",
                                                           color = MaterialTheme.colorScheme.background
                                                       )
                                                       Text(
                                                           style = MaterialTheme.typography.labelLarge,
                                                           textAlign = TextAlign.Center,

                                                           modifier = Modifier
                                                               .padding(4.dp)
                                                               .fillMaxWidth(),
                                                           text = topData.get(0).toPersianDigits()
                                                               .toString(),
                                                           color = MaterialTheme.colorScheme.background
                                                       )
                                                   }
                                               }

                                                //card3
                                               Card(
                                                   elevation = CardDefaults.elevatedCardElevation(4.dp),
                                                   modifier = Modifier.weight(1f),
                                                   shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
                                                   colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary)

                                               ) {
                                                   Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                       Text(
                                                           textAlign = TextAlign.Center,
                                                           style = MaterialTheme.typography.labelSmall,

                                                           modifier = Modifier
                                                               .padding(4.dp)
                                                               .fillMaxWidth(),
                                                           text = "وضعیت",
                                                           color = MaterialTheme.colorScheme.background
                                                       )
                                                       Text(
                                                           style = MaterialTheme.typography.labelLarge,
                                                           modifier = Modifier.padding(4.dp),
                                                           text = convertEngToPerStats(
                                                               topData.get(1).toString()
                                                           ),
                                                           color = MaterialTheme.colorScheme.background
                                                       )
                                                   }
                                               }


                                           }
                                       }
                                        //Mabda
                                        Column(modifier = Modifier.padding(2.dp)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    modifier = Modifier.weight(1f),
                                                    style = MaterialTheme.typography.labelLarge,
                                                    text = "مبدا",
                                                    color = MaterialTheme.colorScheme.background
                                                )
                                                Icon(
                                                    painter = painterResource(R.drawable.icon_origin),
                                                    tint = MaterialTheme.colorScheme.background,
                                                    modifier = Modifier.size(16.dp),
                                                    contentDescription = ""
                                                )
                                            }
                                            Column(modifier = Modifier.padding(8.dp)) {
                                                //row1
                                                Row(
                                                    horizontalArrangement = Arrangement.Center,
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.fillMaxWidth()) {
                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        modifier = Modifier.padding(4.dp),
                                                        text = detail.arrival.airport.name.toString()?:"",
                                                        color = MaterialTheme.colorScheme.background
                                                    )
                                                    Spacer(Modifier.weight(1f))

                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        text = "نام فرودگاه",
                                                        color = MaterialTheme.colorScheme.background.copy(0.5f)
                                                    )
                                                }
                                                //row2
                                                Row(
                                                    horizontalArrangement = Arrangement.Center,
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.fillMaxWidth()) {
                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        modifier = Modifier.padding(4.dp),
                                                        text = detail.arrival.airport.countryCode.toString()?:"",
                                                        color = MaterialTheme.colorScheme.background
                                                    )
                                                    Spacer(Modifier.weight(1f))
                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        text = "کشور",
                                                        color = MaterialTheme.colorScheme.background.copy(0.5f)
                                                    )
                                                }
                                                //row2.5
                                                Row(
                                                    horizontalArrangement = Arrangement.Center,
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.fillMaxWidth()) {
                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        modifier = Modifier.padding(4.dp),
                                                        text = detail.arrival.airport.timeZone?.split("/")?.get(1).toString()?:"",
                                                        color = MaterialTheme.colorScheme.background
                                                    )
                                                    Spacer(Modifier.weight(1f))
                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        text = "شهر",
                                                        color = MaterialTheme.colorScheme.background.copy(0.5f)
                                                    )
                                                }
                                                //row3
                                                Row(
                                                    horizontalArrangement = Arrangement.Center,
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.fillMaxWidth()) {
                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        modifier = Modifier.padding(4.dp),
                                                        text = detail.arrival.airport.timeZone.toString()?:"",
                                                        color = MaterialTheme.colorScheme.background
                                                    )
                                                    Spacer(Modifier.weight(1f))

                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        text = "منظقه زمانی",
                                                        color = MaterialTheme.colorScheme.background.copy(0.5f)
                                                    )
                                                }
                                                //row4

                                                Row(
                                                    horizontalArrangement = Arrangement.Center,
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.fillMaxWidth()) {
                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        modifier = Modifier.padding(4.dp),
                                                        text = detail.arrival.scheduledTime?.utc.toString()?:"",
                                                        color = MaterialTheme.colorScheme.background
                                                    )
                                                    Spacer(Modifier.weight(1f))

                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        text = "زمان برنامه ریزی شده",
                                                        color = MaterialTheme.colorScheme.background.copy(0.5f)
                                                    )
                                                }
                                                //row4
                                                Row(
                                                    horizontalArrangement = Arrangement.Center,
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.fillMaxWidth()) {
                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        modifier = Modifier.padding(4.dp),
                                                        text = detail.arrival.revisedTime?.utc.toString()?:"",
                                                        color = MaterialTheme.colorScheme.background
                                                    )
                                                    Spacer(Modifier.weight(1f))

                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        text = "زمان بازبینی شده",
                                                        color = MaterialTheme.colorScheme.background.copy(0.5f)
                                                    )
                                                }
                                                //row5
                                                Row(
                                                    horizontalArrangement = Arrangement.Center,
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.fillMaxWidth()) {
                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        modifier = Modifier.padding(4.dp),
                                                        text = detail.arrival.predictedTime?.utc.toString()?:"",
                                                        color = MaterialTheme.colorScheme.background
                                                    )
                                                    Spacer(Modifier.weight(1f))

                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        text = "پیشبینی هوشمند",
                                                        color = MaterialTheme.colorScheme.background.copy(0.5f)
                                                    )
                                                }


                                            }
                                        }
                                        //Maghsad
                                        Column(modifier = Modifier.padding(2.dp)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    modifier = Modifier.weight(1f),
                                                    style = MaterialTheme.typography.labelLarge,
                                                    text = "مقصد",
                                                    color = MaterialTheme.colorScheme.background
                                                )
                                                Icon(
                                                    painter = painterResource(R.drawable.icon_pin),
                                                    tint = MaterialTheme.colorScheme.background,
                                                    modifier = Modifier.size(16.dp),
                                                    contentDescription = ""
                                                )
                                            }
                                            Column(modifier = Modifier.padding(8.dp)) {
                                                //row1
                                                Row(
                                                    horizontalArrangement = Arrangement.Center,
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.fillMaxWidth()) {
                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        modifier = Modifier.padding(4.dp),
                                                        text = detail.departure.airport.name.toString()?:"",
                                                        color = MaterialTheme.colorScheme.background
                                                    )
                                                    Spacer(Modifier.weight(1f))

                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        text = "نام فرودگاه",
                                                        color = MaterialTheme.colorScheme.background.copy(0.5f)
                                                    )
                                                }
                                                //row2
                                                Row(
                                                    horizontalArrangement = Arrangement.Center,
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.fillMaxWidth()) {
                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        modifier = Modifier.padding(4.dp),
                                                        text = detail.departure.airport.countryCode.toString()?:"",
                                                        color = MaterialTheme.colorScheme.background
                                                    )
                                                    Spacer(Modifier.weight(1f))
                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        text = "کشور",
                                                        color = MaterialTheme.colorScheme.background.copy(0.5f)
                                                    )
                                                }
                                                //row2.5
                                                Row(
                                                    horizontalArrangement = Arrangement.Center,
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.fillMaxWidth()) {
                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        modifier = Modifier.padding(4.dp),
                                                        text = detail.departure.airport.timeZone?.split("/")?.get(1).toString()?:"",
                                                        color = MaterialTheme.colorScheme.background
                                                    )
                                                    Spacer(Modifier.weight(1f))
                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        text = "شهر",
                                                        color = MaterialTheme.colorScheme.background.copy(0.5f)
                                                    )
                                                }
                                                //row3
                                                Row(
                                                    horizontalArrangement = Arrangement.Center,
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.fillMaxWidth()) {
                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        modifier = Modifier.padding(4.dp),
                                                        text = detail.departure.airport.timeZone.toString()?:"",
                                                        color = MaterialTheme.colorScheme.background
                                                    )
                                                    Spacer(Modifier.weight(1f))

                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        text = "منظقه زمانی",
                                                        color = MaterialTheme.colorScheme.background.copy(0.5f)
                                                    )
                                                }
                                                //row4

                                                Row(
                                                    horizontalArrangement = Arrangement.Center,
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.fillMaxWidth()) {
                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        modifier = Modifier.padding(4.dp),
                                                        text = detail.departure.scheduledTime?.utc.toString()?:"",
                                                        color = MaterialTheme.colorScheme.background
                                                    )
                                                    Spacer(Modifier.weight(1f))

                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        text = "زمان برنامه ریزی شده",
                                                        color = MaterialTheme.colorScheme.background.copy(0.5f)
                                                    )
                                                }
                                                //row4
                                                Row(
                                                    horizontalArrangement = Arrangement.Center,
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.fillMaxWidth()) {
                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        modifier = Modifier.padding(4.dp),
                                                        text = detail.departure.revisedTime?.utc.toString()?:"",
                                                        color = MaterialTheme.colorScheme.background
                                                    )
                                                    Spacer(Modifier.weight(1f))

                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        text = "زمان بازبینی شده",
                                                        color = MaterialTheme.colorScheme.background.copy(0.5f)
                                                    )
                                                }
                                                //row5
                                                Row(
                                                    horizontalArrangement = Arrangement.Center,
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.fillMaxWidth()) {
                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        modifier = Modifier.padding(4.dp),
                                                        text = detail.departure.predictedTime?.utc.toString()?:"",
                                                        color = MaterialTheme.colorScheme.background
                                                    )
                                                    Spacer(Modifier.weight(1f))

                                                    Text(
                                                        style = MaterialTheme.typography.labelMedium,
                                                        text = "پیشبینی هوشمند",
                                                        color = MaterialTheme.colorScheme.background.copy(0.5f)
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
        }

    }
}


@Composable
fun CopyableText(text: String) {
    val clipboardManager: androidx.compose.ui.platform.ClipboardManager =
        LocalClipboardManager.current

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(R.drawable.icon_copy),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
            modifier = Modifier.clickable {
                clipboardManager.setText(AnnotatedString(text))
            })

    }
}

