package com.example.parvaznama.view.component

import Airline
import FlightArrival
import FlightDeparture
import android.content.ClipboardManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.parvaznama.R
import com.example.parvaznama.utils.AirportRepository
import com.example.parvaznama.utils.calculateDistance

@Composable
fun BottomSheetComp(
    departure: FlightDeparture?,
    arrival: FlightArrival?,
    originCoord: Pair<Double, Double>?,
    destinationCoord: Pair<Double, Double>?,
    departureIata: String?,
    arrivalIata: String?,
    repo: AirportRepository
) {
    var arrivalAirportName = arrival?.airport.toString()
    var departureAirportName = departure?.airport.toString()
    var dist = calculateDistance(
        originCoord?.first ?: 0.1,
        originCoord?.second ?: 0.1,
        destinationCoord?.first ?: 0.1,
        destinationCoord?.second ?: 0.1
    )
    var deTerminal = departure?.terminal ?: "نامعلوم"
    var deGate = departure?.gate ?: "نامعلوم"
    var arrTerminal = arrival?.terminal ?: "نامعلوم"
    var arrGate = arrival?.gate ?: "نامعلوم"

    Box(
        modifier = Modifier

            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .fillMaxHeight(0.4f),
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                modifier = Modifier
                    .alpha(0.9f)
                    .fillMaxSize()
                    .padding(4.dp),
                elevation = CardDefaults.elevatedCardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary)
            ) {
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
                    Text(
                        text = " مسافت بین مبدا تا مقصد: ${Math.round(dist)}کیلومتر ",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.background
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(8.dp)
                    )
                    //---------------------------------------------------------

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column (
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
                                    text = "${deGate}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
                                )
                                Text(
                                    text = "گیت:",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.background
                                )
                            }
                            Row {

                                Text(
                                    text = "${deTerminal}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
                                )
                                Text(
                                    text = "ترمینال:",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.background
                                )
                            }

                        }
                        //------------------
                        Column (
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
                                    text = "${arrGate}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
                                )
                                Text(
                                    text = "گیت:",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.background
                                )
                            }
                            Row {

                                Text(
                                    text = "${arrTerminal}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
                                )
                                Text(
                                    text = "ترمینال:",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.background
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
            }
        )

    }
}

