package com.example.parvaznama.view.navigation

import Airline
import FlightArrival
import FlightDeparture
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInBack
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.example.parvaznama.R
import com.example.parvaznama.utils.AirportRepository
import com.example.parvaznama.utils.getCoordinateWithCity
import com.example.parvaznama.view.anim.LoadingAnim
import com.example.parvaznama.view.component.BottomSheetComp
import com.example.parvaznama.view.component.MapBoxScreen
import com.example.parvaznama.view.component.ToolbarComp
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    @Composable
    override fun Content() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.background,
                text = "پروازنما"
            )

            Column(
                modifier = Modifier
            ) {
                TabNavigator(AirportSearchTab) { tabNavigator ->
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


}

data class InformationSc(
    val departure: FlightDeparture?,
    val arrival: FlightArrival?,
    val airline: Airline?,
    var departureIata: String?,
    var arrivalIata: String?,
    var repo: AirportRepository
) : Screen {
    @Composable
    override fun Content() {
        var nav = LocalNavigator.currentOrThrow
        var ctx = LocalContext.current
        var originCity = repo.getCityByIATA(arrivalIata ?: "")
        var destinationCity = repo.getCityByIATA(departureIata ?: "")

        var originCoord = getCoordinateWithCity(originCity ?: "", ctx)
        var destinationCoord = getCoordinateWithCity(destinationCity ?: "", ctx)


        Column() {


            ToolbarComp(
                airline?.name.toString(),
                R.drawable.icon_back,
                iconClick = { nav.pop() })


            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {


                MapBoxScreen(
                    //جهت تنظیم زوم دوربین
                    Ulocation = false,
                    long1 = originCoord?.first ?: 1.2,
                    lat1 = originCoord?.second ?: 1.2,
                    long2 = destinationCoord?.first ?: 1.2,
                    lat2 = destinationCoord?.second ?: 1.2
                )



                BottomSheetComp(
                    departure = departure,
                    arrival = arrival,
                    originCoord = originCoord,
                    destinationCoord = destinationCoord,
                    departureIata = departureIata,
                    arrivalIata = arrivalIata,
                    repo = repo
                )
            }
        }
    }

}

object MapSc : Screen {
    @Composable
    override fun Content() {
        TODO("Not yet implemented")
    }

}



