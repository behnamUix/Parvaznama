package com.example.parvaznama.view.component.item

import Airline
import FlightDetail
import FlightArrival
import FlightData
import FlightDeparture
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.parvaznama.R
import com.example.parvaznama.utils.AirportRepository
import com.example.parvaznama.utils.convertEngToPerStats
import com.example.parvaznama.utils.convertMiladiToShamsi
import com.example.parvaznama.utils.toPersianDigits
import com.example.parvaznama.view.navigation.InformationSc

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AirlineListItem(
    flight:FlightDetail?,
    status: String?,
    departures: FlightDeparture?,
    arrival: FlightArrival?,
    airline: Airline?,
    date: String,
    repo: AirportRepository,
    departureIata: String?,
    arrivalIata: String?

) {
    var nav = LocalNavigator.currentOrThrow
    var strokeColor: Brush =
        if (convertEngToPerStats(status.toString() ?: "").equals("در حال انجام")) {
            Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.error
                )
            )
        } else {
            Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.onPrimary,
                    MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    Card(
        modifier = Modifier
            .border(
                width = 1.dp,
                brush = strokeColor, shape = RoundedCornerShape(16.dp)
            )
            .padding(4.dp)
            .clickable {
                nav.parent?.push(
                    InformationSc(
                        flight=flight,
                        departure = departures,
                        arrival = arrival,
                        departureIata = departureIata,
                        arrivalIata = arrivalIata,
                        repo = repo,
                        airline = airline
                    )
                )
            },
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary)


    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(0.2f),
                    shape = RoundedCornerShape(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            modifier = Modifier

                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall,
                            text = "IATA",

                            color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
                        )
                        Text(
                            modifier = Modifier

                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            text = airline?.iata ?: "",

                            color = MaterialTheme.colorScheme.background
                        )
                    }
                }
                Text(
                    textAlign = TextAlign.Center,

                    style = MaterialTheme.typography.bodyLarge,
                    text = convertMiladiToShamsi(date).toPersianDigits(),

                    color = MaterialTheme.colorScheme.background
                )
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(0.3f),
                    shape = RoundedCornerShape(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),

                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {

                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall,
                            text = "ICAO",

                            color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
                        )
                        Text(

                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,

                            style = MaterialTheme.typography.bodyLarge,
                            text = airline?.icao ?: "",

                            color = MaterialTheme.colorScheme.background
                        )
                    }
                }
            }
            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelMedium,
                text = "وضعیت پرواز:${convertEngToPerStats(status.toString() ?: "")}",

                color = MaterialTheme.colorScheme.background
            )
            HorizontalDivider(
                thickness = 0.5.dp,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(4.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(150.dp),
                        style = MaterialTheme.typography.labelMedium,
                        text = "${repo.getCityByIATA(departureIata ?: "")}",

                        color = MaterialTheme.colorScheme.background
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(150.dp),
                        style = MaterialTheme.typography.labelMedium,
                        text = repo.getCountryName(
                            repo.getCountryByIATA(
                                departureIata ?: ""
                            ) ?: ""
                        ),

                        color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
                    )

                    Text(
                        style = MaterialTheme.typography.headlineSmall,
                        text = departureIata ?: "NUL",
                        color = MaterialTheme.colorScheme.background
                    )

                }
                Icon(
                    painter = painterResource(R.drawable.icon_airplane),
                    contentDescription = "Airplane",
                    modifier = Modifier
                        .rotate(0f)
                        .weight(1f)
                        .size(50.dp)
                        .rotate(90f),
                    tint = MaterialTheme.colorScheme.primary
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {


                    Text(
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(150.dp),
                        style = MaterialTheme.typography.labelMedium,
                        text = "${repo.getCityByIATA(arrivalIata ?: "")}",

                        color = MaterialTheme.colorScheme.background
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(150.dp),
                        style = MaterialTheme.typography.labelMedium,
                        text = repo.getCountryName(
                            repo.getCountryByIATA(
                                arrivalIata ?: ""
                            ) ?: ""
                        ),

                        color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
                    )
                    Text(
                        style = MaterialTheme.typography.headlineSmall,
                        text = arrivalIata ?: "NULL",
                        color = MaterialTheme.colorScheme.background
                    )

                }

            }


        }
    }

}

