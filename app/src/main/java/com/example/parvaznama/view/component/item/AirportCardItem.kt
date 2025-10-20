package com.example.parvaznama.view.component.item

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.parvaznama.R
import com.example.parvaznama.remote.ktor.model.AirportItem

@Composable
fun AirportListCardItem(it: AirportItem,itemClick:()->Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .animateContentSize()
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Image(
                    modifier = Modifier
                        .border(color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f), width = 0.2.dp, shape = CircleShape)
                        .size(80.dp)
                        .clip(CircleShape),
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text(
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth(0.8f),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 2,
                            style = MaterialTheme.typography.bodyMedium,
                            text = it.name,
                            color = MaterialTheme.colorScheme.background
                        )
                        Text(
                            style = MaterialTheme.typography.labelMedium,
                            text = it.countryCode.toString(),
                            color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
                        )
                    }
                    OutlinedCard(
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.width(48.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(2.dp).fillMaxWidth(),
                            style = MaterialTheme.typography.labelMedium,
                            text = it.iata.toString(),
                            color = MaterialTheme.colorScheme.background
                        )
                    }
                }
            }
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    itemClick()
                }) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        text = "نمایش روی نقشه"
                    )
                    Icon(
                        painter = painterResource(R.drawable.icon_map),
                        contentDescription = ""
                    )
                }
            }

        }

    }
}